package android.permission.aop;

import andorid.permisson.request.RequestExecutor;

public class Rationale {
    private String[] permissions;
    private RequestExecutor requestExecutor;

    public Rationale() {

    }

    void setExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public String getPermission() {
        StringBuilder stringBuilder = new StringBuilder();
        if (permissions != null) {
            for (String permission : permissions) {
                stringBuilder.append(permission).append("、");
            }
        }
        return stringBuilder.toString();
    }

    public void onRationale() {
        requestExecutor.execute();
    }
}
