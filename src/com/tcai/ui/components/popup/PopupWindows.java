package com.tcai.ui.components.popup;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;
import android.app.Activity;
import android.content.Context;

public class PopupWindows {
	protected Context mContext;
	protected PopupWindow mWindow;
	protected View mRootView;
	protected Drawable mBackground = null;
	protected WindowManager mWindowManager;
		
	/**
	 * Constructor.
	 * 
	 * @param context Context
	 */
	public PopupWindows(Context context) {
		mContext	= context;
		mWindow 	= new PopupWindow(context);

		mWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					mWindow.dismiss();
					
					return true;
				}
				
				return false;
			}
		});

		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}
	
	/**
	 * On dismiss
	 */
	protected void onDismiss() {		
	}
	
	/**
	 * On show
	 */
	protected void onShow() {		
	}
	
	public PopupWindow getPopWindow(){
		return mWindow;
	}

	/**
	 * On pre show
	 */
	protected void preShow() {
		if (mRootView == null) 
			throw new IllegalStateException("setContentView was not called with a view to display.");
	
		onShow();
		
		mRootView.setOnKeyListener(keyListener);

		if (mBackground == null) 
			mWindow.setBackgroundDrawable(new BitmapDrawable());
		else 
			mWindow.setBackgroundDrawable(mBackground);


		mWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
		mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		mWindow.setTouchable(true);
		mWindow.setFocusable(true);
		mWindow.setOutsideTouchable(true);
		mWindow.setContentView(mRootView);
	}

	/**
	 * Set background drawable.
	 * 
	 * @param background Background drawable
	 */
	public void setBackgroundDrawable(Drawable background) {
		mBackground = background;
	}

	/**
	 * Set content view.
	 * 
	 * @param root Root view
	 */
	public void setContentView(View root) {
		mRootView = root;
		
		mWindow.setContentView(root);
	}

	/**
	 * Set content view.
	 * 
	 * @param layoutResID Resource id
	 */
	public void setContentView(int layoutResID) {
		LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		setContentView(inflator.inflate(layoutResID, null));
	}

	/**
	 * Set listener on window dismissed.
	 * 
	 * @param listener
	 */
	public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
		mWindow.setOnDismissListener(listener);  
	}

	/**
	 * Dismiss the popup window.
	 */
	public void dismiss() {
		mWindow.dismiss();
	}
	
 private OnKeyListener keyListener = new OnKeyListener() {
		 
         @Override
         public boolean onKey(View v, int keyCode, KeyEvent event) {
                 if (event.getAction() == KeyEvent.ACTION_DOWN
                                 && keyCode == KeyEvent.KEYCODE_MENU) {
                	 dismiss();
                         return true;
                 }
                 return false;
         }
 };
	
  }