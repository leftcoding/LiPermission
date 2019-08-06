package android.permission.api.model;

import java.io.Serializable;

public class Result implements Serializable {
    /**
     * 是否被永久禁止请求权限
     */
    public boolean isWayDenied;

    /**
     * 是否已经启动过
     */
    public boolean isStarted;

    /**
     * 是否授权成功
     */
    public boolean isGranted;

    public Result() {
    }

    public Result(boolean isGranted) {
        this.isGranted = isGranted;
    }
}
