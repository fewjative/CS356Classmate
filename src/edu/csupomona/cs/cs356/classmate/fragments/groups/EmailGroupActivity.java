package edu.csupomona.cs.cs356.classmate.fragments.groups;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.Group;
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import static edu.csupomona.cs.cs356.classmate.fragments.GroupsFragment.INTENT_KEY_GROUP;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

public class EmailGroupActivity extends Activity implements View.OnClickListener {
	private EditText etEmailSubject;
	private EditText etEmailMessage;
	private Button btnSendEmail;

	private ListView lvSearchResults;
	private LinearLayout llProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_group_activity);

		final Group g = getIntent().getParcelableExtra(INTENT_KEY_GROUP);

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
					tvSubjMessage.setText("You are ready to send the email!");
					tvSubjMessage.setTextColor(getResources().getColor(R.color.green));
					btnSendEmail.setEnabled(true);
				} else {
					tvSubjMessage.setText("Subject and Message Required");
					tvSubjMessage.setTextColor(getResources().getColor(R.color.red));
					btnSendEmail.setEnabled(false);
				}
			}
		};

		etEmailSubject.addTextChangedListener(textWatcher);
		etEmailMessage.addTextChangedListener(textWatcher);

		/*
		etFriendName = (EditText)findViewById(R.id.etFriendName);
		etFriendName.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				if (e.length() == 0) {
					return;
				}

				llProgressBar.setVisibility(View.VISIBLE);
				lvSearchResults.setAdapter(null);

				RequestParams params = new RequestParams();
				params.put("search", e.toString());
				params.put("group_id", Integer.toString(g.getID()));

				AsyncHttpClient client = new AsyncHttpClient();
				client.get("http://www.lol-fc.com/classmate/searchfriends.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						List<Friend> search_results = new ArrayList<Friend>();
						if (1 < response.length()) {
							try {
								JSONObject jObj;
								JSONArray myjsonarray = new JSONArray(response);
								for (int i = 0; i < myjsonarray.length(); i++) {
									jObj = myjsonarray.getJSONObject(i);
									search_results.add(new Friend(
										jObj.getInt("user_id"),
										jObj.getString("username"),
										jObj.getString("email")
									));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						//AddMemberAdapter adapter = new AddMemberAdapter(EmailGroupActivity.this, g, search_results);
						//lvSearchResults.setAdapter(adapter);
						llProgressBar.setVisibility(View.GONE);
					}
				});
			}
		});*/
	}

	public void onClick(View v) {
		//assert etEmailSubject.getText().toString().compareTo(etPass2.getText().toString()) == 0;

		final ProgressDialog pg = ProgressDialog.show(this, getResources().getString(R.string.register), getResources().getString(R.string.registerLoading));

		final String subject = etEmailSubject.getText().toString();
		final String message = etEmailMessage.getText().toString();

		final Group g = getIntent().getParcelableExtra(INTENT_KEY_GROUP);

		RequestParams params = new RequestParams();
		params.put("group_id", Integer.toString(g.getID()));
		params.put("subject", subject);
		params.put("message", message);


		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/emailgroup.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				pg.dismiss();

				int id;
				try {
					id = Integer.parseInt(response);
				} catch (NumberFormatException e) {
					id = NULL_USER;
				}

				if (id > 0) {
					AlertDialog d = new AlertDialog.Builder(EmailGroupActivity.this).create();
					d.setTitle("Email Sent");
					d.setMessage("The email to the group was successfully sent!");
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



				/*Intent i = new Intent();
				i.putExtra(LoginActivity.INTENT_KEY_USERID, id);
				i.putExtra(LoginActivity.INTENT_KEY_EMAIL, emailAddress);
				i.putExtra(LoginActivity.INTENT_KEY_USERNAME, username);
				i.putExtra(LoginActivity.INTENT_KEY_REMEMBER, cbRememberMe.isChecked());
				setResult(RESULT_OK, i);*/
				finish();
			}
		});
	}
}