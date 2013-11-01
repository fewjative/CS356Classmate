package edu.csupomona.cs.cs356.classmate.utils;

public class MenuItemModel {
	int title;
	CharSequence titleString;
	int icon;
	int counter;
	boolean isHeader;
	boolean isMasterHeader;

	public MenuItemModel(int title, int icon) {
		this(title, icon, false, false);
	}

	public MenuItemModel(int title, int icon, boolean header, boolean master) {
		this(title, icon, header, master, 0);
	}

	public MenuItemModel(int title, int icon, boolean header, boolean master, int counter) {
		this.title = title;
		this.icon = icon;
		this.isHeader = header;
		this.isMasterHeader = master;
		this.counter = counter;
	}

	public MenuItemModel(CharSequence title, int icon) {
		this(title, icon, false, false);
	}

	public MenuItemModel(CharSequence title, int icon, boolean header, boolean master) {
		this(title, icon, header, master, 0);
	}

	public MenuItemModel(CharSequence title, int icon, boolean header, boolean master, int counter) {
		this.titleString = title;
		this.icon = icon;
		this.isHeader = header;
		this.isMasterHeader = master;
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

	public boolean isMasterHeader() {
		return isMasterHeader;
	}

	public void setMasterHeader(boolean b) {
		isMasterHeader = b;
	}
}
