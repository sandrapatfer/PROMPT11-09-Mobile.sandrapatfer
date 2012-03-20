package prompt.yamba.test1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

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
