package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import static android.app.Activity.RESULT_OK;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

public class LoginActivity extends Activity implements OnClickListener {
	static final String KEY_USERNAME = "userName";

	private static final int CODE_REGISTERATION_FORM = 0x0f0f0f0f;
	private static final int CODE_RECOVERY_FORM = 0xf0f0f0f0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		Button b = (Button)findViewById(R.id.btnRecover);
		b.setOnClickListener(this);

		b = (Button)findViewById(R.id.btnRegister);
		b.setOnClickListener(this);

		final Button btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		btnLogin.setEnabled(false);

		final EditText etUserName = (EditText)findViewById(R.id.etUserName);
		final EditText etPassword = (EditText)findViewById(R.id.etPassword);
		TextWatcher tw = new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				String s = etUserName.getText().toString();
				if (s.isEmpty()) {
					btnLogin.setEnabled(false);
					return;
				}

				s = etPassword.getText().toString();
				if (s.isEmpty()) {
					btnLogin.setEnabled(false);
					return;
				}

				btnLogin.setEnabled(true);
			}
		};

		etUserName.addTextChangedListener(tw);
		etPassword.addTextChangedListener(tw);
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
			case R.id.btnLogin:
				verifyLogin();
				break;
			case R.id.btnRecover:
				String userName = ((EditText)findViewById(R.id.etUserName)).getText().toString();

				i = new Intent(this, RecoveryActivity.class);
				i.putExtra(KEY_USERNAME, userName);
				startActivityForResult(i, CODE_RECOVERY_FORM);
				break;
			case R.id.btnRegister:
				userName = ((EditText)findViewById(R.id.etUserName)).getText().toString();

				i = new Intent(this, RegistrationActivity.class);
				i.putExtra(KEY_USERNAME, userName);
				startActivityForResult(i, CODE_REGISTERATION_FORM);
				break;
		}
	}

	private void sendToMainMenu() {
		Intent i = new Intent(this, MainMenu.class);
		i.putExtra("ID", "1");
		startActivity(i);
	}

	private void verifyLogin() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		EditText etUserName = (EditText)findViewById(R.id.etUserName);
		String userName = etUserName.getText().toString();

		EditText etPassword = (EditText)findViewById(R.id.etPassword);
		String password = etPassword.getText().toString();

		System.out.println(userName);
		System.out.println(password);

		params.put("email", userName);
		params.put("password", password);
		client.get("http://www.lol-fc.com/classmate/login.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				if (Integer.parseInt(response) == 2) {
					sendToMainMenu();
				} else {
					AlertDialog d = new AlertDialog.Builder(LoginActivity.this).create();
					d.setTitle(R.string.loginErrorTitle);
					d.setMessage(getResources().getString(R.string.loginError));
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CODE_RECOVERY_FORM:
				if (resultCode != RESULT_OK) {
					break;
				}

				Toast.makeText(this, getResources().getString(R.string.recoverSent), Toast.LENGTH_LONG).show();
				break;
			case CODE_REGISTERATION_FORM:
				if (resultCode != RESULT_OK) {
					break;
				}

				sendToMainMenu();
				break;
		}
	}
}
