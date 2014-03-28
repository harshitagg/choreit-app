package com.pineapps.choreit.util;

import static android.util.Log.*;

public class Log {
    public static void logVerbose(String message) {
        v("CHOREIT", message);
    }

    public static void logInfo(String message) {
        i("CHOREIT", message);
    }

    public static void logDebug(String message) {
        d("CHOREIT", message);
    }

    public static void logWarn(String message) {
        w("CHOREIT", message);
    }

    public static void logError(String message) {
        e("CHOREIT", message);
    }
}
