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
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

public class RecoveryActivity extends Activity implements View.OnClickListener {
	private Button btnRecover;
	private EditText etEmailAddress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recovery_activity);

		btnRecover = (Button)findViewById(R.id.btnRecover);
		btnRecover.setOnClickListener(this);

		etEmailAddress = (EditText)findViewById(R.id.etEmailAddress);
		etEmailAddress.setText(getIntent().getExtras().getString(LoginActivity.INTENT_KEY_EMAIL, ""));
		btnRecover.setEnabled(etEmailAddress.getText().length() != 0);
		etEmailAddress.setSelectAllOnFocus(true);
		etEmailAddress.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				btnRecover.setEnabled(e.length() != 0);
			}
		});
	}

	public void onClick(View v) {
		final ProgressDialog pg = ProgressDialog.show(this, getResources().getString(R.string.recover), getResources().getString(R.string.loginLoading));

		String emailAddress = etEmailAddress.getText().toString();

		RequestParams params = new RequestParams();
		params.put("email", emailAddress);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/email.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				pg.dismiss();

				int result;
				try {
					result = Integer.parseInt(response);
				} catch (NumberFormatException e) {
					result = NULL_USER;
				}

				AlertDialog d = new AlertDialog.Builder(RecoveryActivity.this).create();
				d.setTitle(R.string.recover);
				if (result == 2) {
					d.setMessage(getResources().getString(R.string.recoverSent));
					d.setIcon(android.R.drawable.ic_dialog_info);
					d.setOnCancelListener(new DialogInterface.OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
							setResult(RESULT_OK);
							finish();
						}
					});

					d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							setResult(RESULT_OK);
							finish();
						}
					});
				} else {
					d.setMessage(getResources().getString(R.string.recoverError));
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
				}

				d.show();
			}
		});

	}
}
