/*
 * Copyright © Zhenjie Yan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.left.permission.base.checker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
 * Created by Zhenjie Yan on 2018/1/15.
 */
class CameraTest implements PermissionTest {

    private Context mContext;

    CameraTest(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean test() throws Throwable {
        Camera camera = null;
        try {
            camera = mayOpenCamera();
            return true;
        } catch (Throwable e) {
            PackageManager packageManager = mContext.getPackageManager();
            return !packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        } finally {
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewCallback(null);
                camera.release();
            }
        }
    }

    /**
     * 尝试打开摄像头，rk3328 android 使用Camera.open()，会跳转失败页面，尝试使用 Camera.open(0)
     */
    private Camera mayOpenCamera() {
        Camera camera;
        camera = Camera.open();
        if (camera == null) {
            camera = Camera.open(0);
        }
        Camera.Parameters parameters = camera.getParameters();
        camera.setParameters(parameters);
        camera.setPreviewCallback(PREVIEW_CALLBACK);
        camera.startPreview();
        return camera;
    }

    private static final Camera.PreviewCallback PREVIEW_CALLBACK = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
        }
    };
}