package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

public class RegistrationActivity extends Activity implements View.OnClickListener {
	private Button btnRegister;
	private EditText etEmailAddress;
	private EditText etUsername;
	private EditText etPass1;
	private EditText etPass2;
	private TextView tvPasswordMatcher;
	private CheckBox cbAutoLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_activity_layout);

		btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
		btnRegister.setEnabled(false);

		etEmailAddress = (EditText)findViewById(R.id.etEmailAddress);
		etEmailAddress.setText(getIntent().getStringExtra(INTENT_KEY_EMAIL));
		etEmailAddress.setSelectAllOnFocus(true);

		etUsername = (EditText)findViewById(R.id.etUsername);
		etUsername.requestFocus();

		etPass1 = (EditText)findViewById(R.id.etPassword);
		etPass2 = (EditText)findViewById(R.id.etConfirmPassword);
		tvPasswordMatcher = (TextView)findViewById(R.id.tvPasswordMatcher);
		TextWatcher textWatcher = new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				// TODO: Safely clear strings from memory using some char array
				String s1 = etPass1.getText().toString();
				String s2 = etPass2.getText().toString();
				if (!s1.isEmpty() && s1.compareTo(s2) == 0) {
					tvPasswordMatcher.setText(R.string.passwords_match);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.green));
					btnRegister.setEnabled(true);
				} else {
					tvPasswordMatcher.setText(R.string.passwords_do_not_match);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.red));
					btnRegister.setEnabled(false);
				}

				String username = etUsername.getText().toString();
				if (username.isEmpty()) {
					btnRegister.setEnabled(false);
				}

				String emailAddress = etEmailAddress.getText().toString();
				if (emailAddress.isEmpty()) {
					btnRegister.setEnabled(false);
				}
			}
		};

		etEmailAddress.addTextChangedListener(textWatcher);
		etUsername.addTextChangedListener(textWatcher);
		etPass1.addTextChangedListener(textWatcher);
		etPass2.addTextChangedListener(textWatcher);

		cbAutoLogin = (CheckBox)findViewById(R.id.cbAutoLogin);
		cbAutoLogin.setChecked(getIntent().getBooleanExtra(INTENT_KEY_AUTOLOGIN, false));
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnRegister:
				String email = etEmailAddress.getText().toString();
				String password = etPass1.getText().toString();
				String confirmPassword = etPass2.getText().toString();
				String username = etUsername.getText().toString();

				assert password.compareTo(confirmPassword) == 0;
				registerAccount(email, password, username);
				break;
		}
	}

	private void registerAccount(final String email, final String password, final String name) {
		final ProgressDialog loadingDialog = new ProgressDialog(this);
		loadingDialog.setTitle(getString(R.string.dialog_register_load_title));
		loadingDialog.setMessage(getString(R.string.dialog_register_load));
		loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadingDialog.setCancelable(true);
		loadingDialog.show();

		String device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

		RequestParams params = new RequestParams();
		params.put(PHP_PARAM_EMAIL, email);
		params.put(PHP_PARAM_PASSWORD, password);
		params.put(PHP_PARAM_DEVICEID, device_id);
		params.put(PHP_PARAM_NAME, name);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(PHP_BASE_ADDRESS + "register.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				if (!loadingDialog.isShowing()) {
					return;
				}

				loadingDialog.dismiss();

				int id = NO_USER;
				try {
					id = Integer.parseInt(response);
				} catch (NumberFormatException e) {
					id = NO_USER;
				} finally {
					if (id == NO_USER) {
						AlertDialog.Builder errorDialog = new AlertDialog.Builder(RegistrationActivity.this);
						errorDialog.setTitle(R.string.dialog_register_error_title);
						errorDialog.setMessage(R.string.dialog_register_error);
						errorDialog.setPositiveButton(R.string.registration_action_forgot_password, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								RecoveryActivity.recoverAccount(RegistrationActivity.this, email);
							}
						});

						errorDialog.setNegativeButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});

						errorDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								//...
							}
						});

						errorDialog.show();
					} else {
						Intent i = new Intent();
						i.putExtra(INTENT_KEY_USERID, id);
						i.putExtra(INTENT_KEY_EMAIL, email);
						i.putExtra(INTENT_KEY_NAME, name);
						i.putExtra(INTENT_KEY_AUTOLOGIN, cbAutoLogin.isChecked());
						setResult(RESULT_OK, i);
						finish();
					}
				}
			}
		});
	}
}
