package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import static android.app.Activity.RESULT_CANCELED;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
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
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

public class RegistrationActivity extends Activity implements View.OnClickListener {
	private Button btnRegister;
	private EditText etUsername;
	private EditText etPass1;
	private EditText etPass2;

	private boolean attemptingRegister;
	private boolean registerPossible;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.registration_activity);

		btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
		btnRegister.setEnabled(false);

		etUsername = (EditText)findViewById(R.id.etUserName);
		etUsername.setText(getIntent().getExtras().getString(LoginActivity.INTENT_KEY_USERNAME, ""));
		etUsername.setSelectAllOnFocus(true);

		etPass1 = (EditText)findViewById(R.id.etPassword);
		etPass2 = (EditText)findViewById(R.id.etConfirmPassword);
		final TextView tvPasswordMatcher = (TextView)findViewById(R.id.tvPasswordMatcher);
		TextWatcher textWatcher = new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				String s1 = etPass1.getText().toString();
				String s2 = etPass2.getText().toString();
				if (!s1.isEmpty() && s1.compareTo(s2) == 0) {
					tvPasswordMatcher.setText(R.string.passwordsmatch);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.green));
					btnRegister.setEnabled(true);
				} else {
					tvPasswordMatcher.setText(R.string.passwordsdonotmatch);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.red));
					btnRegister.setEnabled(false);
				}

				String userName = etUsername.getText().toString();
				if (userName.isEmpty()) {
					btnRegister.setEnabled(false);
				}

				// TODO: Safely clear strings from memory using some char array
			}
		};

		etUsername.addTextChangedListener(textWatcher);
		etPass1.addTextChangedListener(textWatcher);
		etPass2.addTextChangedListener(textWatcher);
	}

	@Override
	public void onClick(View v) {
		assert etPass1.getText().toString().compareTo(etPass2.getText().toString()) == 0;

		final ProgressDialog pg = ProgressDialog.show(this, getResources().getString(R.string.register), getResources().getString(R.string.registerLoading));

		final String userName = etUsername.getText().toString();
		String password = etPass1.getText().toString();
		String device = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

		RequestParams params = new RequestParams();
		params.put("email", userName);
		params.put("password", password);
		params.put("device_id", device);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/register.php", params, new AsyncHttpResponseHandler() {
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
					AlertDialog d = new AlertDialog.Builder(RegistrationActivity.this).create();
					d.setTitle(R.string.registerErrorTitle);
					d.setMessage(getResources().getString(R.string.registerError));
					d.setIcon(android.R.drawable.ic_dialog_alert);
					d.setOnCancelListener(new DialogInterface.OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
							setResult(RESULT_CANCELED);
							finish();
						}
					});

					d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							setResult(RESULT_CANCELED);
							finish();
						}
					});

					d.show();
					return;
				}

				CheckBox cbRememberMe = (CheckBox)findViewById(R.id.cbRememberMe);

				Intent i = new Intent();
				i.putExtra(LoginActivity.INTENT_KEY_USERID, id);
				i.putExtra(LoginActivity.INTENT_KEY_USERNAME, userName);
				i.putExtra(LoginActivity.INTENT_KEY_REMEMBER, cbRememberMe.isChecked());
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}
}
