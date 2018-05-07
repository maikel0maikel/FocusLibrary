package com.sinohb.lib.focus.example;

import android.app.Instrumentation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.sinohb.lib.focus.example.fragment.FragmentFive;
import com.sinohb.lib.focus.example.fragment.FragmentFour;
import com.sinohb.lib.focus.example.fragment.FragmentOne;
import com.sinohb.lib.focus.example.fragment.FragmentSeven;
import com.sinohb.lib.focus.example.fragment.FragmentSix;
import com.sinohb.lib.focus.example.fragment.FragmentThree;
import com.sinohb.lib.focus.example.fragment.FragmentTwo;
import com.sinohb.lib.keyeventhandle.BaseFocusFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity2 extends AppCompatActivity {
    private List<Fragment> fragments = new ArrayList<>();
    private boolean send;
    private Thread sendThead;
    private BaseFocusFragment currentFragment;
    private int currentIndex = 0;
    private static final String CURRENT_INDEX = "CURRENT_INDEX";
    private ListView mListview;
    private ViewGroup mViewGropu;
    View backV;
    private Map<View, Integer> focusView = new HashMap<>();
    private SparseArray<View> sparseArray = new SparseArray<>(64);
    private int lastViewid;
    private View mFocusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_main);
        mViewGropu = findViewById(R.id.rootView);
        getWindow().getDecorView().clearFocus();
        mListview = findViewById(R.id.listFragments);
        List<String> datas = initData();
        MyAdapterList<String> myAdapter = new MyAdapterList<>(datas, this);
        mListview.setAdapter(myAdapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showFragment(position);
                view.setSelected(true);

            }
        });
        final FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            FragmentOne fragmentOne = (FragmentOne) fragmentManager.findFragmentByTag("TAG" + 0);
            FragmentTwo fragmentTwo = (FragmentTwo) fragmentManager.findFragmentByTag("TAG" + 1);
            FragmentThree fragmentThree = (FragmentThree) fragmentManager.findFragmentByTag("TAG" + 2);
            FragmentFour fragmentFour = (FragmentFour) fragmentManager.findFragmentByTag("TAG" + 3);
            FragmentFive fragmentFive = (FragmentFive) fragmentManager.findFragmentByTag("TAG" + 4);
            FragmentSix fragmentSix = (FragmentSix) fragmentManager.findFragmentByTag("TAG" + 5);
            FragmentSeven fragmentSeven = (FragmentSeven) fragmentManager.findFragmentByTag("TAG" + 6);
            fragments.add(fragmentOne);
            fragments.add(fragmentTwo);
            fragments.add(fragmentThree);
            fragments.add(fragmentFour);
            fragments.add(fragmentFive);
            fragments.add(fragmentSix);
            fragments.add(fragmentSeven);
            currentIndex = savedInstanceState.getInt(CURRENT_INDEX, 0);
            restoreFragment();
        } else {
            fragments.add(new FragmentOne());
            fragments.add(new FragmentTwo());
            fragments.add(new FragmentThree());
            fragments.add(new FragmentFour());
            fragments.add(new FragmentFive());
            fragments.add(new FragmentSix());
            fragments.add(new FragmentSeven());
            showFragment(0);
        }

        backV = findViewById(R.id.backBtn);
        focusView.put(backV, 1);
        sparseArray.append(1, backV);
        Log.e("onKeyDown", "backVid:" + backV.getId());

        backV.setFocusable(true);
        backV.requestFocus();
        mFocusView = backV;
        mListview.post(new Runnable() {
            @Override
            public void run() {
                appendView(mListview, focusView.size());
                currentFragment.getContentViewGroup().post(new Runnable() {
                    @Override
                    public void run() {
                        appendView(currentFragment.getContentViewGroup(), focusView.size());
                    }
                });
            }
        });

//        sendThead = new Thread(new KeySendRunnable(MainActivity2.this));
//        sendThead.start();
        //mListview.setSelection(0);
    }

    private void appendView(ViewGroup group, int size) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = group.getChildAt(i);
            if (view == null) {
                continue;
            }
            focusView.put(view, (size + (i + 1)));
            sparseArray.append((size + (i + 1)), view);
        }
    }

    private View getFirstView(ViewGroup group) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = group.getChildAt(i);
            if (view == null) {
                continue;
            }
            return view;
        }
        return null;
    }

    private View getLastView(ViewGroup group) {
        int childCount = group.getChildCount();
        for (int i = childCount - 1; i > 0; i--) {
            View view = group.getChildAt(i);
            if (view == null) {
                continue;
            }
            return view;
        }
        return null;
    }

    private void bindOrderFocus(View up, View down) {
        if (up == null || down == null) {

            Log.e("onKeyDown", "onKeyDown:view is null");
            return;
        }
        //up.setNextFocusDownId(down.getId());
        // down.setNextFocusUpId(up.getId());
        //  up.setNextFocusForwardId(down.getId());

    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        View currentFocusView = getCurrentFocus();
//        if (currentFocusView == null) {
//            Log.e("onKeyDown", "currentFocusView is null:");
//            return true;
//        }
//        Log.e("onKeyDown", "currentFocusView:" + currentFocusView.getId());
//        Log.e("onKeyDown", "currentFocusView getNextFocusDownId:" + currentFocusView.getNextFocusDownId() + ",currentFocusView getNextFocusUpId:" + currentFocusView.getNextFocusUpId());
//        switch (event.getKeyCode()) {
//            case KeyEvent.KEYCODE_DPAD_UP:
//                int focusUpViewId = currentFocusView.getNextFocusDownId();
//                View focusUpView = mViewGropu.findViewById(focusUpViewId);
//                focusUpView.requestFocus();
//                focusUpView.setFocusable(true);
//                break;
//            case KeyEvent.KEYCODE_DPAD_DOWN:
//                if (currentFocusView == backV) {
//                    mListview.setItemsCanFocus(true);
//                    return mListview.dispatchKeyEvent(event);
//                }
//                if (currentFocusView == getLastView(mListview)) {
//                    currentFragment.getView().setFocusable(true);
//                    currentFragment.getView().requestFocus();
//                    return currentFragment.getView().dispatchKeyEvent(event);
//                }
//                break;
//        }
//        return super.dispatchKeyEvent(event);
//    }

    private void setOrderFocus(ListView listView) {
        int childCount = listView.getChildCount();
        Log.e("onKeyDown", "childCount=" + childCount);
        for (int i = 0; i < childCount - 1; i++) {
            View view = listView.getChildAt(i);
            Log.e("onKeyDown", "viewid=" + view.getId());
            if (view == null) {
                continue;
            }
            View nextView = listView.getChildAt(i + 1);
            if (nextView == null) {
                continue;
            }
            Log.e("onKeyDown", "nextView=" + nextView.getId());
            bindOrderFocus(view, nextView);
        }
    }

    private List<String> initData() {
        List<String> list = new ArrayList<>();

        list.add("设置");
        list.add("视频");
        list.add("磁盘");
        list.add("驾驶室设置");
        list.add("模拟设置");
        list.add("按键模拟设置");
        list.add("软件下载");
        return list;
    }

    private void restoreFragment() {
        int size = fragments.size();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < size; i++) {
            BaseFocusFragment fragment = (BaseFocusFragment) fragments.get(i);
            if (i == currentIndex) {
                transaction.show(fragment);
                currentFragment = fragment;
            } else {
                transaction.hide(fragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    private void showFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        BaseFocusFragment fragment = (BaseFocusFragment) fragments.get(index);
        if (fragment == null || fragment == currentFragment) {
            return;
        }
        if (fragment.isAdded()) {
            transaction.hide(currentFragment).show(fragment);
        } else {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.content, fragment, "TAG" + index);
        }
        currentFragment = fragment;
        transaction.commit();
        currentIndex = index;
//        mListview.setSelection(index);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (event.getKeyCode()){
//            case KeyEvent.KEYCODE_DPAD_DOWN:
//                break;
//        }
//        return super.onKeyDown(keyCode, event);
//
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        View currentFocusView = getCurrentFocus();
//        if (currentFocusView == null) {
//            Log.e("onKeyDown", "currentFocusView is null:");
//            return true;
//        }
//        Log.e("onKeyDown", "currentFocusView:" + currentFocusView.getId());
//        Log.e("onKeyDown", "currentFocusView getNextFocusDownId:" + currentFocusView.getNextFocusDownId() + ",currentFocusView getNextFocusUpId:" + currentFocusView.getNextFocusUpId());
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_DPAD_UP:
//                int focusUpViewId = currentFocusView.getNextFocusDownId();
//                View focusUpView = mViewGropu.findViewById(focusUpViewId);
//                focusUpView.requestFocus();
//                focusUpView.setFocusable(true);
//                break;
//            case KeyEvent.KEYCODE_DPAD_DOWN:
//                if (currentFocusView == backV){
//                    mListview.setItemsCanFocus(true);
//                    getFirstView(mListview).requestFocus();
//                    getFirstView(mListview).setFocusable(true);
//                }
//                if (currentFocusView == getLastView(mListview)){
//                    currentFragment.getView().setFocusable(true);
//                    currentFragment.getView().requestFocus();
//                    currentFragment.getView().onKeyDown(keyCode,event);
//                }
//                break;
//        }
//        return true;
//    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int id = mFocusView.getId();
        Log.e("onKeyDown", "id:" + id);
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                break;
            case KeyEvent.ACTION_UP:
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        int postion = focusView.get(mFocusView);
                        Log.e("onKeyDown", "postion:" + postion);
                        postion++;
//            if (mFocusView != getCurrentFocus()) {
//                mViewGropu.clearFocus();
//                mListview.clearFocus();
//                currentFragment.getContentGroup().clearFocus();
//            }
                        if (postion == sparseArray.size()) {
                            backV.setFocusable(true);
                            backV.requestFocus();
                            mFocusView = backV;
                            return true;
                        }
                        View nextV = sparseArray.get(postion);
                        nextV.setFocusable(true);
                        nextV.requestFocus();
                        mFocusView = nextV;
                        return true;
                    default:
                        break;
                }

                break;
        }


        return true;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        int id = mFocusView.getId();
//        Log.e("onKeyDown", "id:" + id);
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            onBackPressed();
//        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
//            int postion = focusView.get(mFocusView);
//            Log.e("onKeyDown", "postion:" + postion);
//            postion++;
////            if (mFocusView != getCurrentFocus()) {
////                mViewGropu.clearFocus();
////                mListview.clearFocus();
////                currentFragment.getContentGroup().clearFocus();
////            }
//            if (postion == sparseArray.size()) {
//                backV.setFocusable(true);
//                backV.requestFocus();
//                mFocusView = backV;
//                return true;
//            }
//            View nextV = sparseArray.get(postion);
//            nextV.setFocusable(true);
//            nextV.requestFocus();
//            mFocusView = nextV;
//        }
//
//        return true;
//    }


    static class KeySendRunnable implements Runnable {
        WeakReference<MainActivity2> mainActivityWeakReference = null;

        KeySendRunnable(MainActivity2 mainActivity2) {
            mainActivityWeakReference = new WeakReference<>(mainActivity2);
        }

        @Override
        public void run() {
            if (mainActivityWeakReference == null) {
                return;
            }
            MainActivity2 mainActivity2 = mainActivityWeakReference.get();
            if (mainActivity2 == null) {
                return;
            }
            mainActivity2.send = true;
            while (mainActivity2.send) {
                Instrumentation instrumentation = new Instrumentation();
                instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        send = false;
        if (sendThead != null)
            sendThead.interrupt();

        sendThead = null;
    }
}
