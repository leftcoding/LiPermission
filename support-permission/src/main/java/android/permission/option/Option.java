package android.permission.option;

import android.permission.request.Request;
import android.permission.launcher.Launcher;

public interface Option {
    Request runtime();

    Launcher launcher();
}
