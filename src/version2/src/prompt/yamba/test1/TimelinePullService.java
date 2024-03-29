package prompt.yamba.test1;

import java.util.List;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class TimelinePullService extends IntentService
{
	private YambaApplication _application;
	
	public static String Action_Pull_Now = "pull_now";
	
	public TimelinePullService()
	{
		super("Timeline pull service");
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		_application = (YambaApplication)getApplication();		
		Log.d(YambaApplication.TAG, "TimelinePullService.onCreate");
	}
		
	private void GetData()
	{
		try
		{
			// get the data
			Twitter t = _application.GetTwitterObject();
			List<Status> list = t.getPublicTimeline();
			Log.d(YambaApplication.TAG, String.format("New list size: %d", list.size()));
			YambaApplication.TweetList = list;
		}
		catch(Exception ex)
		{
			Log.d(YambaApplication.TAG, "Error connecting to Marakana");
		}
	}
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		Log.d(YambaApplication.TAG, "TimelinePullService.onHandleIntent");
		
		String action= intent.getAction();
		if (action.equals(Action_Pull_Now))
		{			
			Log.d(YambaApplication.TAG, "Action = Action_Pull_Now");			
			GetData();
		}
	}

}
