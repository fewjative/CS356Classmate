package edu.csupomona.classmate.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.csupomona.classmate.R;

public class Header extends DrawerListItem {
	public Header(String title) {
		super(title);
	}

	@Override
	View getView(Context c, View convertView, ViewGroup parent) {
		View v = convertView;
		/*if (v != null) {
			return v;
		}*/

		v = LayoutInflater.from(c).inflate(R.layout.drawer_item_header_layout, parent, false);
		TextView tvTitle = (TextView)v.findViewById(R.id.tvTitle);
		tvTitle.setText(title);

		return v;
	}
}
