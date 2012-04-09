package prompt.yamba.activities;

import prompt.yamba.R;
import prompt.yamba.YambaApplication;
import prompt.yamba.R.color;
import prompt.yamba.R.id;
import prompt.yamba.R.layout;
import prompt.yamba.services.UpdateStatusService;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateStatusActivity extends BaseActivity 
{
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

		Log.i(YambaApplication.TAG, "onCreate called id:" + Thread.currentThread().getId());

		Init();
        
        final TextView textNChars = (TextView)findViewById(R.id.textNChars);
        textNChars.setText("" + maxNChar);
        
        final EditText input = (EditText)findViewById(R.id.input);

        Button btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener()
        {	
			public void onClick(View v)
			{
				Log.i(YambaApplication.TAG, "onClick called");
				SubmitPost(input.getText().toString());
			}
		});
        	        
        input.addTextChangedListener(new TextWatcher()
        {
        	public void afterTextChanged (Editable s)
        	{
				Log.i(YambaApplication.TAG, "afterText id:" + Thread.currentThread().getId());
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
    
	protected int GetActivityMenuId()
	{
		return R.id.update_menu;
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