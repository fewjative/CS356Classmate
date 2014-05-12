package edu.csupomona.classmate.fragments.addclassevent;

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
import edu.csupomona.classmate.abstractions.Event;
import edu.csupomona.classmate.abstractions.User;

import java.util.List;

public class SearchEventsAdapter extends ArrayAdapter<Event> implements View.OnClickListener {
	private final User USER;
	private final View ATTACHED_VIEW;

	private boolean lock;

	public SearchEventsAdapter(Context context, User user, List<Event> events, View attached) {
		super(context, 0, events);
		this.USER = user;
		this.ATTACHED_VIEW = attached;
	}

	private static class ViewHolder {
		final TextView tvSnippit;
		final ImageButton btnAdd;

		ViewHolder(TextView tvSnippit, ImageButton btnAdd) {
			this.tvSnippit = tvSnippit;
			this.btnAdd = btnAdd;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Event event = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.user_item_layout, null);

			TextView tvSnippit = (TextView)view.findViewById(R.id.tvSnippit);
			ImageButton btnAdd = (ImageButton)view.findViewById(R.id.btnAdd);
			view.setTag(new ViewHolder(tvSnippit, btnAdd));

			tvSnippit.setSelected(true);

			btnAdd.setTag(event);
			btnAdd.setOnClickListener(this);
			btnAdd.setVisibility(View.VISIBLE);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (event != null && holder != null) {
			if (holder.tvSnippit != null) {
				SpannableStringBuilder builder = new SpannableStringBuilder();

				SpannableString snippit = new SpannableString(event.getFullTime());
				snippit.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, event.getFullTime().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(snippit);
				
				holder.tvSnippit.setText(builder);
			}
		}

		return view;
	}

	public void onClick(View v) {
		Event e = (Event)v.getTag();
		switch (v.getId()) {
			case R.id.btnAdd:
				if (lock) {
					return;
				}

				lock = true;
				sendRequest(e);//add the event to our schedule

				InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(ATTACHED_VIEW.getWindowToken(), 0);
				ATTACHED_VIEW.clearFocus();
				break;
		}
	}

	public void sendRequest(final Event e) {
		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_USERID, Long.toString(USER.getID()));
		params.put(Constants.PHP_PARAM_EVENTID, Integer.toString(e.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_ADDEVENT, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(e);

				AlertDialog.Builder requestSentDialog = new AlertDialog.Builder(getContext());
				requestSentDialog.setTitle(R.string.dialog_event_acceptance_title);
				requestSentDialog.setMessage(getContext().getString(R.string.dialog_event_acceptance, e.getTitle()));
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
