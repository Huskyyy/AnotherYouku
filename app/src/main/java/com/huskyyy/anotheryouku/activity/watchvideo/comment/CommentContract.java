package com.huskyyy.anotheryouku.activity.watchvideo.comment;

import android.graphics.Bitmap;

import com.huskyyy.anotheryouku.activity.base.BasePresenter;
import com.huskyyy.anotheryouku.activity.base.BaseView;
import com.huskyyy.anotheryouku.data.base.Comment;
import com.huskyyy.anotheryouku.data.base.Error;

import java.util.List;

import rx.Subscription;

/**
 * Created by Wang on 2016/8/3.
 */
public interface CommentContract {

    interface Presenter extends BasePresenter {

        void comment(String content, String replyId, String captchaKey, String captchaText);

        void deleteComment(Comment comment);

    }

    interface View extends BaseView<Presenter> {

        void showHotComment(List<Comment> hotComments);

        void showComment(List<Comment> comments);

        void showAddComment(Comment comment);

        void showRemoveComment(Comment comment);

        void showRemoveCommentFailed(Error error, Comment comment);

        void showCaptcha(String key, Bitmap bitmap);

        void showNoNetWorkData();

        void showNoCommentData();

        void showNoUserData();

        void showError(String error);

        void addViewSubscription(Subscription subscription);

    }
}
