package com.sinohb.lib.keyeventhandle.bean;


public class FocusView<T> {
    public FocusView mUpFocusView;
    public FocusView mDownFocusView;
    public T mFocusView;
    public int position;
    public boolean isKeepShadow;
    public boolean isKeep;
    public FocusView(T currentFocusView, FocusView upFocus, FocusView downFocus, int position) {
        this.mFocusView = currentFocusView;
        this.mUpFocusView = upFocus;
        this.mDownFocusView = downFocus;
        this.position = position;
    }

    public FocusView(T view) {
        this(view, null, null,0);
    }
}

