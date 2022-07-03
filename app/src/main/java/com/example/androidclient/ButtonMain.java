package com.example.androidclient;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.androidclient.Button.LikeButton;
import com.example.androidclient.Button.OnLikeListener;

public class ButtonMain extends Activity implements OnLikeListener {
    LikeButton starButton;
    LikeButton likeButton;
    LikeButton thumbButton;
    LikeButton smileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.button_test);

        starButton= (LikeButton)this.findViewById(R.id.star_button);
        likeButton= (LikeButton)this.findViewById(R.id.heart_button);
        smileButton= (LikeButton)this.findViewById(R.id.smile_button);
        thumbButton= (LikeButton)this.findViewById(R.id.thumb_button);

        starButton.setOnLikeListener(this);
        likeButton.setOnLikeListener(this);
        smileButton.setOnLikeListener(this);
        thumbButton.setOnLikeListener(this);

        thumbButton.setLiked(true);

        usingCustomIcons();
    }
    public void usingCustomIcons() {
        smileButton.setUnlikeDrawableRes(R.drawable.heart_off);
        smileButton.setLikeDrawableRes(R.drawable.heart_on);
    }
    @Override
    public void liked(LikeButton likeButton) {
        Toast.makeText(this, "Liked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        Toast.makeText(this, "Disliked!", Toast.LENGTH_SHORT).show();
    }
}