package hanvon.aebr.hvtest;
import android.content.Context;
import android.provider.Settings;

public class hvLockscreenUtils {
	public static final int DISABLELOCKTIME = 604800000;
	public static final int DEFAULTLOCKTIME = 300*1000;
	public static int curlocktime = DEFAULTLOCKTIME;
	
	public static final String DEFLOCKPIC = "/system/media/standby/lockscreen_def.png";
	
	// 0: 没锁屏；1，锁屏；2，解锁画面，3，正在关机，不允许锁屏
	public static final String SETTINGS_SCREENLOCKSTATUS = "hanvon_screenlockstatus";
	
	// 解锁时是否正在显示解锁密码框
	public static final String SETTINGS_UNLOCKPSDSTATUS = "hanvon_isPsdActivityShow";
	
	// 是否有锁屏和开机密码:
	// 1. 如果有开机密码，插上USB线就不提示数据传输， SETTINGS_UNLOCKPSDSTATUS晚于usb消息，在开机时不可靠，开机结束了可靠
	public static final String SETTINGS_HASPASSWORD = "hanvon_haspassword";
	
	public static void setLockEnable(Context context, boolean bEnable){
		// dj Android8.1需要添加禁止和允许锁屏
		if (!bEnable){
			modifyLockTime(context, DISABLELOCKTIME);
		}else{
			int timeTmp = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, DEFAULTLOCKTIME);
			if (timeTmp == DISABLELOCKTIME)
				modifyLockTime(context, curlocktime);
		}
	}
	
	public static void restoreDefLocktime(Context context){
		modifyLockTime(context,DEFAULTLOCKTIME);
	}
	
	public static void forceRestoreDefTimeIfNeed(Context context){
		int timeTmp = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, DEFAULTLOCKTIME);
		if (timeTmp == DISABLELOCKTIME){
			restoreDefLocktime(context);
		}
	}
	
	public static void modifyLockTime(Context context, int time){
		int timeTmp = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, DEFAULTLOCKTIME);
		if (time != timeTmp){
			if (time == DISABLELOCKTIME){
				curlocktime = timeTmp;
			}
			Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, time);
			if (time != DISABLELOCKTIME){
				curlocktime = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, DEFAULTLOCKTIME);
			}
		}
	}
	
	public static void lockScreen(Context context){
		hvApkUtils.startActivityFromeApk(context, 
				  "hanvon.aebr.hvsettings", "hanvon.aebr.hvsettings.HvLockScreenActivity");
	}
	
	public static void unlockScreen(Context context){
		hvApkUtils.sendDynamicBroadcast(context, hvActionUtils.ACTION_HIDE_BACKLOG);
	}
	
	public static boolean isLocked(Context context){
		int locked = Settings.Secure.getInt(context.getContentResolver(), SETTINGS_SCREENLOCKSTATUS, 0);
		return ((locked == 1) || (locked == 2));
	}
	
	public static void setLockStatus(Context context, int status){
		int locked = Settings.Secure.getInt(context.getContentResolver(), SETTINGS_SCREENLOCKSTATUS, 0);
		if (locked != status){
			Settings.Secure.putInt(context.getContentResolver(), hvLockscreenUtils.SETTINGS_SCREENLOCKSTATUS, status);
		}
	}
	
	public static int getLockStatus(Context context){
		int status = Settings.Secure.getInt(context.getContentResolver(), SETTINGS_SCREENLOCKSTATUS, 0);
		return status;
	}

	public static boolean isPsdActivityShow(Context context) {
		return (Settings.Secure.getInt(context.getContentResolver(), SETTINGS_UNLOCKPSDSTATUS, 0) != 0);
	}

	public static void setPsdActivityShow(Context context, boolean visible) {
		int value = (visible)? 1 : 0;
		Settings.Secure.putInt(context.getContentResolver(), SETTINGS_UNLOCKPSDSTATUS, value);
	}
	
	public static void setPsdType(Context context, int type){
		int tmp = Settings.Secure.getInt(context.getContentResolver(), SETTINGS_HASPASSWORD, 
 				0);
		if (tmp != type){
			Settings.Secure.putInt(context.getContentResolver(), SETTINGS_HASPASSWORD, type);
		}
	}
	
	public static int getPsdType(Context context){
		return Settings.Secure.getInt(context.getContentResolver(), SETTINGS_HASPASSWORD, 
 				0);
	}
}

