package edu.csupomona.cs.cs356.classmate.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;
//allow for a user to upload a photo/choose a photo for their profile picture
//allow a user to make multiple schedules and choose the schedule from the settings fragment

public class SettingsFragment extends Fragment implements View.OnClickListener {
	private Button btnChangePass;
	private EditText etOldPass;
	private EditText etNewPass1;
	private EditText etNewPass2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.settings_fragment, null);

		btnChangePass = (Button)root.findViewById(R.id.btnChangePass);
		btnChangePass.setOnClickListener(this);
		btnChangePass.setEnabled(false);

		etOldPass = (EditText)root.findViewById(R.id.etOldPass);
		etNewPass1 = (EditText)root.findViewById(R.id.etPassword);
		etNewPass2 = (EditText)root.findViewById(R.id.etConfirmPassword);

		final TextView tvPasswordMatcher = (TextView)root.findViewById(R.id.tvPasswordMatcher);
		TextWatcherAdapter textWatcher = new TextWatcherAdapter() {
			String s1, s2, oldpass;

			@Override
			public void afterTextChanged(Editable e) {
				s1 = etNewPass1.getText().toString();
				s2 = etNewPass2.getText().toString();
				if (!s1.isEmpty() && s1.compareTo(s2) == 0) {
					tvPasswordMatcher.setText(R.string.passwordsmatch);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.green));
					btnChangePass.setEnabled(true);
				} else {
					tvPasswordMatcher.setText(R.string.passwordsdonotmatch);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.red));
					btnChangePass.setEnabled(false);
				}

				oldpass = etOldPass.getText().toString();
				if (oldpass.isEmpty()) {
					btnChangePass.setEnabled(false);
				}

				// TODO: Safely clear strings from memory using some char array
			}
		};

		etOldPass.addTextChangedListener(textWatcher);
		etNewPass1.addTextChangedListener(textWatcher);
		etNewPass2.addTextChangedListener(textWatcher);

		return root;
	}

	public void onClick(View v) {
		assert etNewPass1.getText().toString().compareTo(etNewPass2.getText().toString()) == 0;

		final ProgressDialog loadingDialog = new ProgressDialog(getActivity());
		loadingDialog.setTitle(getString(R.string.changePass));
		loadingDialog.setMessage(getString(R.string.changePassLoading));
		loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		//loadingDialog.setCancelable(true);
		loadingDialog.show();

		String newpassword = etNewPass1.getText().toString();
		String oldpassword = etOldPass.getText().toString();

		RequestParams params = new RequestParams();
		final int id = getActivity().getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);

		params.put("user_id", Integer.toString(id));
		params.put("oldpassword", oldpassword);
		params.put("newpassword", newpassword);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/changepass.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				if (!loadingDialog.isShowing()) {
					return;
				}

				loadingDialog.dismiss();

				int id;
				try {
					id = Integer.parseInt(response);
				} catch (NumberFormatException e) {
					id = NULL_USER;
				}

				if (id <= NULL_USER) {
					AlertDialog d = new AlertDialog.Builder(getActivity()).create();
					d.setTitle("Error With Changing Your Password");
					d.setMessage("Please double check to make sure you have input all the forms correctly");
					d.setIcon(android.R.drawable.ic_dialog_alert);
					d.setOnCancelListener(new DialogInterface.OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
                                                  // setResult(0);
							//finish();
						}
					});

					d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
                                                        //setResult(RESULT_CANCELED);
							//finish();
						}
					});

					d.show();
					return;
				} else {
					AlertDialog d = new AlertDialog.Builder(getActivity()).create();
					d.setTitle("Success!");
					d.setMessage("Your password has been successfully changed!");
					d.setIcon(android.R.drawable.ic_dialog_alert);
					d.setOnCancelListener(new DialogInterface.OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
                                                  // setResult(0);
							//finish();
						}
					});

					d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
                                                        //setResult(RESULT_CANCELED);
							//finish();
						}
					});

					d.show();
					etNewPass1.setText("");
					etNewPass2.setText("");
					etOldPass.setText("");
					return;
				}

				/*        Intent i = new Intent();
				 i.putExtra(LoginActivity.INTENT_KEY_USERID, id);
				 i.putExtra(LoginActivity.INTENT_KEY_EMAIL, emailAddress);
				 i.putExtra(LoginActivity.INTENT_KEY_USERNAME, username);
				 i.putExtra(LoginActivity.INTENT_KEY_REMEMBER, cbRememberMe.isChecked());
				 setResult(RESULT_OK, i);
				 finish();
				 */
			}
		});
	}
}
