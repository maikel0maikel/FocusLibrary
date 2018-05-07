package com.sinohb.lib.keyeventhandle;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class BaseFoucsHandleActivity extends AutoLayoutActivity {
    private Map<View, Integer> focusView = new HashMap<>();
    private List<View> sparseArray = new ArrayList<>();
    private View mFocusView;
    private int size = 0;
    private int index = 1;
    private int currentFocusIndex = 0;
    @Override
    protected void onStart() {
        super.onStart();

    }
    protected void addView(View view){
        focusView.put(view, index);
        sparseArray.add(view);
        size++;
        index++;
    }
    protected void setFirstFocusView(int position){
        mFocusView = sparseArray.get(position);
        currentFocusIndex = position;
    }
    protected void setFirstFocusView(View position){
        mFocusView = position;
    }
    protected void appendView(ViewGroup group) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = group.getChildAt(i);
            if (view == null) {
                continue;
            }
            focusView.put(view, index);
            sparseArray.add(view);
            size++;
            index++;
        }
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        switch (event.getAction()) {
//            case KeyEvent.ACTION_DOWN:
//                return true;
//            case KeyEvent.ACTION_UP:
//                switch (event.getKeyCode()) {
//
//                }
//                return true;
//            default:
//                break;
//        }
//        return super.dispatchKeyEvent(event);
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:

                if (currentFocusIndex == sparseArray.size()-1) {
                    currentFocusIndex = 0;
                }else {
                    currentFocusIndex++;
                }
                View nextV = sparseArray.get(currentFocusIndex);
                nextV.setFocusable(true);
                nextV.requestFocus();
                if (!nextV.hasFocus()){
                    nextV.requestFocus();
                }
                mFocusView = nextV;
                break;
            case KeyEvent.KEYCODE_ENTER:
                mFocusView.performClick();
                break;
            default:
                return super.onKeyDown(keyCode,event);
        }
        return true;
    }

    public abstract ViewGroup getContentViewGroup();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentViewGroup().clearFocus();
    }
}
