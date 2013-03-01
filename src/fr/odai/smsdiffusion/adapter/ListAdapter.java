package fr.odai.smsdiffusion.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.odai.smsdiffusion.R;

public class ListAdapter extends ArrayAdapter<POJOList> {

	public ListAdapter(Context context, int textViewResourceId, ArrayList<POJOList> lists) {
		super(context, textViewResourceId, lists);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.item_list, null);
		}
		POJOList list = getItem(position);
		TextView name = (TextView) convertView.findViewById(R.id.item_list_name);
		name.setText(list.name);	
		return convertView;
	}
}
