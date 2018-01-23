package com.jungho.reddit;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.jungho.reddit.adapter.RedditListAdapter;
import com.jungho.reddit.api.retrofit.RedditRetrofitWebService;
import com.jungho.reddit.api.model.Child;
import com.jungho.reddit.api.model.Data;
import com.jungho.reddit.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Locale;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private RedditRetrofitWebService webService;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RedditListAdapter adapter;

    private String after;

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.toolbar.setTitle("New on Reddit");

        webService = ((RedditApplication) getApplication()).getRedditRetrofitWebService();

        after = null;
        adapter = new RedditListAdapter();
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    // load more
                    setRedditList(false);
                }
            }
        });

        // swipe to refresh
        swipeRefreshLayout = binding.swipeRefresh;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRedditList(true);
            }
        });
        // initial api call
        setRedditList(true);
    }

    private void setRedditList(final boolean reset) {
        // if true, reset after
        if (reset) {
            after = null;
        }
        // show loading animation
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }

        // make an api call
        disposable = webService.fetchRedditList(after).subscribe(new Consumer<Data>() {
            @Override
            public void accept(@NonNull Data data) throws Exception {
                int itemCount = 0;
                List<Child> children = data.getChildren();
                if (children != null && children.size() > 0) {
                    // set list
                    adapter.setList(children, reset);
                    // update after
                    String after = data.getAfter();
                    if (after != null) {
                        MainActivity.this.after = after;
                    }
                    itemCount = children.size();
                }
                Toast.makeText(MainActivity.this, String.format(Locale.US, "%d new items loaded", itemCount), Toast.LENGTH_SHORT).show();
                // hide loading animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}