package fr.odai.smsdiffusion.adapter;

import java.util.ArrayList;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;

public class POJOContact {
	public String lookupKey = "";

	public String name;
	public String phoneType;
	public String phone;

	public Uri icon;
	
	public POJOContact(Context ctx, String phone) {
		super();
		this.phone = phone;
		Cursor contactCursor = ctx.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.NUMBER + "=?", new String[]{phone}, null);
		contactCursor.moveToFirst();
		this.name = contactCursor.getString(contactCursor
				.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME));
		this.lookupKey = contactCursor.getString(contactCursor
				.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
		String photoId = contactCursor.getString(contactCursor
				.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
		if (photoId != null) {
			this.icon = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI,
					Long.parseLong(photoId));
		}
		this.phoneType = CommonDataKinds.Phone.getTypeLabel(
				ctx.getResources(),
				contactCursor.getInt(contactCursor.getColumnIndex(CommonDataKinds.Phone.TYPE)),
				contactCursor.getString(contactCursor
						.getColumnIndex(CommonDataKinds.Phone.LABEL))).toString();
		contactCursor.close();
	}
	
	public String toString() {
		return name + "<" + phone.replace(" ", "") + ">";
	}

	
	public POJOContact() {
		super();
	}

	/**
	 * Return a list of all contacts on this device
	 * 
	 * @param ctx
	 * @return
	 */
	public static ArrayList<POJOContact> getAllContacts(Context ctx) {
		ArrayList<POJOContact> allContacts = new ArrayList<POJOContact>();

		Cursor contactCursor = ctx.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		contactCursor.moveToFirst();
		while (!contactCursor.isAfterLast()){
			POJOContact contact = new POJOContact();

			contact.lookupKey = contactCursor.getString(contactCursor
					.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
			String photoId = contactCursor.getString(contactCursor
					.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
			if (photoId != null) {
				contact.icon = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI,
						Long.parseLong(photoId));
			}
			contact.name = contactCursor.getString(contactCursor
					.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME));
			contact.phoneType = CommonDataKinds.Phone.getTypeLabel(
					ctx.getResources(),
					contactCursor.getInt(contactCursor.getColumnIndex(CommonDataKinds.Phone.TYPE)),
					contactCursor.getString(contactCursor
							.getColumnIndex(CommonDataKinds.Phone.LABEL))).toString();
			contact.phone = contactCursor.getString(contactCursor
					.getColumnIndex(CommonDataKinds.Phone.NUMBER));
			allContacts.add(contact);
			contactCursor.moveToNext();
		}

		contactCursor.close();

		return allContacts;
	}
}
