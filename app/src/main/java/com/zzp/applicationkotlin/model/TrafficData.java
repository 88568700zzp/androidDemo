package com.zzp.applicationkotlin.model;

import android.content.pm.ApplicationInfo;

/**
 * Created by samzhang on 2021/3/4.
 */
public class TrafficData {
    private ApplicationInfo applicationInfo;
    private long total;

    public TrafficData(ApplicationInfo applicationInfo, long total) {
        this.applicationInfo = applicationInfo;
        this.total = total;
    }

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public void setApplicationInfo(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
