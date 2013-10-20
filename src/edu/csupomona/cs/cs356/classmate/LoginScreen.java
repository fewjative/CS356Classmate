package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LoginScreen extends Activity implements OnClickListener {
	private static final int REGISTERATION_FORM_CODE = 0x0F0F0F0F;
	private static final int RECOVERY_FORM_CODE = 0xF0F0F0F0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Button b = (Button)findViewById(R.id.btnLogin);
		b.setOnClickListener(this);

		b = (Button)findViewById(R.id.btnRecover);
		b.setOnClickListener(this);

		b = (Button)findViewById(R.id.btnRegister);
		b.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
			case R.id.btnLogin:
				sendToMainMenu();
				break;
			case R.id.btnRecover:
				i = new Intent(this, RecoveryForm.class);
				startActivityForResult(i, RECOVERY_FORM_CODE);
				break;
			case R.id.btnRegister:
				i = new Intent(this, RegistrationForm.class);
				startActivityForResult(i, REGISTERATION_FORM_CODE);
				break;
		}
	}

	private void sendToMainMenu() {
		Intent i = new Intent(this, MainMenu.class);
		startActivity(i);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case RECOVERY_FORM_CODE:
				if (resultCode != RESULT_OK) {
					break;
				}
				
				Toast.makeText(this, getResources().getString(R.string.recoverSent), Toast.LENGTH_LONG).show();
				break;
			case REGISTERATION_FORM_CODE:
				if (resultCode != RESULT_OK) {
					break;
				}

				sendToMainMenu();
				break;
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}*/
}
