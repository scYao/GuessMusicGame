package com.example.administrator.guessmusicgame.uitl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class Util {

    public static View getView(Context context, int layoutId) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, null);
        return view;
    }
}
