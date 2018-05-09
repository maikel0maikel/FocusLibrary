package com.sinohb.lib.keyeventhandle;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;


public abstract class BaseFocusPopupWindow extends PopupWindow implements View.OnKeyListener {
    private KeyEventHandler keyEventHandler = new KeyEventHandler();

    public BaseFocusPopupWindow(Context context) {
        super(context);
        setFocusable(true);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (KeyEvent.ACTION_DOWN == event.getAction()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                case KeyEvent.KEYCODE_DPAD_DOWN:
                case KeyEvent.KEYCODE_ENTER:
                    return keyEventHandler.handleKeyEvent(keyCode, event);
            }

        }
        return false;
    }

    public void startFocusPosition(int position) {
        keyEventHandler.startFocusPosition(position);
    }

    public void addPreparedFocusView(View view) {
        keyEventHandler.addPreparedFocusView(view);
        view.setOnKeyListener(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        keyEventHandler.clearAllFocus();
    }

    public void setKeyListener() {
        getContentView().setFocusable(true);
        getContentView().setFocusableInTouchMode(true);
        getContentView().setOnKeyListener(this);
    }

    public void handleEdittext(EditText editText) {
        keyEventHandler.handleEdittext(editText);
    }
}
