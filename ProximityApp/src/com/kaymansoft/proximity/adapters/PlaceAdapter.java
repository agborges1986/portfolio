package com.kaymansoft.proximity.adapters;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.model.PlaceDesc;

public class PlaceAdapter extends ArrayAdapter<PlaceDesc> {
	PlaceDesc[] places = null;
	Activity activity = null;
	int nComments = 3;
	public PlaceAdapter(Activity activity, PlaceDesc[] places) {
		super(activity, android.R.layout.simple_list_item_1, places);
		this.places = places;
		this.activity = activity;
	}
	
	public PlaceAdapter(Activity activity, PlaceDesc[] places, int index) {
		super(activity, android.R.layout.simple_list_item_1, places);
		this.places = places;
		this.activity = activity;
		nComments = places[0].comments.length;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View cell = convertView;
		PlaceHolder holder = null;
		if (cell == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			cell = inflater.inflate(R.layout.place_row, null);

			holder = new PlaceHolder(cell);
			cell.setTag(holder);
		} else {
			holder = (PlaceHolder) cell.getTag();
		}

		holder.populateFrom(places[position], nComments);
		return cell;
	}
	
	static class PlaceHolder {
		private TextView name = null;
		private TextView desc = null;

		public PlaceHolder(View cell) {
			name = (TextView) cell.findViewById(R.id.place);
			desc = (TextView) cell.findViewById(R.id.desc);
		}

		void populateFrom(PlaceDesc place, int nComments) {
			name.setText(place.name);
			StringBuilder sb = new StringBuilder();
			int size = place.comments.length;

			for (int i = 0; i < nComments && i < size; i++){
				DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
				Date date = new Date(place.comments[i].creationTime);
				String dateStr = dateFormatter.format(date);
				sb.append("» ").append(place.comments[i].user).append(": ").append(dateStr).append("\n")
							  .append(place.comments[i].text).append("\n\n");
			}
			
			sb.append(" Comments: ").append(size);

			desc.setText(sb.toString());
		}
	}
}

