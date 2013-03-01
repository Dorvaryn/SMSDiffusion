package fr.odai.smsdiffusion.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.odai.smsdiffusion.adapter.POJOContact;
import fr.odai.smsdiffusion.adapter.POJOList;

public class DBHelper {

	public static final Object sDataLock = new Object();

	private static SQLiteDatabase getDatabase(Context context) {
		DB db = new DB(context);
		return db.getReadableDatabase();
	}

	public static void insertContact(Context context, int list_id,
			String phoneNumber) {
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);

			ContentValues values = new ContentValues();
			values.put("number", phoneNumber);
			values.put("list_id", list_id);
			db.insert("contacts", null, values);
			db.close();
		}
	}

	public static void insertKeyword(Context context, int list_id, String value) {
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);

			ContentValues values = new ContentValues();
			values.put("value", value);
			values.put("list_id", list_id);
			db.insert("keywords", null, values);
			db.close();
		}
	}

	public static void insertList(Context context, String name, boolean enable) {
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);

			ContentValues values = new ContentValues();
			values.put("name", name);
			values.put("enable", enable);
			db.insert("diffusion_lists", null, values);
			db.close();
		}
	}

	public static ArrayList<POJOContact> getContacts(Context context,
			int list_id) {

		ArrayList<POJOContact> contacts = new ArrayList<POJOContact>();
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);

			Cursor cursor = db.query(true, "contacts",
					new String[] { "number" }, "list_id = ?", new String[]{String.valueOf(list_id)}, null, null,
					"_id DESC", null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				POJOContact entry = new POJOContact(context,
						cursor.getString(0));

				contacts.add(entry);
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
		}
		return contacts;
	}
	
	public static ArrayList<String> getContactsPhoneOnly(Context context,
			int list_id) {

		ArrayList<String> contacts = new ArrayList<String>();
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);

			Cursor cursor = db.query(true, "contacts",
					new String[] { "number" }, "list_id = ?", new String[]{String.valueOf(list_id)}, null, null,
					"_id DESC", null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				contacts.add(cursor.getString(0));
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
		}
		return contacts;
	}

	public static ArrayList<POJOList> getDiffusionLists(Context context) {

		ArrayList<POJOList> lists = new ArrayList<POJOList>();
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);

			Cursor cursor = db.query(true, "diffusion_lists", new String[] {
					"_id", "name", "enable" }, null, null, null, null,
					"_id DESC", null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				boolean enable = cursor.getInt(2) == 1;
				POJOList entry = new POJOList(cursor.getInt(0),
						cursor.getString(1), enable);
				lists.add(entry);
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
		}
		return lists;
	}
	
	public static ArrayList<POJOList> getEnabledDiffusionLists(Context context) {

		ArrayList<POJOList> lists = new ArrayList<POJOList>();
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);

			Cursor cursor = db.query(true, "diffusion_lists", new String[] {
					"_id", "name", "enable" }, "enabled = ?", new String[]{String.valueOf(1)}, null, null,
					"_id DESC", null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				boolean enable = cursor.getInt(2) == 1;
				POJOList entry = new POJOList(cursor.getInt(0),
						cursor.getString(1), enable);
				lists.add(entry);
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
		}
		return lists;
	}
	
	public static POJOList getDiffusionList(Context context, int list_id) {
		POJOList entry;
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);

			Cursor cursor = db.query(true, "diffusion_lists", new String[] {
					"_id", "name", "enable" }, "_id = ?", new String[]{String.valueOf(list_id)}, null, null,
					"_id DESC", null);
			cursor.moveToFirst();
			boolean enable = cursor.getInt(2) == 1;
			entry = new POJOList(cursor.getInt(0),
					cursor.getString(1), enable);			
			db.close();
		}
		return entry;
	}

	public static ArrayList<String> getKeywords(Context context, int list_id) {

		ArrayList<String> keywords = new ArrayList<String>();
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);

			Cursor cursor = db.query(true, "keywords",
					new String[] { "value" }, "list_id = ?", new String[]{String.valueOf(list_id)}, null, null,
					"_id DESC", null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				keywords.add(cursor.getString(0));
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
		}
		return keywords;
	}
	
	public static void updateList(Context context, POJOList list){
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);
			
			ContentValues values = new ContentValues();
			values.put("name", list.name);
			values.put("enable", list.enable);
			db.update("diffusion_lists", values, "_id = ?", new String[]{String.valueOf(list.getId())});
			db.close();
		}
	}

	public static void removeContact(Context context, int list_id, String phoneNumber) {
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);

			db.delete("contacts", "number = ? AND list_id = ?", new String[] { phoneNumber, String.valueOf(list_id) });
			db.close();
		}
	}
	
	public static void removeKeyword(Context context, int list_id, String value) {
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);

			db.delete("keywords", "value = ? AND list_id = ?", new String[] { value, String.valueOf(list_id) });
			db.close();
		}
	}
	
	public static void removeList(Context context, int list_id) {
		synchronized (DBHelper.sDataLock) {
			SQLiteDatabase db = getDatabase(context);
			db.delete("diffusion_lists", "_id = ?", new String[] { String.valueOf(list_id) });
			db.delete("keywords", "list_id = ?", new String[] { String.valueOf(list_id) });
			db.delete("contacts", "list_id = ?", new String[] { String.valueOf(list_id) });
			db.close();
		}
	}
}
