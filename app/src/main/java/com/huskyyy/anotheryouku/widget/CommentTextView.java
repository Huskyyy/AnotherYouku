package com.huskyyy.anotheryouku.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.util.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wang on 2016/8/24.
 */
public class CommentTextView extends TextView {

    private static Map<String, Integer> emoticonsMap;
    private NameClickListener listener;

    public CommentTextView(Context context) {
        super(context);
    }

    public CommentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setComment(String commentText) {

        if(emoticonsMap == null) {
            String[] emoticonStrings = ArrayUtils.getStringArray(this.getContext(), R.array.emoticon_strings);
            int[] emoticonIds = ArrayUtils.getIdArray(this.getContext(), R.array.emoticon_imgs);
            emoticonsMap = new HashMap<>();
            for(int i = 0; i < emoticonStrings.length; i++) {
                emoticonsMap.put(emoticonStrings[i], emoticonIds[i]);
            }
        }

        if(!TextUtils.isEmpty(commentText)) {
            // 用户名高亮
            SpannableString ss = new SpannableString(commentText);
            String[] usersComment = commentText.split("//@");
            int startIndex = usersComment[0].length();
            for (int i = 1; i < usersComment.length; i++) {
                int endIndex = usersComment[i].indexOf(':');
                if(endIndex != -1) {
                    final String user = usersComment[i].substring(0, endIndex);
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            if(listener != null) {
                                listener.onNameClickListener(user);
                            }
                        }
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                        }
                    };
                    ss.setSpan(clickableSpan, startIndex + 2, startIndex + 2 + endIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                startIndex += usersComment[i].length() + 3;
            }

            // 表情
            usersComment = commentText.split("\\[");
            int lineHeight = this.getLineHeight();
            startIndex = usersComment[0].length();
            for(int i = 1; i < usersComment.length; i++) {
                int endIndex = usersComment[i].indexOf(']');
                if(endIndex != -1) {
                    String emoticonString  = "[" + usersComment[i].substring(0, endIndex + 1);
                    if(emoticonsMap.containsKey(emoticonString)) {
                        Drawable drawable = this.getResources().getDrawable(emoticonsMap.get(emoticonString));
                        drawable.setBounds(0, 0, lineHeight, lineHeight);
                        ImageSpan span = new ImageSpan(drawable, emoticonString, ImageSpan.ALIGN_BOTTOM);
                        ss.setSpan(span, startIndex, startIndex + endIndex + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                startIndex += usersComment[i].length() + 1;
            }


            this.setText(ss);
            this.setMovementMethod(ClickableMovementMethod.getInstance());
            this.setFocusable(false);
            this.setClickable(false);
            this.setLongClickable(false);

        }
    }

    public void setNameClickListener(NameClickListener listener) {
        this.listener = listener;
    }

    public interface NameClickListener {
        void onNameClickListener(String name);
    }
}
