package prompt.yamba.stores;

import prompt.yamba.providers.TimelineProviderContract;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TimelinePersistentStore
{
	private DBHelper _helper;
	private static final String DATABASE_NAME = "yamba.db";
	private static final int DB_VERSION = 1;
	
	private static final String STATUS_TABLE_NAME = "t_status";
		
	public TimelinePersistentStore(Context context)
	{
		_helper = new DBHelper(context);
	}
	
	public Cursor GetAllStatus(String[] projection)
	{
		SQLiteDatabase db = _helper.getReadableDatabase();
		return db.query(STATUS_TABLE_NAME, projection, null, null, null, null, null);
	}

	private class DBHelper extends SQLiteOpenHelper
	{

		public DBHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT)", STATUS_TABLE_NAME,
				TimelineProviderContract.COL_ID, TimelineProviderContract.COL_USER, TimelineProviderContract.COL_TEXT));
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			// TODO Auto-generated method stub
			
		}
	}
}
