package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener{

	private Button scheduleButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
		
		scheduleButton = (Button) findViewById(R.id.schedule_button);
		scheduleButton.setText("" + getIntent().getStringExtra("ID"));
	}
	
	private void goToSchedule(){
		Intent i = new Intent(this, Schedule.class);
		startActivity(i);
	}

	@Override
	public void onClick(DialogInterface event, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
