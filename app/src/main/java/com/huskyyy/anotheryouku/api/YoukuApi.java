package com.huskyyy.anotheryouku.api;

import com.huskyyy.anotheryouku.data.base.AuthResult;
import com.huskyyy.anotheryouku.data.base.Captcha;
import com.huskyyy.anotheryouku.data.base.Comment;
import com.huskyyy.anotheryouku.data.base.CommentResponse;
import com.huskyyy.anotheryouku.data.base.CommentsByVideo;
import com.huskyyy.anotheryouku.data.base.MarkFavoriteResponse;
import com.huskyyy.anotheryouku.data.base.SubscribeUserResponse;
import com.huskyyy.anotheryouku.data.base.User;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;
import com.huskyyy.anotheryouku.data.base.VideosByRelated;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wang on 2016/7/3.
 */
public interface YoukuApi {

    @Headers("Cache-control: max-stale=1")
    @GET("videos/by_category.json")
    Observable<VideosByCategory> getVideosByCategory(@Query("client_id") String clientId,
                                                     @Query("category") String category,
                                                     @Query("genre") String genre,
                                                     @Query("period") String period,
                                                     @Query("orderby") String orderBy,
                                                     @Query("page") int page,
                                                     @Query("count") int count);

    @Headers("Cache-Control: no-cache")
    @GET("users/show.json")
    Observable<User> getUserData(@Query("client_id") String clientId,
                                 @Query("user_id") long userId);

    @Headers("Cache-Control: no-cache")
    @GET("videos/show.json")
    Observable<VideoData> getVideoData(@Query("client_id") String clientId,
                                       @Query("video_id") String videoId);


    @Headers("Cache-Control: no-cache")
    @GET("comments/by_video.json")
    Observable<CommentsByVideo> getCommentsByVideo(@Query("client_id") String clientId,
                                                   @Query("video_id") String videoId,
                                                   @Query("page") int page,
                                                   @Query("count") int count);

    @Headers("Cache-Control: no-cache")
    @GET("comments/hot/by_video.json")
    Observable<CommentsByVideo> getHotCommentsByVideo(@Query("client_id") String clientId,
                                                      @Query("video_id") String videoId,
                                                      @Query("page") int page,
                                                      @Query("count") int count);


    @Headers("Cache-Control: no-cache")
    @GET("videos/by_related.json")
    Observable<VideosByRelated> getVideosByRelated(@Query("client_id") String clientId,
                                                   @Query("video_id") String videoId);


    @Headers("Cache-Control: no-cache")
    @FormUrlEncoded
    @POST("oauth2/token")
    Observable<AuthResult> getAuthTokenByCode(@Field("client_id") String clientId,
                                              @Field("client_secret") String clientSecret,
                                              @Field("grant_type") String grantType,
                                              @Field("code") String code,
                                              @Field("redirect_uri") String redirectUrl);


    @Headers("Cache-Control: no-cache")
    @FormUrlEncoded
    @POST("oauth2/token")
    Observable<AuthResult> getAuthTokenByRefresh(@Field("client_id") String clientId,
                                                 @Field("client_secret") String clientSecret,
                                                 @Field("grant_type") String grantType,
                                                 @Field("refresh_token") String refreshToken);


    @Headers("Cache-Control: no-cache")
    @GET("users/myinfo.json")
    Observable<User> getAccountData(@Query("client_id") String clientId,
                                    @Query("access_token") String accessToken);


    @Headers("Cache-Control: no-cache")
    @FormUrlEncoded
    @POST("videos/favorite/create.json")
    Observable<MarkFavoriteResponse> markFavoriteVideo(@Field("client_id") String clientId,
                                                       @Field("access_token") String accessToken,
                                                       @Field("video_id") String videoId);


    @Headers("Cache-Control: no-cache")
    @FormUrlEncoded
    @POST("videos/favorite/destroy.json")
    Observable<MarkFavoriteResponse> unMarkFavoriteVideo(@Field("client_id") String clientId,
                                                         @Field("access_token") String accessToken,
                                                         @Field("video_id") String videoId);


    @Headers("Cache-Control: no-cache")
    @FormUrlEncoded
    @POST("users/friendship/create.json")
    Observable<SubscribeUserResponse> subscribeUser(@Field("client_id") String clientId,
                                                    @Field("access_token") String accessToken,
                                                    @Field("user_id") long userId,
                                                    @Field("user_name") String userName);


    @Headers("Cache-Control: no-cache")
    @FormUrlEncoded
    @POST("comments/create.json")
    Observable<CommentResponse> commentVideo(@Field("client_id") String clientId,
                                             @Field("access_token") String accessToken,
                                             @Field("video_id") String videoId,
                                             @Field("content") String content,
                                             @Field("reply_id") String replyId,
                                             @Field("captcha_key") String captchaKey,
                                             @Field("captcha_text") String captchaText);

    @Headers("Cache-Control: no-cache")
    @FormUrlEncoded
    @POST("comments/destroy.json")
    Observable<CommentResponse> cancelCommentVideo(@Field("client_id") String clientId,
                                                   @Field("access_token") String accessToken,
                                                   @Field("comment_id") String commentId);

    @Headers("Cache-Control: no-cache")
    @GET("captcha/get.json")
    Observable<Captcha> getCaptchaData(@Query("client_id") String clientId);

    @Headers("Cache-Control: no-cache")
    @GET("comments/show.json")
    Observable<Comment> getCommentData(@Query("client_id") String clientId,
                                       @Query("comment_id") String commentId);

    @Headers("Cache-control: no-cache")
    @GET("searches/video/by_tag.json")
    Observable<VideosByCategory> getVideosByTag(@Query("client_id") String clientId,
                                                @Query("category") String category,
                                                @Query("tag") String tag,
                                                @Query("period") String period,
                                                @Query("orderby") String orderBy,
                                                @Query("page") int page,
                                                @Query("count") int count);


}


