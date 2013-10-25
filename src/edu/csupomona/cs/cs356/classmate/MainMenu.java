package edu.csupomona.cs.cs356.classmate;

import android.app.ActionBar;
import android.app.Activity;
import static android.app.Activity.RESULT_CANCELED;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainMenu extends Activity implements OnClickListener{
	private static final int CODE_ADD_CLASS = 2001;
	private static final int CODE_SEARCH_CLASSES = 2002;

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

		Button btnFriendManager = (Button)findViewById(R.id.btnFriendManager);
		btnFriendManager.setOnClickListener(this);

		Button btnLogin = (Button)findViewById(R.id.btnLogout);
		btnLogin.setOnClickListener(this);

		ActionBar ab = getActionBar();
		ab.setTitle("What's Happening Today");

		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");
		ab.setSubtitle(df.format(c.getTime()));
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
				startActivityForResult(i, CODE_ADD_CLASS);
				break;
			case R.id.btnSearchClasses:
				i = new Intent(this, SearchClassActivity.class);
				startActivityForResult(i, CODE_SEARCH_CLASSES);
				break;
			case R.id.btnFriendManager:
				i = new Intent(this, FriendManagerActivity.class);
				i.putExtra(LoginActivity.KEY_USERNAME, getIntent().getExtras().getString(LoginActivity.KEY_USERNAME));
				startActivity(i);
				break;
			case R.id.btnLogout:
				AlertDialog d = new AlertDialog.Builder(this).create();
				d.setTitle(R.string.logoutConfirmationTitle);
				d.setMessage(getResources().getString(R.string.logoutConfirmation));
				d.setIcon(android.R.drawable.ic_dialog_info);
				d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						sendToLoginActivity();
					}
				});

				d.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						setResult(RESULT_CANCELED);
					}
				});

				d.show();
				break;
		}
	}

	private void sendToLoginActivity() {
		Intent i = new Intent(this, AddClassActivity.class);
		i.putExtra(LoginActivity.KEY_REMEMBERME, false);
		setResult(RESULT_OK, i);
		finish();
	}
}
