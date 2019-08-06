package andorid.permisson.option;

import andorid.permisson.launcher.Launcher;
import andorid.permisson.request.Request;

public interface Option {
    Request runtime();

    Launcher launcher();
}
