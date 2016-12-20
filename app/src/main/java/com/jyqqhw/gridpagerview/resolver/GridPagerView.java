package com.jyqqhw.gridpagerview.resolver;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.ListAdapter;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 16-12-17.
 * @hide
 */
public class GridPagerView extends CustomLinearLayout<ListAdapter> {

	private static final int TYPE_PAGE_CHANGED = 1;
	private static final int TYPE_DATA_CHANGED = 2;

	private static final int ANIMATOR_DURATION = 250;

	private int columnNum = 4;
	private int rowNum = 2;

	private int standardWidth, standardHeight;
	private int paddingTop, paddingBottom, paddingLeft, paddingRight;
	private int itemHorizontalGap, itemVerticalGap;

	private int currentPage, lastPage;
	private int totalPage;

	private int viewWidth, viewHeight;

	private int touchSlop;
	private Scroller scroller;

	private LongSparseArray<View> itemViews = new LongSparseArray<View>();
	private LongSparseArray<Rect> itemLocations = new LongSparseArray<Rect>();

	private OnItemClickListener onItemClickListener;
	private OnItemLongClickListener onItemLongClickListener;
	private List<OnPageChangeListener> onPageChangeListeners = new ArrayList<OnPageChangeListener>();

	public GridPagerView(Context context) {
		super(context);
		init(context);
	}

	public GridPagerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public GridPagerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context){
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		scroller = new Scroller(context, new LinearInterpolator());
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int heightSize = 0;
		if(itemViews.size()>0){
			View v = itemViews.get(0);
			standardWidth = v.getMeasuredWidth();
			standardHeight = v.getMeasuredHeight();
		}
		if(itemViews.size() > 4){
			heightSize = 2*standardHeight + paddingTop+itemVerticalGap+ paddingBottom;
		}else{
			heightSize = standardHeight+paddingTop+paddingBottom;
		}
		setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));

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

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		computeChildPosition();

		int cnt = null==mAdapter?0:mAdapter.getCount();
		int pageCnt = columnNum*rowNum;

		int pc = cnt/pageCnt;
		int rest = cnt%pageCnt==0?0:1;
		totalPage = pc + rest;
		int current = 0;
		int baseWidth = 0;
		for(int i=0;i < totalPage;i++){
			current = cnt-(i+1)*pageCnt>=0?pageCnt:cnt-i*pageCnt;
			for(int j=0;j<current;j++){
				View v = itemViews.get(j+i*pageCnt);
				Rect rect = itemLocations.get(j);
				v.layout(baseWidth+rect.left, rect.top, baseWidth+rect.right, rect.bottom);
			}
			baseWidth += getMeasuredWidth();
		}
	}

	private int downX, downY;
	private int cX, cY;
	private int diffX, diffY;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		int action = ev.getActionMasked();
		switch (action){
			case MotionEvent.ACTION_DOWN:
				downX = (int) ev.getX();
				downY = (int) ev.getY();
				Log.i("wj","on intercept down "+touchSlop);
				break;
			case MotionEvent.ACTION_MOVE:
				cX = (int) ev.getX();
				cY = (int) ev.getY();
				int diffX = cX - downX;
				Log.i("wj","on intercept move not intercept "+diffX+", cX="+cX+". downX="+downX);

				if( Math.abs(diffX) > touchSlop ){
					downX = (int) ev.getX();
					downY = (int) ev.getY();
					Log.i("wj","on intercept move ");
					return true;
				}

				break;
			case MotionEvent.ACTION_UP:
				break;
			default:
				break;
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();
		switch (action){
			case MotionEvent.ACTION_DOWN:
				downX = (int) event.getX();
				downY = (int) event.getY();
				Log.i("wj","on touch down ");
				break;
			case MotionEvent.ACTION_MOVE:
				cX = (int) event.getX();
				cY = (int) event.getY();
				diffX = cX - downX;
				if( Math.abs(diffX) > 1 ){
					scrollTo(currentPage*getWidth()-diffX, 0);
					Log.i("wj","scroll diffX "+diffX);
				}
				break;
			case MotionEvent.ACTION_UP:
				resumeToPage();
				break;
			default:
				break;
		}
		return true;
	}

	private void resumeToPage(){
		lastPage = currentPage;
		int halfW = (int) (getWidth()/3.5);
		if(diffX + halfW < 0){
			if( currentPage < totalPage-1){
				currentPage++;
			}else{
				//恢复到当前尾页.
			}
		}else{
			//恢复到当前页.
		}

		if( diffX - halfW > 0){
			if(currentPage>0){
				currentPage--;
			}else{
				//恢复到当前首页.
			}
		}else{
			//恢复到当前页.
		}

		smoothScrollToX(currentPage*getWidth());

		if(currentPage!=lastPage){
			dispatchPageChangeEvent(TYPE_PAGE_CHANGED);
		}
		Log.d("wj","ready to resume"+currentPage*getWidth());
	}

	private void smoothScrollToX(int finalX){
		int cx = getScrollX();
		scroller.startScroll(cx, 0, finalX - cx, 0, ANIMATOR_DURATION);
		postInvalidate();
	}


	@Override
	public void computeScroll() {
		super.computeScroll();
		if(scroller.computeScrollOffset()){
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
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
		super.setAdapter(adapter);
		refreshGridView();
	}

	private void refreshGridView(){
		int cnt = mAdapter.getCount();
		if(cnt != itemCount){
			itemViews.clear();
			removeAllViews();

			resizeViewToStandard();
			LayoutParams lp = generateDefaultLayoutParams();
			lp.width = -2;
			lp.height = -2;
//			lp.width = standardWidth;
//			lp.height = standardHeight;
			for(int i=0; i<cnt; i++){
				View v = mAdapter.getView(i, null, this);
				itemViews.put(i, v);
			addView(v, lp);
//				addView(v);
				initItemEvents(v, i);
			}
			itemCount = cnt;
			requestLayout();
		}
	}

	private void resizeViewToStandard(){

		paddingRight = 20;
		paddingBottom = 17;
		itemHorizontalGap = 48;
		itemVerticalGap = 16;
		paddingLeft = (getMeasuredWidth() - (itemHorizontalGap+standardWidth)*columnNum+itemHorizontalGap)/2;
		paddingTop = (getMeasuredHeight() - (itemVerticalGap+standardHeight)*rowNum+itemVerticalGap)/2;
	}

	private void computeChildPosition(){
		resizeViewToStandard();
		int widthExtra = paddingLeft+paddingRight;
		int heightExtra = paddingTop+paddingBottom;
		int deltaX=0, deltaY=0;
		itemLocations.clear();
		for(int i=0;i<rowNum; i++){
			for(int j=0;j<columnNum;j++){
				Rect rect = new Rect();
				rect.left = paddingLeft + (standardWidth+itemHorizontalGap)*j;
				rect.top = paddingTop + (standardHeight+itemVerticalGap)*i;
				rect.right = rect.left + standardWidth;
				rect.bottom = rect.top + standardHeight;

//				rect.top = deltaY+heightExtra;
//				rect.right = deltaX += standardWidth;
//				rect.bottom = deltaY + standardHeight;
				itemLocations.put(j+i*columnNum, rect);
			}
//			deltaY+=heightExtra+standardHeight;
//			deltaX = 0;
		}
	}

	public void setOnItemClickListener(OnItemClickListener listener){
		this.onItemClickListener = listener;
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener){
		this.onItemLongClickListener = listener;
	}


	private void initItemEvents(View v, int position){
		long id = mAdapter.getItemId(position);
		setItemViewClickListener(v, position, id);
		setItemViewLongClickListener(v, position, id);
	}

	private void setItemViewClickListener(final View target, final int position, final long id){
		if(null != target){
			target.setTag(position);
			target.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(null != onItemClickListener){
						onItemClickListener.onItemClick(GridPagerView.this, target, position, id);
					}
				}
			});
		}
	}

	private void setItemViewLongClickListener(final View target, final int position, final long id){
		if(null != target){
			target.setTag(position);
			target.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if(null != onItemLongClickListener){
						onItemLongClickListener.onItemLongClick(GridPagerView.this, target, position, id);
						return true;
					}
					return false;
				}
			});
		}
	}

	public void setOnPageChangeListener(OnPageChangeListener listener){
		if(null != listener){
			onPageChangeListeners.add(listener);
		}
	}

	private void dispatchPageChangeEvent(int type){
		if(TYPE_PAGE_CHANGED == type){
			for(OnPageChangeListener l : onPageChangeListeners){
				l.onPageSelected(currentPage);
			}
		}
		if( TYPE_DATA_CHANGED == type ) {
			for(OnPageChangeListener l : onPageChangeListeners){
				l.onPageDataSetChanged(true, currentPage);
			}
		}
	}

	public int getPageCount(){
		return totalPage;
	}

	private void checkPageValid(){
		if(currentPage >= totalPage){
			currentPage = totalPage - 1;
		}
		smoothScrollToX(currentPage*getWidth());
	}

	@Override
	public void handleTraversal() {
		super.handleTraversal();
		refreshGridView();
		postDelayed(new Runnable() {
			@Override
			public void run() {
				checkPageValid();
				dispatchPageChangeEvent(TYPE_DATA_CHANGED);
			}
		}, 300);
	}
}