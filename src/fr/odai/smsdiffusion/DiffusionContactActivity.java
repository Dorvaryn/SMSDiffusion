package fr.odai.smsdiffusion;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class DiffusionContactActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diffusion_contact);

		AutoCompleteTextView phoneNumber = (AutoCompleteTextView) findViewById(R.id.text_contact);

		ArrayAdapter<String> acontactslist = new ArrayAdapter<String>(this, R.layout.list_item_contacts);
		Cursor contactCursor = getContentResolver().query(
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
	}
}
