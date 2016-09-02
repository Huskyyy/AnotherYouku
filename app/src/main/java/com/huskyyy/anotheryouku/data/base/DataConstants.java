package com.huskyyy.anotheryouku.data.base;

import android.content.Context;
import android.content.Intent;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.util.ArrayUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wang on 2016/7/22.
 */
public class DataConstants {

    private DataConstants(){}

    // 视频信息：操作限制
    public static final String VIDEO_OPERATION_COMMENT_DISABLED = "COMMENT_DISABLED";
    public static final String VIDEO_OPERATION_DOWNLOAD_DISABLED = "DOWNLOAD_DISABLED";

    // 节目信息：是否付费
    public static final int SHOW_PAID_TRUE = 1;
    public static final int SHOW_PAID_FALSE = 0;

    // 视频分类和子分类映射
    public static final Map<String, List<String>> videoCategoryMap = new HashMap<>();

    // 视频子分类和tag映射
    public static final Map<String, List<String>> videoGenreMap = new HashMap<>();

    public static void generateCategoryMap(Context context) {

        List<String> videoCategories = ArrayUtils.getStringList(context, R.array.video_category);

        List<Integer> categoryGenreArrayIds = ArrayUtils
                .getIdList(context, R.array.video_category_genre);
        List<Integer> genreTagArrayOfArrayIds = ArrayUtils
                .getIdList(context, R.array.video_category_genre_tag);

        for(int i = 0; i < videoCategories.size(); i++) {

            List<String> genres = ArrayUtils
                    .getStringList(context, categoryGenreArrayIds.get(i));
            videoCategoryMap.put(videoCategories.get(i), genres);
            List<Integer> genreTagArrayIds = ArrayUtils
                    .getIdList(context, genreTagArrayOfArrayIds.get(i));

            for(int j = 0; j < genres.size(); j++) {

                videoGenreMap.put(genres.get(j), ArrayUtils
                        .getStringList(context, genreTagArrayIds.get(j)));
            }
        }
    }
}
