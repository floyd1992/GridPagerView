package com.jyqqhw.gridpagerview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.view.View;
import android.widget.ListAdapter;

/**
 * Created by wj on 16-12-17.
 */
public class SimpleGridView  extends CustomLinearLayout<ListAdapter> {

	private int columnNum = 4;
	private int rowNum = 2;

	private int itemCount;

	private int viewWidth, viewHeight;

	private ListAdapter mAdapter;

	private LongSparseArray<View> itemViews = new LongSparseArray<View>();
	private LongSparseArray<Rect> itemLocations = new LongSparseArray<Rect>();

	public SimpleGridView(Context context) {
		super(context);
	}

	public SimpleGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SimpleGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//		int cnt = null==mAdapter?0:mAdapter.getCount();
//		for(int i=0; i< cnt; i++){
//
//		}
//
//
//		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewWidth = w;
		viewHeight = h;
	}

	private int width;

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		computeChildPosition();

		int cnt = null==mAdapter?0:mAdapter.getCount();
		int pageCnt = columnNum*rowNum;
		for(int i=0; i< cnt; i++){
			View v = itemViews.get(i);
			Rect rect = itemLocations.get(i%pageCnt);
			v.layout(rect.left, rect.top, rect.right, rect.bottom);
		}
	}

	@Override
	public ListAdapter getAdapter() {
		return mAdapter;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if(null == adapter){
			return;
		}
		mAdapter = adapter;
		int cnt = mAdapter.getCount();

		resizeViewToStandard();
		LayoutParams lp = generateDefaultLayoutParams();
		lp.width = standardWidth;
		lp.height = standardHeight;
		for(int i=0; i<cnt; i++){
			View v = mAdapter.getView(i, null, this);
			itemViews.put(i, v);
			addView(v, lp);
		}
	}

	private int standardWidth, standardHeight;
	private int paddingTop, paddingBottom, paddingLeft, paddingRight;

	private void resizeViewToStandard(){
		standardWidth = 80;
		standardHeight = 80;
		paddingLeft = 10;
		paddingTop = 10;
		paddingRight = 10;
		paddingBottom = 10;
	}

	private void computeChildPosition(){
		resizeViewToStandard();
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

		int widthExtra = paddingLeft+paddingRight;
		int heightExtra = paddingTop+paddingBottom;
		int deltaX=0, deltaY=0;
		for(int i=0;i<rowNum; i++){
			for(int j=0;j<columnNum;j++){
				Rect rect = new Rect();
				rect.left = deltaX;
				rect.top = deltaY;
				rect.right = deltaX += widthExtra + standardWidth;
				rect.bottom = deltaY + heightExtra + standardHeight;
				itemLocations.put(j+i*columnNum, rect);
			}
			deltaY+=heightExtra+standardHeight;
			deltaX = 0;
		}

	}



}
