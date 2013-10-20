package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import static android.app.Activity.RESULT_OK;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RecoveryForm extends Activity implements OnClickListener {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.recovery_form);

		final Button b = (Button)findViewById(R.id.btnRecover);
		b.setOnClickListener(this);
		b.setEnabled(false);

		final EditText etBroncoName = (EditText)findViewById(R.id.etBroncoName);
		etBroncoName.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				String userName = etBroncoName.getText().toString();
				b.setEnabled(!userName.isEmpty());
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//...
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//...
			}
		});
	}

	public void onClick(View v) {
		AlertDialog d = new AlertDialog.Builder(this).create();
		d.setTitle(R.string.recover);
		d.setMessage(getResources().getString(R.string.recoverSent));
		d.setIcon(android.R.drawable.ic_dialog_info);
		d.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				setResult(RESULT_OK);
				finish();
			}
		});

		d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				setResult(RESULT_OK);
				finish();
			}
		});

		d.show();
	}
}
