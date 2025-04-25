package com.biaoguoworks.refresher;

import com.biaoguoworks.refresh.AbsConfigRefresh;

/**
 * @author wuxin
 * @date 2025/04/26 00:03:40
 */
public enum RefresherType {

    NACOS(NacosConfigRefresher.class),
    CONFIG(null)
    ;

    RefresherType(Class<? extends AbsConfigRefresh> refresherClass) {
        this.refresherClass = refresherClass;
    }

    public Class<? extends AbsConfigRefresh> getRefresherClass() {
        return refresherClass;
    }



    private Class<? extends AbsConfigRefresh> refresherClass;

}
