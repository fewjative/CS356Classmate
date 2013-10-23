package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainMenu extends Activity implements OnClickListener{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);

		ImageButton btnSchedule = (ImageButton)findViewById(R.id.btnSchedule);
		btnSchedule.setOnClickListener(this);
		
		ImageButton btnAddClass = (ImageButton)findViewById(R.id.btnAddClass);
		btnAddClass.setOnClickListener(this);
		
		ImageButton btnSearchClass = (ImageButton)findViewById(R.id.btnSearchClasses);
		btnSearchClass.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
			case R.id.btnSchedule:
				i = new Intent(this, Schedule.class);
				i.putExtra(LoginActivity.KEY_USERNAME, getIntent().getExtras().getString(LoginActivity.KEY_USERNAME));
				startActivity(i);
				break;
				
			case R.id.btnAddClass:
				i = new Intent(this, AddClassActivity.class);
				i.putExtra(LoginActivity.KEY_USERNAME, getIntent().getExtras().getString(LoginActivity.KEY_USERNAME));
				startActivity(i);
				break;
				
			case R.id.btnSearchClasses:
				i = new Intent(this, SearchClassActivity.class);
				i.putExtra(LoginActivity.KEY_USERNAME, getIntent().getExtras().getString(LoginActivity.KEY_USERNAME));
				startActivity(i);
		}
	}
}
