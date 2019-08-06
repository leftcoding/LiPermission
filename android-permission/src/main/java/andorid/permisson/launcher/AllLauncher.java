package andorid.permisson.launcher;


import andorid.permisson.source.Source;

public class AllLauncher implements Launcher {
    private final Source source;

    public AllLauncher(Source source) {
        this.source = source;
    }

    @Override
    public void start(int code) {
        PlatformLauncher platformLauncher = new PlatformLauncher(source);
        platformLauncher.start(code);
    }
}
