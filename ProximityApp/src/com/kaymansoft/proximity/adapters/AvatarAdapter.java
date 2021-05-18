package com.kaymansoft.proximity.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.gui.CS;
import com.kaymansoft.proximity.model.CategoryDesc;

public class AvatarAdapter extends BaseAdapter {
	private static int convertViewCounter = 0;
	private Context mContext;
	private LayoutInflater mInflater;

	static class ViewHolder {
		ImageView image;
	}

	private Bitmap[] tilesImages = new Bitmap[CS.tiles.length];
	private Bitmap[] tilesThumbs = new Bitmap[CS.tiles.length];

	public AvatarAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		for (int i = 0; i < CS.tiles.length; i++) {
			tilesImages[i] = BitmapFactory.decodeResource(context.getResources(), CS.tiles[i]);
			tilesThumbs[i] = Bitmap.createScaledBitmap(tilesImages[i], 100,	100, false);
		}
	}

	public int getCount() {
		return CS.tiles.length;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public int getItemViewType(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.avatar_cell, null);
			convertViewCounter++;
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.imageView1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.image.setImageBitmap(tilesThumbs[position]);
		return convertView;
	}

	public Object getItem(int position) {
		return tilesImages[position];
	}

	public long getItemId(int position) {
		return position;
	}
}