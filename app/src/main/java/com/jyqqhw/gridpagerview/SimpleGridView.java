package com.jyqqhw.gridpagerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;

/**
 * Created by wj on 16-12-17.
 */
public class SimpleGridView  extends AdapterView<ListAdapter> {


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
	public ListAdapter getAdapter() {
		return null;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {

	}

	@Override
	public View getSelectedView() {
		return null;
	}

	@Override
	public void setSelection(int position) {

	}
}
