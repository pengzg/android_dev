package com.bikejoy.testdemo.location;

import com.baidu.location.BDLocation;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/9
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface LocationListener {
    void locationSuccess(BDLocation location);
    void locationError(BDLocation location);
}
