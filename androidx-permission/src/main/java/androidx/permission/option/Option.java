package androidx.permission.option;

import androidx.permission.launcher.Launcher;
import androidx.permission.request.Request;

public interface Option {
    Request runtime();

    Launcher launcher();
}
