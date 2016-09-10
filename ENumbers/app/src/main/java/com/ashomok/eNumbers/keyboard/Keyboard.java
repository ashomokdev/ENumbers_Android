package com.ashomok.eNumbers.keyboard;

import android.content.Context;

/**
 * Created by iuliia on 9/5/16.
 */
public interface Keyboard {

    void init(Context context);

    boolean isVisible();

    void setOnKeyboardSwitchListener(OnKeyboardSwitchListener listener);

    void setOnSubmitListener(OnSubmitListener onSubmitListener);

    void show();

    void hide();
}
