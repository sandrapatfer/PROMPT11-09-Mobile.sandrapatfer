package prompt.yamba.test1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// esta classe deve ter read-only props. O numero de instancias é igual ao numero de processos
// as instancias devem ser thread-safe

public class YambaActivity extends BaseActivity 
{
	private static String TAG = "Yamba";
	private static int maxNChar = 140;
	private static int warningNChar = 115;
	private static int nearEndNChar = 20;
	
	private int _nearEnd_color;
	private int _warning_color;
	private int _ok_color;
		
	private void Init()
	{
		Resources res = getResources();
		_nearEnd_color = res.getColor(R.color.nearEnd_color);
		_warning_color = res.getColor(R.color.warning_color);
		_ok_color = res.getColor(R.color.ok_color);		
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		Log.i(TAG, "onCreate called id:" + Thread.currentThread().getId());

		Init();
        
        final TextView textNChars = (TextView)findViewById(R.id.textNChars);
        textNChars.setText("" + maxNChar);
        
        final EditText input = (EditText)findViewById(R.id.input);

        Button btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener()
        {	
			public void onClick(View v)
			{
				Log.i(TAG, "onClick called");
				SubmitPost(input.getText().toString());
			}
		});
        	        
        input.addTextChangedListener(new TextWatcher()
        {
        	public void afterTextChanged (Editable s)
        	{
				Log.i(TAG, "afterText id:" + Thread.currentThread().getId());
        		int len = s.length();
        		textNChars.setText("" + (maxNChar - len));
        		if ((maxNChar - len) < nearEndNChar)
        		{
        			textNChars.setTextColor(_nearEnd_color);
        		}
        		else if ((maxNChar - len) < warningNChar)
        		{
        			textNChars.setTextColor(_warning_color);
        		}
        		else
        		{
        			textNChars.setTextColor(_ok_color);
        		}
        	}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3)
			{
			}

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
        });
    }
    
	private void SubmitPost(String text)
	{
		Intent msg = new Intent(this, UpdateStatusService.class);
		msg.putExtra(
				UpdateStatusService.IntentMessageKey,
				text);
		startService(msg);
	}
}