package edu.csupomona.cs.cs356.classmate;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.cs.cs356.classmate.Constants.CODE_LOGIN;
import static edu.csupomona.cs.cs356.classmate.Constants.CODE_RECOVER;
import static edu.csupomona.cs.cs356.classmate.Constants.CODE_REGISTER;
import static edu.csupomona.cs.cs356.classmate.Constants.INTENT_KEY_AUTOLOGIN;
import static edu.csupomona.cs.cs356.classmate.Constants.INTENT_KEY_EMAIL;
import static edu.csupomona.cs.cs356.classmate.Constants.INTENT_KEY_NAME;
import static edu.csupomona.cs.cs356.classmate.Constants.INTENT_KEY_USERID;
import static edu.csupomona.cs.cs356.classmate.Constants.NO_USER;
import static edu.csupomona.cs.cs356.classmate.Constants.PHP_BASE_ADDRESS;
import static edu.csupomona.cs.cs356.classmate.Constants.PHP_PARAM_DEVICEID;
import static edu.csupomona.cs.cs356.classmate.Constants.PHP_PARAM_EMAIL;
import static edu.csupomona.cs.cs356.classmate.Constants.PHP_PARAM_NAME;
import static edu.csupomona.cs.cs356.classmate.Constants.PHP_PARAM_PASSWORD;
import static edu.csupomona.cs.cs356.classmate.Constants.PHP_PARAM_USERID;
import static edu.csupomona.cs.cs356.classmate.Constants.PREFS_KEY_AUTOLOGIN;
import static edu.csupomona.cs.cs356.classmate.Constants.PREFS_KEY_EMAIL;
import static edu.csupomona.cs.cs356.classmate.Constants.PREFS_WHICH;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements View.OnClickListener {
	private EditText etEmailAddress;
	private EditText etPassword;
	private CheckBox cbAutoLogin;
	private Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_layout);

		SharedPreferences prefs = getSharedPreferences(PREFS_WHICH, Context.MODE_PRIVATE);
		boolean bAutoLogin = prefs.getBoolean(PREFS_KEY_AUTOLOGIN, false);

		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		btnLogin.setEnabled(false);

		etEmailAddress = (EditText)findViewById(R.id.etEmailAddress);
		etPassword = (EditText)findViewById(R.id.etPassword);
		TextWatcher tw = new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				if (etEmailAddress.getText().length() == 0) {
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

		etEmailAddress.addTextChangedListener(tw);
		etPassword.addTextChangedListener(tw);

		cbAutoLogin = (CheckBox)findViewById(R.id.cbAutoLogin);
		cbAutoLogin.setChecked(bAutoLogin);

		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.login_activity_logo_layout);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		Button b = (Button)findViewById(R.id.btnRecover);
		b.setOnClickListener(this);

		b = (Button)findViewById(R.id.btnRegister);
		b.setOnClickListener(this);

		String emailAddress = prefs.getString(PREFS_KEY_EMAIL, null);
		if (bAutoLogin && emailAddress != null) {
			attemptLogin(emailAddress, null);
		}
	}

	@Override
	protected void onStop() {
		SharedPreferences prefs = getSharedPreferences(PREFS_WHICH, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		String email = etEmailAddress.getText().toString();
		if (!email.isEmpty()) {
			editor.putString(PREFS_KEY_EMAIL, email);
		}

		editor.putBoolean(PREFS_KEY_AUTOLOGIN, cbAutoLogin.isChecked());
		editor.commit();
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CODE_LOGIN:
				switch (resultCode) {
					case Activity.RESULT_OK:
						etEmailAddress.setText("");
						etPassword.setText("");
						cbAutoLogin.setChecked(false);
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
					etEmailAddress.setText(data.getStringExtra(INTENT_KEY_EMAIL));
					cbAutoLogin.setChecked(data.getBooleanExtra(INTENT_KEY_AUTOLOGIN, false));

					int id = data.getIntExtra(INTENT_KEY_USERID, NO_USER);
					String username = data.getStringExtra(INTENT_KEY_NAME);
					login(id, username);
				}

				break;
		}
	}

	@Override
	public void onClick(View v) {
		String email = etEmailAddress.getText().toString();
		switch (v.getId()) {
			case R.id.btnLogin:
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
		loadingDialog.setTitle(getString(R.string.dialog_login_load_title));
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
		client.get(PHP_BASE_ADDRESS + "login.php", params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				if (!loadingDialog.isShowing()) {
					return;
				}

				loadingDialog.dismiss();

				int id = NO_USER;
				String name = null;
				try {
					id = json.getInt(PHP_PARAM_USERID);
					name = json.getString(PHP_PARAM_NAME);
				} catch (JSONException e) {
					id = NO_USER;
					name = null;
				} finally {
					if (id == NO_USER) {
						// Session has expired (logged on from another device)
					} else if (password != null) {
						AlertDialog.Builder errorDialog = new AlertDialog.Builder(LoginActivity.this);
						errorDialog.setTitle(R.string.dialog_login_error_title);
						errorDialog.setMessage(R.string.dialog_login_error);
						errorDialog.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								//...
							}
						});
					} else {
						login(id, name);
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
		i.putExtra(INTENT_KEY_AUTOLOGIN, cbAutoLogin.isChecked());
		startActivityForResult(i, CODE_REGISTER);
	}

	private void login(int id, String name) {
		assert id != NO_USER;

		String email = etEmailAddress.getText().toString();
		if (email.isEmpty()) {
			SharedPreferences prefs = getSharedPreferences(PREFS_WHICH, Context.MODE_PRIVATE);
			email = prefs.getString(PREFS_KEY_EMAIL, null);
			assert email != null;
		}

		Intent i = new Intent();
		i.putExtra(Constants.INTENT_KEY_USERID, id);
		i.putExtra(Constants.INTENT_KEY_EMAIL, email);
		i.putExtra(Constants.INTENT_KEY_NAME, name);
		startActivityForResult(i, CODE_LOGIN);
	}
}
