package prompt.yamba.activities;

import prompt.yamba.R;
import prompt.yamba.YambaApplication;
import prompt.yamba.providers.TimelineProviderContract;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TimelineViewActivity extends BaseActivity
{
	private Cursor _cursor;
	private final int COL_INDEX_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Log.d(YambaApplication.TAG, "TimelineViewActivity.onCreate");

		setContentView(R.layout.timeline_listview);
		ListView listView = (ListView) findViewById(R.id.timeline_listview);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Log.d(YambaApplication.TAG, "TimelineViewActivity.onItemClick");
				Uri itemUri = ContentUris.withAppendedId(Uri.parse(TimelineProviderContract.TIMELINE_LIST_URI), _cursor.getLong(COL_INDEX_ID));
				Log.i(YambaApplication.TAG, itemUri.toString());
				Intent intent = new Intent();
				intent
					.setClass(TimelineViewActivity.this, StatusViewActivity.class)
					.setData(itemUri);
				
				startActivity(intent);
			}
		});
		
		_cursor = getContentResolver().query(Uri.parse(TimelineProviderContract.TIMELINE_LIST_URI),
				new String[] {TimelineProviderContract.COL_ID, TimelineProviderContract.COL_TEXT},
				null, null, null);

		startManagingCursor(_cursor);
		final TimelineAdapter adapter = new TimelineAdapter(this, _cursor); 
		listView.setAdapter(adapter);
	}
	
	protected int GetActivityMenuId()
	{
		return R.id.timeline_menu;
	}
	
	private class TimelineAdapter extends CursorAdapter
	{

		public TimelineAdapter(Context context, Cursor c)
		{
			super(context, c);
		}

		@Override
		public void bindView(View view, Context arg1, Cursor cursor)
		{			
			int index = cursor.getColumnIndex(TimelineProviderContract.COL_TEXT);
			String txt = cursor.getString(index);
			((TextView)view).setText(txt);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			View newItemView = TimelineViewActivity.this.getLayoutInflater().inflate(R.layout.timeline_item_view, null);
			int index = cursor.getColumnIndex(TimelineProviderContract.COL_TEXT);
			String txt = cursor.getString(index);
			((TextView)newItemView).setText(txt);
			return newItemView;
		}

		@Override
		protected void onContentChanged()
		{
			super.onContentChanged();
			
			Log.d(YambaApplication.TAG, "TimelineAdapter.onContentChanged");

			_cursor = getContentResolver().query(Uri.parse(TimelineProviderContract.TIMELINE_LIST_URI),
					new String[] {TimelineProviderContract.COL_ID, TimelineProviderContract.COL_TEXT},
					null, null, null);
			startManagingCursor(_cursor);
			changeCursor(_cursor);				
		}

		
	}
	
}
