package prompt.yamba.test1.old;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import prompt.yamba.test1.YambaApplication;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TimelinePullService_timer extends IntentService
{
	private YambaApplication _application;
	private TimerTask _timerTask;
	private Timer _currentTimer;
	
	public static String Action_Pull_Now = "pull_now";
	public static String Action_Start_Pull = "start_pull";
	public static String Action_Stop_Pull = "stop_pull";
	
	public TimelinePullService_timer()
	{
		super("Timeline pull service");
		_currentTimer = null;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		_application = (YambaApplication)getApplication();
		
		_timerTask = new TimerTask()
		{
			@Override
			public void run()
			{
				Log.d(YambaApplication.TAG, "Timer task run");
				Intent msg = new Intent();
				msg.setClass(_application, TimelinePullService_timer.class);
				msg.setAction(Action_Start_Pull);
				startService(msg);
			}
		};		
	}
		
	private void GetData()
	{
		try
		{
			// get the data
			Twitter t = _application.GetTwitterObject();
			List<Status> list = t.getPublicTimeline();
			Log.d(YambaApplication.TAG, String.format("New list size: %d", list.size()));
	/*		for (Status status: list)
			{
				Log.d(YambaApplication.TAG, String.format("%s: %s", status.user.name, status.text));
			}*/
		}
		catch(Exception ex)
		{
			Log.d(YambaApplication.TAG, "Error connecting to Marakana");
		}
	}
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		Log.d(YambaApplication.TAG, "onHandleIntent");
		
		String action= intent.getAction();
		
		if (action.equals(Action_Pull_Now))
		{			
			Log.d(YambaApplication.TAG, "Action = Action_Pull_Now");
			// cancel the current timer
			if (_currentTimer != null)
			{
				_currentTimer.cancel();
				_currentTimer = new Timer();
				_currentTimer.schedule(_timerTask, 60000);
			}
			
			GetData();
		}
		else if (action.equals(Action_Stop_Pull))
		{
			Log.d(YambaApplication.TAG, "Action = Action_Stop_Pull");
			// cancel the current timer
			if (_currentTimer != null)
			{
				_currentTimer.cancel();
				_currentTimer = null;
			}
		}
		else // action == Action_Start_Pull
		{
			Log.d(YambaApplication.TAG, "Action = Action_Start_Pull");
			// set the next timer
			_currentTimer = new Timer();
			_currentTimer.schedule(_timerTask, 60000);
			
			GetData();
		}	
	}

}
