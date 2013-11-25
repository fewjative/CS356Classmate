package edu.csupomona.cs.cs356.classmate.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import edu.csupomona.cs.cs356.classmate.R;

public class ItemDivider extends DrawerListItem {
	public ItemDivider() {
		super("");
	}

	@Override
	View getView(Context c, View convertView) {
		View v = convertView;
		if (v != null) {
			return v;
		}

		v = LayoutInflater.from(c).inflate(R.layout.drawer_item_item_divider_layout, null);
		return v;
	}
}
