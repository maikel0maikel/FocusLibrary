package com.sinohb.lib.keyeventhandle;

import android.content.Context;
import android.preference.PreferenceFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.List;

public abstract class BaseFocusPreferenceFragment extends PreferenceFragment {
    protected BaseFoucsHandlerActivity mActivity;
    protected ViewGroup mViewGroup;
    public abstract ViewGroup getContentViewGroup();

    public abstract List<View> getPreparedFocusViews();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseFoucsHandlerActivity) context;
    }
    public void addPreparedFocusGroup(ViewGroup group) {
        mActivity.addPreparedFocusGroup(group);
    }

    public void startFocusPosition(int position) {
        mActivity.startFocusPosition(position);
    }


    public int remove(View view) {
        return mActivity.remove(view);
    }
    public int findView(View view){
        return mActivity.findView(view);
    }
    public void clearAllFocus() {
        mActivity.clearAllFocus();
    }

    public void addFocusView(ViewGroup group) {
        mActivity.addFocusView(group);
    }

    public void addFilterListenerView(ViewGroup group) {
        mActivity.addFilterListenerView(group);
    }

    public void addFocusView(AbsListView group) {
        mActivity.addFocusView(group);
    }

    public void addPreparedFocusView(View view) {
        mActivity.addPreparedFocusView(view);
    }
    public void addPreparedFocusView(View view,int position) {
        mActivity.addPreparedFocusView(view,position);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewGroup = getContentViewGroup();
//        if (mViewGroup!=null){
//            mActivity.addPreparedFocusView(mViewGroup);
//        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && mViewGroup != null) {
            mActivity.remove(getContentViewGroup());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewGroup != null) {
            mActivity.remove(mViewGroup);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
