package prompt.yamba.test1.old;

import prompt.yamba.test1.TimelinePullService;
import winterwell.jtwitter.Twitter;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApplication_timer extends Application implements OnSharedPreferenceChangeListener {
	
	private SharedPreferences _prefs;
	private volatile Twitter _twitter;
	private boolean _timelineServiceUp = false;
	
	public static final String TAG = "Yamba";
	private static final boolean DEBUG = true;

	@Override
	public void onCreate()
	{
		Log.d(TAG, "onCreate");
		super.onCreate();
		Init();
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		Log.d(TAG, "onSharedPreferenceChanged");
		_twitter = null;
		CheckTimelinePrefs();
	}
	
	private void CheckTimelinePrefs()
	{
		// versao do pull com start/stop nas preferences
/*		if (_timelineServiceUp != _prefs.getBoolean("timeline_service_up", false))
		{
			if (!_timelineServiceUp)
			{				
				Intent msg = new Intent();
				msg.setClass(this, TimelinePullService_timer.class);
				msg.setAction(TimelinePullService_timer.Action_Start_Pull);
				startService(msg);
				
				_timelineServiceUp = true;
			}
			else
			{
				Intent msg = new Intent();
				msg.setClass(this, TimelinePullService_timer.class);
				msg.setAction(TimelinePullService_timer.Action_Stop_Pull);
				startService(msg);

				_timelineServiceUp = false;
			}
		}*/
	}

	private void Init()
	{		
		_prefs = PreferenceManager.getDefaultSharedPreferences(this);
		_prefs.registerOnSharedPreferenceChangeListener(this);
		_twitter = null;
		
		// clearing prefs because debug kills the service
/*		if (DEBUG)
		{
			Editor editor = _prefs.edit();
			editor.putBoolean("timeline_service_up", false);
			editor.commit();
		}
		else
		{
			CheckTimelinePrefs();
		}*/
	}
	
	synchronized public Twitter GetTwitterObject()
	{
		if (_twitter == null)
		{
			String u = _prefs.getString("username", "");
			String p = _prefs.getString("password", "");
			_twitter = new Twitter(u, p);
			String url = _prefs.getString("site_url", "");
			_twitter.setAPIRootUrl(url);
		}
		return _twitter;
	}
	
	public void NetworkChange(boolean isNetworkDown)
	{
		Log.d(TAG, "NetworkChange: " + isNetworkDown);
		if (!isNetworkDown)
		{
			AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			Intent msg = new Intent();
			msg.setClass(this, TimelinePullService.class);
			msg.setAction(TimelinePullService.Action_Pull_Now);
			PendingIntent operation = PendingIntent.getService(this, -1, msg, PendingIntent.FLAG_UPDATE_CURRENT);
			mgr.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, operation);
		}
	}
}
