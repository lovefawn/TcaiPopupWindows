package com.tcai.ui.components.popup.sysmenu;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;

import com.tcai.popup.R;
import com.tcai.ui.components.popup.PopupWindows;
import com.tcai.utils.FontUtil;

public class SysMenu extends PopupWindows implements OnDismissListener {

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
	public SysMenu(Context context) {
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
	public SysMenu(Context context, int orientation) {
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
		
		
		Button mMainActivity_MenuAddBookmark = (Button) mRootView.findViewById(R.id.MainActivity_MenuAddBookmark);
		mMainActivity_MenuAddBookmark.getBackground().setAlpha(180);
		mMainActivity_MenuAddBookmark.setTypeface(FontUtil.getCustomFont(mContext));
		mMainActivity_MenuAddBookmark.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mOnMenuItemClickListener!=null)
				mOnMenuItemClickListener.onMenuItemClick(v);
				dismiss();
			}
			
		});

		Button mMainActivity_MenuBookmarks = (Button) mRootView.findViewById(R.id.MainActivity_MenuBookmarks);
		mMainActivity_MenuBookmarks.getBackground().setAlpha(180);
		mMainActivity_MenuBookmarks.setTypeface(FontUtil.getCustomFont(mContext));
		mMainActivity_MenuBookmarks.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mOnMenuItemClickListener!=null)
				mOnMenuItemClickListener.onMenuItemClick(v);
				dismiss();
			}
			
		});

		Button mMainActivity_MenuIncognitoTab = (Button) mRootView.findViewById(R.id.MainActivity_MenuIncognitoTab);
		mMainActivity_MenuIncognitoTab.getBackground().setAlpha(180);
		mMainActivity_MenuIncognitoTab.setTypeface(FontUtil.getCustomFont(mContext));
		mMainActivity_MenuIncognitoTab.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mOnMenuItemClickListener!=null)
				mOnMenuItemClickListener.onMenuItemClick(v);
				dismiss();
			}
			
		});

		
		Button mMainActivity_MenuFullScreen = (Button) mRootView.findViewById(R.id.MainActivity_MenuFullScreen);
		mMainActivity_MenuFullScreen.getBackground().setAlpha(180);
		mMainActivity_MenuFullScreen.setTypeface(FontUtil.getCustomFont(mContext));
		mMainActivity_MenuFullScreen.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mOnMenuItemClickListener!=null)
				mOnMenuItemClickListener.onMenuItemClick(v);
				dismiss();
			}
			
		});
		
		Button mMainActivity_MenuSharePage = (Button) mRootView.findViewById(R.id.MainActivity_MenuSharePage);
		mMainActivity_MenuSharePage.getBackground().setAlpha(180);
		mMainActivity_MenuSharePage.setTypeface(FontUtil.getCustomFont(mContext));
		mMainActivity_MenuSharePage.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mOnMenuItemClickListener!=null)
				mOnMenuItemClickListener.onMenuItemClick(v);
				dismiss();
			}
			
		});
		
		Button mMainActivity_MenuSearch = (Button) mRootView.findViewById(R.id.MainActivity_MenuSearch);
		mMainActivity_MenuSearch.getBackground().setAlpha(180);
		mMainActivity_MenuSearch.setTypeface(FontUtil.getCustomFont(mContext));
		mMainActivity_MenuSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mOnMenuItemClickListener!=null)
				mOnMenuItemClickListener.onMenuItemClick(v);
				dismiss();
			}
			
		});
		
		Button mMainActivity_MenuPreferences = (Button) mRootView.findViewById(R.id.MainActivity_MenuPreferences);
		mMainActivity_MenuPreferences.getBackground().setAlpha(180);
		mMainActivity_MenuPreferences.setTypeface(FontUtil.getCustomFont(mContext));
		mMainActivity_MenuPreferences.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mOnMenuItemClickListener!=null)
				mOnMenuItemClickListener.onMenuItemClick(v);
				dismiss();
			}
			
		});
		
		Button mMainActivity_MenuExit = (Button) mRootView.findViewById(R.id.MainActivity_MenuExit);
		mMainActivity_MenuExit.getBackground().setAlpha(180);
		mMainActivity_MenuExit.setTypeface(FontUtil.getCustomFont(mContext));
		mMainActivity_MenuExit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mOnMenuItemClickListener!=null)
				mOnMenuItemClickListener.onMenuItemClick(v);
				dismiss();
			}
			
		});
		
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
	
	public Button findItem(int ButtonId){
		
		Button mButton = (Button) mRootView.findViewById(ButtonId);
		
		return mButton;
		
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
	public void setOnDismissListener(SysMenu.OnDismissListener listener) {
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
		public abstract void onMenuItemClick(View v);
	}

	/**
	 * Listener for window dismiss
	 * 
	 */
	public interface OnDismissListener {
		public abstract void onDismiss();
	}
	
		
	
}