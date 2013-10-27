package edu.csupomona.cs.cs356.classmate.utils;

public class MenuItemModel {
	protected int title;
	protected CharSequence titleString;
	protected int icon;
	protected int counter;
	protected boolean isHeader;

	public MenuItemModel(int title, int icon) {
		this(title, icon, false);
	}

	public MenuItemModel(int title, int icon, boolean header) {
		this(title, icon, header, 0);
	}

	public MenuItemModel(int title, int icon, boolean header, int counter) {
		this.title = title;
		this.icon = icon;
		this.isHeader = header;
		this.counter = counter;
	}

	public MenuItemModel(CharSequence title, int icon) {
		this(title, icon, false);
	}

	public MenuItemModel(CharSequence title, int icon, boolean header) {
		this(title, icon, header, 0);
	}

	public MenuItemModel(CharSequence title, int icon, boolean header, int counter) {
		this.titleString = title;
		this.icon = icon;
		this.isHeader = header;
		this.counter = counter;
	}

	public int getTitleRes() {
		return title;
	}

	public void setTitleRes(int title) {
		this.title = title;
	}

	public CharSequence getTitle() {
		return titleString;
	}

	public void setTitleRes(CharSequence title) {
		this.titleString = title;
	}

	public int getIconRes() {
		return icon;
	}

	public void setIconRes(int icon) {
		this.icon = icon;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean b) {
		isHeader = b;
	}
}
