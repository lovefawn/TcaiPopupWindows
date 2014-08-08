package com.tcai.ui.components.popup.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcai.popup.R;
import com.tcai.utils.FontUtil;

public class GirdViewAdapter extends BaseAdapter {

	private int imgRecouse[];

	private String title[];

	LayoutInflater inflater;

	Context context;

	public GirdViewAdapter(Context context, int[] imgRecouse, String[] title) {

		this.context = context;

		inflater = LayoutInflater.from(context);

		this.imgRecouse = imgRecouse;

		this.title = title;
	}

	public int getCount() {

		return imgRecouse.length;
	}

	@Override
	public Object getItem(int position) {

		return imgRecouse[position];
	}

	public String getItemTitle(int position) {

		return title[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View currentView, ViewGroup arg2) {

		currentView = inflater.inflate(R.layout.popup_menu_imagebutton, null);

		ImageView imageView = (ImageView) currentView
				.findViewById(R.id.imgbtn_img);
		TextView textView = (TextView) currentView
				.findViewById(R.id.imgbtn_text);
		textView.setTypeface(FontUtil.getCustomFont(context));

		imageView.setBackgroundResource(imgRecouse[position]);
		textView.setText(title[position]);

		return currentView;
	}

}
