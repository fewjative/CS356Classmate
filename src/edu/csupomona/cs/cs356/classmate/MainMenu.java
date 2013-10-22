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
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
			case R.id.btnSchedule:
				i = new Intent(this, Schedule.class);
				startActivity(i);
				break;
		}
	}
}
