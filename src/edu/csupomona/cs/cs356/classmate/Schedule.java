package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Schedule extends Activity {

	private String[] optionList;
	private ListView listView;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private ArrayAdapter<String> aAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);

		optionList = getResources().getStringArray(R.array.optionsArray);
		listView = (ListView)findViewById(R.id.left_drawer);
		drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

		aAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item_layout, optionList);
		listView.setAdapter(aAdapter);

		drawerToggle = new ActionBarDrawerToggle(
			this,
			drawerLayout,
			R.drawable.ic_schedule,
			R.string.drawerOpen,
			R.string.drawerClose
		);

		drawerLayout.setDrawerListener(drawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
