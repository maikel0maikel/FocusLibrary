package com.sinohb.lib.focus.example;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.sinohb.lib.keyeventhandle.BaseFoucsHandlerActivity;
import com.sinohb.logger.LogTools;

public class MainActivity4 extends BaseFoucsHandlerActivity {
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_radio);
        relativeLayout = findViewById(R.id.rootView);
        RadioGroup group = findViewById(R.id.radioBtn);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getCurrentFocus().clearFocus();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        addPreparedFocusView(findViewById(R.id.b1));
        addPreparedFocusView(findViewById(R.id.b2));
        addPreparedFocusView(findViewById(R.id.b3));
        addPreparedFocusView(findViewById(R.id.b4));
        addPreparedFocusView(findViewById(R.id.radio1));
        addPreparedFocusView(findViewById(R.id.radio2));
        addPreparedFocusView(findViewById(R.id.radio3));
        startFocusPosition(0);
    }

    public void click(View view){
        getWindow().getDecorView().clearFocus();
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
