package andorid.permisson.source;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class FragmentSource extends Source {
    private Fragment fragment;

    public FragmentSource(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public Context getContext() {
        return fragment.getContext();
    }

    @Override
    public void startActivity(Intent intent) {
        fragment.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        fragment.startActivityForResult(intent, requestCode);
    }
}
