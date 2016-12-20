package com.jyqqhw.gridpagerview.resolver;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by wj on 16-12-18.
 * @hide
 */
public abstract class IndicShape {

	protected Paint selectedPaint, unselectedPaint;

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
		selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		selectedPaint.setColor(Color.GREEN);
		selectedPaint.setStrokeWidth(2);

		unselectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		unselectedPaint.setColor(Color.GRAY);
		unselectedPaint.setStrokeWidth(2);
	}

	public abstract void drawSelf(Canvas canvas);

	public void drawSelfSelected(Canvas canvas){

	}

	public void drawSelfUnselected(Canvas canvas){

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
