package edu.csupomona.cs.cs356.classmate;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
		 AsyncHttpClient client = new AsyncHttpClient();
		 RequestParams params = new RequestParams();
		 final EditText pass1 = (EditText)findViewById(R.id.etBroncoName);
		 String p1 = pass1.getText().toString();
		 params.put("email", p1);
		 
		 final AlertDialog d = new AlertDialog.Builder(this).create();
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
			
			final AlertDialog d2 = new AlertDialog.Builder(this).create();
			d2.setTitle(R.string.recover);
			d2.setMessage(getResources().getString(R.string.recoverError));
			d2.setIcon(android.R.drawable.ic_dialog_info);
			d2.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					setResult(RESULT_OK);
					finish();
				}
			});

			d2.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					setResult(RESULT_OK);
					finish();
				}
			});

	
			
		 client.get("http://www.lol-fc.com/classmate/email.php", params, new AsyncHttpResponseHandler() {
		     @Override
		     public void onSuccess(String response) {
		        
		   	  System.out.println(response);
		   	  if(Integer.parseInt(response)==2)
		   	  {
		   		  d.show();
		   	  }
		   	  else
		   	  {
		   		  d2.show();
		   	  }
		        
		     }
		 });
		
		 
		
	}
	
	
}
