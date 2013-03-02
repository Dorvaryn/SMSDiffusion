package fr.odai.smsdiffusion.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import fr.odai.smsdiffusion.R;
import fr.odai.smsdiffusion.model.POJOContact;
import fr.odai.smsdiffusion.widget.HiddenQuickActionSetup;
import fr.odai.smsdiffusion.widget.SwipeableHiddenView;

public class ContactAdapter extends ArrayAdapter<POJOContact> {

	private HiddenQuickActionSetup mQuickActionSetup;
	
	public ContactAdapter(Context context, int textViewResourceId, ArrayList<POJOContact> contacts, HiddenQuickActionSetup setup) {
		super(context, textViewResourceId, contacts);
		this.mQuickActionSetup = setup;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = (SwipeableHiddenView) vi.inflate(
					R.layout.item_contact, null);
			((SwipeableHiddenView) convertView).setHiddenViewSetup(mQuickActionSetup);

			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.item_contact_name);
			holder.phone = (TextView) convertView.findViewById(R.id.item_contact_phone);
			holder.phoneType = (TextView) convertView.findViewById(R.id.item_contact_phone_type);
			holder.badge = (QuickContactBadge) convertView.findViewById(R.id.item_contact_badge);
					
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		POJOContact contact = getItem(position);
		holder.name.setText(contact.name);
		holder.phone.setText(contact.phone);
		holder.phoneType.setText(contact.phoneType);
		if (contact.icon != null)
			holder.badge.setImageURI(contact.icon);
		else
			holder.badge.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_contact));
		holder.badge.assignContactUri(Uri.withAppendedPath(
				ContactsContract.Contacts.CONTENT_LOOKUP_URI,
				String.valueOf(contact.lookupKey)));
		return convertView;
	}

	private class ViewHolder {
		public TextView name;
		public TextView phone;
		public TextView phoneType;
		public QuickContactBadge badge;
	}
}
