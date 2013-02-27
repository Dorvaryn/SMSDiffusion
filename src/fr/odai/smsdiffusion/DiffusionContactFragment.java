package fr.odai.smsdiffusion;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class DiffusionContactFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.activity_diffusion_contact, container, false);
		AutoCompleteTextView phoneNumber = (AutoCompleteTextView) root.findViewById(R.id.text_contact);

		ArrayAdapter<String> acontactslist = new ArrayAdapter<String>(getActivity(), R.layout.list_item_contacts);
		Cursor contactCursor = getActivity().getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		contactCursor.moveToFirst();
		do {
			String name = contactCursor.getString(contactCursor
					.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME));
			String num = contactCursor.getString(contactCursor
					.getColumnIndex(CommonDataKinds.Phone.NUMBER));
			acontactslist.add(num);
			Log.e("wtf", num);
		} while (contactCursor.moveToNext());

		phoneNumber.setAdapter(acontactslist);
		return root;
	}
}
