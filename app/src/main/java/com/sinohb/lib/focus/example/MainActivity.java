package com.sinohb.lib.focus.example;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sinohb.lib.keyeventhandle.BaseFoucsHandlerActivity;
import com.sinohb.logger.LogTools;

public class MainActivity extends BaseFoucsHandlerActivity {
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = findViewById(R.id.rootView);

    }
    public void click(View view){
        switch (view.getId()){
            case R.id.b1:
                LogTools.e("click","b1");
                break;
            case R.id.b2:
                LogTools.e("click","b2");
                break;
            case R.id.b3:
                LogTools.e("click","b3");
                break;
            case R.id.b4:
                LogTools.e("click","b4");
                break;

        }
    }

    @Override
    public ViewGroup getContentViewGroup() {
        return relativeLayout;
    }
}
