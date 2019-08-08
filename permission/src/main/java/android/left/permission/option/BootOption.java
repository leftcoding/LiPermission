package android.left.permission.option;

import android.left.permission.base.RAndPermission;
import android.left.permission.launcher.AllLauncher;
import android.left.permission.launcher.Launcher;
import android.left.permission.request.Request;
import android.left.permission.source.Source;

public class BootOption implements Option {
    private Source source;

    public BootOption(Source source) {
        this.source = source;
    }

    @Override
    public Request runtime() {
        return new RAndPermission(source.getContext());
    }

    @Override
    public Launcher launcher() {
        return new AllLauncher(source);
    }
}
