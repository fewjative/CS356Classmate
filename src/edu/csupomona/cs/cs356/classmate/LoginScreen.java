package edu.csupomona.cs.cs356.classmate;


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
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginScreen extends Activity implements OnClickListener {
	private static final int REGISTERATION_FORM_CODE = 0x0F0F0F0F;
	private static final int RECOVERY_FORM_CODE = 0xF0F0F0F0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		final Button b1 = (Button)findViewById(R.id.btnLogin);
		b1.setOnClickListener(this);

		Button b2 = (Button)findViewById(R.id.btnRecover);
		b2.setOnClickListener(this);

		Button b3 = (Button)findViewById(R.id.btnRegister);
		b3.setOnClickListener(this);
		
		final EditText etBroncoName = (EditText)findViewById(R.id.etBroncoName);
		etBroncoName.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				String userName = etBroncoName.getText().toString();
				b1.setEnabled(!userName.isEmpty());
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//...
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//...
			}
		});
		
		final EditText etPassword = (EditText)findViewById(R.id.etPassword);
		etPassword.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				String password = etPassword.getText().toString();
				b1.setEnabled(!password.isEmpty());
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//...
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//...
			}
		});
	
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
			case R.id.btnLogin:
				verifyLogin();//sendToMainMenu();
				break;
			case R.id.btnRecover:
				i = new Intent(this, RecoveryForm.class);
				startActivityForResult(i, RECOVERY_FORM_CODE);
				break;
			case R.id.btnRegister:
				i = new Intent(this, RegistrationForm.class);
				startActivityForResult(i, REGISTERATION_FORM_CODE);
				break;
		}
	}

	private void sendToMainMenu() {
		Intent i = new Intent(this, MainMenu.class);
		i.putExtra("ID", "1");
		startActivity(i);
	}
	
	private void verifyLogin(){ 
		final int result = 0;
		
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		 final EditText pass1 = (EditText)findViewById(R.id.etBroncoName);
		 String p1 = pass1.getText().toString();
		 
		 final EditText pass2 = (EditText)findViewById(R.id.etPassword);
		 String p2 = pass2.getText().toString();
		 
		 final AlertDialog d = new AlertDialog.Builder(this).create();
			d.setTitle(R.string.loginErrorTitle);
			d.setMessage(getResources().getString(R.string.loginError));
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
		 client.get("http://www.lol-fc.com/classmate/login.php",params, new AsyncHttpResponseHandler() {
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case RECOVERY_FORM_CODE:
				if (resultCode != RESULT_OK) {
					break;
				}
				
				Toast.makeText(this, getResources().getString(R.string.recoverSent), Toast.LENGTH_LONG).show();
				break;
			case REGISTERATION_FORM_CODE:
				if (resultCode != RESULT_OK) {
					break;
				}

				sendToMainMenu();
				break;
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}*/
}
