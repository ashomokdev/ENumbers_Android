package com.ashomok.eNumbers.keyboard;

import android.content.Context;

/**
 * Created by iuliia on 9/5/16.
 */
interface Keyboard {

    void init(Context context);

    boolean isVisible();

    void setOnSubmitListener(OnSubmitListener onSubmitListener);

    void setOnVisibilityChangedListener(OnVisibilityChangedListener listener);

    void show();

    void hide();
}
