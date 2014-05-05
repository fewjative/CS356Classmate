package edu.csupomona.cs.cs356.classmate.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.User;

public class UserHeader extends Header {
	private final User USER;

	public UserHeader(User user) {
		super(user.getUsername());
		this.USER = user;
	}

	@Override
	View getView(Context c, View convertView, ViewGroup parent) {
		View v = convertView;
		/*if (v != null) {
			return v;
		}*/

		v = LayoutInflater.from(c).inflate(R.layout.drawer_item_userheader_layout, parent, false);
		TextView tvTitle = (TextView)v.findViewById(R.id.tvTitle);
		tvTitle.setText(title);

		ImageView ivAvatar = (ImageView)v.findViewById(R.id.ivAvatar);
		USER.loadAvatar(ivAvatar);
		return v;
	}
}
