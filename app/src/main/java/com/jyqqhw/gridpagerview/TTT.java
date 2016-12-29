//package com.jyqqhw.gridpagerview;
//
//import android.content.DialogInterface;
//import android.graphics.Point;
//import android.view.ActionMode;
//import android.view.ContextMenu;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.accessibility.AccessibilityEvent;
//
///**
// * Created by wj on 16-12-21.
// */
//public class TTT implements DialogInterface, Window.Callback,
//		KeyEvent.Callback, View.OnCreateContextMenuListener {
//
//
//	public Point test(){
//
//		Window window;
//		window.setContentView();
//		window.getDecorView();
//		window.getDecorView();
//		window.setLayout();
//
//		Point point = new Point();
//		point.x = 460;
//		point.y = -2;
//		return test(1,2);
//	}
//
//	public Point test(int width, int height){
//
//		String packageName = mContext.getPackageName();
//		if( packageName.contains("bbk") ||
//				packageName.equals("com.android.systemui") ||
//				packageName.equals("com.android.camera2") ||
//				packageName.equals("com.android.settings") ) {
//			parent.setBackgroundResource(com.android.internal.R.drawable.dialog_full_holo_light);
//		} else {
//			parent.setBackgroundResource(com.android.internal.R.drawable.other_dialog_full_holo_light);
//		}
//
//
//		return new Point(width, height);
//	}
//
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		return false;
//	}
//
//	@Override
//	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
//		return false;
//	}
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		return false;
//	}
//
//	@Override
//	public boolean dispatchTrackballEvent(MotionEvent event) {
//		return false;
//	}
//
//	@Override
//	public boolean dispatchGenericMotionEvent(MotionEvent event) {
//		return false;
//	}
//
//	@Override
//	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
//		return false;
//	}
//
//	@Override
//	public View onCreatePanelView(int featureId) {
//		return null;
//	}
//
//	@Override
//	public boolean onCreatePanelMenu(int featureId, Menu menu) {
//		return false;
//	}
//
//	@Override
//	public boolean onPreparePanel(int featureId, View view, Menu menu) {
//		return false;
//	}
//
//	@Override
//	public boolean onMenuOpened(int featureId, Menu menu) {
//		return false;
//	}
//
//	@Override
//	public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		return false;
//	}
//
//	@Override
//	public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
//
//	}
//
//	@Override
//	public void onContentChanged() {
//
//	}
//
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//
//	}
//
//	@Override
//	public void onAttachedToWindow() {
//
//	}
//
//	@Override
//	public void onDetachedFromWindow() {
//
//	}
//
//	@Override
//	public void onPanelClosed(int featureId, Menu menu) {
//
//	}
//
//	@Override
//	public boolean onSearchRequested() {
//		return false;
//	}
//
//	@Override
//	public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
//		return null;
//	}
//
//	@Override
//	public void onActionModeStarted(ActionMode mode) {
//
//	}
//
//	@Override
//	public void onActionModeFinished(ActionMode mode) {
//
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		return false;
//	}
//
//	@Override
//	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
//		return false;
//	}
//
//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event) {
//		return false;
//	}
//
//	@Override
//	public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
//		return false;
//	}
//
//	@Override
//	public void cancel() {
//
//	}
//
//	@Override
//	public void dismiss() {
//
//	}
//
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//
//	}
//}
