package com.sinohb.lib.keyeventhandle;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;

import com.zhy.autolayout.AutoLayoutActivity;


public abstract class BaseFoucsHandlerActivity extends AutoLayoutActivity {
    private KeyEventHandler mHandler = new KeyEventHandler();
    @Override
    protected void onStart() {
        super.onStart();
    }

    public void addPreparedFocusGroup() {
        mHandler.addPreparedFocusGroup(getContentViewGroup());
    }
    public void addPreparedFocusGroup(ViewGroup group) {
        mHandler.addPreparedFocusGroup(group);
    }
    public void startFocusPosition(int position) {
        mHandler.startFocusPosition(position);
    }


    public int remove(View view) {
        return mHandler.remove(view);
    }
    public int findView(View view){
        return mHandler.findView(view);
    }
    public void clearAllFocus() {
        mHandler.clearAllFocus();
    }

    public void addFocusView(ViewGroup group) {
        mHandler.addFocusView(group);
    }

    public void addFilterListenerView(ViewGroup group) {
        mHandler.addFilterListenerView(group);
    }

    public void addFocusView(AbsListView group) {
        mHandler.addFocusView(group);
    }

    public void addPreparedFocusView(View view) {
        mHandler.addPreparedFocusView(view);
    }
    public void addPreparedFocusView(View view,int position) {
        mHandler.addPreparedFocusView(view,position);
    }
    public void handleEdittext(EditText editText) {
        mHandler.handleEdittext(editText);
    }
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        switch (event.getAction()) {
//            case KeyEvent.ACTION_DOWN:
//                switch (event.getKeyCode()){
//                    case KeyEvent.KEYCODE_DPAD_UP:
//                        return true;
//                    case KeyEvent.KEYCODE_DPAD_DOWN:
//                        return true;
//                    case KeyEvent.KEYCODE_ENTER:
//                        return true;
//                    default:
//                        return super.dispatchKeyEvent(event);
//                }
//            case KeyEvent.ACTION_UP:
//                switch (event.getKeyCode()) {
//                    case KeyEvent.KEYCODE_DPAD_UP:
//                        mCurrentFocusView = mCurrentFocusView.mUpFocusView;
//                        setFocus(View.FOCUS_UP);
//                        break;
//                    case KeyEvent.KEYCODE_DPAD_DOWN:
//                        mCurrentFocusView = mCurrentFocusView.mDownFocusView;
//                        setFocus(View.FOCUS_DOWN);
//                        break;
//                    case KeyEvent.KEYCODE_ENTER:
//                        mCurrentFocusView.mFocusView.performClick();
//                        break;
//                    default:
//                        return super.dispatchKeyEvent(event);
//                }
//                return true;
//            default:
//                break;
//        }
//        return super.dispatchKeyEvent(event);
//    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mHandler.handleKeyEvent(keyCode,event)){
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public abstract ViewGroup getContentViewGroup();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.clearAllFocus();
    }
}
