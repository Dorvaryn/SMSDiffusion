package fr.odai.smsdiffusion.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.odai.smsdiffusion.R;
import fr.odai.smsdiffusion.model.POJOList;
import fr.odai.smsdiffusion.widget.HiddenQuickActionSetup;
import fr.odai.smsdiffusion.widget.SwipeableHiddenView;

public class ListAdapter extends ArrayAdapter<POJOList> {
	private HiddenQuickActionSetup mQuickActionSetup;
	
	public ListAdapter(Context context, int textViewResourceId, ArrayList<POJOList> lists, HiddenQuickActionSetup setup) {
		super(context, textViewResourceId, lists);
		mQuickActionSetup = setup;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = (SwipeableHiddenView) vi.inflate(
					R.layout.item_list, null);
			((SwipeableHiddenView) convertView).setHiddenViewSetup(mQuickActionSetup);

			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.item_list_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		POJOList list = getItem(position);
		holder.name.setText(list.name);
		holder.name.setEnabled(list.enable);
		return convertView;
	}

	private class ViewHolder {
		public TextView name;
	}
}
