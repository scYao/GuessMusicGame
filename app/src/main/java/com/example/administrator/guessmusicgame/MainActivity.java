package com.example.administrator.guessmusicgame;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.guessmusicgame.data.Constant;
import com.example.administrator.guessmusicgame.model.IWordButtonClickListener;
import com.example.administrator.guessmusicgame.model.Song;
import com.example.administrator.guessmusicgame.model.WordButton;
import com.example.administrator.guessmusicgame.ui.MyGridView;
import com.example.administrator.guessmusicgame.uitl.Util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //唱片相关动画
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;

    //拨杆相关动画
    private Animation mBarInAnim;
    private LinearInterpolator mPanInLin;

    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;

    // 唱片控件
    private ImageView mViewPan;
    // 拨杆控件
    private ImageView mViewPanBar;

    // Play 按键事件
    private ImageButton mBtnPlayStart;

    // 当前动画是否正在运行
    private boolean mIsRunning = false;

    // 文字框容器
    private List<WordButton> mAllWords;
    private MyGridView mMyGridView;
    private List<WordButton> mBtnSelectWords;

    //已选择文字框UI容器
    private LinearLayout mViewWordsContainer;

    //当前歌曲
    private Song mCurrentSong;
    //当前关的索引
    private int mCurrentStageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化控件
        mMyGridView = (MyGridView) findViewById(R.id.gridview);
        mViewWordsContainer = (LinearLayout) findViewById(R.id.word_select_container);
        mMyGridView.registerOnWordButtonClickListenr(new IWordButtonClickListener() {
            @Override
            public void onWordButtonClick(WordButton wordButton) {

            }
        });

        //初始化动画
        mPanAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);
        mPanAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPanBar.startAnimation(mBarOutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
        mPanInLin = new LinearInterpolator();
        mBarInAnim.setInterpolator(mPanInLin);
        mBarInAnim.setFillAfter(true);
        mBarInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPan.startAnimation(mPanAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_d_45);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setInterpolator(mBarOutLin);
        mBarOutAnim.setFillAfter(true);
        mBarOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsRunning = false;
                mBtnPlayStart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        mBtnPlayStart = (ImageButton) findViewById(R.id.btn_play_start);
        mBtnPlayStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                handlePlayButton();
            }
        });

        // 初始化控件
        mViewPan = (ImageView) findViewById(R.id.imageView1);
        mViewPanBar = (ImageView) findViewById(R.id.imageView2);

        // 初始化游戏数据
        initCurrentStageData();
    }

    private void handlePlayButton() {
        if (!mIsRunning && mViewPanBar != null) {
            mViewPanBar.startAnimation(mBarInAnim);
            mBtnPlayStart.setVisibility(View.INVISIBLE);
            mIsRunning = true;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewPan.clearAnimation();
    }

    private Song loadStageSongInfo(int stageIndex){
        Song song = new Song();

        String[] stage = Constant.SONG_INFO[stageIndex];
        song.setSongFileName(stage[Constant.INDEX_FILE_NAME]);
        song.setSongName(stage[Constant.INDEX_SONG_NAME]);
        return song;
    }

    private void initCurrentStageData() {

        //获取当前关歌曲信息
        mCurrentSong = loadStageSongInfo(mCurrentStageIndex);

        //初始化已选择框
        mBtnSelectWords = initWordSelect();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(140, 140);
        for (int i = 0, length = mBtnSelectWords.size(); i < length; i++) {
            mViewWordsContainer.addView(mBtnSelectWords.get(i).mViewButton, layoutParams);
        }

        // 获得数据
        mAllWords = initAllWord();
        // 更新数据- MyGridView
        mMyGridView.updateData(mAllWords);
    }


    /**
     * 初始化待选文字框
     *
     * @return
     */
    private List<WordButton> initAllWord() {

        List<WordButton> data = new ArrayList<>();

        // 获得所有待选文字
        // .........

        WordButton button;

        for (int i = 0; i < MyGridView.COUNTS_WORDS; i++) {
            button = new WordButton();
            button.mWordStrig = "ss";
            data.add(button);
        }

        return data;
    }

    /**
     * 初始化已选择文字框
     *
     * @return
     */
    private List<WordButton> initWordSelect() {
        List<WordButton> data = new ArrayList<>();

        View view;
        WordButton holder;

        for (int i = 0; i < mCurrentSong.getNameLength(); i++) {
            view = Util.getView(MainActivity.this, R.layout.self_ui_gridview_item);
            holder = new WordButton();
            holder.mViewButton = view.findViewById(R.id.item_btn);
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.mViewButton.setText("");
            holder.mIsVisible = false;
            holder.mViewButton.setBackgroundResource(R.mipmap.game_wordblank);
            data.add(holder);

        }

        return data;
    }

    /**
     * 生成随机汉字
     * @return
     */
    private char getRandomChar(){
        String str = "";
        Integer highPos;
        Integer lowPos;

        Random random = new Random();
        highPos = (176+Math.abs(random.nextInt(39)));
        lowPos =(161+Math.abs(random.nextInt(93)));

        byte[] bytes = new byte[2];

        bytes[0] = highPos.byteValue();
        bytes[1] = lowPos.byteValue();

        try {
            str = new String(bytes,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str.charAt(0);
    }
}
