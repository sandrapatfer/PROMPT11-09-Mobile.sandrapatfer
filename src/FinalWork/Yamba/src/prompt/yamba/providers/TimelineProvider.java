package prompt.yamba.providers;

import prompt.yamba.YambaApplication;
import winterwell.jtwitter.Twitter.Status;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.net.Uri;
import android.util.Log;

public class TimelineProvider extends ContentProvider
{
	private YambaApplication _application;
	private static final UriMatcher _uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	private static final int INDEX_STATUS_LIST = 0;
	private static final int INDEX_STATUS_ITEM = 1;
	private static final String[] _mimes = new String[]
	{ 
		"vnd.android.cursor.dir/status",
		"vnd.android.cursor.item/status"
	};
	
	static
	{
		_uriMatcher.addURI(TimelineProviderContract.AUTHORITY, "/", INDEX_STATUS_LIST);
		_uriMatcher.addURI(TimelineProviderContract.AUTHORITY, "#", INDEX_STATUS_ITEM);
	}
	
	@Override
	public boolean onCreate()
	{
		_application = (YambaApplication)getContext().getApplicationContext();
		Log.d(YambaApplication.TAG, "TimelineProvider.onCreate");
		return false;
	}

	@Override
	public String getType(Uri uri)
	{
		int match = _uriMatcher.match(uri);
		if (match == UriMatcher.NO_MATCH)
		{
			return null;
		}
		
		return _mimes[match];
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		int match = _uriMatcher.match(uri);
		if (match == UriMatcher.NO_MATCH)
		{
			return null;
		}
				
		MatrixCursor cursor = new MatrixCursor(projection);

		if (match == INDEX_STATUS_ITEM)
		{
			String itemId = uri.getLastPathSegment();
			long itemId2 = Long.parseLong(itemId);
			Status s = _application.Store.Get(itemId2);
			RowBuilder newRow = cursor.newRow();			
			for (String col : projection)
			{
				newRow.add(getStatusColumn(s, col));
			}
		}
		else if (match == INDEX_STATUS_LIST)
		{
			Iterable<Status> list = _application.Store;
			for (Status s : list)
			{
				RowBuilder newRow = cursor.newRow();
				for (String col : projection)
				{
					newRow.add(getStatusColumn(s, col));
				}
			}
			cursor.setNotificationUri(getContext().getContentResolver(), Uri.parse(TimelineProviderContract.TIMELINE_LIST_URI));
		}
		return cursor;
	}
	
	private Object getStatusColumn(Status s, String colName)
	{
		if (colName == TimelineProviderContract.COL_ID)
		{
			return s.getId();
		}
		if (colName == TimelineProviderContract.COL_TEXT)
		{
			return s.getText();
		}
		if (colName == TimelineProviderContract.COL_USER)
		{
			return s.getUser().getName();
		}
		
		return null;
	}


	@Override
	public int delete(Uri arg0, String arg1, String[] arg2)
	{
		return 0;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
