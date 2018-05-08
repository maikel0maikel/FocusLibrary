package com.sinohb.lib.keyeventhandle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;

import com.sinohb.lib.keyeventhandle.bean.FocusView;


public class KeyEventHandler {

    protected LinkedFocusViewStack<View> mFocusViews = new LinkedFocusViewStack<>();
    private FocusView<View> mCurrentFocusView;
    private int mPosition = -1;
    private int recycleViewMoveDeration;
    private int recycleFocusPos;
    private boolean isHandleRecyclerView = false;
    //private int recycleViewChildCount = 0;
    //    private List<View> mListViews = new ArrayList<>();
//    private boolean isPause = false;
    private boolean isSetFocusInTouch;

    public boolean handleKeyEvent(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (mPosition == -1) {
                    mCurrentFocusView = mFocusViews.get(0);
                    startFocusPosition(0);
                    mPosition = 0;
                    return true;
                }
                if (isHandleRecyclerView) {
                    recycleFocusPos--;
                    recycleViewMoveDeration = View.FOCUS_UP;
                    handleRecyclerViewItemFocus(recycleFocusPos, View.FOCUS_UP);
                    return true;
                }
                recycleViewMoveDeration = View.FOCUS_UP;
                mCurrentFocusView = mCurrentFocusView.mUpFocusView;
                setFocus(View.FOCUS_UP);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (mPosition == -1) {
                    mCurrentFocusView = mFocusViews.get(0);
                    startFocusPosition(0);
                    mPosition = 0;
                    return true;
                }
                if (isHandleRecyclerView) {
                    recycleFocusPos++;
                    recycleViewMoveDeration = View.FOCUS_DOWN;
                    handleRecyclerViewItemFocus(recycleFocusPos, View.FOCUS_DOWN);
                    return true;
                }
                recycleViewMoveDeration = View.FOCUS_DOWN;
                mCurrentFocusView = mCurrentFocusView.mDownFocusView;
                setFocus(View.FOCUS_DOWN);
                break;
            case KeyEvent.KEYCODE_ENTER:
                if (mPosition == -1) {
                    return false;
                }
                if (mCurrentFocusView.mFocusView instanceof AbsListView) {
                    return false;
                } else {
                    mCurrentFocusView.mFocusView.performClick();
                }
                break;
            default:
                return false;
        }
        return true;
    }

    public void setFocus(int deration) {
        if (mCurrentFocusView == null) {
            if (mFocusViews.isEmpty()) {
                return;
            }
            mCurrentFocusView = mFocusViews.get(0);
        }
        if (mCurrentFocusView == mFocusViews.getHeader() || mCurrentFocusView.mFocusView == null) {
            if (deration == View.FOCUS_DOWN) {
                mCurrentFocusView = mCurrentFocusView.mDownFocusView;
            } else {
                mCurrentFocusView = mCurrentFocusView.mUpFocusView;
            }
        }
        if (mCurrentFocusView.mFocusView == null) {
            return;
        }
        mCurrentFocusView.mFocusView.setFocusableInTouchMode(true);
        mCurrentFocusView.mFocusView.requestFocusFromTouch();
        mCurrentFocusView.mFocusView.setFocusable(true);
        mCurrentFocusView.mFocusView.requestFocus();
        if (mCurrentFocusView.mFocusView instanceof ViewGroup) {
            if (isListGroup(mCurrentFocusView.mFocusView)) {
                handleListView(deration);
            } else if (mCurrentFocusView.mFocusView instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) mCurrentFocusView.mFocusView;
              int  recycleViewChildCount = recyclerView.getAdapter().getItemCount();
                if (recycleViewChildCount > 0) {
                    isHandleRecyclerView = true;
                    if (deration == View.FOCUS_DOWN) {
                        recycleFocusPos = 0;
                    } else {
                        recycleFocusPos = recycleViewChildCount - 1;
                    }
                    handleRecyclerViewItemFocus(recycleFocusPos, deration);
                }else {
                    isHandleRecyclerView = false;
                }
            }else {
                isHandleRecyclerView = false;
            }
        }
    }

    private void handleListView(int deration) {
        AbsListView listView = (AbsListView) mCurrentFocusView.mFocusView;
        if (deration == View.FOCUS_DOWN) {
            listView.setSelection(0);
        } else {
            listView.setSelection(listView.getCount() - 1);
        }
    }

    private void handleRecyclerViewItemFocus(int pos, int deration) {

        if (pos <= -1 && deration == View.FOCUS_UP) {
            isHandleRecyclerView = false;
            mCurrentFocusView = mCurrentFocusView.mUpFocusView;
            setFocus(View.FOCUS_UP);
            return;
        }
        RecyclerView recyclerView = (RecyclerView) mCurrentFocusView.mFocusView;
        int  recycleViewChildCount = recyclerView.getAdapter().getItemCount();
        if (pos >= recycleViewChildCount && deration == View.FOCUS_DOWN) {
            isHandleRecyclerView = false;
            mCurrentFocusView = mCurrentFocusView.mDownFocusView;
            setFocus(View.FOCUS_DOWN);
            return;
        }
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        manager.scrollToPosition(pos);
        View view = manager.findViewByPosition(pos);
        if (view != null) {
            view.setFocusableInTouchMode(true);
            view.setFocusable(true);
            view.requestFocusFromTouch();
            view.requestFocus();
        }else {
            if (deration == View.FOCUS_DOWN){
                recycleFocusPos--;
            }else {
                recycleFocusPos++;
            }
        }
    }


    private boolean isListGroup(View view) {

        return (view instanceof ListView) || (view instanceof GridView);
    }


    public int remove(View view) {
        return mFocusViews.remove(view);
    }

    public int findView(View view) {
        return mFocusViews.findFocusView(view);
    }

    public void clearAllFocus() {
        mFocusViews.clear();
        mPosition = -1;
    }

    public void addFocusView(ViewGroup group) {
        if (group instanceof ListView) {
            addFocusView((ListView) group);
            return;
        }
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childeView = group.getChildAt(i);
            if (childeView == null) {
                continue;
            }
//            if (!isPause){
//                mListViews.add(childeView);
//            }
            addPreparedFocusView(childeView);
        }
    }

    public void addFilterListenerView(ViewGroup group) {
        int childCount = group.getChildCount();
        if (group instanceof ListView) {
            addFocusView((ListView) group);
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View childeView = group.getChildAt(i);
            if (childeView == null) {
                continue;
            }
            if (!childeView.hasOnClickListeners()) {
                continue;
            }
//            if (!isPause){
//                mListViews.add(childeView);
//            }
            addPreparedFocusView(childeView);
        }
    }

    public void addFocusView(final ListView group) {
        int childCount = group.getCount();
        for (int i = 0; i < childCount; i++) {
            View childeView = group.getAdapter().getView(i, null, group);
            if (childeView == null) {
                continue;
            }
            addPreparedFocusView(childeView);
        }
    }

    public void addPreparedFocusView(final View view) {
        mFocusViews.add(view);
//        if (!isPause){
//            mListViews.add(view);
//        }
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    v.setFocusableInTouchMode(false);
                }
//                else if (view instanceof RecyclerView){
////                    RecyclerView recyclerView = (RecyclerView) view;
////                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
////                    recyclerView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
////                    manager.setStackFromEnd(true);
////                    int pos = 0;
////                    if (recyclerView.getAdapter().getItemCount() > 0) {
////                        if (recycleViewMoveDeration == View.FOCUS_DOWN) {
////                            pos = 0;
////                        } else {
////                            pos = recyclerView.getAdapter().getItemCount() - 1;
////                        }
////                        View view = manager.findViewByPosition(3);
////                        if (view!=null){
////                            view.setFocusable(true);
////                            view.requestFocus();
////                            recyclerView.smoothScrollToPosition(3);
////                        }
////                    }
//                    RecyclerView recyclerView = (RecyclerView) mCurrentFocusView.mFocusView;
//                    recycleViewChildCount = recyclerView.getAdapter().getItemCount();
//                    if (recycleViewMoveDeration == View.FOCUS_DOWN) {
//                        recycleFocusPos = 0;
//                    } else {
//                        recycleFocusPos = recycleViewChildCount - 1;
//                    }
//                    handleRecyclerViewItemFocus(recycleFocusPos, recycleViewMoveDeration);
//                }
            }
        });
    }

    public void addPreparedFocusView(final View view, int postion) {
        mFocusViews.add(postion, view);
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    v.setFocusableInTouchMode(false);
                }
            }
        });
    }

    public void addPreparedFocusGroup(ViewGroup group) {
        addFocusView(group);
        mCurrentFocusView = mFocusViews.get(0);
        setFocus(View.FOCUS_DOWN);
    }

    public void startFocusPosition(int position) {
        if (position<0){
            position = 0;
        }
        mPosition = 0;
        this.mCurrentFocusView = this.mFocusViews.get(position);
        this.setFocus(View.FOCUS_DOWN);
    }
//    public void pauseFocusViews() {
//        clearAllFocus();
//        isPause = true;
//    }
//
//    public void resumeFocuViews() {
//        if (isPause) {
//            clearAllFocus();
//            for (View view : mListViews) {
//                mFocusViews.add(view);
//            }
//            isPause = false;
//            mListViews.clear();
//        }
//        startFocusPosition(0);
//    }

    public void setSetFocusInTouch(boolean focusInTouch) {
        this.isSetFocusInTouch = focusInTouch;
    }
}
