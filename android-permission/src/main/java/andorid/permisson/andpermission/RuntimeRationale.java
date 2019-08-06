package andorid.permisson.andpermission;

import android.content.Context;

import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

import andorid.permisson.request.RationaleListener;

public class RuntimeRationale implements Rationale<List<String>> {
    private final RationaleListener runnable;
    private RequestExecutor executor;

    public RuntimeRationale(RationaleListener rationaleListener) {
        this.runnable = rationaleListener;
    }

    private final andorid.permisson.request.RequestExecutor requestExecutor = new andorid.permisson.request.RequestExecutor() {
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
