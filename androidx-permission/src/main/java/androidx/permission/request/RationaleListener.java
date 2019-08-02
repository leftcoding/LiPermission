package androidx.permission.request;

import android.content.Context;

import java.util.List;

public interface RationaleListener {
    void showRationale(Context context, List<String> permissions, final RequestExecutor executor);
}
