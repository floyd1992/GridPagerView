package com.jyqqhw.gridpagerview.resolver;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 16-12-18.
 * @hide
 */
public class GridPagerIndicator extends View implements CustomLinearLayout.OnPageChangeListener {

	private int viewWidth, viewHeight;
	private int indicatorCnt;
	private int indicatorGap = 10;
	private int paddingLeft, paddingTop=9, paddingRight, paddingBottom;
	private int indicatorSize = 10;
	private Rect drawRect = new Rect();
	private IndicShape indicShape;
	private List<IndicShape> indicShapes = new ArrayList<IndicShape>();
	private Paint unSelectedPaint, selectedPaint;
	private int currentPage;
	private GridPagerView gridPagerView;


	public GridPagerIndicator(Context context) {
		super(context);
		init();
	}

	public GridPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GridPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init(){
		indicShape = new CircleShape();
		selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		selectedPaint.setStrokeWidth(2);
		selectedPaint.setColor(Color.GREEN);

		unSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		unSelectedPaint.setStrokeWidth(2);
		unSelectedPaint.setColor(Color.GRAY);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewWidth = w;
		viewHeight = h;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawIndicator(canvas);
	}

	private void drawIndicator(Canvas canvas){
		if(null==indicShapes || indicShapes.size() ==1){
			setVisibility(INVISIBLE);
			return;
		}
		Rect rect = new Rect(drawRect);
		rect.top += paddingTop;
		rect.bottom -= paddingBottom;
		int c = 0;
		for(IndicShape shape: indicShapes){
			rect.right = rect.left + shape.getWidth();
			shape.setRect(rect);
			if(c == currentPage){
				shape.drawSelfSelected(canvas);
			}else{
				shape.drawSelfUnselected(canvas);
			}
			rect.left = rect.right + indicatorGap;
			c++;
		}
	}

	private void calculateDrawingRegion(){
		int measuredWidth = 0;
		int measuredHeight = 0;
		for(IndicShape shape: indicShapes){
			measuredWidth += shape.getWidth()+indicatorGap;
			measuredHeight = shape.getHeight();
		}
		if(0!= measuredWidth){
			measuredWidth-=indicatorGap;
		}

		int rW = viewWidth - measuredWidth - paddingLeft - paddingRight;
		if(rW > 0){
			drawRect.left = rW/2;
			drawRect.right = viewWidth - rW/2;
		}else{
			throw new IllegalArgumentException("there is no enough width space..");
		}

		int rH = viewHeight - measuredHeight - paddingTop - paddingBottom;
		if(rH > 0){
			drawRect.top = rH/2;
			drawRect.bottom = viewHeight - rH/2;
		}else{
//			drawRect.top = 0;
//			drawRect.bottom = viewHeight;
			throw new IllegalArgumentException("there is no enough height space..");
		}

	}

	private void initIndicator(){
		int pc = gridPagerView.getPageCount();
		if(pc != indicatorCnt){
			indicShapes.clear();
			for(int i=0;i<pc;i++){
				IndicShape indicShape = getNewIndicShape();
				indicShapes.add(indicShape);
			}
			calculateDrawingRegion();
			if(getVisibility()!=VISIBLE && 2==pc&& 1== indicatorCnt){
				setVisibility(VISIBLE);
			}
			indicatorCnt = pc;
		}
		invalidate();
	}

	public void setGridPagerView(GridPagerView gridPagerView){
		if(null != gridPagerView){
			this.gridPagerView = gridPagerView;
			setOnPagerChangeListener();
			postDelayed(new Runnable() {
				@Override
				public void run() {
					initIndicator();
				}
			}, 300);
		}
	}


	private IndicShape getNewIndicShape(){
		//default implement.
		return new CircleShape();
	}

	private void setOnPagerChangeListener(){
		gridPagerView.setOnPageChangeListener(this);
	}


	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		currentPage = position;
		initIndicator();
	}

	@Override
	public void onPageDataSetChanged(boolean changed, int position) {
		if(changed){
			currentPage = position;
			initIndicator();
		}
	}

}
