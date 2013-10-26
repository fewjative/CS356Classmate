package edu.csupomona.cs.cs356.classmate;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

public class LoginActivity extends Activity implements View.OnClickListener {
	static final String INTENT_KEY_USERNAME = "userName";
	static final String INTENT_KEY_REMEMBER = "remember";
	static final String INTENT_KEY_USERID = "userID";

	private static final String PREFS_LOGIN = "login_activity_prefs";
	private static final String PREFS_KEY_USERNAME = "userName";
	private static final String PREFS_KEY_REMEMBER = "remember";

	private static final int CODE_REGISTERATION_FORM = 1001;
	private static final int CODE_RECOVERY_FORM = 1002;
	//private static final int CODE_MAINMENU = 1003;

	private Button btnLogin;
	private EditText etUsername;
	private EditText etPassword;
	private CheckBox cbRemember;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		SharedPreferences preferences = getSharedPreferences(PREFS_LOGIN, MODE_PRIVATE);
		boolean remember = preferences.getBoolean(PREFS_KEY_REMEMBER, false);
		String userName = preferences.getString(PREFS_KEY_USERNAME, null);
		if (remember && userName != null) {
			String device = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

			RequestParams params = new RequestParams();
			params.put("email", userName);
			params.put("device_id", device);

			AsyncHttpClient client = new AsyncHttpClient();
			client.get("http://www.lol-fc.com/classmate/login.php", params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					int id = Integer.parseInt(response);
					if (NULL_USER < id) {
						login(id);
						return;
					} else {
						// Error, invalid device id, session has expired
					}
				}
			});
		}

		setContentView(R.layout.login_activity);

		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		btnLogin.setEnabled(false);

		etUsername = (EditText)findViewById(R.id.etUserName);
		etPassword = (EditText)findViewById(R.id.etPassword);
		TextWatcher tw = new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				if (etUsername.getText().length() == 0) {
					btnLogin.setEnabled(false);
					return;
				}

				if (etPassword.getText().length() == 0) {
					btnLogin.setEnabled(false);
					return;
				}

				btnLogin.setEnabled(true);
			}
		};

		etUsername.addTextChangedListener(tw);
		etPassword.addTextChangedListener(tw);

		cbRemember = (CheckBox)findViewById(R.id.cbRememberMe);
		cbRemember.setChecked(remember);

		ActionBar ab = getActionBar();
		ab.setCustomView(R.layout.logo_layout);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		Button b = (Button)findViewById(R.id.btnRecover);
		b.setOnClickListener(this);

		b = (Button)findViewById(R.id.btnRegister);
		b.setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

		SharedPreferences preferences = getSharedPreferences(PREFS_LOGIN, MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREFS_KEY_USERNAME, etUsername.getText().toString());
		editor.putBoolean(PREFS_KEY_REMEMBER, cbRemember.isChecked());

		editor.commit();
	}

	private void login(int user) {
		assert NULL_USER < user;

		//Intent i = new Intent(this, MainMenu.class);
		//i.putExtra(KEY_USERID, userid);
		//i.putExtra(KEY_USERNAME, userName);
		//startActivityForResult(i, CODE_MAINMENU);
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
			case R.id.btnLogin:
				attemptLogin();
				break;
			case R.id.btnRecover:
				recoverAccount();
				break;
			case R.id.btnRegister:
				registerAccount();
				break;
		}
	}

	private void attemptLogin() {
		final ProgressDialog pg = ProgressDialog.show(this, getResources().getString(R.string.login), getResources().getString(R.string.loginLoading));

		String userName = etUsername.getText().toString();
		String password = etPassword.getText().toString();
		String device = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

		RequestParams params = new RequestParams();
		params.put("email", userName);
		params.put("password", password);
		params.put("device_id", device);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/login.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				pg.dismiss();

				int id;
				try {
					id = Integer.parseInt(response);
				} catch (NumberFormatException e) {
					id = NULL_USER;
				}

				if (id <= NULL_USER) {
					AlertDialog d = new AlertDialog.Builder(LoginActivity.this).create();
					d.setTitle(R.string.loginErrorTitle);
					d.setMessage(getResources().getString(R.string.loginError));
					d.setIcon(android.R.drawable.ic_dialog_alert);
					d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});

					d.show();
					return;
				}

				login(id);
			}
		});
	}

	private void recoverAccount() {
		String userName = etUsername.getText().toString();

		Intent i = new Intent(this, RecoveryActivity.class);
		i.putExtra(INTENT_KEY_USERNAME, userName);
		startActivityForResult(i, CODE_RECOVERY_FORM);
	}

	private void registerAccount() {
		String userName = etUsername.getText().toString();

		Intent i = new Intent(this, RegistrationActivity.class);
		i.putExtra(INTENT_KEY_USERNAME, userName);
		startActivityForResult(i, CODE_REGISTERATION_FORM);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CODE_RECOVERY_FORM:
				if (resultCode != RESULT_OK) {
					break;
				}

				break;
			case CODE_REGISTERATION_FORM:
				if (resultCode != RESULT_OK) {
					break;
				}

				// Setting these values here should add them into preferences correctly
				etUsername.setText(data.getStringExtra(INTENT_KEY_USERNAME));
				cbRemember.setChecked(data.getBooleanExtra(INTENT_KEY_REMEMBER, false));

				int user = data.getIntExtra(INTENT_KEY_USERID, NULL_USER);
				login(user);
				break;
//			case CODE_MAINMENU:
//				if (resultCode != RESULT_OK) {
//					break;
//				}
//
//				SharedPreferences.Editor editor = prefs.edit();
//				editor.putBoolean(PREFS_LOGIN_REMEMBERME, false);
//				editor.putString(PREFS_LOGIN_USERNAME, null);
//				editor.commit();
//
//				etUserName = (EditText)findViewById(R.id.etUserName);
//				etUserName.setText("");
//
//				EditText etPassword = (EditText)findViewById(R.id.etPassword);
//				etPassword.setText("");
//
//				cbRememberMe = (CheckBox)findViewById(R.id.cbRememberMe);
//				cbRememberMe.setChecked(false);
//				break;
		}
	}
}
