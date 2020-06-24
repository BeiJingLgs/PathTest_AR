package hanvon.aebr.hvtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class MySurfaceView_Thread extends SurfaceView implements Callback,Runnable
{
    private Context mContex;
    private float mX;
    private float mY;

    private SurfaceHolder sfh;
    private Canvas canvas;

    private final Paint mGesturePaint = new Paint();
    private final Path mPath = new Path();
    private final Rect mInvalidRect = new Rect();
    private final Rect mTmpRect = new Rect();
    private final int offset =2;
    
    private boolean isDrawing;
    //HANVONEBK ebk = new HANVONEBK();
    
    private boolean bWhiteBackground = true;
    private final Object mLock = new Object();
    private final int COUNT_LIMIT = 16;
    
    @Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
    	//ebk.EnterHWMode(1);
    	String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/trace1";
    	//Debug.startMethodTracing(path);
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		//ebk.EnterHWMode(0);
		//Debug.stopMethodTracing();
		super.onDetachedFromWindow();
	}

	public MySurfaceView_Thread(Context context)
    {
        super(context);
        mContex = context;
        sfh = this.getHolder();
        sfh.addCallback(this); 
        mGesturePaint.setAntiAlias(false);
        mGesturePaint.setStyle(Style.STROKE);
        mGesturePaint.setStrokeWidth(6);
        if (bWhiteBackground){
        	mGesturePaint.setColor(Color.BLACK);
        }else{
        	mGesturePaint.setColor(Color.WHITE);
        }
        // TODO Auto-generated constructor stub
    }

    public void drawCanvas(Rect rt) {
        try {
        	//Android2.2 6寸高分辨率 lockCanvas(rt)跟随非常快，满意，canvas = sfh.lockCanvas();慢一些
        	//Android4.4 9寸低分辨率 点更大，绘制慢一些
        	//重点研究下Android2.2 lockCanvas比普通的画线快的原因
            canvas = sfh.lockCanvas(rt);
            if (canvas != null) {
            	if (bWhiteBackground){
            		canvas.drawColor(Color.WHITE);
            	}
            	canvas.drawPath(mPath, mGesturePaint);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);
        }
    }
    
    //long time = 0;
    //long time1 = 0;
    Rect rect = null;
    Rect rectMove = new Rect();
    int nCount = 0;
    boolean mIsRunning = false; 
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            	rect = touchDown(event);
                drawCanvas(rect);
                //invalidate(rect);
                return true;

            case MotionEvent.ACTION_MOVE:
                if (isDrawing)
                {
                	//time = System.currentTimeMillis();
                	//Log.v("haha2", "haha2 move time=" + (time - time1));
                    //time1 = time;
                	while (true){
	                	synchronized (mLock){
	                    rectMove.setEmpty();
	                	int N = event.getHistorySize();
	                	for (int i=0; i<N; i++) {
	                		rect = touchMove(event.getHistoricalX(i), event.getHistoricalY(i));
	                		rectMove.union(rect);
	                		nCount++;
	                	}
	                    final float x = event.getX();
	                    final float y = event.getY();
	                    rect = touchMove(x, y);
	                    //如果不调用drawCanvas和invalidate，报点速度最多0-13ms，调用了报点速度最少21-30，所以可以把drawCanvas放到线程里
	                    rectMove.union(rect);
	                    nCount++;
	                    if (nCount >= COUNT_LIMIT){
	                    	nCount = 0;
	                    	Log.v("haha9", "haha9 COUNT_LIMIT");
	                    	break;
	                    }
	                	}
                	}
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (isDrawing)
                {
                    touchUp(event);
                    //invalidate(UPDATE_MODE_PARTIAL);//为了调试Move时画不出线，临时注释
                    return true;
                }
                break;        
        }
        return super.onTouchEvent(event);
    }

    private Rect touchDown(MotionEvent event)
    {
        isDrawing = true;
        float x = event.getX();
        float y = event.getY();
        mX = x;
        mY = y;

        mPath.reset();
        mPath.moveTo(x, y);
        setInvalidateRect(x, y);
        return mInvalidRect;
    }
    
    private Rect touchMove(float x, float y)
    {
    	mPath.lineTo(x, y);
        setInvalidateRect(x, y);
        mX = x;
        mY = y;
        return mInvalidRect;
    }
    
    private void setInvalidateRect(float x, float y){
        mInvalidRect.set((int) Math.min(mX,
				x) - offset, (int) Math.min(mY,
				y) - offset, (int) Math.max(mX,
				x) + offset, (int) Math.max(mY,
				y) + offset); //关键点在此，画线必须是一个矩形(left<right ,top < bottom)，否则画不出来
    }
    
    private void touchUp(MotionEvent event)
    {
        isDrawing = false;
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) { 
     // TODO Auto-generated method stub 
    	if (bWhiteBackground){
    		canvas =sfh.lockCanvas();
    		canvas.drawColor(Color.WHITE);
    		sfh.unlockCanvasAndPost(canvas);
    	}
    	mIsRunning = true;
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (mIsRunning){
			if (!rectMove.isEmpty()){
				synchronized (mLock){
					drawCanvas(rectMove);
                	Log.v("haha9", "haha9 drawCanvas");
                	rectMove.setEmpty();
				}
			}
		}	
	} 
}
