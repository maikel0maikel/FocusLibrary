package com.sinohb.lib.keyeventhandle;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sinohb.lib.keyeventhandle.bean.FocusView;


public abstract class BaseFoucsHandlerActivity extends Activity {
    protected LinkedFocusViewStack<View> mFocusViews = new LinkedFocusViewStack<>();
    private FocusView<View> mCurrentFocusView;

    @Override
    protected void onStart() {
        super.onStart();
        addFocusView();
        mCurrentFocusView = mFocusViews.get(0);
        setFocus(View.FOCUS_DOWN);
    }

    private void setFocus(int deration) {
        if (mCurrentFocusView.mFocusView == null) {
            if (deration == View.FOCUS_DOWN) {
                mCurrentFocusView = mCurrentFocusView.mDownFocusView;
            } else {
                mCurrentFocusView = mCurrentFocusView.mUpFocusView;
            }
        }
        mCurrentFocusView.mFocusView.setFocusable(true);
        mCurrentFocusView.mFocusView.requestFocus();
    }

    private void addFocusView() {
        ViewGroup group = getContentViewGroup();
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childeView = group.getChildAt(i);
            if (childeView == null) {
                continue;
            }
            if (!childeView.hasOnClickListeners()){
                continue;
            }
            mFocusViews.add(childeView);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getAction()) {
            case KeyEvent.ACTION_UP:
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        mCurrentFocusView = mCurrentFocusView.mUpFocusView;
                        setFocus(View.FOCUS_UP);
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        mCurrentFocusView = mCurrentFocusView.mDownFocusView;
                        setFocus(View.FOCUS_DOWN);
                        break;
                    case KeyEvent.KEYCODE_ENTER:
                        mCurrentFocusView.mFocusView.performClick();
                        break;
                    default:
                        return super.dispatchKeyEvent(event);
                }
                return true;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    public abstract ViewGroup getContentViewGroup();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentViewGroup().clearFocus();
    }
}
