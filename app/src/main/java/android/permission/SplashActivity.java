package android.permission;

import android.content.Context;
import android.content.Intent;
import android.left.permission.LiPermission;
import android.left.permission.base.Permissions;
import android.left.permission.request.RationaleListener;
import android.left.permission.request.RequestExecutor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

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
//        checkPermission();
    }

    //    @PermissionRequest(permissions = {Permissions.WRITE_EXTERNAL_STORAGE}, repeat = true, toFrontRequest = true)
    private void checkPermission() {
//        startActivity(new Intent(this, MainActivity.class));
        LiPermission.with(this)
                .runtime()
                .checkPermission(Permissions.CAMERA)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        executor.execute();
                    }
                })
                .setCallback(new android.left.permission.request.RequestCallback() {
                    @Override
                    public void onGranted(List<String> list) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onDenied(List<String> list) {
                        if (LiPermission.hasAlwaysDeniedPermission(SplashActivity.this, list)) {
                            LiPermission.with(SplashActivity.this).launcher().start(100);
                        }
                        Toast.makeText(SplashActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }
}
