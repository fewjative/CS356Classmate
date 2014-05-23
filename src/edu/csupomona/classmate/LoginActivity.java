package edu.csupomona.classmate;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.classmate.Constants.CODE_MAIN;
import static edu.csupomona.classmate.Constants.CODE_RECOVER;
import static edu.csupomona.classmate.Constants.CODE_REGISTER;
import static edu.csupomona.classmate.Constants.INTENT_KEY_EMAIL;
import static edu.csupomona.classmate.Constants.INTENT_KEY_USER;
import static edu.csupomona.classmate.Constants.NO_USER;
import static edu.csupomona.classmate.Constants.PHP_ADDRESS_LOGIN;
import static edu.csupomona.classmate.Constants.PHP_BASE_ADDRESS;
import static edu.csupomona.classmate.Constants.PHP_PARAM_DEVICEID;
import static edu.csupomona.classmate.Constants.PHP_PARAM_EMAIL;
import static edu.csupomona.classmate.Constants.PHP_PARAM_NAME;
import static edu.csupomona.classmate.Constants.PHP_PARAM_PASSWORD;
import static edu.csupomona.classmate.Constants.PHP_PARAM_USERID;
import static edu.csupomona.classmate.Constants.PREFS_KEY_AUTOLOGIN;
import static edu.csupomona.classmate.Constants.PREFS_KEY_EMAIL;
import static edu.csupomona.classmate.Constants.PREFS_WHICH;
import edu.csupomona.classmate.abstractions.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements View.OnClickListener {
	private SharedPreferences prefs;

	private EditText etEmailAddress;
	private EditText etPassword;
//	private CheckBox cbAutoLogin;
	private Button btnLogin;
	private TextView btnRegister;
	private TextView btnRecover;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_layout);

		prefs = getSharedPreferences(PREFS_WHICH, Context.MODE_PRIVATE);
		boolean bAutoLogin = prefs.getBoolean(PREFS_KEY_AUTOLOGIN, true);

		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
//		btnLogin.setEnabled(false);

		etEmailAddress = (EditText)findViewById(R.id.etEmailAddress);
		etPassword = (EditText)findViewById(R.id.etPassword);

//		TextWatcher tw = new TextWatcherAdapter() {
//			@Override
//			public void afterTextChanged(Editable e) {
//				if (etEmailAddress.getText().length() == 0) {
//					btnLogin.setEnabled(false);
//					return;
//				}
//
//				if (etPassword.getText().length() == 0) {
//					btnLogin.setEnabled(false);
//					return;
//				}
//
//				btnLogin.setEnabled(true);
//			}
//		};
//
//		etEmailAddress.addTextChangedListener(tw);
//		etPassword.addTextChangedListener(tw);

//		cbAutoLogin = (CheckBox)findViewById(R.id.cbAutoLogin);
//		cbAutoLogin.setChecked(bAutoLogin);

		ActionBar actionBar = getActionBar();
		actionBar.hide();
//		actionBar.setCustomView(R.layout.login_activity_logo_layout);
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);


		btnRegister = (TextView)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
		btnRecover = (TextView)findViewById(R.id.btnRecover);
		btnRecover.setOnClickListener(this);

		String emailAddress = prefs.getString(PREFS_KEY_EMAIL, null);
		if (savedInstanceState == null && bAutoLogin && emailAddress != null) {
			attemptLogin(emailAddress, null);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		SharedPreferences.Editor editor = prefs.edit();
		String email = etEmailAddress.getText().toString();
		if (!email.isEmpty()) {
			editor.putString(PREFS_KEY_EMAIL, email);
		}

//		editor.putBoolean(PREFS_KEY_AUTOLOGIN, cbAutoLogin.isChecked());
		editor.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CODE_MAIN:
				switch (resultCode) {
					case Activity.RESULT_OK:
						etEmailAddress.setText("");
						etPassword.setText("");
//						cbAutoLogin.setChecked(false);
						etEmailAddress.requestFocus();
						break;
					case Activity.RESULT_CANCELED:
						finish();
						break;
				}

				break;
			case CODE_RECOVER:
				//...
				break;
			case CODE_REGISTER:
				if (resultCode == Activity.RESULT_OK) {
					User user = data.getParcelableExtra(INTENT_KEY_USER);
					etEmailAddress.setText(user.getEmail());
//					cbAutoLogin.setChecked(data.getBooleanExtra(INTENT_KEY_AUTOLOGIN, false));
					login(user);
				}

				break;
		}
	}

	@Override
	public void onClick(View v) {
		String email = etEmailAddress.getText().toString();
		switch (v.getId()) {
			case R.id.btnLogin:
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etEmailAddress.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);

				String password = etPassword.getText().toString();
				attemptLogin(email, password);
				break;
			case R.id.btnRecover:
				recoverAccount(email);
				break;
			case R.id.btnRegister:
				registerAccount(email);
				break;
		}
	}

	private void attemptLogin(final String email, final String password) {
		final ProgressDialog loadingDialog = new ProgressDialog(this);
		//loadingDialog.setTitle(getString(R.string.dialog_login_load_title));
		loadingDialog.setMessage(getString(R.string.dialog_login_load));
		loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadingDialog.setCancelable(true);
		loadingDialog.show();

		String device_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

		RequestParams params = new RequestParams();
		params.put(PHP_PARAM_EMAIL, email);
		params.put(PHP_PARAM_PASSWORD, password);
		params.put(PHP_PARAM_DEVICEID, device_id);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(PHP_BASE_ADDRESS + PHP_ADDRESS_LOGIN, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				if (!loadingDialog.isShowing()) {
					return;
				}

				loadingDialog.dismiss();

				long id = NO_USER;
				String name = null;
				try {
					JSONObject json = jsona.getJSONObject(0);
					id = json.getLong(PHP_PARAM_USERID);
					name = json.getString(PHP_PARAM_NAME);
				} catch (JSONException e) {
					id = NO_USER;
					name = null;
				} finally {
					if (id == NO_USER) {
						// Session has expired (logged on from another device)
						if (password != null) {
							AlertDialog.Builder errorDialog = new AlertDialog.Builder(LoginActivity.this);
							errorDialog.setTitle(R.string.dialog_login_error_title);
							errorDialog.setMessage(R.string.dialog_login_error);
							errorDialog.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									//...
								}
							});

							errorDialog.show();
						}
					} else {
						login(new User(id, name, email));
					}
				}
			}
		});
	}

	private void recoverAccount(String email) {
		Intent i = new Intent(this, RecoveryActivity.class);
		i.putExtra(INTENT_KEY_EMAIL, email);
		startActivityForResult(i, CODE_RECOVER);
	}

	private void registerAccount(String email) {
		Intent i = new Intent(this, RegistrationActivity.class);
		i.putExtra(INTENT_KEY_EMAIL, email);
//		i.putExtra(INTENT_KEY_AUTOLOGIN, cbAutoLogin.isChecked());
		startActivityForResult(i, CODE_REGISTER);
	}

	private void login(User user) {
		assert user.getID() != NO_USER;

		String email = etEmailAddress.getText().toString();
		if (email.isEmpty()) {
			email = prefs.getString(PREFS_KEY_EMAIL, null);
			assert email != null;

			String welcomeMessage = getString(R.string.login_welcome_back, user.getUsername());
			Toast.makeText(this, welcomeMessage, Toast.LENGTH_LONG).show();
		}

		Intent i = new Intent(this, MainActivity.class);
		i.putExtra(INTENT_KEY_USER, user);
		startActivityForResult(i, CODE_MAIN);
	}
}
