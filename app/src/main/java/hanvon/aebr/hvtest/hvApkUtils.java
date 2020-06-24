package hanvon.aebr.hvtest;


import java.io.File;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

public class hvApkUtils {
	public static void updateApk(File selectedFile, Context context){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.setDataAndType(Uri.fromFile(selectedFile), "application/vnd.android.package-archive");
	    context.startActivity(intent);
	}
	
	public static void updateApk(String selectedFilePath, Context context){
		File selectedFile = new File(selectedFilePath);
		if (selectedFile.exists()){
			updateApk(selectedFile, context);
		}
	}
	
	public static boolean startActivityFromIntent(Context context, Intent intent){
		boolean bOK = true;
        try {
        	context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        	bOK = false;
        };
        return bOK;
	}
	
    
	public static void startActivityFromeApk(Context mContext, String pkg, String cls){
		startMyActivity(mContext, pkg, cls, null, null);
	}
	
	public static void startMyActivity(Context mContext, String pkg, String cls, String name, String value){
	 	ComponentName cn = new ComponentName(pkg, cls);
	    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
	    intent.setComponent(cn);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    if (name != null && value != null)
	    	intent.putExtra(name, value);
	    hvApkUtils.startActivityFromIntent(mContext, intent);
	 	
	}
	
	public static void startMyActivity2(Context mContext, String pkg, String cls, String name, String value, String name1, String value1){
	 	ComponentName cn = new ComponentName(pkg, cls);
	    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
	    intent.setComponent(cn);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    if (name != null && value != null)
	    	intent.putExtra(name, value);
	    if (name1 != null && value1 != null)
	    	intent.putExtra(name1, value1);
	 	mContext.startActivity(intent);
	}
	
	// 静态广播，写到AndroidManfest.xml里的全局广播
	public static void sendStaticBroadcast(Context mContext, String action, String packagename, String receivername){
		 Intent myIntent = new Intent();
		 myIntent.setAction(action);
		 //myIntent.setComponent(new ComponentName("hanvon.aebr.hvsettings", "hanvon.aebr.hvsettings.ScreencapBroadCastRecevier"));
		 myIntent.setComponent(new ComponentName(packagename, receivername));
		 mContext.sendBroadcast(myIntent);
	}
	
	public static void sendStaticBroacast_hvSetting(Context mContext, String action){
		sendStaticBroadcast(mContext, action, "hanvon.aebr.hvsettings", "hanvon.aebr.hvsettings.ScreencapBroadCastRecevier");
	}
	 
	// 动态广播，有注册接收器和注销接收器
	 public static void sendDynamicBroadcast(Context mContext, String action){
		 Intent myIntent = new Intent();
		 myIntent.setAction(action);
		 mContext.sendBroadcast(myIntent);
	 }
	 
	 public static void sendDynamicBroadcastAndStr(Context mContext, String action, String name, String value){
		 Intent myIntent = new Intent();
		 myIntent.setAction(action);
		 myIntent.putExtra(name, value);
		 mContext.sendBroadcast(myIntent);
	 }
	 
	 
	 public static void startMyActivity(Context mContext, Class<?> cls){
	      final Intent intent = new Intent();  
	      intent.setClass(mContext, cls);
	      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	      mContext.startActivity(intent);
	 }
	 
	 public static void startMyActivityForPic(Context mContext, Class<?> cls, boolean useassets, String strPath){
	      final Intent intent = new Intent();
	      intent.setClass(mContext, cls);
	      intent.putExtra("useassets", useassets);
	      intent.putExtra("path", strPath);
	      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	      mContext.startActivity(intent);
	 }
	 
	 

		
		/* 获取可用内存大小 */
		public static long getAvailMemory(Context context) {
			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
			am.getMemoryInfo(mi);
			return mi.availMem;
		}
		
		public static String formatSize(Context context, long size) {
			return Formatter.formatFileSize(context, size);
		}
 
}

