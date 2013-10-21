package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener{

	private Button scheduleButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
		
		scheduleButton = (Button) findViewById(R.id.schedule_button);
		scheduleButton.setText("" + getIntent().getStringExtra("ID"));
		scheduleButton.setOnClickListener(this);
	}
	
	private void goToSchedule(){
		Intent i = new Intent(this, Schedule.class);
		startActivity(i);
	}

	@Override
	public void onClick(View ae) {
		// TODO Auto-generated method stub
		if(ae == scheduleButton){
			goToSchedule();
		}
		
		
	}
}
