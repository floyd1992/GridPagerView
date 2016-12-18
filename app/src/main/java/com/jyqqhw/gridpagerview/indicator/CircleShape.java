package com.jyqqhw.gridpagerview.indicator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by wj on 16-12-18.
 */
public class CircleShape extends IndicShape {

	private  int defaultWidth = 8;
	private  int defaultHeight = 8;

	private int x, y,radius;

	public CircleShape(){
		super(8, 8);
	}

	@Override
	public void drawSelf(Canvas canvas) {
		calculateParam();
		canvas.drawCircle(x, y, radius, paint);
	}

	public void drawSelf(Canvas canvas, Paint paint){
		if(null != paint){
			this.paint = new Paint(paint);
		}
		drawSelf(canvas);
	}

	private void calculateParam(){
		x = rect.left + rect.width()/2;
		y = rect.top + rect.height()/2;
		radius = rect.width()/2;
	}



}
