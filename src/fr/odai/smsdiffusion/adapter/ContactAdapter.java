package fr.odai.smsdiffusion.adapter;

import java.util.ArrayList;

import fr.odai.smsdiffusion.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<POJOContact> {

	protected ArrayList<POJOContact> contacts;

	public ContactAdapter(Context context, int textViewResourceId, ArrayList<POJOContact> contacts) {
		super(context, textViewResourceId, contacts);
		this.contacts = contacts;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.item_contact, null);
		}

		POJOContact contact = getItem(position);
		TextView name = (TextView) convertView.findViewById(R.id.item_contact_name);
		name.setText(contact.name);

		TextView phone = (TextView) convertView.findViewById(R.id.item_contact_phone);
		phone.setText(contact.phone);

		return convertView;
	}

}
