package com.camera.andraft;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class OverlayView extends View {
    public static interface UpdateDoneCallback { 
        public void onUpdateDone(); 
    }  
   
    private UpdateDoneCallback updateDoneCb = null; 
    private Bitmap targetBMP = null;
    private Rect targetRect = null;
    private Paint paint=null;
    

    public OverlayView(Context c, AttributeSet attr) {
        super(c, attr); 
    }

    public void DrawResult(Bitmap bmp) {
        if ( targetRect == null)
            targetRect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        targetBMP = bmp;
        postInvalidate(); 
    }

    public void setUpdateDoneCallback(UpdateDoneCallback cb) {
        updateDoneCb = cb;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if ( targetBMP != null ) {   
        	paint = new Paint();
        	paint.setColor(Color.GREEN);
        	paint.setTextSize(10); 
        	canvas.drawText("lalka", 0, 0, paint);
            canvas.drawBitmap(targetBMP, null, targetRect, null);

            
            
                        
            if ( updateDoneCb != null)
                updateDoneCb.onUpdateDone();
        }
    }

	

}
