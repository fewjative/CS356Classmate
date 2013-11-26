package edu.csupomona.cs.cs356.classmate.drawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class DrawerListItem {
	abstract View getView(Context c, View convertView, ViewGroup parent);

	String title;

	public DrawerListItem(String title) {
		this.title = title;
	}
}
