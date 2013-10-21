package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Schedule extends Activity {
	
	private String[] optionList;
	private ListView listView;
	private DrawerLayout drawerLayout;
	private ArrayAdapter aAdapter;
	
	public void onCreate(Bundle saveInstance){
		super.onCreate(saveInstance);
		//setContentView();
		optionList = getResources().getStringArray(R.array.optionsArray);
		listView = (ListView) findViewById(R.id.left_drawer);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	
		aAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionList);
	
		listView.setAdapter(aAdapter);
	}

}
