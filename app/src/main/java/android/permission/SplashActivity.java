package android.permission;

import android.content.Intent;
import android.os.Bundle;
import android.permission.aop.annotation.PermissionRequest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.permission.andpermission.Permissions;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @PermissionRequest(permissions = {Permissions.WRITE_EXTERNAL_STORAGE}, isNeed = true)
    private void checkPermission() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
