package com.kaymansoft.proximity.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.gui.CS;
import com.kaymansoft.proximity.model.CategoryDesc;

public class CategoryAdapter extends ArrayAdapter<CategoryDesc> {
	Activity activity = null;
	public CategoryAdapter(Activity activity) {
		super(activity, android.R.layout.simple_list_item_1, CS.categories);
		this.activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View cell = convertView;
		CategoryHolder holder = null;
		if (cell == null) {                  
			LayoutInflater inflater = activity.getLayoutInflater();
			cell = inflater.inflate(R.layout.gridview_cell, null);
			cell.setBackgroundColor(Color.WHITE);
			holder = new CategoryHolder(cell);
			cell.setTag(holder);				
		}else{
			holder = (CategoryHolder)cell.getTag();
		}
		
		holder.populateFrom(CS.categories[position]);
		return cell;
	}	
	
	static class CategoryHolder{
		private TextView name = null;
		public CategoryHolder(View cell) {
			name=(TextView)cell.findViewById(R.id.title);
		}
		void populateFrom(CategoryDesc category){
			name.setText(category.getName());
		}
	}
}    

