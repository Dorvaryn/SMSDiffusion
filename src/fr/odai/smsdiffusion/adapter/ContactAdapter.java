package fr.odai.smsdiffusion.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import fr.odai.smsdiffusion.R;

public class ContactAdapter extends ArrayAdapter<POJOContact> {

	public ArrayList<POJOContact> contacts;
	private OnTouchListener listener;

	public ContactAdapter(Context context, int textViewResourceId, ArrayList<POJOContact> contacts,
			OnTouchListener listener) {
		super(context, textViewResourceId, contacts);
		this.contacts = contacts;
		this.listener = listener;
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

		TextView phoneType = (TextView) convertView.findViewById(R.id.item_contact_phone_type);
		phoneType.setText(contact.phoneType);

		QuickContactBadge badge = (QuickContactBadge) convertView
				.findViewById(R.id.item_contact_badge);
		if (contact.icon != null)
			badge.setImageURI(contact.icon);
		else
			badge.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_contact));
		badge.assignContactUri(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI,
				String.valueOf(contact.lookupKey)));

		if (this.listener != null)
			convertView.setOnTouchListener(this.listener);

		return convertView;
	}

}
