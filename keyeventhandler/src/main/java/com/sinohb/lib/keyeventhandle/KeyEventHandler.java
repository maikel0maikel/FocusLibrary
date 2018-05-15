package com.sinohb.lib.keyeventhandle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.sinohb.lib.keyeventhandle.bean.FocusView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;


public class KeyEventHandler {

    protected LinkedFocusViewStack<View> mFocusViews = new LinkedFocusViewStack<>();
    private FocusView<View> mCurrentFocusView;
    private int mPosition = -1;
    //private int recycleViewMoveDeration;
    private int recycleFocusPos;
    private boolean isHandleRecyclerView = false;
    private View recycleFocusView = null;
    //private int recycleViewChildCount = 0;
    //    private List<View> mListViews = new ArrayList<>();
//    private boolean isPause = false;
    private boolean isSetFocusInTouch;
    private HashMap<Integer, Drawable> viewDrawableHashMap = new HashMap<>();

    public boolean handleKeyEvent(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (mPosition == -1) {
                    if (mFocusViews.isEmpty()) {
                        return false;
                    }
                    mCurrentFocusView = mFocusViews.get(0);
                    startFocusPosition(0);
                    mPosition = 0;
                    return true;
                }
                if (isHandleRecyclerView) {
                    recycleFocusPos--;
                    //recycleViewMoveDeration = View.FOCUS_UP;
                    handleRecyclerViewItemFocus(recycleFocusPos, View.FOCUS_UP);
                    return true;
                }
                //recycleViewMoveDeration = View.FOCUS_UP;
                mCurrentFocusView = mCurrentFocusView.mUpFocusView;
                setFocus(View.FOCUS_UP);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (mPosition == -1) {
                    if (mFocusViews.isEmpty()) {
                        return false;
                    }
                    mCurrentFocusView = mFocusViews.get(0);
                    startFocusPosition(0);
                    mPosition = 0;
                    return true;
                }
                if (isHandleRecyclerView) {
                    recycleFocusPos++;
                    //recycleViewMoveDeration = View.FOCUS_DOWN;
                    handleRecyclerViewItemFocus(recycleFocusPos, View.FOCUS_DOWN);
                    return true;
                }
                //recycleViewMoveDeration = View.FOCUS_DOWN;
                mCurrentFocusView = mCurrentFocusView.mDownFocusView;
                setFocus(View.FOCUS_DOWN);
                break;
            case KeyEvent.KEYCODE_ENTER:
                if (mPosition == -1) {
                    return false;
                }
                if (mCurrentFocusView.mFocusView instanceof AbsListView) {
                    return false;
                } else if (isHandleRecyclerView && recycleFocusView != null) {
                    recycleFocusView.performClick();
                    recycleFocusView.requestFocus();
                } else {
                    if (mCurrentFocusView == null || mCurrentFocusView.mFocusView == null) {
                        return false;
                    }
                    if (mCurrentFocusView.mFocusView.hasOnClickListeners()) {
                        mCurrentFocusView.mFocusView.performClick();
                    } else {
                        if (mCurrentFocusView.mFocusView instanceof ViewGroup) {
                            performClick((ViewGroup) mCurrentFocusView.mFocusView);
                        }
                    }
                }
                break;
            default:
                if (mCurrentFocusView != null && mCurrentFocusView.mFocusView != null) {
                    mCurrentFocusView.mFocusView.setFocusable(false);
                }
                return false;
        }
        return true;
    }

    private void performClick(ViewGroup group) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = group.getChildAt(i);
            if (view == null) {
                continue;
            }
            if (view instanceof ViewGroup) {
                performClick((ViewGroup) view);
            } else {
                view.performClick();
            }
        }
    }

    public boolean setFocus(int deration) {
        if (mCurrentFocusView == null) {
            if (mFocusViews.isEmpty()) {
                return false;
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
            return false;
        }
        mCurrentFocusView.mFocusView.setFocusableInTouchMode(true);
        mCurrentFocusView.mFocusView.requestFocusFromTouch();
        mCurrentFocusView.mFocusView.setFocusable(true);
        mCurrentFocusView.mFocusView.requestFocus();
        if (mCurrentFocusView.mFocusView instanceof ViewGroup) {
            if (isListGroup(mCurrentFocusView.mFocusView)) {
                handleListView(deration);
                return false;
            } else if (mCurrentFocusView.mFocusView instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) mCurrentFocusView.mFocusView;
                int recycleViewChildCount = recyclerView.getAdapter().getItemCount();
                if (recycleViewChildCount > 0) {
                    isHandleRecyclerView = true;
                    if (deration == View.FOCUS_DOWN) {
                        recycleFocusPos = 0;
                    } else {
                        recycleFocusPos = recycleViewChildCount - 1;
                    }
                    handleRecyclerViewItemFocus(recycleFocusPos, deration);
                    return true;
                } else {
                    isHandleRecyclerView = false;
                    return false;
                }
            } else {
                isHandleRecyclerView = false;
                return false;
            }
        } else {
            return true;
        }
    }

    private void handleListView(int deration) {
        AbsListView listView = (AbsListView) mCurrentFocusView.mFocusView;
        if (listView == null) {
            setFocus(deration);
            return;
        }
        /**
         * 防止多个button共用一个ListView，其中一个button没有列表把recycleView设置为不可见但是又不清空数据的情况
         */
        if (listView.getVisibility() != View.VISIBLE) {
            setFocus(deration);
            return;
        }
        int count = listView.getCount();
        if (count > 0) {
            if (deration == View.FOCUS_DOWN) {
                listView.setSelection(0);
            } else {
                listView.setSelection(listView.getCount() - 1);
            }
        } else {
            setFocus(deration);
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
        if (recyclerView == null) {
            isHandleRecyclerView = false;
            return;
        }
        /**
         * 防止多个button共用一个recycleView，其中一个button没有列表把recycleView设置为不可见但是又不清空数据的情况
         */
        if (recyclerView.getVisibility() != View.VISIBLE) {
            isHandleRecyclerView = false;
            return;
        }
        int recycleViewChildCount = recyclerView.getAdapter().getItemCount();
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
            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        v.setFocusableInTouchMode(false);
                        v.setFocusable(false);
                    }
                }
            });
        } else {
            if (deration == View.FOCUS_DOWN) {
                recycleFocusPos--;
            } else {
                recycleFocusPos++;
            }
        }
        if (recycleFocusPos < -1 || recycleFocusPos > recycleViewChildCount) {
            isHandleRecyclerView = false;
        }
        recycleFocusView = view;
    }


    private boolean isListGroup(View view) {

        return (view instanceof ListView) || (view instanceof GridView);
    }


    public int remove(View view) {
        viewDrawableHashMap.remove(view.getId());
        return mFocusViews.remove(view);
    }

    public int findView(View view) {
        return mFocusViews.findFocusView(view);
    }

    public void clearAllFocus() {
        mFocusViews.clear();
        mPosition = -1;
        viewDrawableHashMap.clear();
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
        handlePreparedView(view);
    }

    private void handlePreparedView(View view) {
        view.setOnFocusChangeListener(new FocusListener());
        hookViewClickListener(view);
    }

    class FocusListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                v.setFocusableInTouchMode(false);
                v.setFocusable(false);
                if (isNoNeedSetBg(v)) {
                    return;
                }
                setNormalBackground(v);
            } else {
                if (isNoNeedSetBg(v)) {
                    return;
                }
                Drawable drawable = null;
                if (v instanceof ImageView) {
                    ImageView imageView = (ImageView) v;
                    drawable = imageView.getDrawable();
                } else {
                    drawable = v.getBackground();
                }
                viewDrawableHashMap.put(v.getId(), drawable);
                ShadowDrawable.setShadowDrawable(v);
            }
        }
    }

    private boolean isNoNeedSetBg(View view) {

        return (view instanceof AbsListView) || (view instanceof RecyclerView) || isHandleRecyclerView;
    }

    public void addPreparedFocusView(final View view, int postion) {
        mFocusViews.add(postion, view);
        handlePreparedView(view);
    }

    public void addPreparedFocusGroup(ViewGroup group) {
        addFocusView(group);
        mCurrentFocusView = mFocusViews.get(0);
        setFocus(View.FOCUS_DOWN);
    }

    public void startFocusPosition(int position) {
        if (position < 0) {
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


    public void handleEdittext(final EditText view) {
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setFocusableInTouchMode(true);
                    view.setFocusable(true);
                    view.requestFocus();
                    return true;
                }
                return false;
            }
        });
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_UP:
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                        case KeyEvent.KEYCODE_ENTER:
                            InputMethodManager imm = (InputMethodManager) v
                                    .getContext().getSystemService(
                                            Context.INPUT_METHOD_SERVICE);
                            if (imm.isActive()) {
                                imm.hideSoftInputFromWindow(
                                        view.getWindowToken(), 0);
                            }
                            v.setFocusableInTouchMode(false);
                            v.setFocusable(false);
                            return true;
                        default:
                            if (!view.hasFocus()) {
                                view.setFocusableInTouchMode(true);
                                view.setFocusable(true);
                                view.requestFocus();
                            }
                            return false;
                    }
                }
                return false;
            }
        });
    }

    private void hookViewTouchListener(View view) {
        try {
            Method getLinstenerInfo = View.class.getDeclaredMethod("getListenerInfo");
            getLinstenerInfo.setAccessible(true);
            Object listenerInfo = getLinstenerInfo.invoke(view);
            Class<?> listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
            Field mOnTouchListener = listenerInfoClz.getDeclaredField("mOnTouchListener");
            mOnTouchListener.setAccessible(true);
            View.OnTouchListener originOnTouchListener = (View.OnTouchListener) mOnTouchListener.get(listenerInfo);
            HookListener hookListener = new HookListener(originOnTouchListener);
            mOnTouchListener.set(listenerInfo, hookListener);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    class HookListener implements View.OnTouchListener {
        private View.OnTouchListener listener;

        public HookListener(View.OnTouchListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return listener.onTouch(v, event);
        }
    }

    private void hookViewClickListener(View view) {
        try {
            Method getLinstenerInfo = View.class.getDeclaredMethod("getListenerInfo");
            getLinstenerInfo.setAccessible(true);
            Object listenerInfo = getLinstenerInfo.invoke(view);
            Class<?> listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
            Field mOnclickListener = listenerInfoClz.getDeclaredField("mOnClickListener");
            mOnclickListener.setAccessible(true);
            View.OnClickListener originOnClickListener = (View.OnClickListener) mOnclickListener.get(listenerInfo);
            HookOnClickListener hookListener = new HookOnClickListener(originOnClickListener);
            mOnclickListener.set(listenerInfo, hookListener);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    class HookOnClickListener implements View.OnClickListener {
        private View.OnClickListener listener;

        public HookOnClickListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if (!isHandleRecyclerView && mCurrentFocusView != null && v != mCurrentFocusView.mFocusView && mCurrentFocusView.mFocusView != null) {
                mCurrentFocusView.mFocusView.setFocusable(false);
                //mCurrentFocusView.mFocusView.setBackground(viewDrawableHashMap.get(mCurrentFocusView.mDownFocusView));
            }
            if (isNoNeedSetBg(v)) {
                return;
            }
            setNormalBackground(v);
            if (listener == null){
                return;
            }
            listener.onClick(v);
        }
    }

    private void setNormalBackground(View v) {
        Drawable drawable = viewDrawableHashMap.get(v.getId());
        if (drawable != null) {
            if (v instanceof ImageView) {
                ImageView imageView = (ImageView) v;
                imageView.setImageDrawable(drawable);
            } else {
                if (Build.VERSION.SDK_INT >= 16) {
                    v.setBackground(drawable);
                } else {
                    v.setBackgroundDrawable(drawable);
                }
            }
        }
    }
}
