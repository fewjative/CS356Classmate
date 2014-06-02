package edu.csupomona.classmate;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.classmate.Constants.CODE_MAIN;
import static edu.csupomona.classmate.Constants.CODE_RECOVER;
import static edu.csupomona.classmate.Constants.CODE_REGISTER;
import static edu.csupomona.classmate.Constants.INTENT_KEY_EMAIL;
import static edu.csupomona.classmate.Constants.INTENT_KEY_FBUSER;
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
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements View.OnClickListener {
	private SharedPreferences prefs;

	private EditText etEmailAddress;
	private EditText etPassword;
//	private CheckBox cbAutoLogin;
	private Button btnLogin;
	private LoginButton btnFbLogin;
	private TextView btnRegister;
	private TextView btnRecover;

	private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
    	@Override
    	public void call(Session session, SessionState state, Exception exception) {
    		onSessionStateChange(session, state, exception);
	    }
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_layout);

		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);

		prefs = getSharedPreferences(PREFS_WHICH, Context.MODE_PRIVATE);
		// enable auto-login by default
		boolean bAutoLogin = prefs.getBoolean(PREFS_KEY_AUTOLOGIN, true);

		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
//		btnLogin.setEnabled(false);

		btnFbLogin = (LoginButton)findViewById(R.id.btnFbLogin);
		btnFbLogin.setReadPermissions(Arrays.asList("email"));

		etEmailAddress = (EditText)findViewById(R.id.etEmailAddress);
		etEmailAddress.setTypeface(Typeface.DEFAULT);

		etPassword = (EditText)findViewById(R.id.etPassword);
		etPassword.setTypeface(Typeface.DEFAULT);
		etPassword.setTransformationMethod(new PasswordTransformationMethod());
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
	    uiHelper.onPause();

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
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);

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
		int id = v.getId();
		if (id == R.id.btnLogin) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etEmailAddress.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
			String password = etPassword.getText().toString();
			attemptLogin(email, password);
		} else if (id == R.id.btnRecover) {
			recoverAccount(email);
		} else if (id == R.id.btnRegister) {
			registerAccount(email);
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
					id = Long.parseLong(json.getString(PHP_PARAM_USERID));
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

	private void facebookLogin(long id, String username, String email) {
		assert NO_USER < id;

		String emailAddress = email;

		if (emailAddress.isEmpty()) {
			SharedPreferences preferences = getSharedPreferences(PREFS_WHICH, MODE_PRIVATE);
			emailAddress = preferences.getString(PHP_PARAM_EMAIL, null);
		}
		//as far as I know, every fbook account needs an email so the above if statement will never be called
//		Intent widgetIntent = new Intent(ClassmateProvider.UPDATE_ID);
//		widgetIntent.putExtra(INTENT_KEY_USER, id);
//		sendBroadcast(widgetIntent);
//		System.out.println("Should have broadcasted id " + id);

		Intent i = new Intent(this, MainActivity.class);
		i.putExtra(PHP_PARAM_USERID, id);
		i.putExtra(PHP_PARAM_EMAIL, emailAddress);
		i.putExtra(PHP_PARAM_USERID, username);
		i.putExtra(INTENT_KEY_FBUSER,true);
		startActivityForResult(i, CODE_MAIN);
	}

	@Override
	public void onResume() {
	    super.onResume();
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    uiHelper.onResume();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
	        Log.i("FACEBOOK", "Logged in...");

	        // Request Facebook user data and start main activity
	        Request.newMeRequest(session, new Request.GraphUserCallback() {
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (user != null) {
						// Log.i("FACEBOOK", user.getId()+" ***** "+user.getName());

						String device = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

						RequestParams params = new RequestParams();
						params.put("email", user.asMap().get("email").toString());
						params.put("user_id", user.getId());
						params.put("name", user.getName());
						params.put("device_id", device);

						Toast.makeText(getApplicationContext(), user.getId()+" ***** "+user.getName()+" ***** "+user.asMap().get("email").toString()+" ***** "+device, Toast.LENGTH_SHORT).show();

						AsyncHttpClient client = new AsyncHttpClient();
						client.get("http://www.lol-fc.com/classmate/facebooklogin.php", params, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {

								System.out.println("response: " + response);
								System.out.println("response length: " + response.length());

								long id = NO_USER;
								String username = null;
								String email = null;
								if (response.length() > 1) {
									try {
										JSONObject jObj;
										JSONArray myjsonarray = new JSONArray(response);
										for (int i = 0; i < myjsonarray.length(); i++) {
											jObj = myjsonarray.getJSONObject(i);
											id = Long.parseLong(jObj.getString("user_id"));
											username = jObj.getString("username");
											email = jObj.getString("email");
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}

								if (NO_USER < id) {
									login(new User(id, username, email));
								}
								else
								{
									AlertDialog d = new AlertDialog.Builder(LoginActivity.this).create();
									d.setTitle(R.string.dialog_login_error);
									d.setMessage(getResources().getString(R.string.dialog_fb_login_error));
									d.setIcon(android.R.drawable.ic_dialog_alert);
									d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.global_action_okay), new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
										}
									});
									d.show();
								}
							}
						});
					}
				}
	        }).executeAsync();

	    } else if (state.isClosed()) {
	        Log.i("FACEBOOK", "Logged out...");
	    }
	}
}
