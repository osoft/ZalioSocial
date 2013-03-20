package net.zalio.android.social.utils;

import android.util.Log;

public class MyLog {
	private static final int STACK_TRACE_INDEX_START = 3;
	private static final String COLON_SEPERATOR = ": ";
	static final private String TAG = "ZalioSocial";
    static private boolean iFlag;
    static private boolean dFlag;
    static private boolean wFlag;
    static private boolean eFlag;
    static private boolean vFlag;
    
    static{
        iFlag = dFlag = wFlag = vFlag = true;
        eFlag = true;
    }
    
    public static final void i(String tag, String content){
        if(iFlag)
            Log.i(TAG, tag + COLON_SEPERATOR + content);
    }
    
    public static final void d(String tag, String content){
        if(dFlag)
            Log.d(TAG, tag + COLON_SEPERATOR + content);       
    }
    
    public static final void w(String tag, String content){
        if(wFlag)
            Log.w(TAG, tag + COLON_SEPERATOR + content);    
    }
    
    public static final void e(String tag, String content){
        if(eFlag)
            Log.e(TAG, tag + COLON_SEPERATOR + content);
    }
    
    public static final void v(String tag, String content){
        if(vFlag)
            Log.v(TAG, tag + COLON_SEPERATOR + content);
    }
    
    public static final void dumpStack(String tag) {
    	StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
    	StringBuilder stackTrace = new StringBuilder();
    	stackTrace.append("----- Dumping current stack ------\n");
    	for (int index = STACK_TRACE_INDEX_START; index < stackTraceElements.length; index++) {
    		StackTraceElement stackTraceElement = stackTraceElements[index];
    		stackTrace.append("\t");
    		stackTrace.append(stackTraceElement.getClassName());
    		stackTrace.append(".");
    		stackTrace.append(stackTraceElement.getMethodName());
    		stackTrace.append(COLON_SEPERATOR);
    		stackTrace.append(stackTraceElement.getLineNumber());
    		stackTrace.append("\n");
    	}
    	stackTrace.append("----- End dumping current stack -----\n");
    	MyLog.d(tag, stackTrace.toString());
    }
}
