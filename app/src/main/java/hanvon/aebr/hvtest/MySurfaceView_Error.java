package hanvon.aebr.hvtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView_Error extends SurfaceView 
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
    private final int offset = 2;
    
    private boolean isDrawing;
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

	public MySurfaceView_Error(Context context)
    {
        super(context);
        mContex = context;
        sfh = this.getHolder();
        mGesturePaint.setAntiAlias(false);
        mGesturePaint.setStyle(Style.STROKE);
        mGesturePaint.setStrokeWidth(3);
        mGesturePaint.setColor(Color.WHITE);
        // TODO Auto-generated constructor stub
    }

    public void drawCanvas(Rect rt) {
        try {
        	//Android2.2 lockCanvas(rt)跟随非常快，满意，canvas = sfh.lockCanvas();慢一些
        	//Android4.4 lockCanvas(rt)比lockCanvas()慢
        	//Android4.4 
            if (canvas != null) {
                canvas.drawPath(mPath, mGesturePaint);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);
        }
    }
    
    long time = 0;
    long time1 = 0;
    Rect rect = null;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            	rect = touchDown(event);
                drawCanvas(rect);
                return true;

            case MotionEvent.ACTION_MOVE:
                if (isDrawing)
                {
                	time = System.currentTimeMillis();
                	Log.v("haha2", "haha2 move time=" + (time - time1));
                    time1 = time;
//                	int N = event.getHistorySize();
//                	for (int i=0; i<N; i++) {
//                		rect = touchMove(event.getHistoricalX(i), event.getHistoricalY(i));
//                	}
                    final float x = event.getX();
                    final float y = event.getY();
                    rect = touchMove(x, y);
                    //如果不调用drawCanvas和invalidate，报点速度最多0-13ms，调用了报点速度最少21-30，所以可以把drawCanvas放到线程里
                    drawCanvas(rect);
                    return true;
                }
                break;
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
        mPath.reset();
        float x = event.getX();
        float y = event.getY();
        
        mX = x;
        mY = y;
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
//        mInvalidRect.set((int) Math.min(mX,
//				x) - offset, (int) Math.min(mY,
//				y) - offset, (int) Math.max(mX,
//				x) + offset, (int) Math.max(mY,
//				y) + offset); //关键点在此，画线必须是一个矩形(left<right ,top < bottom)，否则画不出来
    	mInvalidRect.set((int)mX, (int)mY, (int)x, (int)y);
    }
    
    private void touchUp(MotionEvent event)
    {
        isDrawing = false;
    }
}
