package com.tcai.ui.components.popup.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcai.popup.R;
import com.tcai.ui.components.popup.PopupWindows;
import com.tcai.utils.DPUtil;
import com.tcai.utils.FontUtil;

public class PopupMenu extends PopupWindows implements OnDismissListener {
	//private View mRootView;
	private ImageView mArrowUp;
	private ImageView mArrowDown;
	private LayoutInflater mInflater;
    private RelativeLayout.LayoutParams params;
	private OnDismissListener mDismissListener;
	private OnPopMenuBtnClickListener mPopMenuBtnClickListener;
	private int screenwidth;
	private boolean mDidAction;
	private ViewGroup mViewGroup;
	private ViewPager mViewPager;
	private ArrayList<View> listViews;
	private int viewOffset;
	private int imgWidth;
	private ImageView iv_cursor;
	private TextView tv_main;
	private TextView tv_utils;
	private TextView tv_set;

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
	public PopupMenu(Context context) {
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
	public PopupMenu(Context context, int orientation) {
		super(context);

		mOrientation = orientation;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (mOrientation == HORIZONTAL) {
			setRootViewId(R.layout.popup_menu);
		} else {
			setRootViewId(R.layout.popup_menu);
		}

		mAnimStyle = ANIM_AUTO;
	}

	/**
	 * Set root view.
	 * 
	 * @param id
	 *            Layout resource id
	 */
	public void setRootViewId(int id) {
		mRootView = mInflater.inflate(id, null);

		mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);
		mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);

		mViewGroup = (ViewGroup) mRootView.findViewById(R.id.popup_menu_group);
		tv_main = (TextView) mRootView.findViewById(R.id.tv_main);
		tv_main.setTypeface(FontUtil.getCustomFont(mContext));
		tv_utils = (TextView) mRootView.findViewById(R.id.tv_utils);
		tv_utils.setTypeface(FontUtil.getCustomFont(mContext));
		tv_set = (TextView) mRootView.findViewById(R.id.tv_set);
		tv_set.setTypeface(FontUtil.getCustomFont(mContext));
        this.tv_main.setOnClickListener(new myOnClick(0));
        this.tv_utils.setOnClickListener(new myOnClick(1));
        this.tv_set.setOnClickListener(new myOnClick(2));
        
		iv_cursor = (ImageView) mRootView.findViewById(R.id.iv_cursor);
        setCursorWidth();
        params = (RelativeLayout.LayoutParams) iv_cursor.getLayoutParams();

		mViewPager = (ViewPager) mRootView.findViewById(R.id.viewPagerw);
		mViewPager.setFocusableInTouchMode(true);
		mViewPager.setFocusable(true);

		listViews = new ArrayList<View>();
		listViews.add(mInflater.inflate(R.layout.popup_grid_menu, null));
		listViews.add(mInflater.inflate(R.layout.popup_grid_menu, null));
		listViews.add(mInflater.inflate(R.layout.popup_grid_menu, null));
		mViewPager.setAdapter(new myPagerAdapter());
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		mArrowUp.setAlpha(180);
		mArrowDown.setAlpha(180);
		mViewGroup.getBackground().setAlpha(180);
		mRootView.setFocusableInTouchMode(true);
	
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
	 * Show quickaction popup. Popup is automatically positioned, on top or
	 * bottom of anchor view.
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

		boolean onTop = (dyTop > dyBottom) ? true : false;

		if (onTop) {
			if (rootHeight > dyTop) {
				yPos = 15;
				LayoutParams l = mViewGroup.getLayoutParams();
				l.height = dyTop - anchor.getHeight();
			} else {
				yPos = anchorRect.top - rootHeight;
			}
		} else {
			yPos = anchorRect.bottom;

			if (rootHeight > dyBottom) {
				LayoutParams l = mViewGroup.getLayoutParams();
				l.height = dyBottom;
			}
		}

		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos-10);

		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

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
	 * Set listener for action item gird View button clicked.
	 * 
	 * @param listener
	 *            Listener
	 */
	public void setOnPopMenuBtnClickListener(
			OnPopMenuBtnClickListener listener) {
		mPopMenuBtnClickListener = listener;
	}

	/**
	 * Set listener for window dismissed. This listener will only be fired if
	 * the quicakction dialog is dismissed by clicking outside the dialog or
	 * clicking on sticky item.
	 */
	public void setOnDismissListener(PopupMenu.OnDismissListener listener) {
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
	public interface OnActionItemClickListener {
		public abstract void onItemClick(PopupMenu source, int pos, int actionId);
	}

	/**
	 * Listener for window dismiss
	 * 
	 */
	public interface OnDismissListener {
		public abstract void onDismiss();
	}
	
	/**
	 * Listener for item girdView button click
	 * 
	 */
	public interface OnPopMenuBtnClickListener {
		public abstract void onPopMenuBtnClick(int girdId,int position);
	}
	 public void setCursorWidth()
	    {
	        screenwidth = getScreenWidth()-DPUtil.dip2px(mContext, 10);
	        // imgWidth = screenwidth / 3 - 40;// 40 是为了让指示器 稍微小于屏幕大小的1/3
	        // viewOffset = 20; // 让指示器显示在中间
	        imgWidth = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_cursor).getWidth();// 获取图片宽度
	        viewOffset = (screenwidth / 3 - imgWidth) / 2;
	        Matrix matrix = new Matrix();
	        matrix.postTranslate(viewOffset, 0);
	        iv_cursor.setImageMatrix(matrix);
	        Log.e("TAG", screenwidth + "");

	    }

	    public int getScreenWidth()
	    {
	        DisplayMetrics dm = new DisplayMetrics();
	        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
	        int screenW = dm.widthPixels;
	        return screenW;

	    }
	
	 public class myPagerAdapter extends PagerAdapter
	    {

	        @Override
	        public void destroyItem(View container, int position, Object object)
	        {

	            ((ViewPager) container).removeView(listViews.get(position));

	        }

	        @Override
	        public int getCount()
	        {

	            return listViews.size();

	        }

	        public Object instantiateItem(View container, int position)
	        {

	            if (position < 3)
	            {
	                ((ViewPager) container).addView(listViews.get(position % 3), 0);

	            }
	            // 将来添加菜单的时候 新建一个gridviewadapter 然后new个gridview 添加到这里就可以
	    		int imgRecouse[] = new int[] { R.drawable.menu_add_to_bookmark, R.drawable.menu_bookmark, R.drawable.menu_refresh,
	    				R.drawable.menu_night, R.drawable.menu_account, R.drawable.menu_download, R.drawable.menu_fullscreen,
	    				R.drawable.menu_exit };

	    		String title[] = new String[] { "添加书签", "书签/历史", "刷新", "夜间模式", "账户", "下载",
	    				"全屏", "退出" };
	            GirdViewAdapter girdViewAdapter = new GirdViewAdapter(mContext, imgRecouse, title);
	            switch (position)
	            {
	                case 0:// 选项卡1

	                    GridView gridView = (GridView) container.findViewById(R.id.myGridView);

	                    gridView.setAdapter(girdViewAdapter);

	                    gridView.setOnItemClickListener(new OnItemClickListener()
	                    {

	                        @Override
	                        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	                        {

	                        	if (mPopMenuBtnClickListener != null) {
	            					mPopMenuBtnClickListener.onPopMenuBtnClick(1,position);
	            				}

	                        }
	                    });

	                    break;
	                case 1:// 选项卡2
	                    GridView gridView2 = (GridView) container.findViewById(R.id.myGridView);

	    	    		int imgRecouse2[] = new int[] { R.drawable.menu_setup, R.drawable.menu_theme, R.drawable.menu_fillscreen,
	    	    				R.drawable.menu_pagemode, R.drawable.menu_nopic, R.drawable.menu_auto_rotation, R.drawable.menu_enter_incognito_mode,
	    	    				R.drawable.menu_brightness };

	    	    		String title2[] = new String[] { "系统设置", "皮肤管理", "适应屏幕", "翻页模式", "无图", "转屏",
	    	    				"无痕浏览", "亮度调节" };
	    	            GirdViewAdapter girdViewAdapter2 = new GirdViewAdapter(mContext, imgRecouse2, title2);
	                    gridView2.setAdapter(girdViewAdapter2);

	                    gridView2.setOnItemClickListener(new OnItemClickListener()
	                    {

	                        @Override
	                        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	                        {

	                        	if (mPopMenuBtnClickListener != null) {
	            					mPopMenuBtnClickListener.onPopMenuBtnClick(2,position);
	            				}

	                        }
	                    });
	                    break;
	                case 2:// 选项卡3
	                    GridView gridView3 = (GridView) container.findViewById(R.id.myGridView);

	    	    		int imgRecouse3[] = new int[] { R.drawable.menu_lookup, R.drawable.menu_update, R.drawable.menu_feedback,
	    	    				R.drawable.menu_data_usage, R.drawable.menu_report, R.drawable.menu_help};

	    	    		String title3[] = new String[] { "业内查找", "检查更新", "反馈意见", "省流查询", "举报网址", "帮助说明"};
	    	    		GirdViewAdapter girdViewAdapter3 = new GirdViewAdapter(mContext, imgRecouse3, title3);
	                    gridView3.setAdapter(girdViewAdapter3);

	                    gridView3.setOnItemClickListener(new OnItemClickListener()
	                    {

	                        @Override
	                        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	                        {
	                        	if (mPopMenuBtnClickListener != null) {
	            					mPopMenuBtnClickListener.onPopMenuBtnClick(3,position);
	            				}
	                        	

//	                            switch (position)
//	                            {
//
//	                                default:
//
//	                                    Toast.makeText(mContext, "这个是GridView+ViewPager仿UC菜单栏" + position+":"+":"+id, Toast.LENGTH_LONG).show();
//	                                    break;
//	                            }

	                        }
	                    });
	                    break;
	            }

	            return listViews.get(position);

	        }

	        public boolean isViewFromObject(View arg0, Object arg1)
	        {

	            return arg0 == (arg1);

	        }

	    }

	    public class MyOnPageChangeListener implements OnPageChangeListener
	    {

	        int one = viewOffset * 2 + imgWidth;// 页卡1 -> 页卡2 偏移量

	        int two = one * 2;// 页卡1 -> 页卡3 偏移量

	        @Override
	        public void onPageSelected(int arg0)
	        {
	        }

	        @Override
	        public void onPageScrolled(int arg0, float arg1, int arg2)
	        {
	            params.leftMargin = (int) ((arg0+arg1)*one);
	            Log.i("123", " params.leftMargin:"+ params.leftMargin);
	            iv_cursor.setLayoutParams(params);
	        }

	        @Override
	        public void onPageScrollStateChanged(int arg0)
	        {

	        }

	    }

	    /*
	     * 
	     * 对选项卡单击监听的实现方法
	     */
	    public class myOnClick implements View.OnClickListener
	    {

	        int index = 0;

	        public myOnClick(int currentIndex)
	        {

	            index = currentIndex;
	        }

	        public void onClick(View v)
	        {

	            mViewPager.setCurrentItem(index);

	        }

	    }
}