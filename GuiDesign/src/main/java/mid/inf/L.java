package mid.inf;

import android.util.Log;

/**
 * Simple Android logging facility wrapper,
 * <p/>
 * The {@class L} implements a simple wrapper around the logcat logging facility.
 */
public final class L {

	//
	// DEFS
	//

	// Supported log levels
	public static final int LOG_ERROR = 1, LOG_WARNING = 2, LOG_INFO = 3, LOG_DEBUG = 4, LOG_MAX = 5;

	//
	// METHODS
	//
	// Standard logging API.

	/**
	 * Sets a new logging level.
	 */
	public static synchronized void setLogLevel(int level) {
		if (level < LOG_MAX)
			logLevel = level;
	}

	/**
	 * Sets a new logging tag.
	 */
	public static synchronized void setLogTag(String tag) {
		logTag = tag;
	}

	/**
	 * Logs a DEBUG message.
	 */
	public static void DEBUG(String msg) {
		if (logLevel >= LOG_DEBUG){

//				Log.d(logTag, Thread.currentThread().getName() + ": " + msg);
			String trace =  ""+new Exception().getStackTrace()[1];
			trace = trace.substring(trace.indexOf("(") , trace.indexOf(")")+1);
			Log.d(trace,msg);
//			Log.d(trace.substring(5),msg);
		}
	}



	/**
	 * Logs an INFO message.
	 */
	public static void INFO(String msg) {
		if (logLevel >= LOG_INFO)
			Log.i(logTag, Thread.currentThread().getName() + ": " + msg);
	}

	/**
	 * Logs an WARNING message.
	 */
	public static void WARNING(String msg) {
		if (logLevel >= LOG_WARNING)
			Log.w(logTag, Thread.currentThread().getName() + ": " + msg);
	}

	/**
	 * Logs an ERROR message.
	 */
	public static void ERROR(String msg) {
		if (logLevel >= LOG_ERROR)
			Log.e(logTag, Thread.currentThread().getName() + ": " + msg);
	}

	/**
	 * Logs an exception as an error.
	 */
	public static void EXCEPTION(Throwable t, String msg) {
		if (msg != null)
			ERROR(msg + ": " + Log.getStackTraceString(t));
		else
			ERROR(Log.getStackTraceString(t));
	}

	/**
	 * Returns the result of method invocation logging an error message.
	 */
	public static boolean RESULT(boolean methodResult, String errorMsg) {
		if (methodResult == false)
			ERROR(errorMsg);
		return methodResult;
	}

	//
	// MEMBERS
	//

	// Current log tag used to filter the logcat.
	private static String logTag = "?";

	// Current log level
	private static int logLevel = LOG_DEBUG;
}
