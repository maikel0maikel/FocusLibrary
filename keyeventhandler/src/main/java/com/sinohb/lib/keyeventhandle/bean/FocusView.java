package com.sinohb.lib.keyeventhandle.bean;


public class FocusView<T> {
    public FocusView mUpFocusView;
    public FocusView mDownFocusView;
    public T mFocusView;

    public FocusView(T currentFocusView, FocusView upFocus, FocusView downFocus) {
        this.mFocusView = currentFocusView;
        this.mUpFocusView = upFocus;
        this.mDownFocusView = downFocus;
    }

    public FocusView(T view) {
        this(view, null, null);
    }
}

