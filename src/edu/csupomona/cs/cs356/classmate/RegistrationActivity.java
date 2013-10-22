package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import static android.app.Activity.RESULT_CANCELED;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.cs.cs356.classmate.LoginActivity.NULL_USER;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

public class RegistrationActivity extends Activity implements OnClickListener {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.registration_activity);

		final Button btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
		btnRegister.setEnabled(false);

		final EditText etUserName = (EditText)findViewById(R.id.etUserName);
		etUserName.setText(getIntent().getExtras().getString(LoginActivity.KEY_USERNAME, ""));
		etUserName.setSelectAllOnFocus(true);

		final EditText pass1 = (EditText)findViewById(R.id.etPassword);
		final EditText pass2 = (EditText)findViewById(R.id.etConfirmPassword);
		final TextView tvPasswordMatcher = (TextView)findViewById(R.id.tvPasswordMatcher);
		TextWatcher textWatcher = new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable s) {
				String s1, s2;
				/*try {
				 MessageDigest md = MessageDigest.getInstance("SHA-256");
				 md.update(pass1.getText().toString().getBytes());

				 byte[] byteData1 = md.digest();
				 StringBuilder sb1 = new StringBuilder();
				 for (byte b : byteData1) {
				 sb1.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
				 }

				 byte[] byteData2 = md.digest();
				 StringBuilder sb2 = new StringBuilder();
				 for (byte b : byteData2) {
				 sb2.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
				 }

				 s1 = sb1.toString();
				 s2 = sb2.toString();
				 } catch (NoSuchAlgorithmException e) {
				 s1 = pass1.getText().toString();
				 s2 = pass1.getText().toString();
				 } finally {
				 }*/

				s1 = pass1.getText().toString();
				s2 = pass2.getText().toString();
				String userName = etUserName.getText().toString();
				if (!userName.isEmpty() && !s1.isEmpty() && s1.compareTo(s2) == 0) {
					tvPasswordMatcher.setText(R.string.passwordsmatch);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.green));
					btnRegister.setEnabled(true);
				} else {
					tvPasswordMatcher.setText(R.string.passwordsdonotmatch);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.red));
					btnRegister.setEnabled(false);
				}

				// TODO: Safely clear strings from memory using some char array
				s1 = null;
				s2 = null;
			}
		};

		pass1.addTextChangedListener(textWatcher);
		pass2.addTextChangedListener(textWatcher);
	}

	public void onClick(View v) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		EditText etUserName = (EditText)findViewById(R.id.etUserName);
		final String userName = etUserName.getText().toString();

		EditText etPassword = (EditText)findViewById(R.id.etPassword);
		String password = etPassword.getText().toString();

		params.put("email", userName);
		params.put("password", password);
		client.get("http://www.lol-fc.com/classmate/register.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				int id = Integer.parseInt(response);
				if (NULL_USER < id) {
					CheckBox cbRememberMe = (CheckBox)findViewById(R.id.cbRememberMe);

					Intent i = new Intent();
					i.putExtra(LoginActivity.KEY_USERID, id);
					i.putExtra(LoginActivity.KEY_USERNAME, userName);
					i.putExtra(LoginActivity.KEY_REMEMBERME, cbRememberMe.isChecked());
					setResult(RESULT_OK, i);
					finish();
				} else {
					AlertDialog d = new AlertDialog.Builder(RegistrationActivity.this).create();
					d.setTitle(R.string.registerErrorTitle);
					d.setMessage(getResources().getString(R.string.registerError));
					d.setIcon(android.R.drawable.ic_dialog_alert);
					d.setOnCancelListener(new DialogInterface.OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
							setResult(RESULT_CANCELED);
						}
					});

					d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							setResult(RESULT_CANCELED);
						}
					});

					d.show();
				}

			}
		});
	}
}
