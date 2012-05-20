package prompt.yamba.services;

import prompt.yamba.YambaApplication;
import winterwell.jtwitter.Twitter;
import android.app.IntentService;
import android.content.Intent;

public class UpdateStatusService extends IntentService {

	private YambaApplication _application;
	
	public static String IntentMessageKey = "TweetText";
	
	public UpdateStatusService() {
		super("Updater service worker");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Twitter t = _application.GetTwitterObject(); 
		t.updateStatus(intent.getStringExtra(IntentMessageKey));
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		_application = (YambaApplication)getApplication();
	}

}
