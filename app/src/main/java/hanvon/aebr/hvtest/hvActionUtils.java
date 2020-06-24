package hanvon.aebr.hvtest;

public class hvActionUtils {
	
	// hvLauncher需要发送，不需要添加到受保护广播列表
	public static final String ACTION_CHECKPSD = "hanvon.intent.actionHvsettings.checkpsd";
	
	// 内部发送，需要添加到受保护广播列表
	public static final String ACTION_FLIPPAGE = "hanvon.intent.actionHvsettings.FLIPPAGE";
	public static final String ACTION_FORCEUPDATE = "hanvon.intent.actionHvsettings.forceupdate";
	public static final String ACTION_AUTOWAKEUP = "hanvon.intent.actionHvsettings.AUTOWAKEUP";
	
	/*
	public static final String ACTION_FORCEUPDATE1 = "hanvon.intent.action.forceupdate";
	public static final String ACTION_FORCEUPDATE2 = "hanvon.intent.action.forceupdate";	
	public static final String ACTION_FORCEUPDATE3 = "hanvon.intent.action.forceupdate";	
	public static final String ACTION_FORCEUPDATE4 = "hanvon.intent.action.forceupdate";	
	public static final String ACTION_FORCEUPDATE5 = "hanvon.intent.action.forceupdate";
	public static final String ACTION_FORCEUPDATE6 = "hanvon.intent.action.forceupdate";
	public static final String ACTION_FORCEUPDATE7 = "hanvon.intent.action.forceupdate";	
	public static final String ACTION_FORCEUPDATE8 = "hanvon.intent.action.forceupdate";	
	public static final String ACTION_FORCEUPDATE9 = "hanvon.intent.action.forceupdate";	
	*/
	// framework 锁屏时层发送的，已添加到framework/base/core/res/AndroidManifest.xml的受保护广播中
	public static final String ACTION_LOCKWHENUNLOCKPICSHOW = "hanvon.intent.action.lockwhenunlockpicshowing";
	public static final String ACTION_HIDE_BACKLOG = "android.intent.action.HIDE_BACKLOGO";
	
}

