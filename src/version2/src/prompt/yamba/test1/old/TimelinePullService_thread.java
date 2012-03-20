package prompt.yamba.test1.old;

import java.util.List;

import prompt.yamba.test1.YambaApplication;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TimelinePullService_thread extends Service {

	private YambaApplication _application;
	private Thread _thread;
	private boolean _threadRunning;
	
	public TimelinePullService_thread()
	{
		_threadRunning = false;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		_application = (YambaApplication)getApplication();
		
		_thread = new Thread(new Runnable()
		{

			public void run()
			{
				Log.d(YambaApplication.TAG, "thread starting");
				
				Twitter t = _application.GetTwitterObject();
				
				while (true)
				{
					try
					{
						List<Status> list = t.getPublicTimeline();
						for (Status status: list)
						{
							Log.d(YambaApplication.TAG, String.format("%s: %s", status.user.name, status.text));
						}
						Thread.sleep(6000);
					}
					catch(InterruptedException ex)
					{
						break;
					}
				}
			}});
		
	}
	
	@Override
	public void onDestroy() {
		
		_thread.interrupt();
		
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.d(YambaApplication.TAG, "onStartCommand");
		if (!_threadRunning)
		{
			_thread.start();
			_threadRunning = true;
		}
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
