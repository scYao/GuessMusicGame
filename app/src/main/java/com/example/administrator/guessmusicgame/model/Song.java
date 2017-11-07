package com.example.administrator.guessmusicgame.model;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class Song {
    private String mSongName;//歌曲名称
    private String mSongFileName;//歌曲文件名称
    private int mNameLength;//歌曲名字长度

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        this.mSongName = songName;

        this.mNameLength = songName.length();
    }

    public String getSongFileName() {
        return mSongFileName;
    }

    public void setSongFileName(String mSongFileName) {
        this.mSongFileName = mSongFileName;
    }

    public int getNameLength() {
        return mNameLength;
    }

    public char[] getNameCharacters(){
        return mSongName.toCharArray();
    }
}
