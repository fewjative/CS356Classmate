package edu.csupomona.cs.cs356.classmate.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.csupomona.cs.cs356.classmate.R;

public class UserHeader extends DrawerListItem {
	int iconResId;

	public UserHeader(String title, int iconResId) {
		super(title);
		this.iconResId = iconResId;
	}

	@Override
	View getView(Context c, View convertView) {
		View v = convertView;
		if (v != null) {
			return v;
		}

		v = LayoutInflater.from(c).inflate(R.layout.drawer_item_userheader_layout, null);
		TextView tvTitle = (TextView)v.findViewById(R.id.tvTitle);
		tvTitle.setText(title);

		ImageView ivAvatar = (ImageView)v.findViewById(R.id.ivAvatar);
		if (0 < iconResId) {
			ivAvatar.setVisibility(View.VISIBLE);
			ivAvatar.setImageResource(iconResId);
		} else {
			ivAvatar.setVisibility(View.GONE);
		}

		return v;
	}
}
