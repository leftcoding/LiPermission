package android.permission.aop;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.permission.api.PermissionCallback;
import android.permission.api.PermissionRationale;
import android.permission.api.PermissionRequest;
import android.permission.api.model.Result;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import andorid.permisson.LiPermission;
import andorid.permisson.request.RationaleListener;
import andorid.permisson.request.RequestCallback;
import andorid.permisson.request.RequestExecutor;

@Aspect
public class PermissionAspect {
    private Map<String, Result> maps = new HashMap<>();
    private Application application;
    private static final String EXEC_METHOD = "execution(@android.permission.api.PermissionRequest * *(..)) && @annotation(permissionRequest)";

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
        final boolean isRepeat = permissionRequest.repeat();
        Result result = getResult(activity);
        if (result != null && result.isGranted) {
            joinPoint.proceed();
            return;
        }
        requestPermission(activity, permissions, callMethod, rationaleMethod, joinPoint, isRepeat);
        if (application == null) {
            registerLife(activity, new Callback() {
                @Override
                public void onRequest() {
                    requestPermission(activity, permissions, callMethod, rationaleMethod, joinPoint, isRepeat);
                }
            });
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
                            maps.put(name, new Result(true));
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void onDenied(List<String> list) {
                        if (LiPermission.hasAlwaysDeniedPermission(activity, list)) {
                            Result result = getResult(activity);
                            if (result != null) {
                                result.isWayDenied = true;
                            }
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
        Log.d(">>", ">>map.size:" + maps.size());
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
                Log.d(">>", ">>onActivityStarted:" + canonicalName);
                if (TextUtils.equals(sourceCanonicalName, canonicalName)) {
                    Log.d(">>", ">>onActivityStarted:" + canonicalName + ",sourceActivity:" + sourceCanonicalName);
                    Result result = maps.get(canonicalName);
                    if (result == null) {
                        result = new Result();
                        result.isStarted = true;
                        maps.put(canonicalName, result);
                    }

                    if (result.isStarted && result.isWayDenied) {
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

    private Result getResult(Activity activity) {
        return maps.get(getCanonicalName(activity));
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
                .setPositiveButton(R.string.permission_setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        LiPermission.with(activity).launcher().start(100);
                    }
                })
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
        }
    }
}
