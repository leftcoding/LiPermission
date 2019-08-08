package android.permission.aop.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionRequest {
    /**
     * 请求权限数组
     */
    String[] permissions();

    /**
     * 当权限拒绝时，是否再次请求
     */
    boolean repeat() default false;

    /**
     * 权限被永久拒绝时，从设置界面返回界面前台时，是否再次请求
     */
    boolean toFrontRequest() default false;
}
