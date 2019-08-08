package android.permission;

import android.content.Intent;
import android.left.permission.base.Permissions;
import android.os.Bundle;
import android.permission.aop.api.PermissionRequest;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        findViewById(R.id.start_main).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkPermission();
//            }
//        });
        checkPermission();
    }

    @PermissionRequest(permissions = {Permissions.WRITE_EXTERNAL_STORAGE}, repeat = true, toFrontRequest = true)
    private void checkPermission() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
