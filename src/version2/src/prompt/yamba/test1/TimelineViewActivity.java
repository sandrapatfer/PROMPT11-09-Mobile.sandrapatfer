package prompt.yamba.test1;

import winterwell.jtwitter.Twitter.Status;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TimelineViewActivity extends ListActivity
{
	private YambaApplication _application;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		_application = (YambaApplication)getApplication();		
		Log.d(YambaApplication.TAG, "TimelineViewActivity.onCreate");
		
		setListAdapter(new ArrayAdapter<Status>(this, R.layout.timeline_view,
				YambaApplication.TweetList
				));
		
	}
	
}
