package edu.csupomona.cs.cs356.classmate;

import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.loopj.android.http.RequestParams;

import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

public class LoginActivity extends FragmentActivity implements View.OnClickListener {
	public static final String INTENT_KEY_EMAIL = "emailAddress";
	public static final String INTENT_KEY_USERNAME = "userName";
	public static final String INTENT_KEY_REMEMBER = "remember";
	public static final String INTENT_KEY_USERID = "userID";
	public static final String INTENT_KEY_FBUSER = "fb_user";

	private static final String PREFS_LOGIN = "login_activity_prefs";
	private static final String PREFS_KEY_EMAIL = "emailAddress";
	private static final String PREFS_KEY_REMEMBER = "remember";
	
	private static final int CODE_REGISTERATION_FORM = 0x000F;
	private static final int CODE_RECOVERY_FORM = 0x001F;
	private static final int CODE_MAINMENU = 0x002F;
	
	private boolean remember;
	private Button btnLogin;
	private LoginButton authButton;
	private EditText etEmailAddress;
	private EditText etPassword;
	private CheckBox cbRemember;
	
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

		setContentView(R.layout.login_activity);
		
		SharedPreferences preferences = getSharedPreferences(PREFS_LOGIN, MODE_PRIVATE);
		remember = preferences.getBoolean(PREFS_KEY_REMEMBER, false);
		
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
		
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

		Button b = (Button)findViewById(R.id.btnRegister);
		b.setOnClickListener(this);
		
		TextView tv = (TextView)findViewById(R.id.txtRecover);
		tv.setOnClickListener(this);
		
		String emailAddress = preferences.getString(PREFS_KEY_EMAIL, null);
		if (remember && emailAddress != null) {
			attemptLogin(emailAddress, null);
		}
		
		authButton = (LoginButton) findViewById(R.id.btnFacebookLogin);
		authButton.setReadPermissions(Arrays.asList("email"));
		
	}
	
	private void attemptLogin(String emailAddress, final String password) {
		final ProgressDialog loadingDialog = new ProgressDialog(this);
		loadingDialog.setTitle(getString(R.string.login));
		loadingDialog.setMessage(getString(R.string.loginLoading));
		loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadingDialog.setCancelable(true);
		loadingDialog.show();

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
				
				if(!loadingDialog.isShowing())
				{
					return;
				}
				
				loadingDialog.dismiss();

				long id = NULL_USER;
				String username = null;
				if (1 < response.length()) {
					try {
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);
							id = jObj.getLong("user_id");
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

	private void login(long id, String username) {
		assert NULL_USER < id;

		String emailAddress = etEmailAddress.getText().toString();
		if (emailAddress.isEmpty()) {
			SharedPreferences preferences = getSharedPreferences(PREFS_LOGIN, MODE_PRIVATE);
			emailAddress = preferences.getString(PREFS_KEY_EMAIL, null);
		}
		Intent widgetIntent = new Intent(ClassmateProvider.UPDATE_ID);
		widgetIntent.putExtra(INTENT_KEY_USERID, id);
		sendBroadcast(widgetIntent);
		System.out.println("Should have broadcasted id " + id);
		
		Intent i = new Intent(this, MainActivity.class);
		i.putExtra(INTENT_KEY_USERID, id);
		i.putExtra(INTENT_KEY_EMAIL, emailAddress);
		i.putExtra(INTENT_KEY_USERNAME, username);
		i.putExtra(INTENT_KEY_FBUSER,false);
		startActivityForResult(i, CODE_MAINMENU);
	}
	
	private void facebookLogin(long id, String username, String email) {
		assert NULL_USER < id;

		String emailAddress = email;
		
		if (emailAddress.isEmpty()) {
			SharedPreferences preferences = getSharedPreferences(PREFS_LOGIN, MODE_PRIVATE);
			emailAddress = preferences.getString(PREFS_KEY_EMAIL, null);
		}
		//as far as I know, every fbook account needs an email so the above if statement will never be called
		Intent widgetIntent = new Intent(ClassmateProvider.UPDATE_ID);
		widgetIntent.putExtra(INTENT_KEY_USERID, id);
		sendBroadcast(widgetIntent);
		System.out.println("Should have broadcasted id " + id);
		
		Intent i = new Intent(this, MainActivity.class);
		i.putExtra(INTENT_KEY_USERID, id);
		i.putExtra(INTENT_KEY_EMAIL, emailAddress);
		i.putExtra(INTENT_KEY_USERNAME, username);
		i.putExtra(INTENT_KEY_FBUSER,true);
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
		int id = v.getId();
		
		if (id == R.id.btnLogin) {
			attemptLogin(emailAddress, etPassword.getText().toString());
		} 
		else if (id == R.id.btnRegister) {
			registerAccount(emailAddress);
		} 
		else if (id == R.id.txtRecover) {
			recoverAccount(emailAddress);
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
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		
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

				long id = data.getLongExtra(INTENT_KEY_USERID, NULL_USER);
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
	
	@Override
	public void onResume() {
	    super.onResume();
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    uiHelper.onResume();
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
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

								long id = NULL_USER;
								String username = null;
								String email = null;
								
								if (1 < response.length()) {
									try {
										JSONObject jObj;
										JSONArray myjsonarray = new JSONArray(response);
										for (int i = 0; i < myjsonarray.length(); i++) {
											jObj = myjsonarray.getJSONObject(i);
											id = jObj.getLong("user_id");
											username = jObj.getString("username");
											email = jObj.getString("email");
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}

								if (NULL_USER < id) {
									facebookLogin(id, username,email);
									Toast.makeText(LoginActivity.this, "Welcome back " + username + "!", Toast.LENGTH_LONG).show();
								}
								else
								{
									AlertDialog d = new AlertDialog.Builder(LoginActivity.this).create();
									d.setTitle(R.string.loginErrorTitle);
									d.setMessage(getResources().getString(R.string.fbloginError));
									d.setIcon(android.R.drawable.ic_dialog_alert);
									d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
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
