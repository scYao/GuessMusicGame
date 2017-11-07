package com.example.administrator.guessmusicgame.model;

import android.widget.Button;

/**
 * 文字按钮
 * Created by Administrator on 2017/9/21 0021.
 */

public class WordButton {
    public int mIndex;
    public boolean mIsVisible=false;
    public String mWordStrig;
    public Button mViewButton;

    public WordButton() {
        mIsVisible = true;
        mWordStrig ="";
    }
}
