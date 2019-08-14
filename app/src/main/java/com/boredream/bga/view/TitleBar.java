package com.boredream.bga.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bga.R;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chunyangli on 2018/1/31.
 */
public class TitleBar extends FrameLayout {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_iv_left)
    ImageView titleIvLeft;

    public TitleBar(Context context) {
        super(context);
        initView(context, null);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_titlebar, this);
        ButterKnife.bind(this);

//        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
//        String aa = a.getString(R.styleable.TitleBar_title);
//        tv.setText(aa);
//        a.recycle();
    }

    public void setTitle(CharSequence title) {
        titleTv.setText(title);
    }

}
