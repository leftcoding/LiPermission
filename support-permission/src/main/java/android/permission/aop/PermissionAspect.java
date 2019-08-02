package android.permission.aop;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.permission.LiPermission;
import android.permission.aop.annotation.PermissionCallback;
import android.permission.aop.annotation.PermissionRationale;
import android.permission.aop.annotation.PermissionRequest;
import android.permission.request.RationaleListener;
import android.permission.request.RequestCallback;
import android.permission.request.RequestExecutor;
import android.permissionRequest.R;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Aspect
public class PermissionAspect {
    private static final String EXEC_METHOD = "execution(@android.permission.aop.annotation.PermissionRequest * *(..)) && @annotation(permissionRequest)";

    @Pointcut(EXEC_METHOD)
    public void execMethod(PermissionRequest permissionRequest) {

    }

    @Around("execMethod(permissionRequest)")
    public void adviceOnMethod(final ProceedingJoinPoint joinPoint, final PermissionRequest permissionRequest) throws Throwable {
        Object object = joinPoint.getTarget();
        final Activity activity = getActivity(object);
        final Class<?> tClass = object.getClass();
        Method callMethod = getDeniedMethod(tClass);
        Method rationaleMethod = getRationaleMethod(tClass);
        String[] permissions = permissionRequest.permissions();
        requestPermission(activity, permissions, callMethod, rationaleMethod, joinPoint);
    }

    private void requestPermission(final Activity activity, final String[] permissions, final Method deniedCallback,
                                   final Method rationale, final ProceedingJoinPoint joinPoint) {
        LiPermission.with(activity)
                .runtime()
                .checkPermission(permissions)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        invokeRationale(activity, rationale, executor, permissions);
                    }
                })
                .setCallback(new RequestCallback() {
                    @Override
                    public void onGranted(List<String> list) {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void onDenied(List<String> list) {
                        if (LiPermission.hasAlwaysDeniedPermission(activity, list)) {
                            invokeDenied(activity, deniedCallback, list);
                        }
                    }
                })
                .start();
    }

    private void invokeRationale(Activity activity, Method rationale, final RequestExecutor executor, List<String> permissions) {
        if (rationale == null) {
            executor.execute();
            return;
        }
        Class<?>[] types = rationale.getParameterTypes();
        try {
            if (types.length == 1 && TextUtils.equals(types[0].getCanonicalName(), Rationale.class.getCanonicalName())) {
                Rationale r = new Rationale();
                r.setExecutor(executor);
                r.setPermissions(permissions.toArray(new String[0]));
                rationale.invoke(activity, r);
            } else if (types.length == 0) {
                rationale.invoke(activity);
            } else {
                executor.execute();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void invokeDenied(Activity activity, Method deniedCallback, List<String> permissions) {
        if (deniedCallback == null) {
            showDeniedDialog(activity);
            return;
        }
        Class<?>[] types = deniedCallback.getParameterTypes();
        try {
            if (types.length == 1 &&
                    types[0].isArray() &&
                    types[0].getComponentType() == String.class) {
                deniedCallback.invoke(activity, (Object) permissions.toArray(new String[0]));
            } else if (types.length == 0) {
                deniedCallback.invoke(activity);
            } else {
                showDeniedDialog(activity);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Activity getActivity(Object object) {
        Activity activity;
        if (object instanceof Activity) {
            activity = (Activity) object;
        } else if (object instanceof Fragment) {
            activity = ((Fragment) object).getActivity();
        } else {
            throw new RuntimeException("only use on activity or fragment");
        }
        return activity;
    }

    private Method getDeniedMethod(Class<?> cls) {
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PermissionCallback.class)) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }

    private Method getRationaleMethod(Class<?> cls) {
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PermissionRationale.class)) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }

    private void shortToast(Activity activity) {
        Toast.makeText(activity, activity.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
    }

    private static void showDeniedDialog(final Activity activity) {
        if (activity.isDestroyed()) return;
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.permission_title_tips)
                .setMessage(activity.getString(R.string.permission_message_format, activity.getString(R.string.app_name)))
                .setPositiveButton(R.string.permission_setting, null)
                .setNegativeButton(R.string.permission_out, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (!activity.isDestroyed()) {
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            LiPermission.with(activity).launcher().start(100);
                        }
                    });
        }
    }
}
