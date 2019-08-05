package andorid.permisson.source;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ActivitySource extends Source {
    private Activity activity;

    public ActivitySource(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Context getContext() {
        return activity;
    }

    @Override
    public void startActivity(Intent intent) {
        activity.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }
}
