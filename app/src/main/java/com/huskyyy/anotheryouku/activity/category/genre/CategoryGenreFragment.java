package com.huskyyy.anotheryouku.activity.category.genre;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.fragment.BaseFragment;
import com.huskyyy.anotheryouku.activity.watchvideo.WatchVideoActivity;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;
import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.LogUtils;
import com.huskyyy.anotheryouku.util.ToastUtils;

import org.apmem.tools.layouts.FlowLayout;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by Wang on 2016/8/30.
 */
public class CategoryGenreFragment extends BaseFragment implements CategoryGenreContract.View {

    private static final int PRELOAD_SIZE = 6;

    @BindView(R.id.view_root)
    View rootView;
    @BindView(R.id.rv_tags)
    RecyclerView tagRecyclerView;
    @BindView(R.id.rv_videos)
    RecyclerView videoRecyclerView;
    @BindView(R.id.view_tags)
    View tagsView;
    @BindView(R.id.view_bg)
    View bgView;
    @BindView(R.id.flow_tags)
    FlowLayout tagsFlow;
    @BindView(R.id.tv_published)
    TextView publishedTextView;
    @BindView(R.id.tv_view_count)
    TextView viewCountTextView;
    @BindView(R.id.tv_comment_count)
    TextView commentCountTextView;
    @BindView(R.id.tv_favorite_count)
    TextView favoriteCountTextView;
    @BindView(R.id.iv_arrow)
    ImageView arrowImageView;

    private String selectedTag;
    private TextView selectedTagTextView;
    private String selectedOrderBy;
    private TextView selectedOrderByTextView;

    private List<TextView> flowTagsList;

    private CategoryGenreContract.Presenter presenter;

    private CategoryGenreAdapter videoAdapter;
    private GenreTagAdapter tagAdapter;

    private int tagsViewHeight;

    public CategoryGenreFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_genre, container, false);
        ButterKnife.bind(this, view);
        setupTagRecyclerView();
        setupVideoRecyclerView();
        setupOtherWidget();
        //presenter.start();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.start();
    }

    @Override
    public void onDestroyView() {
        clearSubscription();
        super.onDestroyView();
    }



    private void setupTagRecyclerView() {
        tagRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tagAdapter = new GenreTagAdapter(getContext());
        tagAdapter.setTagClickListener(new GenreTagAdapter.TagClickListener() {
            @Override
            public void onTagClick(String tag, int position) {
                tagRecyclerView.smoothScrollToPosition(position);
                tagAdapter.selectTag(tag);
                if(!tag.equals(selectedTag)) {
                    if(selectedTagTextView != null)
                        selectedTagTextView.setSelected(false);
                    flowTagsList.get(position).setSelected(true);
                    selectedTag = tag;
                    selectedTagTextView = flowTagsList.get(position);
                    videoAdapter.prepareLoad(true);
                    presenter.loadData(tag, selectedOrderBy);
                }
            }
        });
        tagRecyclerView.setAdapter(tagAdapter);
    }

    private void setupVideoRecyclerView() {
        videoAdapter = new CategoryGenreAdapter(getContext());
        videoAdapter.setCategoryGenreItemClickListener(
                new CategoryGenreAdapter.CategoryGenreItemClickListener() {
                    @Override
                    public void onLoadClick() {
                        videoAdapter.prepareLoad(true);
                        presenter.loadData(selectedTag, selectedOrderBy);
                    }

                    @Override
                    public void onVideoClick(VideoData data) {
                        presenter.openVideo(data);
                    }

                    @Override
                    public void onLoadMoreClick(int page) {
                        videoAdapter.prepareLoadMore();
                        presenter.loadMore(selectedTag, selectedOrderBy, page);
                    }
                });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        videoRecyclerView.setLayoutManager(layoutManager);
        videoRecyclerView.setAdapter(videoAdapter);
        videoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState != RecyclerView.SCROLL_STATE_IDLE) {
                    ImageLoader.pauseImageLoading();
                } else {
                    ImageLoader.resumeImageLoading();
                }
            }

            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {

                // 自动加载要符合以下条件：
                // 1.数据已加载成功
                // 2.目前不在加载状态
                // 3.若上次加载失败则不自动加载
                // 4.有更多数据可以加载
                // 5.即将到达数据底部
                if (videoAdapter.isDataLoadSucceed() && videoAdapter.getBaseItemCount() != 0
                        && !videoAdapter.isLoadingMore() && videoAdapter.isMoreDataLoadSucceed()
                        && videoAdapter.getBaseItemCount() < videoAdapter.getBaseItemTotalCount()) {
                    boolean startLoadMore = layoutManager.findLastCompletelyVisibleItemPosition() >
                            videoAdapter.getBaseItemCount() - PRELOAD_SIZE;
                    if(startLoadMore) {
                        LogUtils.i("load page " + (videoAdapter.getCurrentPage() + 1));
                        videoAdapter.prepareLoadMore();
                        presenter.loadMore(selectedTag, selectedOrderBy,
                                videoAdapter.getCurrentPage() + 1);
                    }
                }
            }
        });
    }

    private void setupOtherWidget() {

        arrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bgView.getVisibility() == View.INVISIBLE) {
                    showTagsView(true);
                } else {
                    showTagsView(false);
                }
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v instanceof TextView) {
                    TextView textView = (TextView) v;
                    String orderBy = textView.getText().toString();
                    if(!orderBy.equals(selectedOrderBy)) {
                        if(selectedOrderByTextView != null)
                            selectedOrderByTextView.setSelected(false);
                        textView.setSelected(true);
                        selectedOrderBy = orderBy;
                        selectedOrderByTextView = textView;
                        showTagsView(false);
                        videoAdapter.prepareLoad(true);
                        presenter.loadData(selectedTag, selectedOrderBy);
                    }
                }
            }
        };
        publishedTextView.setText(R.string.published);
        publishedTextView.setOnClickListener(listener);

        publishedTextView.setSelected(true);
        selectedOrderBy = getString(R.string.published);
        selectedOrderByTextView = publishedTextView;

        viewCountTextView.setText(R.string.view_count);
        viewCountTextView.setOnClickListener(listener);
        commentCountTextView.setText(R.string.comment_count);
        commentCountTextView.setOnClickListener(listener);
        favoriteCountTextView.setText(R.string.favorite_count);
        favoriteCountTextView.setOnClickListener(listener);

        bgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    showTagsView(false);
                }
                return true;
            }
        });
    }

    private void setTagsFlow(List<String> tags) {
        tagsFlow.setFirstLineOffset(getContext().getResources()
                .getDimensionPixelSize(R.dimen.offset_width));
        flowTagsList = new ArrayList<>();
        for(int i = 0; i < tags.size(); i++) {
            final String tag = tags.get(i);
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.view_tag, tagsFlow, false);
            final TextView textView = (TextView) view.findViewById(R.id.tv_tag);
            textView.setText(tag);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!tag.equals(selectedTag)) {
                        if(selectedTagTextView != null)
                            selectedTagTextView.setSelected(false);
                        textView.setSelected(true);
                        selectedTag = tag;
                        selectedTagTextView = textView;
                        showTagsView(false);
                        tagRecyclerView.smoothScrollToPosition(tagAdapter.findTagPosition(tag));
                        tagAdapter.selectTag(tag);
                        videoAdapter.prepareLoad(true);
                        presenter.loadData(tag, selectedOrderBy);
                    }
                }
            });
            if(i == 0) {
                textView.setSelected(true);
                selectedTag = tag;
                selectedTagTextView = textView;
            }
            tagsFlow.addView(view);
            flowTagsList.add(textView);
        }
    }

    private void showTagsView(final boolean show) {

        if(tagsViewHeight == 0) {
            tagsViewHeight = tagsView.getHeight();
        }
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        final int colorFrom = ContextCompat.getColor(getContext(), R.color.transparent);
        final int colorTo = ContextCompat.getColor(getContext(), R.color.gray_dark_transparent);
        final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = tagsView.getLayoutParams();
                if(show) {
                    params.height = (int) (fraction * tagsViewHeight);
                    bgView.setBackgroundColor(
                            (int) argbEvaluator.evaluate(fraction, colorFrom, colorTo));
                } else {
                    params.height = (int) ((1 - fraction) * tagsViewHeight);
                    bgView.setBackgroundColor(
                            (int) argbEvaluator.evaluate(fraction, colorTo, colorFrom));
                }
                tagsView.setLayoutParams(params);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(show) {
                    bgView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(show) {
                    ViewGroup.LayoutParams params = tagsView.getLayoutParams();
                    params.height = tagsViewHeight;
                    tagsView.setLayoutParams(params);
                } else {
                    bgView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if(show) {
                    ViewGroup.LayoutParams params = tagsView.getLayoutParams();
                    params.height = tagsViewHeight;
                    tagsView.setLayoutParams(params);
                } else {
                    bgView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        if(show) {
            arrowImageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_18dp);
        } else {
            arrowImageView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_18dp);
        }
    }

    public boolean isDataLoaded() {
        return videoAdapter.isDataLoaded();
    }

    @Override
    public void setPresenter(CategoryGenreContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showTags(List<String> tags) {
        tagAdapter.replaceData(tags);
        setTagsFlow(tags);

    }

    @Override
    public void showData(VideosByCategory videos) {
        videoAdapter.replaceData(videos, true);
    }

    @Override
    public void showNoData() {
        videoAdapter.replaceData(null, false);
    }

    @Override
    public void showLoadMore(VideosByCategory videos) {
        videoAdapter.loadMoreData(videos, true);
    }

    @Override
    public void showLoadMoreFailed() {
        videoAdapter.loadMoreData(null, false);
    }

    @Override
    public void showWatchVideo(VideoData data) {
        Intent intent = new Intent(getContext(), WatchVideoActivity.class);
        intent.putExtra(WatchVideoActivity.VIDEO_DATA, data);
        intent.putExtra(WatchVideoActivity.USER_DATA, data.getUser());
        startActivity(intent);
    }

    @Override
    public void showNoNetworkData() {
        ToastUtils.showShort(R.string.network_unavailable);
    }

    @Override
    public void showError(String error) {
        ToastUtils.showShort(error);
    }

    @Override
    public void addViewSubscription(Subscription subscription) {
        addSubscription(subscription);
    }

    @Override
    public void clearViewSubscription() {
        clearSubscription();
    }
}
