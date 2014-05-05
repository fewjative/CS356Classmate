package edu.csupomona.cs.cs356.classmate.fragments.friends;

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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.Constants;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.User;
import java.util.List;

public class FriendListAdapter extends ArrayAdapter<User> implements View.OnClickListener {
	private final User USER;

	public FriendListAdapter(Context context, User user, List<User> friends) {
		super(context, 0, friends);
		this.USER = user;
	}

	private static class ViewHolder {
		final ImageView ivAvatar;
		final TextView tvItemTextUsername;
		final ImageButton btnViewSchedule;
		final ImageButton btnRemove;

		ViewHolder(ImageView ivAvatar, TextView tvItemTextUsername, ImageButton btnViewSchedule, ImageButton btnRemove) {
			this.ivAvatar = ivAvatar;
			this.tvItemTextUsername = tvItemTextUsername;
			this.btnViewSchedule = btnViewSchedule;
			this.btnRemove = btnRemove;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		User f = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.friends_list_tab_item_layout, null);

			ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
			TextView tvItemTextUsername = (TextView)view.findViewById(R.id.tvItemTextUsername);
			ImageButton btnViewSchedule = (ImageButton)view.findViewById(R.id.btnViewSchedule);
			ImageButton btnRemove = (ImageButton)view.findViewById(R.id.btnRemove);
			view.setTag(new ViewHolder(ivAvatar, tvItemTextUsername, btnViewSchedule, btnRemove));

			tvItemTextUsername.setSelected(true);

			btnViewSchedule.setTag(f);
			btnViewSchedule.setOnClickListener(this);

			btnRemove.setTag(f);
			btnRemove.setOnClickListener(this);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (f != null && holder != null) {
			if (holder.tvItemTextUsername != null) {
				SpannableStringBuilder builder = new SpannableStringBuilder();

				SpannableString username = new SpannableString(f.getUsername());
				username.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(username);

				SpannableString email = new SpannableString(String.format(" (%s)", f.getEmail()));
				email.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.cppgold)), 0, email.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(email);

				holder.tvItemTextUsername.setText(builder);
			}

			if (holder.ivAvatar != null) {
				holder.ivAvatar.setVisibility(View.VISIBLE);
				f.loadAvatar(holder.ivAvatar);
			}
		}

		return view;
	}

	public void onClick(View v) {
		User r = (User)v.getTag();
		switch (v.getId()) {
			case R.id.btnViewSchedule:
				viewSchedule(r);
				break;
			case R.id.btnRemove:
				removeFriend(r);
				break;
		}
	}

	private void viewSchedule(final User f) {
		/*if (getContext() instanceof FragmentActivity) {
			// We can get the fragment manager
			int id = f.getID();
			Fragment newFragment = new edu.csupomona.cs.cs356.classmate.fragments.ScheduleFragment(id);
			FragmentActivity activity = ((FragmentActivity)getContext());
			FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();
			t.replace(R.id.flContentFrame, newFragment);//wtf is the first parameter supposed to be
			// t.replace(2131165220, newFragment);//wtf is the first parameter supposed to be
			//2131165220
			//2131165207
			t.addToBackStack(null);

			t.commit();
		}*/
	}

	private void removeFriend(final User f) {
		AlertDialog.Builder removeDialog = new AlertDialog.Builder(getContext());
		removeDialog.setTitle(R.string.dialog_friend_remove_title);
		removeDialog.setMessage(getContext().getResources().getString(R.string.dialog_friend_remove, getContext().getResources().getString(R.string.global_action_yes), getContext().getResources().getString(R.string.global_action_no)));
		removeDialog.setIcon(android.R.drawable.ic_dialog_info);
		removeDialog.setPositiveButton(R.string.global_action_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				RequestParams params = new RequestParams();
				params.put(Constants.PHP_PARAM_EMAIL, USER.getEmail());
				params.put(Constants.PHP_PARAM_USERID, Long.toString(f.getID()));
				params.put(Constants.PHP_PARAM_VERSION, "1");

				AsyncHttpClient client = new AsyncHttpClient();
				client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_REMOVEFRIEND, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						remove(f);
					}
				});
			}
		});

		removeDialog.setNegativeButton(R.string.global_action_no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				RequestParams params = new RequestParams();
				params.put(Constants.PHP_PARAM_EMAIL, USER.getEmail());
				params.put(Constants.PHP_PARAM_USERID, Long.toString(f.getID()));
				params.put(Constants.PHP_PARAM_VERSION, "2");

				AsyncHttpClient client = new AsyncHttpClient();
				client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_REMOVEFRIEND, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						remove(f);
					}
				});
			}
		});

		removeDialog.setNeutralButton(R.string.global_action_cancel, null);
		removeDialog.show();
	}
}
