package com.huskyyy.anotheryouku.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.data.BaseCallback;
import com.huskyyy.anotheryouku.data.DataSource;
import com.huskyyy.anotheryouku.data.base.Captcha;
import com.huskyyy.anotheryouku.data.base.Error;
import com.huskyyy.anotheryouku.util.ToastUtils;

/**
 * Created by Wang on 2016/8/18.
 */
public class CaptchaDialog extends AppCompatDialog {

    private Context context;
    private ImageView captchaImageView;
    private AppCompatEditText captchaEditText;
    private TextView cancelTextView;
    private TextView confirmTextView;

    private String key;
    private Bitmap bitmap;
    private OnButtonClickListener listener;

    public CaptchaDialog (Context context){
        super(context);
        this.context = context;
    }

    public CaptchaDialog(Context context, String key, Bitmap bitmap,
                         OnButtonClickListener listener) {
        this(context);
        this.key = key;
        this.bitmap = bitmap;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = LayoutInflater.from(context).inflate(R.layout.dialog_captcha, null);
        captchaImageView = (ImageView) root.findViewById(R.id.iv_captcha);
        captchaEditText = (AppCompatEditText) root.findViewById(R.id.et_captcha);
        cancelTextView = (TextView) root.findViewById(R.id.tv_cancel);
        confirmTextView = (TextView) root.findViewById(R.id.tv_confirm);
        this.setContentView(root);

        captchaImageView.setImageBitmap(bitmap);
        captchaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSource.getInstance().getCaptchaData(new BaseCallback<Captcha>() {
                    @Override
                    public void onDataLoaded(Captcha captcha) {
                        key = captcha.getKey();
                        bitmap = captcha.getBitmap();
                        captchaImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {
                        ToastUtils.showShort(R.string.captcha_refresh_failed);
                    }
                });
            }
        });

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptchaDialog.this.dismiss();
            }
        });

        confirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptchaDialog.this.dismiss();
                listener.onConfirm(key, captchaEditText.getText().toString());
            }
        });

        captchaEditText.requestFocus();
    }



    public interface OnButtonClickListener {

        void onConfirm(String key, String captcha);
    }


}
