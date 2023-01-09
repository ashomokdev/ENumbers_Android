package com.ashomok.eNumbers.tools;


import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * Created by iuliia on 10/15/16.
 */

public interface RequestPermissionsTool {
    void requestPermissions(Fragment context, String[] permissions);

    boolean isPermissionsGranted(Context context, String[] permissions);

    void onPermissionDenied();
}
