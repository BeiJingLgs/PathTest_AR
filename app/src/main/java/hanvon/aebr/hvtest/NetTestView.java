package hanvon.aebr.hvtest;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hwebook.HANVONEBK;
import android.net.Uri;
import android.net.http.SslError;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NetTestView extends Activity {
		
	WebView webview;

	HANVONEBK ebk = new HANVONEBK();


	private float tx = 0;
	private float ty = 0;
	Context mContext = null;
	public static String FlashPath = Environment.getExternalStorageDirectory().getAbsolutePath();

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.net_test);
		
		mContext = NetTestView.this;
		
		webview = (WebView)findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl("https://www.sina.com.cn");
		//Settings.System.putInt(getContentResolver(), "enterA2", 1);
		//String strName = "https://mini.lezhireading.com/reading-node/reading-app-test/index.html#/home";
		//webview.loadUrl(strName);
		
		//String strSrc = "<dimen name=\"First_Title_Font_Size\">30sp</dimen>";
		//reWriteFile(1.1267604787409969020847397495031, true);
		
		/*
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int height= dm.heightPixels;
        int width= dm.widthPixels;
        int sw=getResources().getConfiguration().smallestScreenWidthDp;
        Log.v("haha", "hoho 屏幕分辨率:" + width + "*" + height+",dpi:"+dm.densityDpi + ", sw=" + sw);
      */

		InitDangxiaoBroadcastReceiver();
		//startwakeup();
		
//		if (bOK)
//		notifySystemToScan(NetTestView.this, file);
		//reWriteFile(1.1267604787409969020847397495031, true);
		

		/*
		path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/我的文档1";
		File file1 = new File(path);
		if (!file1.exists()){
			bOK = file1.mkdir();
		}
		notifySystemToScan(NetTestView.this, file1);

		path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/图片浏览1";
		File file2 = new File(path);
		if (!file2.exists()){
			bOK = file2.mkdir();
		}
		notifySystemToScan(NetTestView.this, file2);
		
		file.delete();
		notifySystemToScan(NetTestView.this, file);
		file1.delete();
		notifySystemToScan(NetTestView.this, file1);
		file2.delete();
		notifySystemToScan(NetTestView.this, file2);
		

		
		Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 604800000);
		long highlight = Settings.System.getLong(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 0);
		*/
		
		String str1 = hvReflectUtils.getStoragePath(mContext, "diskId=disk:179:64");
		str1 += hvReflectUtils.getStoragePath(mContext, "diskId=disk:8:0");
		//Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 300*1000);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");  
        int statusBarHeight = getResources().getDimensionPixelSize(resourceId);  
        testBtn(str1);

	}
	
	
	
	private void testBtn(String str){
		Button btn = (Button) findViewById(R.id.item_button);
		btn.setText(str);
		btn.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				
				String pkg = "hanvon.aebr.hvsettings";
				String cls = "hanvon.aebr.hvsettings.PsdInputActivity";
				ComponentName componet = new ComponentName(pkg, cls);
		    	
		    	Intent i = new Intent(android.content.Intent.ACTION_VIEW);
		    	i.setComponent(componet); 
				try {
					startActivity(i);
			    }catch(ActivityNotFoundException e){
			    	return;
			    }

			}
			
		});
	}
	
	private static PowerManager.WakeLock mSystemWakeLock = null;
	private static int mWakeCount = 0;

	public static void setSystemSleepMode(boolean isValidate, Context context) {
		if (isValidate) {
			if (mSystemWakeLock != null && mWakeCount > 0) {
				mSystemWakeLock.release();
				mWakeCount--;
			}
		} else {
			if (mSystemWakeLock == null) {
				PowerManager pm = (PowerManager) context
						.getSystemService(Context.POWER_SERVICE);
				mSystemWakeLock = pm
						.newWakeLock(PowerManager.FULL_WAKE_LOCK
								| PowerManager.ON_AFTER_RELEASE,
								"HanvonSystemWakeLock");
			}
			mSystemWakeLock.acquire();
			mWakeCount++;
		}
	}
	
    public static void notifyDirUpdate(Context context, File file) {
        ContentResolver resolver =context.getContentResolver();
        Uri uri = Uri.parse("content://" + MediaStore.AUTHORITY + "/external/file");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Files.FileColumns.DATA, file.getAbsolutePath());
        values.put(MediaStore.Files.FileColumns.PARENT, file.getParent());
        values.put(MediaStore.Files.FileColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Files.FileColumns.TITLE, file.getName());
        values.put("format", "12289");
        values.put("storage_id", "65537");
        try {
            resolver.insert(uri, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public static void notifyFileUpdate(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }
    
    public static void notifySystemToScan(Context context , File file) {
        if(file.isDirectory()){
            notifyDirUpdate(context, file);
        }else{
            notifyFileUpdate(context, file);
        }
    }
	
	
	
	BroadcastReceiver mDangxiaoReceiver;
    private void UninitDangxiaoBroadcastReceiver(){
    	if (mDangxiaoReceiver != null){
    		unregisterReceiver(mDangxiaoReceiver);
    		mDangxiaoReceiver = null;
    	}
    }
    private void InitDangxiaoBroadcastReceiver(){
    	mDangxiaoReceiver = new BroadcastReceiver() {
        	  @Override
        	  public void onReceive(Context context, Intent intent) {
        		  String action = intent.getAction();
        		  if (action.equals("android.intent.action.SHOW_BACKLOGO")){
        			  hvApkUtils.startActivityFromeApk(NetTestView.this, 
        					  "hanvon.aebr.hvsettings", "hanvon.aebr.hvsettings.HvLockScreenActivity");
        		  }else if (action.equals("hanvon.intent.action.AUTOWAKEUP")){
        			  
      				PowerManager pm = (PowerManager) context
    						.getSystemService(Context.POWER_SERVICE);
      				mSystemWakeLock = pm
    						.newWakeLock(PowerManager.FULL_WAKE_LOCK
    								| PowerManager.ON_AFTER_RELEASE,
    								"HanvonSystemWakeLock");
      				mSystemWakeLock.acquire();
      				
        			  Log.v("haha10", "haha10 hanvon.intent.action.AUTOWAKEUP");
        		  }else if (action.equals("hanvon.intent.action.forceupdate")){
        		  }
            }
          };

          IntentFilter filter = new IntentFilter();
	      //filter.addAction("android.intent.action.SHOW_BACKLOGO");
	      filter.addAction("hanvon.intent.action.AUTOWAKEUP");
	      filter.addAction("hanvon.intent.action.forceupdate");
      	  registerReceiver(mDangxiaoReceiver, filter);
    }
    
    public void startwakeup(){
    	Intent intentWakeUp = new Intent("hanvon.intent.action.AUTOWAKEUP");
    	PendingIntent piWakeUp = PendingIntent.getBroadcast(this, 0, intentWakeUp, 0);
    	AlarmManager aWakeUpManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
    	aWakeUpManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 50*1000, piWakeUp);
    }
	
	

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		UninitDangxiaoBroadcastReceiver();
		if (mSystemWakeLock != null){
			mSystemWakeLock.release();
			mSystemWakeLock = null;
		}
		super.onDestroy();
	}




	public boolean getRunningAppProcesses(Context context, String packageName) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if ( appProcesses == null){
			return false;
		};
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses){
			if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
				return true;
			}
		}
		return false;
	}
	
	public void reWriteFile(double fMultiple, boolean uft8){
		boolean bCreateOK = false;
		String strRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	       File filedst = new File(strRootPath + "/我的文档5/dimen4.xml");
	       if (!filedst.exists()){
	       	try {
	       		bCreateOK = filedst.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
	       }
	       	
	       InputStream instream = null;
	       OutputStreamWriter out = null;
	       try {
	       OutputStream outstream = new FileOutputStream(filedst);
	       if (uft8){
	    	   out= new OutputStreamWriter(outstream, "utf-8");
	       }else{
	    	   out= new OutputStreamWriter(outstream);
	       }
	       
	       String strContent;
	       File filesrc = new File(strRootPath + "/hv_navigation_bar_dimen.xml");
	       instream = new FileInputStream(filesrc);
	        if (instream != null) {
		        InputStreamReader inputreader = null;
		        if (uft8){
		        	inputreader = new InputStreamReader(instream, "utf-8");
		        }else{
		        	inputreader = new InputStreamReader(instream);
		        }
		        BufferedReader buffreader = new BufferedReader(inputreader);
		        String line;
		        while (( line = buffreader.readLine()) != null) {
		        	strContent = ModifyString(line, fMultiple) + "\n";
		        	out.write(strContent);
		        }
	        }
   	        }catch (java.io.FileNotFoundException e) {
   	        	e.printStackTrace();
   	        }catch (IOException e) {
   	        	e.printStackTrace();
   	        }
   	        if (instream != null){
   		        try {
   					instream.close();
   				} catch (IOException e) {
   					// TODO Auto-generated catch block
   					e.printStackTrace();
   				}
   	        }
   	        if (out != null){
   		        try {
   		        	out.close();
   				} catch (IOException e) {
   					// TODO Auto-generated catch block
   					e.printStackTrace();
   				}
   	        }
   	        if (bCreateOK)
   	        	notifyFileUpdate(NetTestView.this,filedst);
   	        
	}

	//<dimen name="First_Title_Font_Size">30sp</dimen>
	public String ModifyString(String strSrc, double dMultiple){
		String strDst;
		int len = strSrc.length();
		int index1 = strSrc.indexOf(">");
		if (index1 < 0){
			return strSrc;
		}
		String strSub1 = strSrc.substring(0, index1+1);
		String strsubTmp = strSrc.substring(index1+1);
		int index2 = strsubTmp.indexOf("<");
		if (index2 < 0){
			return strSrc;
		}
		String strNeedParse = strsubTmp.substring(0, index2); // 30sp<
		String strSub3 = strsubTmp.substring(index2);
		
		boolean issp = false;
		int index3 = strNeedParse.indexOf("sp");
		if (index3 == -1){
			index3 = strNeedParse.indexOf("dp");
			if (index3 == -1){
				return strSrc;
			}
		}else{
			issp = true;
		}
		String strNum = strNeedParse.substring(0, index3);
		double iNum = Double.parseDouble(strNum);
		double fNum = iNum * dMultiple;
		String strSub2 = new DecimalFormat("##.#").format(fNum);
		if (issp){
			strSub2 += "sp";
		}else{
			strSub2 += "dp";
		}
		
		
		strDst = strSub1 + strSub2 + strSub3;
		Log.v("haha14", "haha14" + strDst);
		return strDst;
	}


}
