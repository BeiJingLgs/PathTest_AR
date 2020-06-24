package hanvon.aebr.hvtest;
  
import android.content.Context; 
import android.graphics.Bitmap; 
import android.graphics.BitmapFactory; 
import android.graphics.Canvas; 
import android.graphics.Color;
import android.graphics.Paint; 
import android.graphics.Paint.Style; 
import android.graphics.Path;
import android.util.AttributeSet; 
import android.view.MotionEvent; 
import android.view.SurfaceHolder; 
import android.view.SurfaceHolder.Callback; 
import android.view.SurfaceView;
import android.view.View;
  
public class GameViewSurface extends SurfaceView implements Callback,Runnable{ 
   
   
 /** 控制游戏更新循环 **/
 boolean mRunning = false; 
   
 /**控制游戏循环**/
 boolean mIsRunning = false; 
   
 /**每50帧刷新一次屏幕**/ 
 public static final int TIME_IN_FRAME = 16; 
  
 private int paintColor=android.graphics.Color.WHITE;//默认画笔颜色为黑色 
   
 private float paintWidth=2f;//默认画笔宽度 
   
 private Style paintStyle=Style.STROKE;//默认画笔风格 
   
 private int paintAlph=255;//默认不透明 
   
 private Path mPath;//轨迹 
   
 private Paint mPaint;//画笔 
   
 private float startX=0.0f;//初始x 
   
 private float startY=0.0f;//初始Y 
   
 private SurfaceHolder surfaceHolder; 
   
 public Canvas mCanvas; 
   
 public boolean first=true; 
   
 Bitmap bg = null;
 
 //private static final int UPDATE_MODE_PARTIAL = EINK_AUTO_MODE_REGIONAL| EINK_WAIT_MODE_NOWAIT | EINK_WAVEFORM_MODE_DU | EINK_UPDATE_MODE_PARTIAL;
 //private static final int UPDATE_MODE_FULL    = EINK_AUTO_MODE_REGIONAL| EINK_WAIT_MODE_WAIT |  EINK_WAVEFORM_MODE_AUTO  |  EINK_UPDATE_MODE_PARTIAL;  
 public GameViewSurface(Context context){ 
  this(context,null); 
 } 
 public GameViewSurface(Context context,AttributeSet attrs){ 
  this(context,attrs,0); 
 } 
 public GameViewSurface(Context context, AttributeSet attrs, int defStyle) { 
  super(context, attrs, defStyle); 
  // TODO Auto-generated constructor stub 
  this.setFocusable(true);//设置当前view拥有触摸事件 
    
  surfaceHolder=getHolder(); 
  surfaceHolder.addCallback(this); 
    
  mPath=new Path(); 
  initPaint();
 } 
 
 @Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
	}
 /** 
 * @Title: initPaint 
 * @Description: TODO(初始化画笔) 
 * @param  设定文件 
 * @return void 返回类型 
 * @throws 
 */
 private void initPaint(){ 
  mPaint=new Paint();
  mPaint.setAntiAlias(false);
  mPaint.setColor(paintColor);//画笔颜色 
  mPaint.setStyle(paintStyle);//设置画笔风格 
  mPaint.setStrokeWidth(paintWidth);//设置画笔宽度 
 } 
   
 public void doDraw(){
  mCanvas=surfaceHolder.lockCanvas(); 
  mCanvas.drawPath(mPath, mPaint);//绘制 
  surfaceHolder.unlockCanvasAndPost(mCanvas);
 }  
 @Override
 public boolean onTouchEvent(MotionEvent event) { 
  // TODO Auto-generated method stub 
    
  switch (event.getAction()) { 
  case MotionEvent.ACTION_DOWN: 
   //手接触屏幕时触发 
   doTouchDown(event); 
   break; 
  case MotionEvent.ACTION_MOVE: 
   //手滑动时触发 
//  	int N = event.getHistorySize();
//  	for (int i=0; i<N; i++) {
//  		doTouchMove(event.getHistoricalX(i), event.getHistoricalY(i));
//  	}
   doTouchMove(event.getX(), event.getY()); 
   break; 
     
  case MotionEvent.ACTION_UP: 
   //手抬起时触发 
     
   break; 
     
  
  default: 
   break; 
  } 
  return true; 
 } 
   
 /** 
 * @Title: doTouchDown 
 * @Description: TODO(手触摸到屏幕时需要做的事情) 
 * @param @param event 设定文件 
 * @return void 返回类型 
 * @throws 
 */
 private void doTouchDown(MotionEvent event){ 
    
  float touchX=event.getX(); 
  float touchY=event.getY(); 
  startX=touchX; 
  startY=touchY; 
  mPath.reset(); 
  mPath.moveTo(touchX, touchY); 
 } 
   
 /** 
 * @Title: doTouchMove 
 * @Description: TODO(手在屏幕上滑动时要做的事情) 
 * @param @param event 设定文件 
 * @return void 返回类型 
 * @throws 
 */
 private void doTouchMove(float x, float y){ 
   mPath.lineTo(x, y);
 } 
 
 
   
 @Override
 public void run() { 
  // TODO Auto-generated method stub 
  while (mIsRunning) { 
     
   /** 取得更新游戏之前的时间 **/
   long startTime = System.currentTimeMillis(); 
   /** 在这里加上线程安全锁 **/
    doDraw(); 
     
   /** 取得更新游戏结束的时间 **/
   long endTime = System.currentTimeMillis(); 
   
   /** 计算出游戏一次更新的毫秒数 **/
   int diffTime = (int) (endTime - startTime); 
   
   /** 确保每次更新时间为50帧 **/
   while (diffTime <= TIME_IN_FRAME) { 
    diffTime = (int) (System.currentTimeMillis() - startTime); 
    /** 线程等待 **/
    Thread.yield();
   } 
   
   } 
    
 } 
 @Override
 public void surfaceCreated(SurfaceHolder holder) { 
  // TODO Auto-generated method stub 
  mCanvas =surfaceHolder.lockCanvas();
  //mCanvas.drawBitmap(bg, 0,0, null);
  //mCanvas.drawColor(Color.WHITE);
  surfaceHolder.unlockCanvasAndPost(mCanvas); 
    
  mIsRunning=true; 
  new Thread(this).start(); 
 } 
 @Override
 public void surfaceChanged(SurfaceHolder holder, int format, int width, 
   int height) { 
  // TODO Auto-generated method stub 
    
 } 
 @Override
 public void surfaceDestroyed(SurfaceHolder holder) { 
  // TODO Auto-generated method stub 
   mIsRunning = false; 
 } 
   
   
} 