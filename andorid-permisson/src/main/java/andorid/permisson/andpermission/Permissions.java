package andorid.permisson.andpermission;

import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Permissions {
    public static final String READ_CALENDAR = "android.permission.READ_CALENDAR";
    public static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";

    public static final String CAMERA = "android.permission.CAMERA";

    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";

    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";

    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";

    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String CALL_PHONE = "android.permission.CALL_PHONE";
    public static final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    public static final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
    public static final String ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL";
    public static final String ADD_VOICEMAIL_MANIFEST = "android.permission.ADD_VOICEMAIL";
    public static final String USE_SIP = "android.permission.USE_SIP";
    public static final String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";
    public static final String READ_PHONE_NUMBERS = "android.permission.READ_PHONE_NUMBERS";
    public static final String ANSWER_PHONE_CALLS = "android.permission.ANSWER_PHONE_CALLS";

    public static final String BODY_SENSORS = "android.permission.BODY_SENSORS";

    public static final String SEND_SMS = "android.permission.SEND_SMS";
    public static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    public static final String READ_SMS = "android.permission.READ_SMS";
    public static final String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
    public static final String RECEIVE_MMS = "android.permission.RECEIVE_MMS";

    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static final class Group {

        public static final String[] CALENDAR = new String[]{Permissions.READ_CALENDAR, Permissions.WRITE_CALENDAR};

        public static final String[] CAMERA = new String[]{Permissions.CAMERA};

        public static final String[] CONTACTS = new String[]{Permissions.READ_CONTACTS, Permissions.WRITE_CONTACTS,
                Permissions.GET_ACCOUNTS};

        public static final String[] LOCATION = new String[]{Permissions.ACCESS_FINE_LOCATION,
                Permissions.ACCESS_COARSE_LOCATION};

        public static final String[] MICROPHONE = new String[]{Permissions.RECORD_AUDIO};

        public static final String[] PHONE;

        static {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PHONE = new String[]{Permissions.READ_PHONE_STATE, Permissions.CALL_PHONE, Permissions.READ_CALL_LOG,
                        Permissions.WRITE_CALL_LOG, Permissions.ADD_VOICEMAIL, Permissions.USE_SIP,
                        Permissions.PROCESS_OUTGOING_CALLS, Permissions.READ_PHONE_NUMBERS, Permissions.ANSWER_PHONE_CALLS};
            } else {
                PHONE = new String[]{Permissions.READ_PHONE_STATE, Permissions.CALL_PHONE, READ_CALL_LOG,
                        Permissions.WRITE_CALL_LOG, Permissions.ADD_VOICEMAIL, Permissions.USE_SIP,
                        Permissions.PROCESS_OUTGOING_CALLS};
            }
        }

        public static final String[] SENSORS = new String[]{Permissions.BODY_SENSORS};

        public static final String[] SMS = new String[]{Permissions.SEND_SMS, Permissions.RECEIVE_SMS,
                Permissions.READ_SMS, Permissions.RECEIVE_WAP_PUSH, Permissions.RECEIVE_MMS};

        public static final String[] STORAGE = new String[]{Permissions.READ_EXTERNAL_STORAGE,
                Permissions.WRITE_EXTERNAL_STORAGE};
    }

    /**
     * Turn permissions into text.
     */
    public static List<String> transformText(Context context, String... permissions) {
        return transformText(context, Arrays.asList(permissions));
    }

    /**
     * Turn permissions into text.
     */
    public static List<String> transformText(Context context, String[]... groups) {
        List<String> permissionList = new ArrayList<>();
        for (String[] group : groups) {
            permissionList.addAll(Arrays.asList(group));
        }
        return transformText(context, permissionList);
    }

    /**
     * Turn permissions into text.
     */
    public static List<String> transformText(Context context, List<String> permissions) {
        List<String> textList = new ArrayList<>();
        for (String permission : permissions) {
            switch (permission) {
                case READ_CALENDAR:
                case WRITE_CALENDAR: {
                    String message = context.getString(com.yanzhenjie.permission.R.string.permission_name_calendar);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }

                case CAMERA: {
                    String message = context.getString(com.yanzhenjie.permission.R.string.permission_name_camera);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case READ_CONTACTS:
                case WRITE_CONTACTS: {
                    String message = context.getString(com.yanzhenjie.permission.R.string.permission_name_contacts);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case GET_ACCOUNTS: {
                    String message = context.getString(com.yanzhenjie.permission.R.string.permission_name_accounts);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case ACCESS_FINE_LOCATION:
                case ACCESS_COARSE_LOCATION: {
                    String message = context.getString(com.yanzhenjie.permission.R.string.permission_name_location);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case RECORD_AUDIO: {
                    String message = context.getString(com.yanzhenjie.permission.R.string.permission_name_microphone);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case READ_PHONE_STATE:
                case CALL_PHONE:
                case READ_CALL_LOG:
                case WRITE_CALL_LOG:
                case ADD_VOICEMAIL:
                case ADD_VOICEMAIL_MANIFEST:
                case USE_SIP:
                case PROCESS_OUTGOING_CALLS:
                case READ_PHONE_NUMBERS:
                case ANSWER_PHONE_CALLS: {
                    String message = context.getString(com.yanzhenjie.permission.R.string.permission_name_phone);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case BODY_SENSORS: {
                    String message = context.getString(com.yanzhenjie.permission.R.string.permission_name_sensors);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case SEND_SMS:
                case RECEIVE_SMS:
                case READ_SMS:
                case RECEIVE_WAP_PUSH:
                case RECEIVE_MMS: {
                    String message = context.getString(com.yanzhenjie.permission.R.string.permission_name_sms);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case READ_EXTERNAL_STORAGE:
                case WRITE_EXTERNAL_STORAGE: {
                    String message = context.getString(com.yanzhenjie.permission.R.string.permission_name_storage);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
            }
        }
        return textList;
    }
}
