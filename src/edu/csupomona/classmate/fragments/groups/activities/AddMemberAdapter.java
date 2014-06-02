package edu.csupomona.classmate.fragments.groups.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import edu.csupomona.classmate.abstractions.Group;
import edu.csupomona.classmate.abstractions.User;
import java.util.List;

public class AddMemberAdapter extends ArrayAdapter<User> implements View.OnClickListener {
	private final Group GROUP;
	private final View ATTACHED_VIEW;

	private boolean lock;

	public AddMemberAdapter(Context context, Group group, List<User> friends, View attachedView) {
		super(context, 0, friends);
		this.GROUP = group;
		this.ATTACHED_VIEW = attachedView;
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
		switch (v.getId()) {
			case R.id.btnAdd:
				if (lock) {
					return;
				}

				lock = true;
				joinGroup(u);
				break;
		}
	}

	public void joinGroup(final User u) {
		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_GROUPID, Long.toString(GROUP.getID()));
		params.put(Constants.PHP_PARAM_EMAIL, u.getEmail());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_JOINGROUP, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(u);
				lock = false;

				Activity a = (Activity)getContext();
				InputMethodManager imm = (InputMethodManager)a.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(ATTACHED_VIEW.getWindowToken(), 0);
				ATTACHED_VIEW.clearFocus();

				Intent i = new Intent(getContext(), AddMemberAdapter.class);
				i.putExtra(Constants.INTENT_KEY_USER, u);
				a.setResult(Activity.RESULT_OK, i);
				a.finish();
			}
		});
	}
}