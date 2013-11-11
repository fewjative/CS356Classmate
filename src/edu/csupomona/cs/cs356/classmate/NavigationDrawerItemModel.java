package edu.csupomona.cs.cs356.classmate;

public class NavigationDrawerItemModel {
	private int title;
	private CharSequence titleString;
	private int icon;
	private int counter;
	private boolean isHeader;
	private boolean isMasterHeader;

	public NavigationDrawerItemModel(int title, int icon) {
		this(title, icon, false, false);
	}

	public NavigationDrawerItemModel(int title, int icon, boolean header, boolean master) {
		this(title, icon, header, master, 0);
	}

	public NavigationDrawerItemModel(int title, int icon, boolean header, boolean master, int counter) {
		this.title = title;
		this.icon = icon;
		this.isHeader = header;
		this.isMasterHeader = master;
		this.counter = counter;
	}

	public NavigationDrawerItemModel(CharSequence title, int icon) {
		this(title, icon, false, false);
	}

	public NavigationDrawerItemModel(CharSequence title, int icon, boolean header, boolean master) {
		this(title, icon, header, master, 0);
	}

	public NavigationDrawerItemModel(CharSequence title, int icon, boolean header, boolean master, int counter) {
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