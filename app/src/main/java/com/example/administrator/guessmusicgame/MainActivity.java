package com.example.administrator.guessmusicgame;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.example.administrator.guessmusicgame.uitl.MyLog;
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

    private static final String TAG = "MainActivity";

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
                setSelectWord(wordButton);
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

    /**
     * 设置答案
     *
     * @param wordButton
     */
    private void setSelectWord(WordButton wordButton) {
        for (int i = 0, length = mBtnSelectWords.size(); i < length; i++) {

            if (mBtnSelectWords.get(i).mWordStrig.length() == 0) {

                //设置答案文字框内容及可见性
                mBtnSelectWords.get(i).mViewButton.setText(wordButton.mWordStrig);
                mBtnSelectWords.get(i).mIsVisible = true;
                mBtnSelectWords.get(i).mWordStrig = wordButton.mWordStrig;

                //记录索引
                mBtnSelectWords.get(i).mIndex = wordButton.mIndex;

                MyLog.d(TAG, mBtnSelectWords.get(i).mIndex + "");

                //设置待选框的可见性
                setButtonVisible(wordButton, View.INVISIBLE);
                break;
            }
        }
    }

    private void clearTheAnswer(WordButton wordButton) {

        //设置已选框不可见
        wordButton.mViewButton.setText("");
        wordButton.mWordStrig = "";
        wordButton.mIsVisible = false;

        //设置待选框可见
        setButtonVisible(mAllWords.get(wordButton.mIndex), View.VISIBLE);
    }

    /**
     * 设置待选文字框是否可见
     *
     * @param wordButton
     * @param invisible
     */
    private void setButtonVisible(WordButton wordButton, int invisible) {
        wordButton.mViewButton.setVisibility(invisible);
        wordButton.mIsVisible = (invisible == View.VISIBLE) ? true : false;

        MyLog.d(TAG, wordButton.mIsVisible + "");
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

    private Song loadStageSongInfo(int stageIndex) {
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
        String[] words = generateWords();

        WordButton button;

        for (int i = 0; i < MyGridView.COUNTS_WORDS; i++) {
            button = new WordButton();
            button.mWordStrig = words[i];
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


        for (int i = 0; i < mCurrentSong.getNameLength(); i++) {
            view = Util.getView(MainActivity.this, R.layout.self_ui_gridview_item);
            final WordButton holder = new WordButton();
            holder.mViewButton = view.findViewById(R.id.item_btn);
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.mViewButton.setText("");
            holder.mIsVisible = false;
            holder.mViewButton.setBackgroundResource(R.mipmap.game_wordblank);
            holder.mViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearTheAnswer(holder);
                }
            });
            data.add(holder);

        }

        return data;
    }

    /**
     * 生成所有待选文字
     *
     * @return
     */
    private String[] generateWords() {
        Random random = new Random();
        String[] words = new String[MyGridView.COUNTS_WORDS];

        //存入歌名
        for (int i = 0, length = mCurrentSong.getNameLength(); i < length; i++) {
            words[i] = mCurrentSong.getNameCharacters()[i] + "";
        }

        //获取随机文字并存入数组中
        for (int i = mCurrentSong.getNameLength(), length = MyGridView.COUNTS_WORDS; i < length; i++) {
            words[i] = getRandomChar() + "";
        }

        //打乱文字顺序:每一个元素都随机选择一个元素与之交换
        for (int i = MyGridView.COUNTS_WORDS - 1; i >= 0; i--) {
            int index = random.nextInt(i + 1);
            String buf = words[index];
            words[index] = words[i];
            words[i] = buf;
        }

        return words;
    }

    /**
     * 生成随机汉字
     *
     * @return char
     */
    private char getRandomChar() {
        String str = "";
        Integer highPos;
        Integer lowPos;

        Random random = new Random();
        highPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] bytes = new byte[2];

        bytes[0] = highPos.byteValue();
        bytes[1] = lowPos.byteValue();

        try {
            str = new String(bytes, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str.charAt(0);
    }
}
