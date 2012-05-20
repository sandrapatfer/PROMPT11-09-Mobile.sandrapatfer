package prompt.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class YambaBroadcastReceiver extends BroadcastReceiver
{	
	@Override
	public void onReceive(Context ctx, Intent intent)
	{		
		boolean isNetworkDown = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		YambaApplication app = (YambaApplication)ctx.getApplicationContext();
		app.NetworkChange(!isNetworkDown);
	}
}
