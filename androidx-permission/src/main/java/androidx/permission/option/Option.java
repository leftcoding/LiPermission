package androidx.permission.option;

import android.permission.launcher.Launcher;
import android.permission.request.Request;

public interface Option {
    Request runtime();

    Launcher launcher();
}
