package fr.odai.smsdiffusion.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;

public class POJOContact {
	public String name;
	public String phone;
	
	public String toString()
	{
		return name;
	}

	/**
	 * Return a list of all contacts on this device
	 * @param ctx
	 * @return
	 */
	public static ArrayList<POJOContact> getAllContacts(Context ctx) {
		ArrayList<POJOContact> allContacts = new ArrayList<POJOContact>();
		
		Cursor contactCursor = ctx.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		contactCursor.moveToFirst();
		do {
			POJOContact contact = new POJOContact();
			contact.name = contactCursor.getString(contactCursor
					.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME));
			contact.phone = contactCursor.getString(contactCursor
					.getColumnIndex(CommonDataKinds.Phone.NUMBER));
			allContacts.add(contact);
		} while (contactCursor.moveToNext());
		
		contactCursor.close();
		
		return allContacts;
	}
}
