package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

public class RecoveryActivity extends Activity implements OnClickListener {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.recovery_activity);

		final Button btnRecover = (Button)findViewById(R.id.btnRecover);
		btnRecover.setOnClickListener(this);
		btnRecover.setEnabled(false);

		EditText etUserName = (EditText)findViewById(R.id.etUserName);
		etUserName.setText(getIntent().getExtras().getString(LoginActivity.KEY_USERNAME));
		etUserName.setSelectAllOnFocus(true);
		etUserName.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable s) {
				String userName = s.toString();
				btnRecover.setEnabled(!userName.isEmpty());
			}
		});
	}

	public void onClick(View v) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		EditText etUserName = (EditText)findViewById(R.id.etUserName);
		String userName = etUserName.getText().toString();
		params.put("email", userName);

		client.get("http://www.lol-fc.com/classmate/email.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				AlertDialog d = new AlertDialog.Builder(RecoveryActivity.this).create();
				d.setTitle(R.string.recover);
				if (Integer.parseInt(response) == 2) {
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
