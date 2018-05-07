package com.sinohb.lib.focus.example;

import android.app.Instrumentation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.sinohb.lib.keyeventhandle.BaseFoucsHandlerActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MainActivity1 extends BaseFoucsHandlerActivity {
    private List<Fragment> fragments = new ArrayList<>();
    private boolean send;
    private Thread sendThead;
    private BaseFocusFragment currentFragment;
    private int currentIndex = 0;
    private static final String CURRENT_INDEX = "CURRENT_INDEX";
    private ListView mListview;
    private ViewGroup mViewGropu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_main);
        mViewGropu = findViewById(R.id.rootView);
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

//        sendThead = new Thread(new KeySendRunnable(MainActivity2.this));
//        sendThead.start();
        //mListview.setSelection(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addPreparedFocusView(findViewById(R.id.backBtn));
        addPreparedFocusView(mListview);
//        addPreparedFocusView(currentFragment.getContentGroup());
//        startFocusPosition(0);
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

        transaction.commit();
        currentIndex = index;
        currentFragment = fragment;
//        mListview.setSelection(index);
    }

    @Override
    public ViewGroup getContentViewGroup() {
        return null;
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
        WeakReference<MainActivity1> mainActivityWeakReference = null;

        KeySendRunnable(MainActivity1 mainActivity) {
            mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void run() {
            if (mainActivityWeakReference == null) {
                return;
            }
            MainActivity1 mainActivity = mainActivityWeakReference.get();
            if (mainActivity == null) {
                return;
            }
            mainActivity.send = true;
            while (mainActivity.send) {
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
