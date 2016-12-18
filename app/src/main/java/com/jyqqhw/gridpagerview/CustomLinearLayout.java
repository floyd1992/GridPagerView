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

}
