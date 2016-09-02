package com.huskyyy.anotheryouku.activity.main.grid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wang on 2016/8/22.
 */
public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Integer> categoryImgs;
    private List<String> categoryStrs;

    private ItemClickListener itemClickListener;

    public GridAdapter(Context context, List<Integer> categoryImgs, List<String> categoryStrs) {
        this.context = context;
        this.categoryImgs = categoryImgs;
        this.categoryStrs = categoryStrs;
    }

    @Override
    public int getItemCount() {
        return categoryImgs.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CategoryViewHolder viewHolder = (CategoryViewHolder) holder;
        viewHolder.categoryImageView.setImageResource(categoryImgs.get(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onCategoryClick(categoryStrs.get(position));
                }
            }
        });
        viewHolder.nameTextView.setText(categoryStrs.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.view_category, parent, false);
        return new CategoryViewHolder(view);
    }


    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_category)
        ImageView categoryImageView;
        @BindView(R.id.tv_name)
        TextView nameTextView;
        public CategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface ItemClickListener {
        void onCategoryClick(String category);
    }

}
