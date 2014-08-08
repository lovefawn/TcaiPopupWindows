package com.tcai.ui.components.popup.sysmenu;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;

import com.tcai.popup.R;
import com.tcai.ui.components.popup.PopupWindows;

public class SysMenu2 extends PopupWindows implements OnDismissListener {

	private LayoutInflater mInflater;
	private OnDismissListener mDismissListener;
	private boolean mDidAction;
	private int mOrientation;
	private OnMenuItemClickListener mOnMenuItemClickListener;

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;


	/**
	 * Constructor for default vertical layout
	 * 
	 * @param context
	 *            Context
	 */
	public SysMenu2(Context context) {
		this(context, VERTICAL);
	}

	/**
	 * Constructor allowing orientation override
	 * 
	 * @param context
	 *            Context
	 * @param orientation
	 *            Layout orientation, can be vartical or horizontal
	 */
	public SysMenu2(Context context, int orientation) {
		super(context);

		mOrientation = orientation;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (mOrientation == HORIZONTAL) {
			setRootViewId(R.layout.popup_sys_menu);
		} else {
			setRootViewId(R.layout.popup_sys_menu);
		}

	}

	/**
	 * Set root view.
	 * 
	 * @param id
	 *            Layout resource id
	 */
	public void setRootViewId(int id) {
		mRootView = mInflater.inflate(id, null);
		
//		RelativeLayout rl_weixin = (RelativeLayout) mRootView
//				.findViewById(R.id.rl_weixin);
//		rl_weixin.getBackground().setAlpha(180);
//		RelativeLayout rl_weibo = (RelativeLayout) mRootView
//				.findViewById(R.id.rl_weibo);
//		rl_weibo.getBackground().setAlpha(180);
//		RelativeLayout rl_duanxin = (RelativeLayout) mRootView
//				.findViewById(R.id.rl_duanxin);
//		rl_duanxin.getBackground().setAlpha(180);
//		Button bt_cancle = (Button) mRootView.findViewById(R.id.bt_cancle);
//		bt_cancle.getBackground().setAlpha(180);
		
		
		mRootView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dismiss();
			}
		});
		
		mRootView.setFocusableInTouchMode(true);
		mRootView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		setContentView(mRootView);
	}

	/**
	 * Show quickaction popup. Popup is automatically positioned, on top or
	 * bottom of anchor view.
	 * 
	 */
	public void show(View anchor) {
		preShow();
		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mRootView.startAnimation(AnimationUtils.loadAnimation(
				mContext, R.animator.sys_fade_in));
		LinearLayout ll_popup = (LinearLayout) mRootView.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(
				mContext, R.animator.sys_push_bottom_in));
		mWindow.showAtLocation(anchor, Gravity.BOTTOM, 0, 0);
		
	}

	/**
	 * Set listener for action item clicked.
	 * 
	 * @param listener
	 *            Listener
	 */
	public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
		mOnMenuItemClickListener = listener;
	}
	/**
	 * Set listener for window dismissed. This listener will only be fired if
	 * the quicakction dialog is dismissed by clicking outside the dialog or
	 * clicking on sticky item.
	 */
	public void setOnDismissListener(SysMenu2.OnDismissListener listener) {
		setOnDismissListener(this);

		mDismissListener = listener;
	}

	@Override
	public void onDismiss() {
		if (!mDidAction && mDismissListener != null) {
			mDismissListener.onDismiss();
		}
	}

	/**
	 * Listener for item click
	 * 
	 */
	public interface OnMenuItemClickListener {
		public abstract void onMenuItemClick(SysMenu2 source, int pos, int MenuId);
	}

	/**
	 * Listener for window dismiss
	 * 
	 */
	public interface OnDismissListener {
		public abstract void onDismiss();
	}
	
		
	
}