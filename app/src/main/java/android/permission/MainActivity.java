package android.permission;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.permission.api.PermissionRequest;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import andorid.permisson.andpermission.Permissions;

public class MainActivity extends AppCompatActivity {
    Button permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission = findViewById(R.id.permission);
        permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @PermissionRequest(permissions = {Permissions.WRITE_EXTERNAL_STORAGE})
    private void test() {
        Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
    }

//    @PermissionRationale
//    private void rationale(final Rationale rationale) {
//        new AlertDialog.Builder(MainActivity.this)
//                .setMessage("需要以下的权限" + rationale.getPermission() + "请同意授权")
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        rationale.onRationale();
//                    }
//                })
//                .show();
//    }
//
//    @PermissionCallback
//    private void callback() {
//        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
//    }


}
