package com.jungho.reddit.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jungho.reddit.DetailActivity;
import com.jungho.reddit.R;
import com.jungho.reddit.api.model.Child;
import com.jungho.reddit.api.model.Data_;
import com.jungho.reddit.api.model.Preview;
import com.jungho.reddit.databinding.ItemListRedditBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RedditListAdapter extends Adapter<RecyclerView.ViewHolder> {

    private List<Child> children;

    public RedditListAdapter() {
        children = new ArrayList<>();
    }

    @Override
    public RedditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new RedditViewHolder(inflater.inflate(R.layout.item_list_reddit, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Data_ data = children.get(position).getData();
        Preview preview = data.getPreview();

        String thumbnailUrl = null;
        if (preview != null) {
            thumbnailUrl = preview.getImages().get(0).getSource().getUrl();
        }

        ((RedditViewHolder) holder).bind(thumbnailUrl, data.getTitle(), data.getPermalink());
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    public void setList(List<Child> children, boolean reset) {
        if (reset) {
            this.children.clear();
        }
        this.children.addAll(children);
        notifyDataSetChanged();
    }

    private class RedditViewHolder extends RecyclerView.ViewHolder {

        private final ItemListRedditBinding itemListRedditBinding;
        private final Context context;

        private RedditViewHolder(View itemView) {
            super(itemView);
            itemListRedditBinding = DataBindingUtil.bind(itemView);
            context = itemListRedditBinding.getRoot().getContext();
        }

        void bind(String thumbnailUrl, final String title, final String articleUrl) {
            // set item title
            itemListRedditBinding.itemTitle.setText(title);

            // set item thumbnail
            Picasso.with(context)
                    .load(thumbnailUrl).placeholder(R.mipmap.ic_launcher)
                    .into(itemListRedditBinding.itemThumbnail);

            // set item click listener
            itemListRedditBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // start item detail activity
                    context.startActivity(DetailActivity.getDetailActivity(context, title, articleUrl));
                }
            });
        }
    }
}
