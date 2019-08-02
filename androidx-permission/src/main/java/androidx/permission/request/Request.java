package androidx.permission.request;

public interface Request {
    Request checkPermission(String... permission);

    Request setCallback(RequestCallback callback);

    Request rationale(RationaleListener rationaleListener);

    void start();
}
