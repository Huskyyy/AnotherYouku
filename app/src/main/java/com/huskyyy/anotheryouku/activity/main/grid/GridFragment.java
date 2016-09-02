package com.huskyyy.anotheryouku.activity.main.grid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.fragment.BaseFragment;
import com.huskyyy.anotheryouku.activity.category.CategoryActivity;
import com.huskyyy.anotheryouku.util.ArrayUtils;
import com.huskyyy.anotheryouku.util.ToastUtils;

/**
 * Created by Wang on 2016/8/22.
 */
public class GridFragment extends BaseFragment implements GridContract.View {

    private GridContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        setupRecyclerView((RecyclerView) view.findViewById(R.id.rv));
        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        GridAdapter adapter = new GridAdapter(getContext(),
                ArrayUtils.getIdList(getContext(), R.array.category_imgs).subList(1, 12),
                ArrayUtils.getStringList(getContext(), R.array.home_category).subList(1, 12));
        adapter.setItemClickListener(new GridAdapter.ItemClickListener() {
            @Override
            public void onCategoryClick(String category) {
                presenter.openCategory(category);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    @Override
    public void setPresenter(GridContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showCategory(String category) {
        Intent intent = new Intent(getContext(), CategoryActivity.class);
        intent.putExtra(CategoryActivity.CATEGORY, category);
        startActivity(intent);
    }

    @Override
    public void showNoNetworkData() {
        ToastUtils.showShort(R.string.network_unavailable);
    }
}
