package com.jyqqhw.gridpagerview.resolver;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by wj on 16-12-18.
 * @hide
 */
public class CircleShape extends IndicShape {

	private  int defaultWidth = 8;
	private  int defaultHeight = 8;
	private Paint defaultPaint;

	private int x, y,radius;

	public CircleShape(){
		super(8, 8);
		initPaint();
	}

	private void initPaint(){
		selectedPaint.setColor(Color.parseColor("#68D415"));
		unselectedPaint.setColor(Color.parseColor("#aaaaaa"));
		defaultPaint = unselectedPaint;
	}


	@Override
	public void drawSelf(Canvas canvas) {
		calculateParam();
		canvas.drawCircle(x, y, radius, defaultPaint);
	}

	@Override
	public void drawSelfSelected(Canvas canvas) {
		super.drawSelfSelected(canvas);
		this.defaultPaint = selectedPaint;
		drawSelf(canvas);
	}

	@Override
	public void drawSelfUnselected(Canvas canvas) {
		super.drawSelfUnselected(canvas);
		this.defaultPaint = unselectedPaint;
		drawSelf(canvas);
	}

	private void calculateParam(){
		x = rect.left + rect.width()/2;
		y = rect.top + rect.height()/2;
		radius = rect.width()/2;
	}



}
