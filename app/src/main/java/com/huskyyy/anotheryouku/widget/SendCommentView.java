package com.huskyyy.anotheryouku.widget;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.data.base.Comment;
import com.huskyyy.anotheryouku.util.ArrayUtils;
import com.huskyyy.anotheryouku.util.KeyboardUtils;
import com.huskyyy.anotheryouku.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wang on 2016/8/18.
 */
public class SendCommentView extends RelativeLayout {

    private View contentView;
    private ImageView emoticonImageView;
    private ImageView sendImageView;
    private AppCompatEditText commentEditText;
    private GridView emoticonsView;

    private Context context;

    private String replyId;
    private String replyName;
    private String replyContent;

    private OnClickListener sendImageViewClickListener;

    private TextHandler textHandler;

    public SendCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_send_comment, this);
        emoticonImageView = (ImageView) findViewById(R.id.iv_emoticon);
        sendImageView = (ImageView) findViewById(R.id.iv_send);
        commentEditText = (AppCompatEditText) findViewById(R.id.et_comment);
        emoticonsView = (GridView) findViewById(R.id.view_faces);

        this.context = context;
        setupEmoticons();
        setupEditText();
    }


    private void showEmotionsView() {
        if(emoticonsView != null && emoticonsView.getVisibility() == GONE) {
            emoticonsView.setVisibility(VISIBLE);
            emoticonImageView.setImageResource(R.drawable.ic_list_black_48dp);
        }
    }

    private void hideEmoticonsView() {
        if(emoticonsView != null && emoticonsView.getVisibility() == VISIBLE) {

            final int originalHeight = emoticonsView.getHeight();
            ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 0);
            animator.setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewGroup.LayoutParams params = emoticonsView.getLayoutParams();
                    params.height = (int) animation.getAnimatedValue();
                    emoticonsView.setLayoutParams(params);
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    emoticonsView.setVisibility(GONE);
                    ViewGroup.LayoutParams params = emoticonsView.getLayoutParams();
                    params.height = originalHeight;
                    emoticonsView.setLayoutParams(params);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    emoticonsView.setVisibility(GONE);
                    ViewGroup.LayoutParams params = emoticonsView.getLayoutParams();
                    params.height = originalHeight;
                    emoticonsView.setLayoutParams(params);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();

            //emoticonsView.setVisibility(GONE);
            emoticonImageView.setImageResource(R.drawable.ic_emoticon_48dp);
        }
    }

    public void setContentView(View view) {
        this.contentView = view;
    }

    private void lockContentHeight() {
        if(contentView != null) {
            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams) contentView.getLayoutParams();
            params.height = contentView.getHeight();
            params.weight = 0;
        }
    }

    private void unlockContentHeight() {
        if(contentView != null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    LinearLayout.LayoutParams params =
                            (LinearLayout.LayoutParams) contentView.getLayoutParams();
                    params.weight = 1;
                    contentView.setLayoutParams(params);

                }
            }, 200);
        }
    }

    private void setupEmoticons() {
        emoticonImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(emoticonsView.getVisibility() == VISIBLE) {
                    lockContentHeight();
                    commentEditText.requestFocus();
                    KeyboardUtils.showSoftInput(context, commentEditText);
                    hideEmoticonsView();
                    unlockContentHeight();
                } else {
                    lockContentHeight();
                    KeyboardUtils.hideSoftInput((Activity) context);
                    commentEditText.clearFocus();
                    showEmotionsView();
                    unlockContentHeight();
                }

            }
        });

        emoticonsView.setAdapter(new GridAdapter(context,
                ArrayUtils.getStringList(context, R.array.emoticon_strings),
                ArrayUtils.getIdList(context, R.array.emoticon_imgs)));

    }

    private void setupEditText() {
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString())) {
                    sendImageView.setImageResource(R.drawable.ic_send_gray_48dp);
                    sendImageView.setClickable(false);
                } else {
                    sendImageView.setImageResource(R.drawable.ic_send_blue_48dp);
                    sendImageView.setClickable(true);
                }
            }
        });
        commentEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    hideEmoticonsView();
                } else {
                }
            }
        });
        textHandler = new TextHandler(commentEditText);
    }

    public void setReplyId(Comment comment) {
        replyId = comment.getId();
        replyName = comment.getUser().getName();
        replyName = "@" + replyName + ":";
        textHandler.insert(replyName);

    }

    public String getReplyId() {
        return replyId;
    }

    public String getReplyContent() {

        replyContent = commentEditText.getText().toString();

        ImageSpan replySpan = textHandler.getReplySpan();
        if (replySpan != null) {
            Editable message = commentEditText.getEditableText();
            int start = message.getSpanStart(replySpan);
            int end = message.getSpanEnd(replySpan);
            replyContent = replyContent.substring(0, start) + replyContent.substring(end);
        }

        return replyContent;
    }

    public void clearText() {
        commentEditText.setText("");
    }

    public boolean hasFocus() {
        return emoticonsView != null && emoticonsView.getVisibility() == VISIBLE;
//        return commentEditText.hasFocus() ||
//                emoticonsView != null && emoticonsView.getVisibility() == VISIBLE;
    }

    public void clearFocus() {
        KeyboardUtils.hideSoftInput((Activity) context);
        commentEditText.clearFocus();
        emoticonImageView.requestFocus();
        hideEmoticonsView();
    }

    public void setOnSendClickListener(OnClickListener listener) {
        sendImageViewClickListener = listener;
        sendImageView.setOnClickListener(listener);
    }




    private static class TextHandler implements TextWatcher {

        private AppCompatEditText mEditor;
        private ArrayList<ImageSpan> mEmoticonsToRemove = new ArrayList<>();
        private ImageSpan replySpan;

        public TextHandler(AppCompatEditText editor) {
            // Attach the handler to listen for text changes.
            mEditor = editor;
            mEditor.addTextChangedListener(this);
        }

        public ImageSpan getReplySpan() {
            return replySpan;
        }

        public void insert(String emoticon, int resource) {
            // Create the ImageSpan
            Drawable drawable = mEditor.getResources().getDrawable(resource);
            //drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.setBounds(0, 0, mEditor.getLineHeight(), mEditor.getLineHeight());
            ImageSpan span = new ImageSpan(drawable, emoticon, ImageSpan.ALIGN_BOTTOM);

            // Get the selected text.
            int start = mEditor.getSelectionStart();
            int end = mEditor.getSelectionEnd();
            Editable message = mEditor.getEditableText();

            // Insert the emoticon.
            message.replace(start, end, emoticon);
            message.setSpan(span, start, start + emoticon.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 插入回复评论名
        public void insert(String replyName) {

            Editable message = mEditor.getEditableText();
            if (replySpan != null) {
                int start = message.getSpanStart(replySpan);
                int end = message.getSpanEnd(replySpan);
                // Remove the span
                message.removeSpan(replySpan);
                // Remove the remaining emoticon text.
                if (start != end) {
                    message.delete(start, end);
                }
                if(replyName.equals(replySpan.getSource())) {
                    replySpan = null;
                    return;
                }
            }


            // ImageSpan初始化
            TextView textView = (TextView) LayoutInflater.from(mEditor.getContext())
                    .inflate(R.layout.view_comment_reply_name, null);
            textView.setText(replyName);
            int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            textView.measure(spec, spec);
            textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
            Bitmap b = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(b);
            canvas.translate(-textView.getScrollX(), -textView.getScrollY());
            textView.draw(canvas);
            textView.setDrawingCacheEnabled(true);
            Bitmap cacheBmp = textView.getDrawingCache();
            Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
            textView.destroyDrawingCache();  // destory drawable
            // create bitmap drawable for imagespan
            BitmapDrawable bmpDrawable = new BitmapDrawable(mEditor.getResources(), viewBmp);
            bmpDrawable.setBounds(0, 0,bmpDrawable.getIntrinsicWidth(),bmpDrawable.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(bmpDrawable, replyName, ImageSpan.ALIGN_BOTTOM);


            message.replace(0, 0, replyName);
            message.setSpan(imageSpan, 0, replyName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            replySpan = imageSpan;
        }

        @Override
        public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            // Check if some text will be removed.
            if (count > 0) {
                int end = start + count;
                Editable message = mEditor.getEditableText();
                ImageSpan[] list = message.getSpans(start, end, ImageSpan.class);

                for (ImageSpan span : list) {
                    // Get only the emoticons that are inside of the changed
                    // region.
                    int spanStart = message.getSpanStart(span);
                    int spanEnd = message.getSpanEnd(span);
                    if ((spanStart < end) && (spanEnd > start)) {
                        // Add to remove list
                        mEmoticonsToRemove.add(span);
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable text) {
            Editable message = mEditor.getEditableText();

            // Commit the emoticons to be removed.
            for (ImageSpan span : mEmoticonsToRemove) {
                int start = message.getSpanStart(span);
                int end = message.getSpanEnd(span);

                // 删除了回复名
                if(span.getSource().startsWith("@")) {
                    replySpan = null;
                }

                // Remove the span
                message.removeSpan(span);

                // Remove the remaining emoticon text.
                if (start != end) {
                    message.delete(start, end);
                }
            }
            mEmoticonsToRemove.clear();
        }

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {
        }

    }

    public class GridAdapter extends BaseAdapter {

        private Context context;
        private List<String> emoticonStrings = new ArrayList<>();
        private List<Integer> emoticons = new ArrayList<>();

        public GridAdapter(Context context, List<String> emoticonStrings, List<Integer> emoticons) {

            this.context = context;
            this.emoticonStrings = emoticonStrings;
            this.emoticons = emoticons;
        }

        public int getCount() {
            return (emoticons.size() + 5) / 6 * 6;
        }

        public Integer getItem(int position) {
            if(position < emoticons.size())
                return emoticons.get(position);
            else
                return null;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.view_emoticon, parent, false);
            }
            ImageView iv = (ImageView) convertView.findViewById(R.id.iv_emoticon);
            if(position < emoticons.size()) {
                iv.setImageResource(emoticons.get(position));
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textHandler.insert(emoticonStrings.get(position), emoticons.get(position));
                    }
                });
            } else if(position == (emoticons.size() + 5) / 6 * 6 - 1) {
                iv.setImageResource(R.drawable.ic_backspace_grey_400_24dp);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int action = KeyEvent.ACTION_DOWN;
                        int code = KeyEvent.KEYCODE_DEL;
                        KeyEvent event = new KeyEvent(action, code);
                        commentEditText.onKeyDown(code, event);
                    }
                });
            } else {
                iv.setImageDrawable(null);
                iv.setOnClickListener(null);
            }

            return convertView;
        }

    }
}
