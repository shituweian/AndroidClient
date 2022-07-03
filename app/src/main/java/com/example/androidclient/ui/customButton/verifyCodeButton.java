package com.example.androidclient.ui.customButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.androidclient.R;

@SuppressLint("AppCompatCustomView")
public class verifyCodeButton extends Button {
    private Context mContext;
    private int mClickedBackground;
    private int mBackground;
    private int mWaitedBackground;
    private String mCountdownownText;
    private int mCountdownTime = 60;
    private TimeCount mTimeCount;

    public verifyCodeButton(Context context){
        this(context,null);
    }

    public verifyCodeButton(Context context, AttributeSet attrs){
        this(context,attrs,android.R.attr.buttonStyle);
    }

    public verifyCodeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs){
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.VerifyCodeButton);
        mBackground = typedArray.getResourceId(R.styleable.VerifyCodeButton_android_background,mBackground);
        mClickedBackground = typedArray.getResourceId(R.styleable.VerifyCodeButton_clickedBackground,mClickedBackground);
        mWaitedBackground=typedArray.getResourceId(R.styleable.VerifyCodeButton_waitedBackground,mWaitedBackground);
        mCountdownTime=typedArray.getInt(R.styleable.VerifyCodeButton_countdownTime,mCountdownTime);
        mCountdownownText=typedArray.getString(R.styleable.VerifyCodeButton_countdownText);
        typedArray.recycle();
    }

    private void init(){
        setBackgroundResource(mBackground);
        mTimeCount=new TimeCount(mCountdownTime*1000,1000);
    }

    public void start(){
        mTimeCount.start();
    }

    public void waited(){
        setBackgroundResource(mWaitedBackground);
        setClickable(false);
    }

    public void cancle(){
        mTimeCount.cancel();
        setClickable(true);
        setText(mCountdownownText!=null?mCountdownownText:"");
        setBackgroundResource(mBackground);
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval){
            super(millisInFuture,countDownInterval);
        }
        public void onTick(long millisUntilFinished){
            setClickable(false);
            setText(String.valueOf(millisUntilFinished/1000+"s"));
            setBackgroundResource(mClickedBackground);
        }

        public void onFinish(){
            setClickable(true);
            setText(mCountdownownText!=null?mCountdownownText:"");
            setBackgroundResource(mBackground);
        }
    }

}
