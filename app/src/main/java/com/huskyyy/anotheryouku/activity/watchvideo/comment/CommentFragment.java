package com.huskyyy.anotheryouku.activity.watchvideo.comment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.fragment.BaseFragment;
import com.huskyyy.anotheryouku.data.base.Comment;
import com.huskyyy.anotheryouku.data.base.Error;
import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.ToastUtils;
import com.huskyyy.anotheryouku.widget.CaptchaDialog;
import com.huskyyy.anotheryouku.widget.SendCommentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by Wang on 2016/8/3.
 */
public class CommentFragment extends BaseFragment implements CommentContract.View {

    private CommentContract.Presenter presenter;
    private CommentListAdapter adapter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.view_send_comment)
    SendCommentView sendCommentView;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_comment, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        setupCommentWidget();
        setupProgressDialog();
        presenter.start();
        return view;
    }



    private void setupRecyclerView(){
        adapter = new CommentListAdapter(this.getContext(), recyclerView);
        adapter.setItemClickListener(new CommentListAdapter.CommentItemClickListener() {

            @Override
            public void onLoadClick() {
                adapter.prepareLoad(false);
                presenter.start();
            }

            @Override
            public void onMoreHotCommentsClick() {
                ToastUtils.showShort("MoreHotComment");
            }

            @Override
            public void onMoreCommentsClick() {
                ToastUtils.showShort("MoreComment");
            }

            @Override
            public void onCommentClick(Comment comment) {
                sendCommentView.setReplyId(comment);
            }

            @Override
            public void onDeleteCommentClick(Comment comment) {
                presenter.deleteComment(comment);
            }

            @Override
            public void onUserClick(long id, String name) {
                ToastUtils.showShort(name);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState != RecyclerView.SCROLL_STATE_IDLE) {
                    ImageLoader.pauseImageLoading();
                } else {
                    ImageLoader.resumeImageLoading();
                }
            }
        });
    }

    private void setupCommentWidget() {
        sendCommentView.setContentView(recyclerView);
        sendCommentView.setOnSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                presenter.comment(sendCommentView.getReplyContent(), sendCommentView.getReplyId(),
                        null, null);

            }
        });
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.comment_ing));
        progressDialog.setIndeterminate(true);
    }

    private void showProgressDialog() {
        if(progressDialog != null || !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if(progressDialog != null || progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public boolean hasFocus() {
        return sendCommentView.hasFocus();
    }

    public void clearFocus() {
        sendCommentView.clearFocus();
    }

    public SendCommentView getSendCommentView () {
        return sendCommentView;
    }

    @Override
    public void showComment(List<Comment> comments) {
        sendCommentView.setVisibility(View.VISIBLE);
        adapter.replaceComment(comments, true);
    }

    @Override
    public void showHotComment(List<Comment> hotComments) {
        adapter.replaceHotComment(hotComments);
    }

    @Override
    public void showAddComment(Comment comment) {
        dismissProgressDialog();
        sendCommentView.clearText();
        sendCommentView.clearFocus();
        adapter.addComment(comment);
    }

    @Override
    public void showCaptcha(String key, Bitmap bitmap) {
        dismissProgressDialog();
        final CaptchaDialog dialog = new CaptchaDialog(getContext(), key, bitmap,
                new CaptchaDialog.OnButtonClickListener() {
                    @Override
                    public void onConfirm(String key, String captcha) {
                        showProgressDialog();
                        presenter.comment(sendCommentView.getReplyContent(),
                                sendCommentView.getReplyId(), key, captcha);
                    }
                });
        dialog.show();

    }

    @Override
    public void showRemoveComment(Comment comment) {
        adapter.removeComment(comment);
    }

    @Override
    public void showRemoveCommentFailed(Error error, Comment comment) {
        ToastUtils.showShort(error.toString());
    }

    @Override
    public void showNoCommentData() {
        ToastUtils.showShort(R.string.comment_data_unavailable);
        sendCommentView.setVisibility(View.GONE);
        adapter.replaceComment(null, false);
    }

    @Override
    public void showNoNetWorkData() {
        dismissProgressDialog();
        ToastUtils.showShort(R.string.network_unavailable);
    }

    @Override
    public void showNoUserData() {
        ToastUtils.showShort(R.string.user_data_unavailable);
    }

    @Override
    public void showError(String error) {
        dismissProgressDialog();
        ToastUtils.showShort(error);
    }

    @Override
    public void addViewSubscription(Subscription subscription) {
        addSubscription(subscription);
    }

    @Override
    public void setPresenter(CommentContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
