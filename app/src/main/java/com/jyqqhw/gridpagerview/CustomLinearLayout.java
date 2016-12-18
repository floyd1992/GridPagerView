package com.jyqqhw.gridpagerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by wj on 16-12-17.
 */
public abstract class CustomLinearLayout<ListAdapter> extends LinearLayout {

	protected ListAdapter adapter;

	public CustomLinearLayout(Context context) {
		super(context);
	}

	public CustomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setAdapter(ListAdapter adapter){

	}

	public ListAdapter getAdapter() {
		return adapter;
	}


	public interface OnItemClickListener{
		void onItemClick(CustomLinearLayout<?> parent, View view, int position, long id);
	}


	public interface OnItemLongClickListener{
		boolean onItemLongClick(CustomLinearLayout<?> parent, View view, int position, long id);
	}


	public interface OnPageChangeListener {

		/**
		 * This method will be invoked when the current page is scrolled, either as part
		 * of a programmatically initiated smooth scroll or a user initiated touch scroll.
		 *
		 * @param position Position index of the first page currently being displayed.
		 *                 Page position+1 will be visible if positionOffset is nonzero.
		 * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
		 * @param positionOffsetPixels Value in pixels indicating the offset from position.
		 */
		void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

		/**
		 * This method will be invoked when a new page becomes selected. Animation is not
		 * necessarily complete.
		 *
		 * @param position Position index of the new selected page.
		 */
		void onPageSelected(int position);

		/**
		 * Called when the scroll state changes. Useful for discovering when the user
		 * begins dragging, when the pager is automatically settling to the current page,
		 * or when it is fully stopped/idle.
		 *
		 * @param state The new scroll state.
		 * @see ViewPager#SCROLL_STATE_IDLE
		 * @see ViewPager#SCROLL_STATE_DRAGGING
		 * @see ViewPager#SCROLL_STATE_SETTLING
		 */
		void onPageScrollStateChanged(int state);
	}

}
