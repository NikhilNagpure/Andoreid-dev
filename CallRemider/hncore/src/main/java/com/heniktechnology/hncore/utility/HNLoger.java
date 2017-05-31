package com.heniktechnology.hncore.utility;

import android.util.Log;

/**
 * Created by Master on 1/15/2017.
 */

public class HNLoger {

    private static final int STACK_TRACE_LEVELS_UP = 3;
    private static boolean[] loglevelStatuses = new boolean[5];
    public static String logValueSeperator = "\n";

    private HNLoger() {
    }

    public static boolean isLogEnabled(int logLevel) {
        return loglevelStatuses[logLevel - 2];
    }

    public static void disableLogging() {
        for(int i = 0; i < loglevelStatuses.length; ++i) {
            loglevelStatuses[i] = false;
        }

    }

    public static void enableLogging(int logLevel) {
        for(int i = 0; i < loglevelStatuses.length; ++i) {
            if(i < logLevel - 2) {
                loglevelStatuses[i] = false;
            } else {
                loglevelStatuses[i] = true;
            }
        }

    }

    private static String createLogStatement(String event, String methodName, int lineNumber, String logValueSeperator, String... inputs) {
        StringBuilder logString = null;
        try {
            logString = new StringBuilder();
            String[] var9 = inputs;
            int var8 = inputs.length;

            for(int var7 = 0; var7 < var8; ++var7) {
                String thisInput = var9[var7];
                logString.append(thisInput);
                logString.append(logValueSeperator);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally
        {
            return (new StringBuilder()).append("\n------------------------------------------------------------\n").append(event).append("[").append(methodName).append(" : ").append(lineNumber).append("]").append("\n").append(logString).append("\n------------------------------------------------------------").toString();
        }


    }

    public static void debugStart(String... inputs) {
        try {
            if(isLogEnabled(3)) {
                StackTraceElement currentStackElement = Thread.currentThread().getStackTrace()[3];
                Log.d(currentStackElement.getClassName(), createLogStatement("Entering ", currentStackElement.getMethodName(), currentStackElement.getLineNumber(), logValueSeperator, inputs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void debugEnd(String... inputs) {
        try {
            if(isLogEnabled(3)) {
                StackTraceElement currentStackElement = Thread.currentThread().getStackTrace()[3];
                Log.d(currentStackElement.getClassName(), createLogStatement("Returning from ", currentStackElement.getMethodName(), currentStackElement.getLineNumber(), logValueSeperator, inputs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void debug(String... inputs) {
        try {
            //if(isLogEnabled(3))
            {
                StackTraceElement currentStackElement = Thread.currentThread().getStackTrace()[3];
                Log.d(currentStackElement.getClassName(), createLogStatement("\n ", currentStackElement.getMethodName(), currentStackElement.getLineNumber(), logValueSeperator, inputs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void error(String... inputs) {
        try {
            if(isLogEnabled(6)) {
                StackTraceElement currentStackElement = Thread.currentThread().getStackTrace()[3];
                Log.e(currentStackElement.getClassName(), createLogStatement("\n ", currentStackElement.getMethodName(), currentStackElement.getLineNumber(), logValueSeperator, inputs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void error(Throwable tr, String... inputs) {
        try {
            if(isLogEnabled(6)) {
                StackTraceElement currentStackElement = Thread.currentThread().getStackTrace()[3];
                Log.e(currentStackElement.getClassName(), createLogStatement("\n ", currentStackElement.getMethodName(), currentStackElement.getLineNumber(), logValueSeperator, inputs), tr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void info(String... inputs) {
        try {
            if(isLogEnabled(4)) {
                StackTraceElement currentStackElement = Thread.currentThread().getStackTrace()[3];
                Log.i(currentStackElement.getClassName(), createLogStatement("\n ", currentStackElement.getMethodName(), currentStackElement.getLineNumber(), logValueSeperator, inputs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void verbose(String... inputs) {
        try {
            if(isLogEnabled(2)) {
                StackTraceElement currentStackElement = Thread.currentThread().getStackTrace()[3];
                Log.v(currentStackElement.getClassName(), createLogStatement("\n ", currentStackElement.getMethodName(), currentStackElement.getLineNumber(), logValueSeperator, inputs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void warn(String... inputs) {
        try {
            if(isLogEnabled(5)) {
                StackTraceElement currentStackElement = Thread.currentThread().getStackTrace()[3];
                Log.w(currentStackElement.getClassName(), createLogStatement("\n ", currentStackElement.getMethodName(), currentStackElement.getLineNumber(), logValueSeperator, inputs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Constants {
        String START_SEPERATOR_LINE = "\n------------------------------------------------------------\n";
        String END_SEPERATOR_LINE = "\n------------------------------------------------------------";
        String NEW_LINE_SEPERATOR = "\n";
        String TAB_SEPERATOR = "\t";
    }
}
