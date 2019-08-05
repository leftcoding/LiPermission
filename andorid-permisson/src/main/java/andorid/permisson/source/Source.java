package andorid.permisson.source;

import android.content.Context;
import android.content.Intent;

public abstract class Source {
    public abstract Context getContext();

    public abstract void startActivity(Intent intent);

    public abstract void startActivityForResult(Intent intent, int requestCode);

}
