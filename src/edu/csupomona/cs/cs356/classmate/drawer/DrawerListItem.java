package edu.csupomona.cs.cs356.classmate.drawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

public abstract class DrawerListItem implements Checkable {
	abstract View getView(Context c, View convertView, ViewGroup parent);

	String title;

	public DrawerListItem(String title) {
		this.title = title;
	}

	public boolean isChecked() {
		return false;
	}

	public void setChecked(boolean checked) {
		//...
	}

	public void toggle() {
		//...
	}
}
