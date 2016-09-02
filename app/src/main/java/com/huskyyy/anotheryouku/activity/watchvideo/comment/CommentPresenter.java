package com.huskyyy.anotheryouku.activity.watchvideo.comment;

import android.app.Activity;

import com.huskyyy.anotheryouku.activity.account.AccountUtils;
import com.huskyyy.anotheryouku.api.ErrorConstants;
import com.huskyyy.anotheryouku.data.BaseCallback;
import com.huskyyy.anotheryouku.data.DataSource;
import com.huskyyy.anotheryouku.data.base.Captcha;
import com.huskyyy.anotheryouku.data.base.Comment;
import com.huskyyy.anotheryouku.data.base.CommentResponse;
import com.huskyyy.anotheryouku.data.base.Error;
import com.huskyyy.anotheryouku.data.base.User;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.util.LogUtils;
import com.huskyyy.anotheryouku.util.NetUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Subscription;

/**
 * Created by Wang on 2016/8/4.
 */
public class CommentPresenter implements CommentContract.Presenter {

    private Activity activity;
    private DataSource dataSource;
    private CommentContract.View commentView;

    private VideoData data;

    public CommentPresenter(Activity activity,
                            DataSource dataSource, CommentContract.View commentView,
                            VideoData data) {
        this.activity = activity;
        this.dataSource = dataSource;
        this.commentView = commentView;
        this.data = data;
        this.commentView.setPresenter(this);
    }

    @Override
    public void start() {

        if(!NetUtils.isNetworkAvailable()) {
            commentView.showNoNetWorkData();
            commentView.showNoCommentData();
            return;
        }

        Subscription s1 = dataSource.getHotCommentsData(data.getId(), 1, 10,
                new BaseCallback<List<Comment>>() {
            @Override
            public void onDataLoaded(final List<Comment> comments) {
                Subscription s2 = dataSource.getUsersData(comments,
                        new BaseCallback<List<User>>() {
                            @Override
                            public void onDataLoaded(List<User> users) {
                                for(int i = 0; i < comments.size(); i++) {
                                    comments.get(i).setUser(users.get(i));
                                }
                                commentView.showHotComment(comments);
                            }

                            @Override
                            public void onDataNotAvailable(Error error) {
                                if(error.getCode() == ErrorConstants.UNKNOWN_ERROR) {
                                    //commentView.showNoUserData();
                                } else {
                                    commentView.showError(error.toString());
                                }
                                commentView.showHotComment(comments);
                            }
                        });
                commentView.addViewSubscription(s2);
            }

            @Override
            public void onDataNotAvailable(Error error) {
                if(error.getCode() == ErrorConstants.UNKNOWN_ERROR) {
                    //commentView.showNoCommentData();
                } else {
                    commentView.showError(error.toString());
                }
            }
        });
        commentView.addViewSubscription(s1);

        Subscription s3 = dataSource.getCommentsData(data.getId(), 1, 11,
                new BaseCallback<List<Comment>>() {
            @Override
            public void onDataLoaded(final List<Comment> comments) {

                long accountId = AccountUtils.getAccountId(activity);
                if(accountId != -1) {
                    for(Comment comment : comments) {
                        if(comment.getUser().getId() == accountId) {
                            comment.setSendByAccount(true);
                        }
                    }
                }

                Subscription s4 = dataSource.getUsersData(comments,
                        new BaseCallback<List<User>>() {
                            @Override
                            public void onDataLoaded(List<User> users) {
                                for(int i = 0; i < comments.size(); i++) {
                                    comments.get(i).setUser(users.get(i));
                                }
                                commentView.showComment(comments);
                            }

                            @Override
                            public void onDataNotAvailable(Error error) {
                                commentView.showNoUserData();
                                commentView.showError(error.toString());
                                commentView.showComment(comments);
                            }
                        });
                commentView.addViewSubscription(s4);
            }

            @Override
            public void onDataNotAvailable(Error error) {
                commentView.showError(error.toString());
                commentView.showNoCommentData();
            }
        });
        commentView.addViewSubscription(s3);
    }

    @Override
    public void comment(final String content, final String replyId,
                        final String captchaKey, final String captchaText) {
        comment(content, replyId, captchaKey, captchaText, 1);
    }

    public void comment(final String content, final String replyId,
                        final String captchaKey, final String captchaText,
                        final int count) {

        if(!NetUtils.isNetworkAvailable()) {
            commentView.showNoNetWorkData();
            return;
        }

        Subscription s = dataSource.commentVideo(activity, data.getId(), content, replyId,
                captchaKey, captchaText, new BaseCallback<CommentResponse>() {
                    @Override
                    public void onDataLoaded(final CommentResponse response) {
                        LogUtils.i(response.getId());
                        final Comment comment = new Comment();
                        comment.setSendByAccount(true);
                        comment.setId(response.getId());
                        comment.setContent(content);
                        comment.setPublished(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        Subscription s1 = dataSource.getAccountData(activity,
                                new BaseCallback<User>() {
                                    @Override
                                    public void onDataLoaded(User user) {
                                        comment.setUser(user);
                                        commentView.showAddComment(comment);
                                    }

                                    @Override
                                    public void onDataNotAvailable(Error error) {
                                        commentView.showAddComment(comment);
                                    }
                                });
                        commentView.addViewSubscription(s1);
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {
                        if(count == 1 && error.getCode() == ErrorConstants.ACCESS_TOKEN_EXPIRED) {
                            AccountUtils.invalidateAuthToken(activity, null);
                            comment(content, replyId, captchaKey, captchaText, 2);
                        } else if(error.getCode() == ErrorConstants.NEED_CAPTCHA
                                || error.getCode() == ErrorConstants.CAPTCHA_INVALID) {
                            // 显示验证码
                            Subscription s2 = dataSource.getCaptchaData(new BaseCallback<Captcha>() {
                                @Override
                                public void onDataLoaded(Captcha r) {
                                    commentView.showCaptcha(r.getKey(), r.getBitmap());
                                }

                                @Override
                                public void onDataNotAvailable(Error error) {
                                    commentView.showError(error.toString());
                                }
                            });
                            commentView.addViewSubscription(s2);

                        } else {
                            commentView.showError(error.toString());
                        }
                    }
                });
        commentView.addViewSubscription(s);
    }

    @Override
    public void deleteComment(Comment comment) {
        deleteComment(comment, 1);
    }

    public void deleteComment(final Comment comment, final int count) {

        if(!NetUtils.isNetworkAvailable()) {
            commentView.showNoNetWorkData();
            return;
        }

        Subscription s = dataSource.cancelCommentVideo(activity, comment.getId(),
                new BaseCallback<CommentResponse>() {
                    @Override
                    public void onDataLoaded(CommentResponse response) {
                        commentView.showRemoveComment(comment);
                        LogUtils.i(response.getId());
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {
                        if (count == 1 && error.getCode() == ErrorConstants.ACCESS_TOKEN_EXPIRED) {
                            AccountUtils.invalidateAuthToken(activity, null);
                            deleteComment(comment, 2);
                        } else {
                            commentView.showRemoveCommentFailed(error, comment);
                        }
                    }
                });
        commentView.addViewSubscription(s);
    }
}
