package android.left.permission.option;

import android.left.permission.request.Request;
import android.left.permission.launcher.Launcher;

public interface Option {
    Request runtime();

    Launcher launcher();
}
