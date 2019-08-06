package andorid.permisson.option;

import andorid.permisson.andpermission.RAndPermission;
import andorid.permisson.launcher.AllLauncher;
import andorid.permisson.launcher.Launcher;
import andorid.permisson.request.Request;
import andorid.permisson.source.Source;

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
