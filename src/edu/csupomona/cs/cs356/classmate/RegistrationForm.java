package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationForm extends Activity implements OnClickListener {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.registeration_form);

		final Button b = (Button)findViewById(R.id.btnRegister);
		b.setOnClickListener(this);
		b.setEnabled(false);

		final TextView tvPasswordMatcher = (TextView)findViewById(R.id.tvPasswordMatcher);

		final EditText pass1 = (EditText)findViewById(R.id.etPassword);
		final EditText pass2 = (EditText)findViewById(R.id.etConfirmPassword);

		TextWatcher textWatcher = new TextWatcher() {
			public void afterTextChanged(Editable s) {
				String p1 = pass1.getText().toString();
				String p2 = pass2.getText().toString();
				if (!p1.isEmpty() && p1.compareTo(p2) == 0) {
					tvPasswordMatcher.setText(R.string.passwordsmatch);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.green));
					b.setEnabled(true);
				} else {
					tvPasswordMatcher.setText(R.string.passwordsdonotmatch);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.red));
					b.setEnabled(false);
				}

				// TODO: Safely clear strings from memory using some char array
				p1 = null;
				p2 = null;
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//...
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//...
			}
		};

		pass1.addTextChangedListener(textWatcher);
		pass2.addTextChangedListener(textWatcher);
	}

	public void onClick(View v) {
		setResult(RESULT_OK);
		finish();
	}
}
