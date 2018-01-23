package com.jungho.reddit;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jungho.reddit.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_DETAIL_TITLE = "extra-detail-title";
    private static final String EXTRA_DETAIL_ARTICLE_URL = "extra-detail-article-url";

    public static Intent getDetailActivity(Context context, String title, String articleUrl) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_DETAIL_TITLE, title);
        intent.putExtra(DetailActivity.EXTRA_DETAIL_ARTICLE_URL, articleUrl);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        String title = getIntent().getStringExtra(EXTRA_DETAIL_TITLE);
        binding.detailToolbar.setTitle(title);
        setSupportActionBar(binding.detailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String url = RedditApplication.getBaseUrl() + getIntent().getStringExtra(EXTRA_DETAIL_ARTICLE_URL);
        binding.detailWebview.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
