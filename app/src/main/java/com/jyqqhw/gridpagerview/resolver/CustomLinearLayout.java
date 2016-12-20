package com.jyqqhw.gridpagerview.resolver;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * Created by wj on 16-12-17.
 * @hide
 */
public abstract class CustomLinearLayout<T extends ListAdapter> extends LinearLayout {

	protected DataSetObserver dataSetObserver = new GridPagerObserver();

	protected int itemCount;
	private boolean dataChanged;

	protected T mAdapter;

	public CustomLinearLayout(Context context) {
		super(context);
	}

	public CustomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setAdapter(T adapter){
		this.mAdapter = adapter;
		this.mAdapter.registerDataSetObserver(dataSetObserver);
	}

	public ListAdapter getAdapter() {
		return mAdapter;
	}

	public void handleTraversal(){

	}

	public interface OnItemClickListener{
		void onItemClick(CustomLinearLayout<?> parent, View view, int position, long id);
	}

	public interface OnItemLongClickListener{
		boolean onItemLongClick(CustomLinearLayout<?> parent, View view, int position, long id);
	}

	public interface OnPageChangeListener {

		void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

		void onPageSelected(int position);

		void onPageDataSetChanged(boolean changed, int position);
	}


	class GridPagerObserver extends DataSetObserver{

		public GridPagerObserver() {

		}

		@Override
		public void onChanged() {
			super.onChanged();
			dataChanged = true;
			handleTraversal();
			dataChanged = false;
		}

		@Override
		public void onInvalidated() {
			super.onInvalidated();
			dataChanged = true;
			handleTraversal();
			dataChanged = false;
		}
	}


}
