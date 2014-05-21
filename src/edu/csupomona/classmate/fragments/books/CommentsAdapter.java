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
import edu.csupomona.classmate.abstractions.BookComment;
import edu.csupomona.classmate.abstractions.User;

import java.util.List;

public class CommentsAdapter extends ArrayAdapter<BookComment> {
	
	public CommentsAdapter(Context context,List<BookComment> comments) {
		super(context,0,comments);
	}

	private static class ViewHolder {
		final ImageView ivAvatar;
		final TextView tvUsername;
		final TextView tvComment;

		ViewHolder(ImageView ivAvatar,  TextView tvUsername,TextView tvComment) {
			this.ivAvatar = ivAvatar;
			this.tvComment = tvComment;
			this.tvUsername = tvUsername;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BookComment f = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.books_comments_adapter_layout, null);

			ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
			TextView tvUsername = (TextView)view.findViewById(R.id.tvUsername);
			TextView tvComment = (TextView)view.findViewById(R.id.tvComment);
	
			ImageButton btnViewBook = (ImageButton)view.findViewById(R.id.btnViewBook);
			
			view.setTag(new ViewHolder(ivAvatar, tvUsername,tvComment));

			tvUsername.setSelected(true);//why?
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (f != null && holder != null) {
			if(holder.tvUsername !=null)
			{
				holder.tvUsername.setText(f.getCommenter());
			}
			
			if(holder.tvComment !=null)
			{
				holder.tvComment.setText(f.getComment());
			}
			

			if (holder.ivAvatar != null) {
				holder.ivAvatar.setVisibility(View.VISIBLE);
				f.loadAvatar(holder.ivAvatar);
			}
		}

		return view;
	}

}
