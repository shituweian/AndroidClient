package com.example.androidclient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.classichu.lineseditview.LinesEditView;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.vstechlab.easyfonts.EasyFonts;
import com.white.countdownbutton.CountDownButton;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;
import cn.carbs.android.avatarimageview.library.AvatarImageView;


public class test extends Activity {

    private CountDownButton mCountDownButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        mCountDownButton = (CountDownButton) findViewById(R.id.test_cdb);
// 设置点击事件
        mCountDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击按钮要执行的操作,同时开始倒计时
                Toast.makeText(test.this, "click countdown button", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

