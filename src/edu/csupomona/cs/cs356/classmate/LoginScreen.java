package edu.csupomona.cs.cs356.classmate;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
		startActivity(i);
	}
	
	private void verifyLogin(){ //will worry about fixing this later
		final int result = 0;
		
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		 final EditText pass1 = (EditText)findViewById(R.id.etBroncoName);
		 String p1 = pass1.getText().toString();
		 
		 final EditText pass2 = (EditText)findViewById(R.id.etPassword);
		 String p2 = pass2.getText().toString();
		 
		 params.put("email", p1);
		 params.put("password", p2);
		 client.get("http://www.lol-fc.com/classmate/login.php",params, new AsyncHttpResponseHandler() {
		     @Override
		     public void onSuccess(String response) {
		        
		       
		         	System.out.println(response);
		         	if(Integer.parseInt(response)==2)
		         		sendToMainMenu();
		        
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
