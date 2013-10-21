package edu.csupomona.cs.cs356.classmate;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationForm extends Activity implements OnClickListener {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.registration_form);

		final Button b = (Button)findViewById(R.id.btnRegister);
		b.setOnClickListener(this);
		b.setEnabled(false);

		final TextView tvPasswordMatcher = (TextView)findViewById(R.id.tvPasswordMatcher);

		final EditText pass1 = (EditText)findViewById(R.id.etPassword);
		final EditText pass2 = (EditText)findViewById(R.id.etConfirmPassword);

		TextWatcher textWatcher = new TextWatcher() {
			public void afterTextChanged(Editable s) {
				String p1 = pass1.getText().toString();
				String p2 = pass2.getText().toString();
				if (!p1.isEmpty() && p1.compareTo(p2) == 0) {
					tvPasswordMatcher.setText(R.string.passwordsmatch);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.green));
					b.setEnabled(true);
				} else {
					tvPasswordMatcher.setText(R.string.passwordsdonotmatch);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.red));
					b.setEnabled(false);
				}

				// TODO: Safely clear strings from memory using some char array
				p1 = null;
				p2 = null;
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//...
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//...
			}
		};

		pass1.addTextChangedListener(textWatcher);
		pass2.addTextChangedListener(textWatcher);
	}
	
	private void sendToMainMenu() {
		Intent i = new Intent(this, MainMenu.class);
		startActivity(i);
	}
	
	public void onClick(View v) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		 final EditText pass1 = (EditText)findViewById(R.id.etBroncoName);
		 String p1 = pass1.getText().toString();
		 
		 final EditText pass2 = (EditText)findViewById(R.id.etPassword);
		 String p2 = pass2.getText().toString();
		 
		 final AlertDialog d = new AlertDialog.Builder(this).create();
			d.setTitle(R.string.registerErrorTitle);
			d.setMessage(getResources().getString(R.string.registerError));
			d.setIcon(android.R.drawable.ic_dialog_info);
			d.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					setResult(RESULT_OK);
					//finish();
				}
			});

			d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					setResult(RESULT_OK);
					//finish();
				}
			});
		 
		 params.put("email", p1);
		 params.put("password", p2);
		 client.get("http://www.lol-fc.com/classmate/register.php",params, new AsyncHttpResponseHandler() {
		     @Override
		     public void onSuccess(String response) {
		        
		       
		         	System.out.println(response);
		         	if(Integer.parseInt(response)==2)
		         	{
		         		sendToMainMenu();
		         	}else
		         		d.show();
		        
		     }
		 });
	}
}
