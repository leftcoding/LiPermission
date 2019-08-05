package androidx.permission.aop;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.permission.LiPermission;
import androidx.permission.R;
import androidx.permission.aop.annotation.PermissionCallback;
import androidx.permission.aop.annotation.PermissionRationale;
import androidx.permission.aop.annotation.PermissionRequest;
import androidx.permission.request.RationaleListener;
import androidx.permission.request.RequestCallback;
import androidx.permission.request.RequestExecutor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
public class PermissionAspect {
    private static Map<String, Boolean> maps = new HashMap<>();
    private static Application application;
    private static final String EXEC_METHOD = "execution(@android.permission.aop.annotation.PermissionRequest * *(..)) && @annotation(permissionRequest)";

    @Pointcut(EXEC_METHOD)
    public void execMethod(PermissionRequest permissionRequest) {

    }

    @Around("execMethod(permissionRequest)")
    public void adviceOnMethod(final ProceedingJoinPoint joinPoint, final PermissionRequest permissionRequest) throws Throwable {
        Object object = joinPoint.getTarget();
        final Activity activity = getActivity(object);
        final Class<?> tClass = object.getClass();
        final Method callMethod = getDeniedMethod(tClass);
        final Method rationaleMethod = getRationaleMethod(tClass);
        final String[] permissions = permissionRequest.permissions();
        final boolean isNeed = permissionRequest.isNeed();
        if (isNeed) {
            registerLife(activity, new Callback() {
                @Override
                public void onRequest() {
                    requestPermission(activity, permissions, callMethod, rationaleMethod, joinPoint, true);
                }
            });
        } else {
            requestPermission(activity, permissions, callMethod, rationaleMethod, joinPoint, false);
        }
    }

    private void requestPermission(final Activity activity, final String[] permissions, final Method deniedCallback,
                                   final Method rationale, final ProceedingJoinPoint joinPoint, final boolean isNeed) {
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
                            final String name = getCanonicalName(activity);
                            maps.put(name, true);
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void onDenied(List<String> list) {
                        if (LiPermission.hasAlwaysDeniedPermission(activity, list)) {
                            invokeDenied(activity, deniedCallback, list);
                            return;
                        }

                        if (isNeed) {
                            requestPermission(activity, permissions, deniedCallback, rationale, joinPoint, isNeed);
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

    private void registerLife(final Activity sourceActivity, final Callback callback) {
        final String sourceCanonicalName = getCanonicalName(sourceActivity);
        application = sourceActivity.getApplication();
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                final String canonicalName = getCanonicalName(activity);
                Log.d(">>", ">>onActivityCreated:" + canonicalName + ",sourceActivity:" + sourceCanonicalName);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                final String canonicalName = getCanonicalName(activity);
                Log.d(">>", ">>onActivityStarted:" + canonicalName + ",sourceActivity:" + sourceCanonicalName);
                if (TextUtils.equals(sourceCanonicalName, canonicalName)) {
                    Boolean isGrant = maps.get(canonicalName);
                    if (isGrant == null || !isGrant) {
                        maps.put(canonicalName, false);
                        callback.onRequest();
                    }
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                final String canonicalName = getCanonicalName(activity);
                Log.d(">>", ">>onActivityResumed:" + canonicalName + ",sourceActivity:" + sourceCanonicalName);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                final String canonicalName = getCanonicalName(activity);
                Log.d(">>", ">>onActivityPaused:" + canonicalName + ",sourceActivity:" + sourceCanonicalName);

            }

            @Override
            public void onActivityStopped(Activity activity) {
                final String canonicalName = getCanonicalName(activity);
                Log.d(">>", ">>onActivityStopped:" + canonicalName + ",sourceActivity:" + sourceCanonicalName);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                final String canonicalName = getCanonicalName(activity);
                Log.d(">>", ">>onActivitySaveInstanceState:" + canonicalName + ",sourceActivity:" + sourceCanonicalName);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                final String canonicalName = getCanonicalName(activity);
                Log.d(">>", ">>onActivityDestroyed:" + canonicalName + ",sourceActivity:" + sourceCanonicalName);
                if (maps.containsKey(canonicalName) && TextUtils.equals(sourceCanonicalName, canonicalName)) {
                    maps.remove(canonicalName);
                }
            }
        });
    }

    private String getCanonicalName(Activity activity) {
        return activity.getClass().getCanonicalName();
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
