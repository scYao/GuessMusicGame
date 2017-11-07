package com.example.administrator.guessmusicgame.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.administrator.guessmusicgame.R;
import com.example.administrator.guessmusicgame.model.IWordButtonClickListener;
import com.example.administrator.guessmusicgame.model.WordButton;
import com.example.administrator.guessmusicgame.uitl.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class MyGridView extends GridView {
    private List<WordButton> wordButtonList = new ArrayList<>();
    private MyGridAdapter myGridAdapter;
    private Context mContext;

    public final static int COUNTS_WORDS = 24;

    private Animation mScaleAnimation;

    private IWordButtonClickListener mWordButtonClickListener;


    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        myGridAdapter = new MyGridAdapter();
        this.setAdapter(myGridAdapter);
    }

    public void updateData(List<WordButton> wordButtonList){
        this.wordButtonList =wordButtonList;
        setAdapter(myGridAdapter);
    }

    class MyGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return wordButtonList == null ? 0 : wordButtonList.size();
        }

        @Override
        public Object getItem(int i) {
            return wordButtonList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            final WordButton holder;

            if (view == null){
                view = Util.getView(mContext, R.layout.self_ui_gridview_item);
                holder = wordButtonList.get(i);
                holder.mIndex = i;
                holder.mViewButton = view.findViewById(R.id.item_btn);

                holder.mViewButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mWordButtonClickListener.onWordButtonClick(holder);
                    }
                });

                //加载动画
                mScaleAnimation = AnimationUtils.loadAnimation(mContext,R.anim.scale);
                mScaleAnimation.setStartOffset(i*100);//动画延迟时间


                view.setTag(holder);
            }else {
                holder = (WordButton) view.getTag();
            }
            holder.mViewButton.setText(holder.mWordStrig);

            view.startAnimation(mScaleAnimation);
            return view;
        }
    }

    public void registerOnWordButtonClickListenr(IWordButtonClickListener iWordButtonClickListener){
        this.mWordButtonClickListener = iWordButtonClickListener;
    }
}
