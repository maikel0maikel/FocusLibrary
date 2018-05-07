package com.sinohb.lib.keyeventhandle;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.PopupWindow;

import com.sinohb.lib.keyeventhandle.bean.FocusView;


public abstract class BaseFocusPopupWindow extends PopupWindow implements View.OnKeyListener {
    protected LinkedFocusViewStack<View> mFocusViews = new LinkedFocusViewStack<>();
    private FocusView<View> mCurrentFocusView;

    public BaseFocusPopupWindow(Context context) {
        super(context);
        setFocusable(true);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (KeyEvent.ACTION_DOWN == event.getAction()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    mCurrentFocusView = mCurrentFocusView.mUpFocusView;
                    setFocus(View.FOCUS_UP);
                    return true;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    mCurrentFocusView = mCurrentFocusView.mDownFocusView;
                    setFocus(View.FOCUS_DOWN);
                    return true;
                case KeyEvent.KEYCODE_ENTER:
                    if (mCurrentFocusView.mFocusView instanceof AbsListView) {
                        return false;
                    } else {
                        if (mCurrentFocusView.mFocusView.hasOnClickListeners()) {
                            mCurrentFocusView.mFocusView.performClick();
                        } else {
                            if (mCurrentFocusView.mFocusView instanceof ViewGroup) {
                                performClick((ViewGroup)mCurrentFocusView.mFocusView);
                            }
                        }
                    }
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    private void performClick(ViewGroup group) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = group.getChildAt(i);
            if (view == null) {
                continue;
            }
//            if (view.hasOnClickListeners()) {
//                view.performClick();
//            } else if (view instanceof ViewGroup) {
//                performClick((ViewGroup) view);
//            }
            if (view instanceof ViewGroup){
                performClick((ViewGroup) view);
            }else {
                view.performClick();
            }
        }
    }


    private void setFocus(int deration) {
        if (mCurrentFocusView == null) {
            if (mFocusViews.isEmpty()) {
                return;
            }
            mCurrentFocusView = mFocusViews.get(0);
        }

        if (deration == View.FOCUS_DOWN) {
            if (mCurrentFocusView.mFocusView == null) {
                mCurrentFocusView = mCurrentFocusView.mDownFocusView;
            }
        } else {
            if (mCurrentFocusView.mFocusView == null) {
                mCurrentFocusView = mCurrentFocusView.mUpFocusView;
            }
        }
        if (mCurrentFocusView.mUpFocusView == null) {
            return;
        }
//        mCurrentFocusView.mFocusView.setFocusableInTouchMode(true);
//        mCurrentFocusView.mFocusView.requestFocusFromTouch();
        mCurrentFocusView.mFocusView.setFocusable(true);
        mCurrentFocusView.mFocusView.requestFocus();
    }

    public void startFocusPosition(int position) {
        this.mCurrentFocusView = this.mFocusViews.get(position);
        this.setFocus(View.FOCUS_DOWN);
    }

    public void addPreparedFocusView(View view) {
        mFocusViews.add(view);
        view.setOnKeyListener(this);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        mFocusViews.clear();
    }
//    @Override
//    public void showAtLocation(View parent, int gravity, int x, int y) {
//        setKeyListener();
//        super.showAtLocation(parent, gravity, x, y);
//    }

    public void setKeyListener() {
        getContentView().setFocusable(true);
        getContentView().setFocusableInTouchMode(true);
        getContentView().setOnKeyListener(this);
    }
}
