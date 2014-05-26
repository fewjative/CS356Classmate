package edu.csupomona.classmate.fragments.books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.BookComment;
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
				System.out.println("Commenter: " + f.getCommenter());
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
