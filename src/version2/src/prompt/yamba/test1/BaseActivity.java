package prompt.yamba.test1;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends Activity {

	public BaseActivity() {
		super();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		if (item.getItemId() == R.id.prefs_menu)
		{
			startActivity(new Intent(this, UserPreferencesActivity.class));
			return true;
		}

		if (item.getItemId() == R.id.pull_menu)
		{
			Intent msg = new Intent();
			msg.setClass(this, TimelinePullService.class);
			msg.setAction(TimelinePullService.Action_Pull_Now);
			startService(msg);

			return true;
		}
		
		return true;
	}

}