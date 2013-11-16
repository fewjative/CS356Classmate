package edu.csupomona.cs.cs356.classmate;

import android.app.ActionBar;
import android.app.Activity;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import static android.content.Context.MODE_PRIVATE;
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
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements View.OnClickListener {
	public static final String INTENT_KEY_EMAIL = "emailAddress";
	public static final String INTENT_KEY_USERNAME = "userName";
	public static final String INTENT_KEY_REMEMBER = "remember";
	public static final String INTENT_KEY_USERID = "userID";

	private static final String PREFS_LOGIN = "login_activity_prefs";
	private static final String PREFS_KEY_EMAIL = "emailAddress";
	private static final String PREFS_KEY_REMEMBER = "remember";

	private static final int CODE_REGISTERATION_FORM = 0x000F;
	private static final int CODE_RECOVERY_FORM = 0x001F;
	private static final int CODE_MAINMENU = 0x002F;

	private Button btnLogin;
	private EditText etEmailAddress;
	private EditText etPassword;
	private CheckBox cbRemember;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		SharedPreferences preferences = getSharedPreferences(PREFS_LOGIN, MODE_PRIVATE);
		boolean remember = preferences.getBoolean(PREFS_KEY_REMEMBER, false);

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

		cbRemember = (CheckBox)findViewById(R.id.cbRememberMe);
		cbRemember.setChecked(remember);

		ActionBar ab = getActionBar();
		ab.setCustomView(R.layout.logo_layout);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		Button b = (Button)findViewById(R.id.btnRecover);
		b.setOnClickListener(this);

		b = (Button)findViewById(R.id.btnRegister);
		b.setOnClickListener(this);

		String emailAddress = preferences.getString(PREFS_KEY_EMAIL, null);
		if (remember && emailAddress != null) {
			attemptLogin(emailAddress, null);
		}
	}

	private void attemptLogin(String emailAddress, final String password) {
		final ProgressDialog pg = ProgressDialog.show(this, getResources().getString(R.string.login), getResources().getString(R.string.loginLoading));

		String device = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

		RequestParams params = new RequestParams();
		params.put("email", emailAddress);
		params.put("device_id", device);

		if (password != null) {
			params.put("password", password);
		}

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/login.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				pg.dismiss();

				int id = NULL_USER;
				String username = null;
				if (1 < response.length()) {
					try {
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);
							id = jObj.getInt("user_id");
							username = jObj.getString("username");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				if (NULL_USER < id) {
					login(id, username);
					Toast.makeText(LoginActivity.this, "Welcome back " + username + "!", Toast.LENGTH_LONG).show();
				} else if (password != null) {
					AlertDialog d = new AlertDialog.Builder(LoginActivity.this).create();
					d.setTitle(R.string.loginErrorTitle);
					d.setMessage(getResources().getString(R.string.loginError));
					d.setIcon(android.R.drawable.ic_dialog_alert);
					d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});

					d.show();
				} else {
					// Device id has expired
				}
			}
		});
	}

	private void login(int id, String username) {
		assert NULL_USER < id;

		String emailAddress = etEmailAddress.getText().toString();
		if (emailAddress.isEmpty()) {
			SharedPreferences preferences = getSharedPreferences(PREFS_LOGIN, MODE_PRIVATE);
			emailAddress = preferences.getString(PREFS_KEY_EMAIL, null);
		}

		Intent i = new Intent(this, MainActivity.class);
		i.putExtra(INTENT_KEY_USERID, id);
		i.putExtra(INTENT_KEY_EMAIL, emailAddress);
		i.putExtra(INTENT_KEY_USERNAME, username);
		startActivityForResult(i, CODE_MAINMENU);
	}

	@Override
	protected void onStop() {
		super.onStop();

		SharedPreferences preferences = getSharedPreferences(PREFS_LOGIN, MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();

		String emailAddress = etEmailAddress.getText().toString();
		if (!emailAddress.isEmpty()) {
			editor.putString(PREFS_KEY_EMAIL, emailAddress);
		}

		editor.putBoolean(PREFS_KEY_REMEMBER, cbRemember.isChecked());

		editor.commit();
	}

	@Override
	public void onClick(View v) {
		String emailAddress = etEmailAddress.getText().toString();
		switch (v.getId()) {
			case R.id.btnLogin:
				attemptLogin(emailAddress, etPassword.getText().toString());
				break;
			case R.id.btnRecover:
				recoverAccount(emailAddress);
				break;
			case R.id.btnRegister:
				registerAccount(emailAddress);
				break;
		}
	}

	private void recoverAccount(String emailAddress) {
		Intent i = new Intent(this, RecoveryActivity.class);
		i.putExtra(INTENT_KEY_USERNAME, emailAddress);
		startActivityForResult(i, CODE_RECOVERY_FORM);
	}

	private void registerAccount(String emailAddress) {
		Intent i = new Intent(this, RegistrationActivity.class);
		i.putExtra(INTENT_KEY_USERNAME, emailAddress);
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

				etEmailAddress.setText(data.getStringExtra(INTENT_KEY_USERNAME));
				cbRemember.setChecked(data.getBooleanExtra(INTENT_KEY_REMEMBER, false));

				int id = data.getIntExtra(INTENT_KEY_USERID, NULL_USER);
				String username = data.getStringExtra(INTENT_KEY_USERNAME);
				login(id, username);
				break;
			case CODE_MAINMENU:
				switch (resultCode) {
					case RESULT_OK:
						etEmailAddress.setText("");
						etPassword.setText("");
						cbRemember.setChecked(false);
						etEmailAddress.requestFocus();
						break;
					case RESULT_CANCELED:
						finish();
						break;
				}

				break;
		}
	}
}
