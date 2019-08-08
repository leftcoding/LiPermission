package android.left.permission.request;

public interface RequestExecutor {

    /**
     * Go request permission.
     */
    void execute();

    /**
     * Cancel the operation.
     */
    void cancel();

}