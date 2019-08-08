package android.left.permission;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.left.permission.source.FragmentSource;
import android.left.permission.base.AndPermission;
import android.support.v4.app.Fragment;

import java.util.List;

import android.left.permission.option.BootOption;
import android.left.permission.option.Option;
import android.left.permission.source.ActivitySource;
import android.left.permission.source.ContextSource;
import android.left.permission.source.Source;

public class LiPermission {
    public static final int RESULT_CODE = 100;

    private LiPermission() {

    }

    public static Option with(Context context) {
        return new BootOption(getSource(context));
    }

    public static Option with(Activity activity) {
        return new BootOption(new ActivitySource(activity));
    }

    public static Option with(Fragment fragment) {
        return new BootOption(new FragmentSource(fragment));
    }

    /**
     * 判断权限是否被永久禁止
     *
     * @param context     上下文
     * @param permissions 权限
     * @return true 被永久禁止，false 相反
     */
    public static boolean hasAlwaysDeniedPermission(Context context, List<String> permissions) {
        return AndPermission.hasAlwaysDeniedPermission(context, permissions);
    }

    private static Source getSource(Context context) {
        if (context instanceof Activity) {
            return new ActivitySource((Activity) context);
        } else if (context instanceof ContextWrapper) {
            return getSource(((ContextWrapper) context).getBaseContext());
        }
        return new ContextSource(context);
    }
}
