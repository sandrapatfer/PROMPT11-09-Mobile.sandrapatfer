package prompt.yamba;

import java.util.List;

import prompt.yamba.providers.TimelineProviderContract;
import prompt.yamba.services.TimelinePullService;
import prompt.yamba.stores.TimelinePersistentStore;
import prompt.yamba.stores.TimelineStore;
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
import winterwell.jtwitter.Twitter.Status;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener {
	
	private SharedPreferences _prefs;
	private volatile Twitter _twitter;
	private volatile PendingIntent activeIntent;
	
	public TimelineStore Store;
	public TimelinePersistentStore PersistentStore;
	
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
		
		if (activeIntent != null && !_prefs.getBoolean("timeline_automatic", false))
		{
			Log.d(TAG, "Stopping alarm cycle");
			AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			mgr.cancel(activeIntent);
			activeIntent = null;
		}
	}

	private void Init()
	{		
		_prefs = PreferenceManager.getDefaultSharedPreferences(this);
		_prefs.registerOnSharedPreferenceChangeListener(this);
		_twitter = null;
		activeIntent = null;
		
		Store = new TimelineStore();
		
		// test
		PersistentStore = new TimelinePersistentStore(this);
		PersistentStore.GetAllStatus(new String[] { TimelineProviderContract.COL_ID });
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
			if (_prefs.getBoolean("timeline_automatic", false))
			{
				Log.d(TAG, "Starting alarm cycle");
				AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
				Intent msg = new Intent();
				msg.setClass(this, TimelinePullService.class);
				msg.setAction(TimelinePullService.Action_Pull_Now);
				activeIntent = PendingIntent.getService(this, -1, msg, PendingIntent.FLAG_UPDATE_CURRENT);
				
				int interval = 60000;// _prefs.getString("timeline_update_interval", "60");
				mgr.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval, activeIntent);
			}
		}
		else if (activeIntent != null)
		{
			Log.d(TAG, "Stopping alarm cycle");
			AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			mgr.cancel(activeIntent);
			activeIntent = null;
		}
	}
}
