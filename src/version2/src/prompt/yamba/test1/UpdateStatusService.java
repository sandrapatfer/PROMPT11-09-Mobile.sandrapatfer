package prompt.yamba.test1;

import winterwell.jtwitter.Twitter;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

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
