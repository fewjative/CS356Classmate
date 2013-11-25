package edu.csupomona.cs.cs356.classmate.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import edu.csupomona.cs.cs356.classmate.R;

public class Header extends DrawerListItem {
	public Header(String title) {
		super(title);
	}

	@Override
	View getView(Context c, View convertView) {
		View v = convertView;
		if (v != null) {
			return v;
		}

		v = LayoutInflater.from(c).inflate(R.layout.drawer_item_header_layout, null);
		TextView tvTitle = (TextView)v.findViewById(R.id.tvTitle);
		tvTitle.setText(title);

		return v;
	}
}
