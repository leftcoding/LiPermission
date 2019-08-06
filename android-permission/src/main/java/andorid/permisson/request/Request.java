package andorid.permisson.request;

public interface Request {
    Request checkPermission(String... permission);

    Request setCallback(RequestCallback callback);

    Request rationale(RationaleListener rationaleListener);

    void start();
}
