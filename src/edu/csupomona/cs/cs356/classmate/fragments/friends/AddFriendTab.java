package edu.csupomona.cs.cs356.classmate.fragments.friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

public class AddFriendTab extends Fragment implements View.OnClickListener {
	private Button btnAddFriend;
	private EditText etFriendName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.tab_friends_add, null);

		btnAddFriend = (Button)root.findViewById(R.id.btnAddFriend);
		btnAddFriend.setEnabled(false);
		btnAddFriend.setOnClickListener(this);

		etFriendName = (EditText)root.findViewById(R.id.etFriendName);
		etFriendName.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				btnAddFriend.setEnabled(e.length() != 0);
			}
		});

		return root;
	}

	public void onClick(View v) {
		String userName = getActivity().getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);
		String friend = etFriendName.getText().toString();

		RequestParams params = new RequestParams();
		params.put("email", userName);
		params.put("friend", friend);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/addfriend.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				int id;
				try {
					id = Integer.parseInt(response);
				} catch (NumberFormatException e) {
					id = 0;
				}

				AlertDialog d = new AlertDialog.Builder(getActivity()).create();
				if (id == 2) {

					d.setTitle(R.string.addFriendSuccessTitle);
					d.setMessage(getResources().getString(R.string.addFriendSuccess));
					d.setIcon(android.R.drawable.ic_dialog_info);
					d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							etFriendName.setText("");
						}
					});
				} else {
					d.setTitle(R.string.addFriendErrorTitle);
					d.setMessage(getResources().getString(R.string.addFriendError));
					d.setIcon(android.R.drawable.ic_dialog_alert);
					d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});

					d.show();
				}

				d.show();
			}
		});
	}
}