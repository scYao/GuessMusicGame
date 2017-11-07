package com.example.administrator.guessmusicgame.uitl;

import android.util.Log;

/**
 * Created by Administrator on 2017/11/7 0007.
 */

public class MyLog {
    public static final boolean DEBUG = true;

    public static  void d(String tag, String message){
        if (DEBUG){
            Log.d(tag, message);
        }
    }

    public static  void w(String tag, String message){
        if (DEBUG){
            Log.w(tag, message);
        }
    }

    public static  void e(String tag, String message){
        if (DEBUG){
            Log.e(tag, message);
        }
    }
}
