package android.left.permission.base;

import android.content.Context;

import java.util.List;

import android.left.permission.request.RationaleListener;

public class RuntimeRationale implements Rationale<List<String>> {
    private final RationaleListener runnable;
    private RequestExecutor executor;

    public RuntimeRationale(RationaleListener rationaleListener) {
        this.runnable = rationaleListener;
    }

    private final android.left.permission.request.RequestExecutor requestExecutor = new android.left.permission.request.RequestExecutor() {
        @Override
        public void execute() {
            if (executor != null) {
                executor.execute();
            }
        }

        @Override
        public void cancel() {
            if (executor != null) {
                executor.cancel();
            }
        }
    };

    @Override
    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        this.executor = executor;
        if (runnable != null) {
            runnable.showRationale(context, permissionNames, requestExecutor);
        }
    }
}
