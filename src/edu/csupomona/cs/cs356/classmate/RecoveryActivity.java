package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.cs.cs356.classmate.Constants.INTENT_KEY_EMAIL;
import static edu.csupomona.cs.cs356.classmate.Constants.PHP_BASE_ADDRESS;
import static edu.csupomona.cs.cs356.classmate.Constants.PHP_PARAM_EMAIL;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

public class RecoveryActivity extends Activity implements View.OnClickListener {
	private Button btnRecover;
	private EditText etEmailAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recovery_activity_layout);

		etEmailAddress = (EditText)findViewById(R.id.etEmailAddress);
		etEmailAddress.setText(getIntent().getStringExtra(INTENT_KEY_EMAIL));
		etEmailAddress.setSelectAllOnFocus(true);

		btnRecover = (Button)findViewById(R.id.btnRecover);
		btnRecover.setEnabled(etEmailAddress.getText().length() != 0);
		btnRecover.setOnClickListener(this);

		etEmailAddress.requestFocus();
		etEmailAddress.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				btnRecover.setEnabled(e.length() != 0);
			}
		});
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnRecover:
				String email = etEmailAddress.getText().toString();
				recoverAccount(email);
				break;
		}
	}

	private void recoverAccount(final String email) {
		RecoveryActivity.recoverAccount(this, email);
	}

	static void recoverAccount(final Activity parent, final String email) {
		final ProgressDialog loadingDialog = new ProgressDialog(parent);
		//loadingDialog.setTitle(parent.getString(R.string.dialog_login_load_title));
		loadingDialog.setMessage(parent.getString(R.string.dialog_login_load));
		loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadingDialog.setCancelable(true);
		loadingDialog.show();

		RequestParams params = new RequestParams();
		params.put(PHP_PARAM_EMAIL, email);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(PHP_BASE_ADDRESS + "recover.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String result) {
				if (!loadingDialog.isShowing()) {
					return;
				}

				loadingDialog.dismiss();

				boolean emailExists = false;
				try {
					int value = Integer.parseInt(result);
					emailExists = value == 2;
				} catch (NumberFormatException e) {
					emailExists = false;
				} finally {
					AlertDialog.Builder resultDialog = new AlertDialog.Builder(parent);
					if (emailExists) {
						resultDialog.setTitle(R.string.dialog_recovery_success_title);
						resultDialog.setMessage(R.string.dialog_recovery_success);
						resultDialog.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								parent.setResult(Activity.RESULT_CANCELED);
								parent.finish();
							}
						});

						resultDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								parent.setResult(Activity.RESULT_CANCELED);
								parent.finish();
							}
						});
					} else {
						resultDialog.setTitle(R.string.dialog_recovery_error_title);
						resultDialog.setMessage(R.string.dialog_recovery_error);
						resultDialog.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								parent.setResult(Activity.RESULT_CANCELED);
							}
						});

						resultDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								parent.setResult(Activity.RESULT_CANCELED);
							}
						});
					}

					resultDialog.show();
				}
			}
		});
	}
}
