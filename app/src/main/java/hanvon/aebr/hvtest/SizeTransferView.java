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
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hwebook.HANVONEBK;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.IWindow;
import android.view.IWindowManager;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SizeTransferView extends Activity {
		
	WebView webview;



	private float tx = 0;
	private float ty = 0;
	private IWindowManager mWindowManagerService = null;
	private IBinder appToken;
	Bitmap bitmap;
	HANVONEBK ebk = new HANVONEBK();
	
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.net_test);

		//webview.loadUrl("http://v.youku.com/v_show/id_XMjY1MDYzOTQ1Mg==.html?f=49258329&spm=a2hww.20023042.m_223465.5~5~5~5!2~5~5~A&from=y1.3-idx-beta-1519-23042.223465.4-1");
		//Settings.System.putInt(getContentResolver(), "enterA2", 1);
		//String strName = "http://www.gaoxiaogif.com";
		//String strName = "https://www.baidu.com";
		//webview.loadUrl(strName);
		
		ebk.SetVersion(1, "4.00");
		
		String strPathFile = "/storage/emulated/0/dimens.xml"; //1.48, 1.69
		sizeTransferFile(strPathFile, 1.1, false, false);

		
		//strPathFile = "/storage/emulated/0/hv_navigation_bar_dimen.xml";
		//sizeTransferFile(strPathFile, 1.48, false, false);

		//strPathFile = "/storage/emulated/0/dimen_filemanager.xml";
		//sizeTransferFile(strPathFile, 1.48, false, false);		
	}
	
	
	public Bitmap convertViewToBitmap(View view){
	    Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
	        Bitmap.Config.ARGB_8888);
	    //利用bitmap生成画布
	    Canvas canvas = new Canvas(bitmap);
	    //把view中的内容绘制在画布上
	    view.draw(canvas);
	  return bitmap;
	}
	
	public Bitmap captureScreenWindow() {
        getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = getWindow().getDecorView().getDrawingCache();
        return bmp;
    }
	
	public Bitmap screenShot(){
		Bitmap bitmap = SurfaceControl.screenshot(1872, 1404);
		bitmap = adjustPhotoRotation(bitmap, 90);
		return bitmap;
	}
	
	public Bitmap activityShot() {
	    /*获取windows中最顶层的view*/
	    View view = getWindow().getDecorView();

	    //允许当前窗口保存缓存信息
	    //view.setDrawingCacheEnabled(true);
	    view.buildDrawingCache();

	    //获取状态栏高度
	    //Rect rect = new Rect();
	    //view.getWindowVisibleDisplayFrame(rect);
	    //int statusBarHeight = rect.top;

	    WindowManager windowManager = getWindowManager();

	    //获取屏幕宽和高
	    DisplayMetrics outMetrics = new DisplayMetrics();
	    windowManager.getDefaultDisplay().getMetrics(outMetrics);
	    int width = outMetrics.widthPixels;
	    int height = outMetrics.heightPixels;

	    /*
	    //去掉状态栏
	    Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight, width,
	            height - statusBarHeight);
	            */
	    
	    Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, width,
	            height);

	    //销毁缓存信息
	    view.destroyDrawingCache();
	    view.setDrawingCacheEnabled(false);

	    return bitmap;
	}
	
	Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
        	targetX = bm.getHeight();
        	targetY = 0;
        } else {
        	targetX = bm.getHeight();
        	targetY = bm.getWidth();
        }

	    final float[] values = new float[9];
	    m.getValues(values);
	
	    float x1 = values[Matrix.MTRANS_X];
	    float y1 = values[Matrix.MTRANS_Y];
	
	    m.postTranslate(targetX - x1, targetY - y1);
	
	    Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
	    Paint paint = new Paint();
	    Canvas canvas = new Canvas(bm1);
	    canvas.drawBitmap(bm, m, paint);
	
	    return bm1;
  }
	

	
	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
	}





	public char[] ReadCharFromFile(File file, boolean utf8, int len){
		char[] buffer = new char[len];
	       //如果path是传递过来的参数，可以做一个非目录的判断
	   	if (!file.exists()){
	   		return null;
	   	}
	   	if (file.isDirectory()){
	   		return null;
	   	}
       	InputStream instream = null;
       	boolean bReadOK = false;
        try {
	        instream = new FileInputStream(file);
	        if (instream != null) {
		        InputStreamReader inputreader = null;
		        if (utf8){
		        	inputreader = new InputStreamReader(instream, "utf-8");
		        }else{
		        	inputreader = new InputStreamReader(instream);
		        }
		        BufferedReader buffreader = new BufferedReader(inputreader);
		        bReadOK = (buffreader.read(buffer) == len);
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
        if (bReadOK){
        	return buffer;
        }
	    return null;
	       
	}
	
	

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public void sizeTransferFile(String strSrc, double fMultiple, boolean chengfa, boolean uft8){
		String strFileDst = strSrc + ".tmp";

	       File filesrc = new File(strSrc); 
	       InputStream instream = null;
	       
	       OutputStreamWriter out = null;
	       File filedst = null;

	       try {
	       String strContent = "";
	       String strContentPart = "";
	       int len = (int)filesrc.length();
	       char[] rbuffer = new char[len]; 
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
		        int len1 = buffreader.read(rbuffer, 0, len);
		        //if (len1 == len){
		        	strContentPart = new String(rbuffer);
		        	strContentPart = strContentPart.substring(0, len1);
		        	strContent = ModifyAllString(strContentPart, fMultiple, chengfa);
		        	if (bModify){
		 	    	   filedst = new File(strFileDst);
		 	    	   try {
		 	    		   filedst.createNewFile();
							} catch (IOException e) {
							// TODO Auto-generated catch block
								e.printStackTrace();
								return;
							}   
		 	    	   OutputStream outstream = new FileOutputStream(filedst);
		 	    	   if (uft8){
		 	    		   out= new OutputStreamWriter(outstream, "utf-8");
		 	    	   }else{
		 	    		   out= new OutputStreamWriter(outstream);
		 	    	   }
		 	    	   out.write(strContent);
		        	}
		        //}
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
   	        if (bModify){
   	        	String strNewName = strSrc + ".tmp1";
   	        	filesrc.renameTo(new File(strNewName));
   	        	filedst.renameTo(new File(strSrc));
   	        	new File(strNewName).delete();
   	        }
   	     
   	        
   	        
	}
	
	String strLeave = new String();
	static boolean bModify = false;
	public String ModifyAllString(String strSrc, double dMultiple, boolean chengfa){
		bModify = false;
		String strDst = "";
		strDst = ModifyString(strSrc, dMultiple, chengfa);
		while(strLeave.length() != 0){
			strSrc = strLeave;
			strDst += ModifyString(strSrc, dMultiple, chengfa);
		}
		return strDst;
	}
	
	//<dimen name="First_Title_Font_Size">30sp</dimen>
	public String ModifyString(String strSrc, double dMultiple, boolean chengfa){
		String strDst;
		
		
		// 1. 找到计量单位
		int type0 = -1; // 0 sp ; 1 dp ; 2 dip;
		int index0 = -1;
		int indexsp = -1;
		int indexdp = -1;
		int indexdip = -1;
		int indexlen = -1;
		String strType = "";
		
		indexsp = strSrc.indexOf("sp");
		indexdp = strSrc.indexOf("dp");
		indexdip = strSrc.indexOf("dip");
		
		if (indexsp == -1 && indexdp == -1 && indexdip == -1){
			strLeave = "";
			return strSrc;
		}
		// 获取计量单位的位置
		if (indexsp != -1 && indexdp != -1 && indexdip != -1){
			index0 = this.Minimum(indexsp, indexdp, indexdip);
		}else if (indexsp == -1){
			if (indexdp < indexdip){
				index0 = (indexdp != -1) ? indexdp : indexdip;
			}else{
				index0 = (indexdip != -1) ? indexdip : indexdp;
			}
		}else if (indexdp == -1){
			if (indexsp < indexdip){
				index0 = (indexsp != -1) ? indexsp : indexdip;
			}else{
				index0 = (indexdip != -1) ? indexdip : indexsp;
			}
		}else{
			if (indexsp < indexdp){
				index0 = (indexsp != -1) ? indexsp : indexdp;
			}else{
				index0 = (indexdp != -1) ? indexdp : indexsp;
			}
		}
		
		if (index0 == indexsp){
			type0 = 0;
			indexlen = 2;
			strType = "sp";
		}else if (index0 == indexdp){
			type0 = 1;
			indexlen = 2;
			strType = "dp";
		}else {
			type0 = 2;
			indexlen = 3;
			strType = "dip";
		}
		
		//String str = "<padding android:left=\"4dp\" android:top=\"2dp\" spssp android:top=\"2dp\" ddddd>";
		
		// 2. 取之前的一段然后找到"或者<
		String strTmp = strSrc.substring(0, index0);//不包含dp <padding android:left=\"4
		strLeave = strSrc.substring(index0 + indexlen);//跳过dp " android:top=\"2dp\" spssp android:top=\"2dp\" ddddd>
		//" android:top=\"2dp
		int index1 = strTmp.lastIndexOf("\"");
		int type1 = 0; // 0: " ; 1: <
		if (index1 < 0){
			index1 = strTmp.lastIndexOf(">");
			if (index1 < 0){
				strTmp = strSrc.substring(0, index0+ indexlen);
				return strTmp;
			}else{
				type1 = 1;
			}
		}else{
			// 处理">400的问题，又有"，又有>，但需要选>
			int index1_1 = strTmp.lastIndexOf(">");
			if (index1_1 > index1){
				index1 = index1_1;
			}
		}
		
		String strSub1 = strTmp.substring(0, index1+1); // 包含"或<那
		String strNum = strTmp.substring(index1+1);
		if (strNum.compareToIgnoreCase("-1") == 0){
			strTmp = strSrc.substring(0, index0+ indexlen);
			return strTmp;
		}
		double iNum = 0.0f;
		try {
			iNum = Double.parseDouble(strNum);
		}catch(NumberFormatException e){
			strTmp = strSrc.substring(0, index0+ indexlen);
			return strTmp;
		}
		double fNum = iNum / dMultiple;
		if (chengfa){
			fNum = iNum * dMultiple;
		}
		String strSub2 = new DecimalFormat("##.#").format(fNum);
		strSub2 += strType;
		strDst = strSub1 + strSub2;
		//Log.v("haha14", "haha14" + strDst);
		bModify = true;
		return strDst;
	}
	
	public void SizeTransferDir(String Path, String Extension, double fMultiple, boolean chengfa)
	{
	    File[] files = new File(Path).listFiles();
	 
	    for (int i = 0; i < files.length; i++)
	    {
	        File f = files[i];
	        if (f.isFile())
	        {
	            if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)){
	            	sizeTransferFile(f.getPath(), fMultiple, chengfa, false);
	            }
	        }
	        else if (f.isDirectory() && f.getPath().indexOf("/.") == -1)  //忽略点文件（隐藏文件/文件夹）
	        	SizeTransferDir(f.getPath(), Extension, fMultiple, chengfa);
	    }
	}
	
	private int Minimum(int num1,int num2,int num3)
	{
	    int min = (num1 < num2) ? num1 : num2;
	    min = (min < num3) ? min : num3;

	    return min;
	}
	
	private int needResetLantingFont(Context context){		
		String filePath1 = "/system/fonts/DroidSansFallback.ttf";
		String filePath2 = "/device/hanvon_resources/font/font2.ttf"; // 老用户
		String filePath3 = "/device/hanvon_resources/font/SourceHanSans-Medium.ttf"; //新用户
		File file1 = new File(filePath1);
		File file2 = new File(filePath2);
		File file3 = new File(filePath3);
		File fileTmp = null;
		if (!file1.exists()){
			Log.v("haha13", "haha13 failed2");
			return 0;
		}

		long len1 = file1.length();
		long len2 = 0;
		char[] buffer1 = null;
		char[] buffer2 = null;
		boolean bfile2Exist = false;
		if (file2.exists()){ // 老用户
			fileTmp = file2;
			bfile2Exist = true;
		}else if (file3.exists()){
			//fileTmp = file3;
			//bfile2Exist = false;
			int value = Settings.System.getInt(context.getContentResolver(), "hanvon_black", 0);
			if (value == 1){
				File file4 = new File("/system/etc/system_fonts.xml");
				File file5 = new File("/system/etc/system_fonts_black.xml");		
				buffer1 = ReadCharFromFile(file4, false, 1024);
				buffer2 = ReadCharFromFile(file5, false, 1024);
				if (buffer1 != null && buffer2 != null){
					String str1 = new String(buffer1);
					String str2 = new String(buffer2);
					if (str1.compareTo(str2) != 0){
						return 2;
					}
				}
				
			}
			return 0;
		}else{
			return 0;
		}
		len2 = fileTmp.length();
		if (len1 != len2){
			if (bfile2Exist)
				return 1;
		}
		buffer1 = ReadCharFromFile(file1, false, 10);
		buffer2 = ReadCharFromFile(fileTmp, false, 10);
		if (buffer1 != null && buffer2 != null){
			String str1 = new String(buffer1);
			String str2 = new String(buffer2);
			if (str1.compareTo(str2) != 0){
				if (bfile2Exist)
					return 1;
			}
		}
		Log.v("haha13", "haha13 failed 3");
		return 0;
	}


}
