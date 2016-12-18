package com.jyqqhw.gridpagerview.indicator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by wj on 16-12-18.
 */
public abstract class IndicShape {

	protected Paint paint;

	protected Rect rect;

	protected int width;

	protected int height;

	public IndicShape(){
		init();
	}

	public IndicShape(int w, int h){
		this();
		if(w>=0){
			width = w;
		}
		if(h>=0){
			height = h;
		}
	}

	private void init(){
		rect = new Rect();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(2);
	}

	public abstract void drawSelf(Canvas canvas);

	public void drawSelf(Canvas canvas, Paint paint){

	}


	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
