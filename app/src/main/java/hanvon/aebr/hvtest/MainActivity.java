package hanvon.aebr.hvtest;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(new MySurfaceView_Error(this));
        //parseString();
        setContentView(new MySurfaceView_OK(this));
        String str = Intent.ACTION_CONFIGURATION_CHANGED;
        //setContentView(new MySurfaceView_OK_Thread(this));
        //setContentView(new GameViewSurface(this));
        //setContentView(new DrawingWithBezier(this));
        //setContentView(new DrawingWithoutBezier(this));
       // setContentView(new MySurfaceView_Thread(this));
        
        //Settings.System.putInt(getContentResolver(), "hanvon_settings_forpsd", 0);
        //startMyActivity();
        //setContentView(R.layout.main);
        
        //EditText edit = (EditText)findViewById(R.id.EditText1);
        //edit.getPaint().setAntiAlias(false);
    }
    
//    private void startMyActivity(){
//    	String pkg = "hanvon.aebr.hvLauncher";
//    	String cls = "hanvon.aebr.hvLauncher.hvFilemanager.FileManagerActivity";
//        ComponentName componet = new ComponentName(pkg, cls);
//     Intent i = new Intent(android.content.Intent.ACTION_VIEW);
//     i.setComponent(componet); 
//     i.setAction("hanvon.com.filemanager.enter.3rd");
//     i.putExtra("3rdpath", "/storage/sdcard0/我的文档/屏幕截图");
//     try {
//         startActivity(i);
//     }catch(ActivityNotFoundException e){
//         Toast.makeText(MainActivity.this, "haha", 
//                Toast.LENGTH_LONG).show();
//         return;
//     }
//
//    }
    
    private void parseString(){
    	String str = "aaaa:";
    	String str1 = "aaaa:bbbb";
    	String str2 = "aaaa:bbbb:";
    	String str3 = "aaaa:bbbb:ccc";
    	int idx = str.indexOf(":");
    	String date[] = str.split(":");
    	int nLen = date.length;
    	String date1[] = str1.split(":");
    	int nLen1 = date1.length;
    	String date2[] = str2.split(":");
    	int nLen2 = date2.length;
    	String date3[] = str3.split(":");
    	int nLen3 = date3.length;
    	int nLen4 = nLen3;
    	
    }
    
}