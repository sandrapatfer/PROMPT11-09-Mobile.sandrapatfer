package prompt.yamba.activities;

import prompt.yamba.R;
import prompt.yamba.R.xml;
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