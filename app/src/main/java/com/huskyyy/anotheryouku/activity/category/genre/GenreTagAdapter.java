package com.huskyyy.anotheryouku.activity.category.genre;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wang on 2016/8/30.
 */
public class GenreTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> tags;

    private String selectedTag;

    private TagClickListener listener;

    public GenreTagAdapter(Context context) {
        this.context = context;
        tags = new ArrayList<>();
    }

    public void replaceData(List<String> tags) {
        this.tags = tags;
        selectedTag = tags.get(0);
        notifyDataSetChanged();
    }

    public int findTagPosition(String tag) {
        return tags.indexOf(tag);
    }

    public void selectTag(String tag) {
        String lastSelectedTag = selectedTag;
        selectedTag = tag;
        notifyItemChanged(tags.indexOf(lastSelectedTag));
        notifyItemChanged(tags.indexOf(selectedTag));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TagViewHolder viewHolder = (TagViewHolder) holder;
        viewHolder.tagTextView.setText(tags.get(position));
        if(tags.get(position).equals(selectedTag)) {
            viewHolder.tagTextView.setSelected(true);
        } else {
            viewHolder.tagTextView.setSelected(false);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_tag, parent, false);
        final TagViewHolder viewHolder = new TagViewHolder(view);
        viewHolder.tagTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    int position = viewHolder.getAdapterPosition();
                    listener.onTagClick(tags.get(position), position);
                }
            }
        });
        return viewHolder;
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_tag)
        TextView tagTextView;
        public TagViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setTagClickListener(TagClickListener listener) {
        this.listener = listener;
    }

    public interface TagClickListener {
        void onTagClick(String tag, int position);
    }
}
