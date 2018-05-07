package com.sinohb.lib.focus.example.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sinohb.lib.focus.example.MyAdapterFragment;
import com.sinohb.lib.focus.example.R;
import com.sinohb.lib.keyeventhandle.BaseFocusFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentSix extends BaseFocusFragment {

    private View mRootView;
    private Activity mActivity;

    @Override
    public ViewGroup getContentViewGroup() {
        return (ViewGroup) ((ViewGroup)mRootView).getChildAt(0);
    }

    @Override
    public List<View> getPreparedFocusViews() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
    @Override
    public void onStart() {
        super.onStart();
        addPreparedFocusView(getContentViewGroup());
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("TestListFragment","FragmentTwo");
        if (mRootView == null){
            mRootView =  inflater.inflate(R.layout.fragment_list,container,false);
            ListView listView = mRootView.findViewById(R.id.listSeetings);
            List<String> datas = initData();
            MyAdapterFragment<String> myAdapter = new MyAdapterFragment<>(datas,mActivity);
            listView.setAdapter(myAdapter);
        }

        ViewGroup parent = (ViewGroup) mRootView.getParent();

        if (parent!=null){
            parent.removeView(mRootView);
        }
        return mRootView;
    }
    private List<String> initData(){
        List<String> list = new ArrayList<>();

        list.add("设置6");
        list.add("视频6");
        list.add("磁盘6");
        list.add("驾驶室设置6");
        list.add("模拟设置6");
        list.add("按键模拟设置6");
        list.add("软件下载6");
        list.add("投诉建议6");
        list.add("很好很好6");
        return list;
    }

}
