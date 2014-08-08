package com.tcai.ui.components.popup;

import com.tcai.ui.components.popup.menu.PopupMenu;
import com.tcai.ui.components.popup.sysmenu.SysMenu;
import com.tcai.ui.components.popup.urllist.UrlItem;
import com.tcai.ui.components.popup.urllist.UrlList;

import net.londatiga.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ExampleActivity extends Activity {
	//action id
	private static final int ID_UP     = 1;
	private static final int ID_DOWN   = 2;
	private static final int ID_SEARCH = 3;
	private static final int ID_INFO   = 4;
	private static final int ID_ERASE  = 5;	
	private static final int ID_OK     = 6;
	    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.popup_main);

		UrlItem nextItem 	= new UrlItem(ID_DOWN, "UC浏览器",null, getResources().getDrawable(R.drawable.fileicon_default));
		UrlItem prevItem 	= new UrlItem(ID_UP, "新浪","http://news.sina.com.cn/c/2013-08-22/173728021067.shtml", getResources().getDrawable(R.drawable.fileicon_default));
        UrlItem searchItem 	= new UrlItem(ID_SEARCH, "百度","http://m.baidu.com/qwertwertyur/ewertyui123456", getResources().getDrawable(R.drawable.fileicon_default));
        UrlItem infoItem 	= new UrlItem(ID_INFO, "淘宝","http://m.taobao.com", getResources().getDrawable(R.drawable.fileicon_default));
        UrlItem infoItem1 	= new UrlItem(ID_INFO, "淘宝1","http://m.taobao.com", getResources().getDrawable(R.drawable.fileicon_default));
        UrlItem infoItem2 	= new UrlItem(ID_INFO, "淘宝2","http://m.taobao.com", getResources().getDrawable(R.drawable.fileicon_default));
        UrlItem infoItem3 	= new UrlItem(ID_INFO, "淘宝3","http://m.taobao.com", getResources().getDrawable(R.drawable.fileicon_default));
        UrlItem infoItem4 	= new UrlItem(ID_INFO, "淘宝4","http://m.taobao.com", getResources().getDrawable(R.drawable.fileicon_default));
        //ActionItem eraseItem 	= new ActionItem(ID_ERASE, "Clear", getResources().getDrawable(R.drawable.menu_eraser));
        //ActionItem okItem 		= new ActionItem(ID_OK, "OK", getResources().getDrawable(R.drawable.menu_ok));
        
        //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
        prevItem.setSticky(true);
        nextItem.setSticky(true);
        searchItem.setSticky(true);
        infoItem.setSticky(true);
		
		//create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout 
        //orientation
		final UrlList quickAction = new UrlList(this, UrlList.VERTICAL);
		final PopupMenu popMenu = new PopupMenu(this, PopupMenu.VERTICAL);
		final SysMenu sysMenu = new SysMenu(this,SysMenu.VERTICAL);
		
		//add action items into QuickAction
        quickAction.addUrlItem(nextItem,true);
		quickAction.addUrlItem(prevItem,false);
        quickAction.addUrlItem(searchItem,false);
        quickAction.addUrlItem(infoItem,false);
        quickAction.addUrlItem(infoItem1,false);
        quickAction.addUrlItem(infoItem2,false);
        quickAction.addUrlItem(infoItem3,false);
        quickAction.addUrlItem(infoItem4,false);
        //quickAction.addActionItem(eraseItem);
        //quickAction.addActionItem(okItem);
        
        //Set listener for action item clicked
		quickAction.setOnUrlItemClickListener(new UrlList.OnUrlItemClickListener() {			
			@Override
			public void onItemClick(UrlList source, int pos, int actionId) {				
				UrlItem actionItem = source.getUrlItem(pos);
                 
				//here we can filter which action item was clicked with pos or actionId parameter
				if (actionId == ID_SEARCH) {
					Toast.makeText(getApplicationContext(), pos+":"+"ID_SEARCH clicked ", Toast.LENGTH_SHORT).show();
				} else if (actionId == ID_INFO) {
					Toast.makeText(getApplicationContext(), pos+":"+"ID_INFO clicked", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), pos+":"+actionItem.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		 //Set listener for action item clicked
//		quickAction.setOnUrlItemCloseBtnClickListener(new UrlList.OnUrlItemCloseBtnClickListener() {			
//			@Override
//			public void onItemCloseBtnClick(UrlList source, int pos, int actionId) {				
//				UrlItem actionItem = source.getUrlItem(pos);
//                 
//				//here we can filter which action item was clicked with pos or actionId parameter
//					Toast.makeText(getApplicationContext(), pos+":"+actionItem.getTitle() + " closed", Toast.LENGTH_SHORT).show();
//
//			}
//		});
		
		//set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
		//by clicking the area outside the dialog.
		quickAction.setOnDismissListener(new UrlList.OnDismissListener() {			
			@Override
			public void onDismiss() {
				Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
			}
		});
		
		//show on btn1
		Button btn1 = (Button) this.findViewById(R.id.btn1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sysMenu.show(v);
			}
		});

		Button btn2 = (Button) this.findViewById(R.id.btn2);
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});
		
		Button btn3 = (Button) this.findViewById(R.id.btn3);
		btn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popMenu.show(v);
			}
		});
	}
}