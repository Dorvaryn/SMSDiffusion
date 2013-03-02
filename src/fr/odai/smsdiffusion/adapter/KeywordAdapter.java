package fr.odai.smsdiffusion.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.odai.smsdiffusion.R;
import fr.odai.smsdiffusion.widget.HiddenQuickActionSetup;
import fr.odai.smsdiffusion.widget.SwipeableHiddenView;

public class KeywordAdapter extends ArrayAdapter<String> {
	
	private ArrayList<String> keywords;
	private HiddenQuickActionSetup mQuickActionSetup;
	
	public KeywordAdapter(Context context, int textViewResourceId, ArrayList<String> keywords, HiddenQuickActionSetup setup) {
		super(context, textViewResourceId, keywords);
		this.mQuickActionSetup = setup;
		this.keywords = keywords;
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
			holder.value = (TextView) convertView.findViewById(R.id.item_list_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String keyword = getItem(position);
		holder.value.setText(keyword);

		return convertView;
	}

	private class ViewHolder {
		public TextView value;
	}
	
	public boolean contains(String keyword){
		return keywords.contains(keyword);
	}
}
