package androidx.permission.option;

import androidx.permission.andpermission.RAndPermission;
import androidx.permission.launcher.AllLauncher;
import androidx.permission.launcher.Launcher;
import androidx.permission.request.Request;
import androidx.permission.source.Source;

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