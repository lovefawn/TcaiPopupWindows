package com.tcai.ui.components.popup.urllist;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;

import java.util.List;
import java.util.ArrayList;

import com.tcai.ui.components.popup.PopupWindows;
import com.tcai.utils.DPUtil;
import com.tcai.utils.FontUtil;

import net.londatiga.android.R;

public class UrlList extends PopupWindows implements OnDismissListener {
	private View mRootView;
	private ImageView mArrowUp;
	private ImageView mArrowDown;
	private LayoutInflater mInflater;
	private ViewGroup mTrack;
	private LinearLayout layoutGroup;
	private ScrollView mScroller;
	private Button mNewTab;
	private OnUrlItemClickListener mItemClickListener;
	private OnUrlItemCloseBtnClickListener mItemCloseBtnClickListener;
	private OnNewTabBtnClickListener mNewTabBtnClickListener;
	private OnDismissListener mDismissListener;

	private List<UrlItem> urlItems = new ArrayList<UrlItem>();

	private boolean mDidAction;

	private int mChildPos;
	private int mInsertPos;
	private int mAnimStyle;
	private int mOrientation;
	private int rootWidth = 0;

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_REFLECT = 4;
	public static final int ANIM_AUTO = 5;
	/**
	 * Constructor for default vertical layout
	 * 
	 * @param context
	 *            Context
	 */
	public UrlList(Context context) {
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
	public UrlList(Context context, int orientation) {
		super(context);

		mOrientation = orientation;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (mOrientation == HORIZONTAL) {
			setRootViewId(R.layout.popup_urls_horizontal);
		} else {
			setRootViewId(R.layout.popup_urls_vertical);
		}

		mAnimStyle = ANIM_AUTO;
		mChildPos = 0;
	}

	/**
	 * Get action item at an index
	 * 
	 * @param index
	 *            Index of item (position from callback)
	 * 
	 * @return Action Item at the position
	 */
	public UrlItem getUrlItem(int index) {
		return urlItems.get(index);
	}

	/**
	 * Set root view.
	 * 
	 * @param id
	 *            Layout resource id
	 */
	public void setRootViewId(int id) {
		mRootView = (ViewGroup) mInflater.inflate(id, null);
		mTrack = (ViewGroup) mRootView.findViewById(R.id.tracks);

		mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);
		mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);

		layoutGroup = (LinearLayout) mRootView.findViewById(R.id.layout_group);

		mScroller = (ScrollView) mRootView.findViewById(R.id.scroller);
		mNewTab =(Button) mRootView.findViewById(R.id.bt_newtab);
		mNewTab.setTypeface(FontUtil.getCustomFont(mContext));
		mNewTab.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (mNewTabBtnClickListener != null) {
					mNewTabBtnClickListener.onNewTabBtnClick();
				}
				dismiss();
				
			}
			
		});

		// This was previously defined on show() method, moved here to prevent
		// force close that occured
		// when tapping fastly on a view to show quickaction dialog.
		// Thanx to zammbi (github.com/zammbi)
		mArrowUp.setAlpha(180);
		mArrowDown.setAlpha(180);
		layoutGroup.getBackground().setAlpha(180);
		mRootView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		setContentView(mRootView);
	}

	/**
	 * Set animation style
	 * 
	 * @param mAnimStyle
	 *            animation style, default is set to ANIM_AUTO
	 */
	public void setAnimStyle(int mAnimStyle) {
		this.mAnimStyle = mAnimStyle;
	}

	/**
	 * Set listener for action item clicked.
	 * 
	 * @param listener
	 *            Listener
	 */
	public void setOnUrlItemClickListener(OnUrlItemClickListener listener) {
		mItemClickListener = listener;
	}

	/**
	 * Set listener for action item close button clicked.
	 * 
	 * @param listener
	 *            Listener
	 */
	public void setOnUrlItemCloseBtnClickListener(
			OnUrlItemCloseBtnClickListener listener) {
		mItemCloseBtnClickListener = listener;
	}
	/**
	 * Set listener for action item clicked.
	 * 
	 * @param listener
	 *            Listener
	 */
	public void setOnNewTabBtnClickListener(OnNewTabBtnClickListener listener) {
		mNewTabBtnClickListener = listener;
	}

	/**
	 * Add action item
	 * 
	 * @param action
	 *            {@link UrlItem}
	 */
	public void addUrlItem(UrlItem mUrlItem, boolean isCurrentTab) {
		urlItems.add(mUrlItem);

		String title = mUrlItem.getTitle();
		String url = mUrlItem.getUrl();
		Drawable icon = mUrlItem.getIcon();

		View container;

		if (mOrientation == HORIZONTAL) {
			container = mInflater.inflate(R.layout.popup_url_item_horizontal, null);
		} else {
			container = mInflater.inflate(R.layout.popup_url_item_vertical, null);
		}

		LinearLayout actionItem = (LinearLayout) container
				.findViewById(R.id.line_action);
		ImageView img = (ImageView) container.findViewById(R.id.iv_icon);
		TextView text = (TextView) container.findViewById(R.id.tv_title);
		text.setTypeface(FontUtil.getCustomFont(mContext));
		TextView tvUrl = (TextView) container.findViewById(R.id.tv_url);
		tvUrl.setTypeface(FontUtil.getCustomFont(mContext));
		ImageView closeBtn = (ImageView) container
				.findViewById(R.id.close_icon);
		if (isCurrentTab) {
			actionItem.setBackgroundResource(R.drawable.tab_center_item_select);
			actionItem.getBackground().setAlpha(200);
		} else
			actionItem.getBackground().setAlpha(150);

		if (icon != null) {
			img.setImageDrawable(icon);
		} else {
			img.setVisibility(View.GONE);
		}

		if (title != null) {
			text.setText(title);
		} else {
			text.setVisibility(View.GONE);
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			layoutParam.setMargins(0, 0, 0, 0);
			tvUrl.setLayoutParams(layoutParam);
			tvUrl.setTextSize(15);
			tvUrl.getLayoutParams().height = DPUtil.dip2px(mContext, 60);
		}

		if (url != null) {
			tvUrl.setText(url);
		} else {
			tvUrl.setVisibility(View.GONE);
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			layoutParam.setMargins(0, 0, 0, 0);
			text.setLayoutParams(layoutParam);
			text.getLayoutParams().height = DPUtil.dip2px(mContext, 60);
		}

		final int pos = urlItems.indexOf(mUrlItem);
		final int actionId = mUrlItem.getActionId();
		final UrlItem urlItem = mUrlItem;

		closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mTrack.getMeasuredHeight() <= mScroller.getMeasuredHeight()) {
					mTrack.removeView((View) v.getParent());
					mScroller.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					mScroller.requestLayout();
				} else
					mTrack.removeView((View) v.getParent());

				// int[] location = new int[2] ;
				// mWindow.getContentView().getLocationInWindow(location);
				// //获取在当前窗口内的绝对坐标
				// mWindow.getContentView().getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
				// mWindow.update(location[0],location[1]+((View)v.getParent()).getHeight(),mWindow.getWidth(),
				// mWindow.getHeight());

				if (mItemCloseBtnClickListener != null) {
					mItemCloseBtnClickListener.onItemCloseBtnClick(
							UrlList.this, pos,urlItem);
				}
				urlItems.remove(urlItem);

			}
		});

		container.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemClickListener != null) {
					mItemClickListener.onItemClick(UrlList.this, pos, actionId);
				}

				if (!getUrlItem(pos).isSticky()) {
					mDidAction = true;

					dismiss();
				}
			}
		});

		container.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				}
				return false;
			}

		});

		container.setFocusable(true);
		container.setClickable(true);

		if (mOrientation == HORIZONTAL && mChildPos != 0) {
			View separator = mInflater.inflate(R.layout.popup_horiz_separator, null);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

			separator.setLayoutParams(params);
			separator.setPadding(5, 0, 5, 0);

			mTrack.addView(separator, mInsertPos);

			mInsertPos++;
		}

		mTrack.addView(container, pos);

		mChildPos++;
		mInsertPos++;
	}

	public void removeUrlItem(int pos) {
		urlItems.remove(pos);
	}

	/**
	 * Popup is automatically positioned, on top or bottom of anchor view.
	 * 
	 */
	public void show(View anchor) {
		preShow();

		int xPos, yPos, arrowPos;

		mDidAction = false;

		int[] location = new int[2];

		anchor.getLocationOnScreen(location);

		Rect anchorRect = new Rect(location[0], location[1], location[0]
				+ anchor.getWidth(), location[1] + anchor.getHeight());

		// mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT));

		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		int rootHeight = mRootView.getMeasuredHeight();

		if (rootWidth == 0) {
			rootWidth = mRootView.getMeasuredWidth();
		}

		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight = mWindowManager.getDefaultDisplay().getHeight();

		// automatically get X coord of popup (top left)
		if ((anchorRect.left + rootWidth) > screenWidth) {
			xPos = anchorRect.left - (rootWidth - anchor.getWidth());
			xPos = (xPos < 0) ? 0 : xPos;

			arrowPos = anchorRect.centerX() - xPos;

		} else {
			if (anchor.getWidth() > rootWidth) {
				xPos = anchorRect.centerX() - (rootWidth / 2);
			} else {
				xPos = anchorRect.left;
			}

			arrowPos = anchorRect.centerX() - xPos;
		}

		int dyTop = anchorRect.top;
		int dyBottom = screenHeight - anchorRect.bottom;
		int head = 15;

		boolean onTop = (dyTop > dyBottom) ? true : false;

		if (onTop) {
			yPos = dyBottom + anchor.getHeight();
			if (rootHeight > dyTop) {
				LayoutParams l = mScroller.getLayoutParams();
				l.height = dyTop - anchor.getMeasuredHeight() - head
						- mNewTab.getMeasuredHeight();
			}

		} else {

			yPos = anchorRect.bottom;
			if (rootHeight > dyBottom) {
				LayoutParams l = mScroller.getLayoutParams();
				l.height = dyBottom - mNewTab.getMeasuredHeight() - 70;
			}

		}

		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos - 10);

		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

		if (onTop)
			mWindow.showAtLocation(anchor, Gravity.BOTTOM, xPos, yPos);
		else
			mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
	}

	/**
	 * Set animation style
	 * 
	 * @param screenWidth
	 *            screen width
	 * @param requestedX
	 *            distance from left edge
	 * @param onTop
	 *            flag to indicate where the popup should be displayed. Set TRUE
	 *            if displayed on top of anchor view and vice versa
	 */
	private void setAnimationStyle(int screenWidth, int requestedX,
			boolean onTop) {
		int arrowPos = requestedX - mArrowUp.getMeasuredWidth() / 2;

		switch (mAnimStyle) {
		case ANIM_GROW_FROM_LEFT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
					: R.style.Animations_PopDownMenu_Left);
			break;

		case ANIM_GROW_FROM_RIGHT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right
					: R.style.Animations_PopDownMenu_Right);
			break;

		case ANIM_GROW_FROM_CENTER:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
					: R.style.Animations_PopDownMenu_Center);
			break;

		case ANIM_REFLECT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Reflect
					: R.style.Animations_PopDownMenu_Reflect);
			break;

		case ANIM_AUTO:
			if (arrowPos <= screenWidth / 4) {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
						: R.style.Animations_PopDownMenu_Left);
			} else if (arrowPos > screenWidth / 4
					&& arrowPos < 3 * (screenWidth / 4)) {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
						: R.style.Animations_PopDownMenu_Center);
			} else {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right
						: R.style.Animations_PopDownMenu_Right);
			}

			break;
		}
	}

	/**
	 * Show arrow
	 * 
	 * @param whichArrow
	 *            arrow type resource id
	 * @param requestedX
	 *            distance from left screen
	 */
	private void showArrow(int whichArrow, int requestedX) {
		final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp
				: mArrowDown;
		final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown
				: mArrowUp;

		final int arrowWidth = mArrowUp.getMeasuredWidth();

		showArrow.setVisibility(View.VISIBLE);

		ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow
				.getLayoutParams();

		param.leftMargin = requestedX - arrowWidth / 2;

		hideArrow.setVisibility(View.INVISIBLE);
	}

	/**
	 * Set listener for window dismissed. This listener will only be fired if
	 * the quicakction dialog is dismissed by clicking outside the dialog or
	 * clicking on sticky item.
	 */
	public void setOnDismissListener(UrlList.OnDismissListener listener) {
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
	public interface OnUrlItemClickListener {
		public abstract void onItemClick(UrlList source, int pos, int actionId);
	}

	/**
	 * Listener for item close button click
	 * 
	 */
	public interface OnUrlItemCloseBtnClickListener {
		public abstract void onItemCloseBtnClick(UrlList source,int pos,
				UrlItem mUrlItem);
	}
	/**
	 * Listener for item newTab button click
	 * 
	 */
	public interface OnNewTabBtnClickListener {
		public abstract void onNewTabBtnClick();
	}

	/**
	 * Listener for window dismiss
	 * 
	 */
	public interface OnDismissListener {
		public abstract void onDismiss();
	}
}