/*
 * Copyright 2019 Zhenjie Yan
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
package android.left.permission.base.runtime.option;

import android.left.permission.base.runtime.PermissionRequest;
import android.left.permission.base.runtime.setting.SettingRequest;
import android.support.annotation.NonNull;

/**
 * Created by Zhenjie Yan on 2/22/19.
 */
public interface RuntimeOption {

    /**
     * One or more permissions.
     */
    PermissionRequest permission(@NonNull String... permissions);

    /**
     * One or more permission groups.
     */
    PermissionRequest permission(@NonNull String[]... groups);

    /**
     * Permission settings.
     */
    SettingRequest setting();
}