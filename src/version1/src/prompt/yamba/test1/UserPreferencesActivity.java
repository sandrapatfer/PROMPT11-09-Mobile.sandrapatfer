package prompt.yamba.test1;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class UserPreferencesActivity extends PreferenceActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.user_prefs);
		
	}

}
