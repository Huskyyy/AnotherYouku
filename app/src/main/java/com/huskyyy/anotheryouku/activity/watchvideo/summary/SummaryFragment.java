package com.huskyyy.anotheryouku.activity.watchvideo.summary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.fragment.BaseFragment;
import com.huskyyy.anotheryouku.data.base.User;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.StringUtils;
import com.huskyyy.anotheryouku.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;

/**
 * Created by Wang on 2016/8/1.
 */
public class SummaryFragment extends BaseFragment implements SummaryContract.View {

    SummaryContract.Presenter presenter;

    @BindView(R.id.tv_title)
    TextView titleTextView;
    @BindView(R.id.tv_view_count)
    TextView playCountTextView;
    @BindView(R.id.tv_comment_count)
    TextView commentCountTextView;
    @BindView(R.id.tv_description)
    TextView descriptionTextView;
    @BindView(R.id.iv_mark)
    ImageView markImageView;
    @BindView(R.id.tv_mark)
    TextView markTextView;
    @BindView(R.id.iv_share)
    ImageView shareImageView;
    @BindView(R.id.tv_share)
    TextView shareTextView;

    @BindView(R.id.iv_avatar)
    CircleImageView avatarImageView;
    @BindView(R.id.tv_name)
    TextView nameTextView;
    @BindView(R.id.tv_publish)
    TextView publishTextView;
    @BindView(R.id.tv_subscribe)
    TextView subscribeTextView;
    @BindView(R.id.tv_user_description)
    TextView userDescriptionTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_summary, container, false);
        ButterKnife.bind(this, view);

        View.OnClickListener shareListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.shareVideo();
            }
        };
        shareImageView.setOnClickListener(shareListener);
        shareTextView.setOnClickListener(shareListener);

        presenter.start();
        return view;
    }

    @Override
    public void setPresenter(SummaryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showVideo(VideoData data) {

        titleTextView.setText(data.getTitle());
        playCountTextView.setText(StringUtils.numberFormatter(data.getViewCount()));
        commentCountTextView.setText(StringUtils.numberFormatter(data.getCommentCount()));
        descriptionTextView.setText(
                StringUtils.descriptionFormatter(this.getContext(), data.getDescription()));
        publishTextView.setText(
                StringUtils.publishFormatter(this.getContext(), data.getPublished()));
    }

    @Override
    public void showUser(User user) {
        ImageLoader.loadAvatarImage(user.getAvatar(), avatarImageView);
        nameTextView.setText(user.getName());
        userDescriptionTextView.setText(
                StringUtils.descriptionFormatter(this.getContext(), user.getDescription()));
    }

    @Override
    public void showMarkFavorite(final boolean mark) {
        if(mark) {
            markImageView.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            markImageView.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

        View.OnClickListener markListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.markVideo(!mark);
            }
        };
        markImageView.setOnClickListener(markListener);
        markTextView.setOnClickListener(markListener);
    }

    @Override
    public void showSubscribeUser(boolean subscribe) {
        if(subscribe) {
            subscribeTextView.setText(R.string.subscribed);
            subscribeTextView.setSelected(true);
            // 没有取消订阅的api。。。
            subscribeTextView.setOnClickListener(null);

        } else {
            subscribeTextView.setText(R.string.to_subscribe);
            subscribeTextView.setSelected(false);
            subscribeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.subscribeUser();
                }
            });
        }
    }

    @Override
    public void showShare(String title, String link) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + link);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, null));
    }

    @Override
    public void showNoNetWorkData() {
        ToastUtils.showShort(R.string.network_unavailable);
    }

    @Override
    public void showNoVideoData() {
        ToastUtils.showShort(R.string.video_data_unavailable);
    }

    @Override
    public void showNoUserData() {
        ToastUtils.showShort(R.string.user_data_unavailable);
    }

    @Override
    public void showNoSubscribeData() {
        ToastUtils.showShort(R.string.subscribe_data_unavailable);
    }

    @Override
    public void showError(String error) {
        ToastUtils.showShort(error);
    }

    @Override
    public void addViewSubscription(Subscription subscription) {
        addSubscription(subscription);
    }
}
