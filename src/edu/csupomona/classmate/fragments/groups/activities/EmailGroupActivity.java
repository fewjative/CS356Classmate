package edu.csupomona.classmate.fragments.groups.activities;

import android.app.Activity;
import static android.app.Activity.RESULT_CANCELED;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import static edu.csupomona.classmate.Constants.INTENT_KEY_GROUP;
import static edu.csupomona.classmate.Constants.NO_USER;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Group;
import edu.csupomona.classmate.utils.TextWatcherAdapter;

public class EmailGroupActivity extends Activity implements View.OnClickListener {
	private EditText etEmailSubject;
	private EditText etEmailMessage;
	private Button btnSendEmail;

	private ListView lvSearchResults;
	private LinearLayout llProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_group_activity_layout);

		final Group GROUP = getIntent().getParcelableExtra(INTENT_KEY_GROUP);

		llProgressBar = (LinearLayout)findViewById(R.id.llProgressBar);

		etEmailSubject = (EditText)findViewById(R.id.etEmailSubject);
		etEmailMessage = (EditText)findViewById(R.id.etEmailMessage);

		etEmailSubject.setSelectAllOnFocus(true);

		btnSendEmail = (Button)findViewById(R.id.btnSendEmail);
		btnSendEmail.setOnClickListener(this);
		btnSendEmail.setEnabled(false);


		final TextView tvSubjMessage = (TextView)findViewById(R.id.tvSubjMessage);

		TextWatcher textWatcher = new TextWatcherAdapter() {
			String subj,message;

			@Override
			public void afterTextChanged(Editable e) {
				subj = etEmailSubject.getText().toString();
				message = etEmailMessage.getText().toString();

				if (!subj.isEmpty() && !message.isEmpty()) {
					tvSubjMessage.setText(R.string.email_group_ready);
					tvSubjMessage.setTextColor(getResources().getColor(R.color.green));
					btnSendEmail.setEnabled(true);
				} else {
					tvSubjMessage.setText(R.string.email_group_missing_field);
					tvSubjMessage.setTextColor(getResources().getColor(R.color.red));
					btnSendEmail.setEnabled(false);
				}
			}
		};

		etEmailSubject.addTextChangedListener(textWatcher);
		etEmailMessage.addTextChangedListener(textWatcher);
	}

	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etEmailSubject.getWindowToken(), 0);
		//imm.hideSoftInputFromWindow(etEmailMessage.getWindowToken(), 0);

		final ProgressDialog sendingDialog = new ProgressDialog(this);
		//loadingDialog.setTitle(getString(R.string.dialog_email_group_sending_title));
		sendingDialog.setMessage(getString(R.string.dialog_email_group_sending));
		sendingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		//loadingDialog.setCancelable(true);
		sendingDialog.show();

		final String SUBJECT = etEmailSubject.getText().toString();
		final String MESSAGE = etEmailMessage.getText().toString();

		final Group GROUP = getIntent().getParcelableExtra(INTENT_KEY_GROUP);

		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_GROUPID, Long.toString(GROUP.getID()));
		params.put(Constants.PHP_PARAM_SUBJECT, SUBJECT);
		params.put(Constants.PHP_PARAM_MESSAGE, MESSAGE);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_EMAILGROUP, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				if(!sendingDialog.isShowing()) {
					return;
				}

				sendingDialog.dismiss();

				long id;
				try {
					id = Long.parseLong(response);
				} catch (NumberFormatException e) {
					id = NO_USER;
				}

				if (NO_USER < id) {
					AlertDialog.Builder d = new AlertDialog.Builder(EmailGroupActivity.this);
					d.setTitle(R.string.dialog_email_group_sent_title);
					d.setMessage(R.string.dialog_email_group_sent);
					d.setIcon(android.R.drawable.ic_dialog_alert);
					d.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							setResult(RESULT_CANCELED);
							finish();
						}
					});

					d.show();
					return;
				}

				finish();
			}
		});
	}
}
