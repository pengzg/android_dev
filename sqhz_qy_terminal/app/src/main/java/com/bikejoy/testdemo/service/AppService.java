package com.bikejoy.testdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/2/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class AppService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
