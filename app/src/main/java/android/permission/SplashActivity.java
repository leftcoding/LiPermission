package android.permission;

import android.content.Intent;
import android.os.Bundle;
import android.permission.api.PermissionRequest;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import andorid.permisson.andpermission.Permissions;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById(R.id.start_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
        checkPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @PermissionRequest(permissions = {Permissions.WRITE_EXTERNAL_STORAGE}, repeat = true)
    private void checkPermission() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
