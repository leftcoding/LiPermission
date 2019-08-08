package android.left.permission.base;

import android.content.Context;

import java.util.List;

import android.left.permission.request.RationaleListener;
import android.left.permission.request.Request;
import android.left.permission.request.RequestCallback;

public class RAndPermission implements Request {
    private final Context context;
    private String[] permissions;
    private RequestCallback callback;
    private RationaleListener rationaleListener;

    public RAndPermission(Context context) {
        this.context = context;
    }

    @Override
    public Request checkPermission(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    @Override
    public Request setCallback(RequestCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public Request rationale(RationaleListener rationaleListener) {
        this.rationaleListener = rationaleListener;
        return this;
    }

    @Override
    public void start() {
        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale(rationaleListener))
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (callback != null) {
                            callback.onGranted(permissions);
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (callback != null) {
                            callback.onDenied(permissions);
                        }
                    }
                })
                .start();
    }
}
