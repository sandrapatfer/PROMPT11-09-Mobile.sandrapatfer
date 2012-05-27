package prompt.yamba.services;

import prompt.yamba.YambaApplication;
import winterwell.jtwitter.Twitter;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class UpdateStatusService extends IntentService {

	private YambaApplication _application;
	
	public static String IntentMessageKey = "TweetText";
	
	public UpdateStatusService() {
		super("Updater service worker");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Twitter t = null;
		try
		{
			t = _application.GetTwitterObject();
		}
		catch (Exception e)
		{
			Log.e(YambaApplication.TAG, "Error getting twitter object");
		}
		if (t != null)
		{
			Log.d(YambaApplication.TAG, "Before sending message");
			try
			{
				t.updateStatus(intent.getStringExtra(IntentMessageKey));
			}
			catch (Exception e)
			{
				Log.e(YambaApplication.TAG, "Error updating status");
				// somehow this should show an error message to the user interface
			}
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		_application = (YambaApplication)getApplication();
	}

}
