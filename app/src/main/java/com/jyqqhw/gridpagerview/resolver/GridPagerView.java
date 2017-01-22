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
 *
 * @hide
 */
public class GridPagerView extends CustomLinearLayout<ListAdapter> {

	private static final String TAG = "GridPagerView";
	private static final boolean DEBUG = false;

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

	private void init(Context context) {
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		scroller = new Scroller(context, new LinearInterpolator());
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int heightSize = 0;
		if (itemViews.size() > 0) {
			View v = itemViews.get(0);
			standardWidth = v.getMeasuredWidth();
			standardHeight = v.getMeasuredHeight();
		}
		if (itemViews.size() > columnNum) {
			heightSize = 2 * standardHeight + paddingTop + itemVerticalGap + paddingBottom;
			rowNum = 2;
		} else {
			heightSize = standardHeight + paddingTop + paddingBottom;
			rowNum = 1;
		}
		setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
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

		int cnt = null == mAdapter ? 0 : mAdapter.getCount();
		int pageCnt = columnNum * rowNum;

		int pc = cnt / pageCnt;
		int rest = cnt % pageCnt == 0 ? 0 : 1;
		totalPage = pc + rest;
		int current = 0;
		int baseWidth = 0;
		for (int i = 0; i < totalPage; i++) {
			current = cnt - (i + 1) * pageCnt >= 0 ? pageCnt : cnt - i * pageCnt;
			for (int j = 0; j < current; j++) {
				View v = itemViews.get(j + i * pageCnt);
				Rect rect = itemLocations.get(j);
				v.layout(baseWidth + rect.left, rect.top, baseWidth + rect.right, rect.bottom);
			}
			baseWidth += getMeasuredWidth();
		}
	}

	private int downX, downY, initX, initY;
	private int cX, cY;
	private int diffX, diffY;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		int action = ev.getActionMasked();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				pointerId = ev.getPointerId(0);
				initX = downX = (int) ev.getX();
				initY = downY = (int) ev.getY();
				if(DEBUG){
					Log.i(TAG, "on intercept down " + touchSlop);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				int p = ev.findPointerIndex(pointerId);
				cX = (int) ev.getX(p);
				cY = (int) ev.getY(p);
				int diffX = cX - downX;
				if(DEBUG){
					Log.i(TAG, "on intercept move not intercept " + diffX + ", cX=" + cX + ". downX=" + downX);
				}
				if (Math.abs(diffX) > touchSlop) {
					initX = downX = (int) ev.getX(p);
					initY = downY = (int) ev.getY(p);
					if(DEBUG){
						Log.i(TAG, "on intercept move ");
					}
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

	private int pointerIndex, pointerId;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();
		int tmpDiffX= 0;
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				pointerId = event.getPointerId(0);
				initX = downX = (int) event.getX();
				initY = downY = (int) event.getY();
				if(DEBUG){
					Log.i(TAG, "on touch down ");
				}
				break;
			case MotionEvent.ACTION_MOVE:
				int p = event.findPointerIndex(pointerId);
				cX = (int) event.getX(p);
				cY = (int) event.getY(p);
				tmpDiffX = diffX;
				diffX = cX - downX;
				if(DEBUG){
					Log.i(TAG, "currentPage=" + currentPage + ", diffX=" + diffX + ", getScrollX="+getScrollX());
				}
				if (checkScrollFromMargin()) {
					break;
				}
				//if current page is in the middle, but scroll below 0 or over totalPage-1, just check that.
				//fix BUG: scroll to first/last from other page, and then scroll back.
				if( (getScrollX() < 0 && diffX > tmpDiffX)
						|| (getScrollX() > (totalPage-1)*getWidth() && diffX < tmpDiffX)
						 ){
					diffX = tmpDiffX;
				}
				tmpDiffX = diffX;
				if (Math.abs(tmpDiffX) > 1) {
					scrollTo(currentPage * getWidth() - tmpDiffX, 0);
					if(DEBUG){
						Log.i(TAG, "scroll diffX " + diffX);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if (checkScrollFromMargin()) {
					break;
				}
				if( getScrollX() < 0){
					Log.d(TAG, "v x is below 0.");
					setScrollX(0);
				}
				resumeToPage(diffX);
				break;
			case MotionEvent.ACTION_POINTER_UP:
				onPointerUp(event);
				break;
			default:
				break;
		}
		return true;
	}

	private void onPointerUp(MotionEvent ev) {
		int index = ev.getActionIndex();
		int pi = ev.getPointerId(index);
		if (pointerId == pi) {
			pointerIndex = index == 0 ? 1 : 0;
			pointerId = ev.getPointerId(pointerIndex);
			int x = (int) ev.getX(pointerIndex);
			int y = (int) ev.getY(pointerIndex);
			cY = y;
			cX = x;
			downX = cX - diffX;
			downY = cY - diffY;
		}
	}

	private void resumeToPage(int diffX) {
		lastPage = currentPage;
		int cp = diffX / getWidth();
		if (cp != 0) {
			currentPage -= cp;
			diffX %= getWidth();
		}
		int halfW = (int) (getWidth() / 3.5);
		if(DEBUG){
			Log.d(TAG, "cp=" + cp + ", diffX=" + diffX + ", getWidth()=" + getWidth()
					+ ", currentPage=" + currentPage + ", halfW=" + halfW);
		}
		if (currentPage > totalPage - 1) {
			currentPage = totalPage - 1;
		} else if (currentPage < 0) {
			currentPage = 0;
		} else {
			if (diffX + halfW < 0) {
				currentPage++;
			}
			if (diffX - halfW > 0) {
				currentPage--;
			}
		}

		smoothScrollToX(currentPage * getWidth());
		if (currentPage != lastPage) {
			dispatchPageChangeEvent(TYPE_PAGE_CHANGED);
		}
		if(DEBUG){
			Log.d(TAG, "ready to resume" + currentPage * getWidth());
		}
	}

	private void smoothScrollToX(int finalX) {
		int cx = getScrollX();
		scroller.startScroll(cx, 0, finalX - cx, 0, ANIMATOR_DURATION);
		postInvalidate();
	}


	@Override
	public void computeScroll() {
		super.computeScroll();
		if (scroller.computeScrollOffset()) {
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
		if (null == adapter) {
			return;
		}
		super.setAdapter(adapter);
		refreshGridView();
	}

	private void refreshGridView() {
		int cnt = mAdapter.getCount();
		if (cnt != itemCount) {
			itemViews.clear();
			removeAllViews();

			resizeViewToStandard();
			LayoutParams lp = generateDefaultLayoutParams();
			lp.width = -2;
			lp.height = -2;
//			lp.width = standardWidth;
//			lp.height = standardHeight;
			for (int i = 0; i < cnt; i++) {
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

	private void resizeViewToStandard() {

		paddingRight = 20;
		paddingBottom = 17;
		itemHorizontalGap = 48;
		itemVerticalGap = 16;
		paddingLeft = (getMeasuredWidth() - (itemHorizontalGap + standardWidth) * columnNum + itemHorizontalGap) / 2;
		paddingTop = (getMeasuredHeight() - (itemVerticalGap + standardHeight) * rowNum + itemVerticalGap) / 2;
	}

	private void computeChildPosition() {
		resizeViewToStandard();
		int widthExtra = paddingLeft + paddingRight;
		int heightExtra = paddingTop + paddingBottom;
		int deltaX = 0, deltaY = 0;
		itemLocations.clear();
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < columnNum; j++) {
				Rect rect = new Rect();
				rect.left = paddingLeft + (standardWidth + itemHorizontalGap) * j;
				rect.top = paddingTop + (standardHeight + itemVerticalGap) * i;
				rect.right = rect.left + standardWidth;
				rect.bottom = rect.top + standardHeight;

//				rect.top = deltaY+heightExtra;
//				rect.right = deltaX += standardWidth;
//				rect.bottom = deltaY + standardHeight;
				itemLocations.put(j + i * columnNum, rect);
			}
//			deltaY+=heightExtra+standardHeight;
//			deltaX = 0;
		}
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.onItemClickListener = listener;
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		this.onItemLongClickListener = listener;
	}


	private void initItemEvents(View v, int position) {
		long id = mAdapter.getItemId(position);
		setItemViewClickListener(v, position, id);
		setItemViewLongClickListener(v, position, id);
	}

	private void setItemViewClickListener(final View target, final int position, final long id) {
		if (null != target) {
			target.setTag(position);
			target.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (null != onItemClickListener) {
						onItemClickListener.onItemClick(GridPagerView.this, target, position, id);
					}
				}
			});
		}
	}

	private void setItemViewLongClickListener(final View target, final int position, final long id) {
		if (null != target) {
			target.setTag(position);
			target.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if (null != onItemLongClickListener) {
						onItemLongClickListener.onItemLongClick(GridPagerView.this, target, position, id);
						return true;
					}
					return false;
				}
			});
		}
	}

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		if (null != listener) {
			onPageChangeListeners.add(listener);
		}
	}

	private void dispatchPageChangeEvent(int type) {
		if (TYPE_PAGE_CHANGED == type) {
			for (OnPageChangeListener l : onPageChangeListeners) {
				l.onPageSelected(currentPage);
			}
		}
		if (TYPE_DATA_CHANGED == type) {
			for (OnPageChangeListener l : onPageChangeListeners) {
				l.onPageDataSetChanged(true, currentPage);
			}
		}
	}

	public int getPageCount() {
		return totalPage;
	}

	private void checkPageValid() {
		if (currentPage >= totalPage) {
			currentPage = totalPage - 1;
		}
		smoothScrollToX(currentPage * getWidth());
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

	private boolean checkScrollFromMargin() {
		//check if current page is 0 or totalPage-1, then forbid left/right scroll event
		if ((diffX < 0 && totalPage - 1 == currentPage) || (0 == currentPage && diffX > 0)) {
			return true;
		}
		return false;
	}
}
