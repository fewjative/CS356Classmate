package edu.csupomona.classmate.fragments.friends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;
import java.util.List;

public class SearchUsersAdapter extends ArrayAdapter<User> implements View.OnClickListener {
	private final User USER;
	private final View ATTACHED_VIEW;

	private boolean lock;

	public SearchUsersAdapter(Context context, User user, List<User> users, View attached) {
		super(context, 0, users);
		this.USER = user;
		this.ATTACHED_VIEW = attached;
	}

	private static class ViewHolder {
		final ImageView ivAvatar;
		final TextView tvItemTextUsername;
		final ImageButton btnAdd;

		ViewHolder(ImageView ivAvatar, TextView tvItemTextUsername, ImageButton btnAdd) {
			this.ivAvatar = ivAvatar;
			this.tvItemTextUsername = tvItemTextUsername;
			this.btnAdd = btnAdd;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		User user = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		//if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.user_item_layout, null);

			ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
			TextView tvItemTextUsername = (TextView)view.findViewById(R.id.tvItemTextUsername);
			ImageButton btnAdd = (ImageButton)view.findViewById(R.id.btnAdd);
			view.setTag(new ViewHolder(ivAvatar, tvItemTextUsername, btnAdd));

			tvItemTextUsername.setSelected(true);

			btnAdd.setTag(user);
			btnAdd.setOnClickListener(this);
			btnAdd.setVisibility(View.VISIBLE);
		//}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (user != null && holder != null) {
			if (holder.tvItemTextUsername != null) {
				SpannableStringBuilder builder = new SpannableStringBuilder();

				SpannableString username = new SpannableString(user.getUsername());
				username.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(username);

				SpannableString email = new SpannableString(String.format(" (%s)", user.getEmail()));
				email.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.cppgold)), 0, email.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(email);

				holder.tvItemTextUsername.setText(builder);
			}

			if (holder.ivAvatar != null) {
				holder.ivAvatar.setVisibility(View.VISIBLE);
				user.loadAvatar(holder.ivAvatar);
			}
		}

		return view;
	}

	public void onClick(View v) {
		User u = (User)v.getTag();
		int id = v.getId();
		if (id == R.id.btnAdd) {
			if (lock) {
				return;
			}
			lock = true;
			sendRequest(u);
			InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ATTACHED_VIEW.getWindowToken(), 0);
			ATTACHED_VIEW.clearFocus();
		}
	}

	public void sendRequest(final User u) {
		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_EMAIL, USER.getEmail());
		params.put(Constants.PHP_PARAM_FRIEND, u.getEmail());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_REQUESTFRIEND, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(u);

				AlertDialog.Builder requestSentDialog = new AlertDialog.Builder(getContext());
				requestSentDialog.setTitle(R.string.dialog_friend_request_title);
				requestSentDialog.setMessage(getContext().getString(R.string.dialog_friend_request, u.getUsername()));
				requestSentDialog.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				requestSentDialog.show();
			}
		});
	}
}
