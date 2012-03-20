package prompt.yamba.test1;

import winterwell.jtwitter.Twitter;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener {
	
	private SharedPreferences _prefs;
	private volatile Twitter _twitter;
	
	public static final String TAG = "Yamba";

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
	}

	private void Init()
	{		
		_prefs = PreferenceManager.getDefaultSharedPreferences(this);
		_prefs.registerOnSharedPreferenceChangeListener(this);
		_twitter = null;
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
	
	public void NetworkChange(boolean isNetworkUp)
	{
		Log.d(TAG, "NetworkChange: " + isNetworkUp);
		if (isNetworkUp)
		{
			// TODO read prefs
			
			Log.d(TAG, "Starting alarm cycle");
			AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			Intent msg = new Intent();
			msg.setClass(this, TimelinePullService.class);
			msg.setAction(TimelinePullService.Action_Pull_Now);
			PendingIntent operation = PendingIntent.getService(this, -1, msg, PendingIntent.FLAG_UPDATE_CURRENT);
			mgr.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, operation);
		}
		else
		{
			Log.d(TAG, "Stopping alarm cycle");
			AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			Intent msg = new Intent();
			msg.setClass(this, TimelinePullService.class);
			msg.setAction(TimelinePullService.Action_Pull_Now);
			PendingIntent operation = PendingIntent.getService(this, -1, msg, PendingIntent.FLAG_UPDATE_CURRENT);
			mgr.cancel(operation);
		}
	}
}
