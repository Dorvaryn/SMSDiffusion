package fr.odai.smsdiffusion.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import fr.odai.smsdiffusion.R;
import fr.odai.smsdiffusion.db.DBHelper;

public class ListAdapter extends ArrayAdapter<POJOList> {
	
	private Context mContext;
	
	public ListAdapter(Context context, int textViewResourceId, ArrayList<POJOList> lists) {
		super(context, textViewResourceId, lists);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.item_list, null);
		}
		final POJOList list = getItem(position);
		TextView name = (TextView) convertView.findViewById(R.id.item_list_name);
		ImageButton remove = (ImageButton) convertView.findViewById(R.id.button_remove);
		name.setText(list.name);
		name.setEnabled(list.enable);
		remove.setFocusable(false);
		remove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

				alert.setTitle(mContext.getResources().getString(R.string.home_menu_remove));
				alert.setMessage(mContext.getResources().getString(
						R.string.home_menu_remove_dialog));

				alert.setPositiveButton(
						mContext.getResources().getString(R.string.dialog_button_positive),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
				            	remove(list);
				            	DBHelper.removeList(mContext, list.getId()); 
							}
						});

				alert.setNegativeButton(
						mContext.getResources().getString(R.string.dialog_button_negative),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						});

				alert.show();
			}
		});
		return convertView;
	}
}
