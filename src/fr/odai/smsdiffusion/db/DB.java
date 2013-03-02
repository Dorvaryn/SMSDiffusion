package fr.odai.smsdiffusion.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

	final static int DB_VERSION = 1;
	final static String DB_NAME = "smsdiffusion.s3db";
	Context context;

	public DB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE diffusion_lists ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, enable INTEGER NOT NULL, total_message_sent INTEGER NOT NULL, last_sent_date INTEGER, last_message TEXT)");
		database.execSQL("CREATE TABLE contacts ( _id INTEGER PRIMARY KEY AUTOINCREMENT, number TEXT NOT NULL, list_id INTEGER NOT NULL, FOREIGN KEY(list_id) REFERENCES diffusion_lists(_id))");
		database.execSQL("CREATE TABLE keywords ( _id INTEGER PRIMARY KEY AUTOINCREMENT, value TEXT NOT NULL, list_id INTEGER NOT NULL, FOREIGN KEY(list_id) REFERENCES diffusion_lists(_id))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// See
		// http://www.drdobbs.com/database/using-sqlite-on-android/232900584?pgno=2
	}
}