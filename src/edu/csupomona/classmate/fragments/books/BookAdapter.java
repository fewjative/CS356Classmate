package edu.csupomona.classmate.fragments.books;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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

import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Book;
import edu.csupomona.classmate.abstractions.User;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> implements View.OnClickListener {
	private final User USER;
	
	public BookAdapter(Context context, User user, List<Book> books) {
		super(context, 0, books);
		this.USER = user;
	}

	private static class ViewHolder {
		final ImageView ivAvatar;
		final TextView tvTitle;
		final TextView tvClassname;
		final TextView tvCondition;
		final TextView tvPrice;
		final ImageButton btnViewBook;

		ViewHolder(ImageView ivAvatar,  TextView tvTitle,TextView tvClassname,TextView tvCondition,TextView tvPrice, ImageButton btnViewBook) {
			this.ivAvatar = ivAvatar;
			this.tvTitle = tvTitle;
			this.tvClassname = tvClassname;
			this.tvCondition = tvCondition;
			this.tvPrice = tvPrice;
			this.btnViewBook = btnViewBook;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Book f = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.book_item_layout, null);

			ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
			TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
			TextView tvClassname = (TextView)view.findViewById(R.id.tvClassname);
			TextView tvCondition = (TextView)view.findViewById(R.id.tvCondition);
			TextView tvPrice = (TextView)view.findViewById(R.id.tvPrice);
			
			ImageButton btnViewBook = (ImageButton)view.findViewById(R.id.btnViewBook);
			
			view.setTag(new ViewHolder(ivAvatar, tvTitle,tvClassname,tvCondition,tvPrice, btnViewBook));

			tvTitle.setSelected(true);//why?

			btnViewBook.setTag(f);
			btnViewBook.setOnClickListener(this);
			btnViewBook.setVisibility(View.VISIBLE);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (f != null && holder != null) {
			if(holder.tvTitle !=null)
			{
				holder.tvTitle.setText(f.getTitle());
			}
			
			if(holder.tvClassname !=null)
			{
				holder.tvClassname.setText(f.getClassname());
			}
			
			if(holder.tvCondition !=null)
			{
				holder.tvCondition.setText(f.getCondition());
			}
			
			if(holder.tvPrice !=null)
			{
				holder.tvPrice.setText(f.getPrice());
			}
			
			if(holder.btnViewBook !=null)
			{
				holder.btnViewBook.setVisibility(View.VISIBLE);
			}
			
		/*	if (holder.tvItemTextUsername != null) {
				SpannableStringBuilder builder = new SpannableStringBuilder();

				SpannableString username = new SpannableString(f.getUsername());
				username.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(username);

				SpannableString email = new SpannableString(String.format(" (%s)", f.getEmail()));
				email.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.cppgold)), 0, email.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(email);

				holder.tvItemTextUsername.setText(builder);
			}*/

			if (holder.ivAvatar != null) {
				holder.ivAvatar.setVisibility(View.VISIBLE);
				f.loadAvatar(holder.ivAvatar);
			}
		}

		return view;
	}

	public void onClick(View v) {
		Book r = (Book)v.getTag();
		switch (v.getId()) {
			case R.id.btnViewBook:
				viewBook(r);
				break;
		}
	}

	private void viewBook(final Book b) {
		   if (getContext() instanceof FragmentActivity) {
               // We can get the fragment manager
                   long id = USER.getID();
               Fragment newFragment = new edu.csupomona.classmate.fragments.books.ViewBookFragment(id,b.getID(),b.getSellerID(),b.getTitle(),b.getClassname(),b.getCondition(),b.getPrice());
               FragmentActivity activity = ((FragmentActivity)getContext());
               FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();
               t.replace(R.id.flContentFrame, newFragment);//wtf is the first parameter supposed to be
               t.addToBackStack(null);

               t.commit();
           }
		/*if (getContext() instanceof FragmentActivity) {
			// We can get the fragment manager
			int id = f.getID();
			Fragment newFragment = new edu.csupomona.classmate.fragments.ScheduleFragment(id);
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
	
	/*
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
	}*/
}
