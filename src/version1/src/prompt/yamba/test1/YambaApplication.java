package prompt.yamba.test1;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener {
	
	private SharedPreferences _prefs;
	private volatile Twitter _twitter;

	@Override
	public void onCreate() {
		super.onCreate();
		Init();
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		_twitter = null;
	}

	private void Init() {		
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
}
