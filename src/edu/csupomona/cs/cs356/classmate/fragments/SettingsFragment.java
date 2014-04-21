package edu.csupomona.cs.cs356.classmate.fragments;

import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

import com.facebook.*;
import com.facebook.widget.LoginButton;

public class SettingsFragment extends Fragment implements View.OnClickListener{
        private Button btnChangePass;
        private LoginButton btnFacebookLink;
        private EditText etOldPass;
        private EditText etNewPass1;
        private EditText etNewPass2;
        
    	private UiLifecycleHelper uiHelper;
        private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
    	    	onSessionStateChange(session, state, exception);
    	    }
    	};
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        		super.onCreateView(inflater, container, savedInstanceState);
        		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.settings_fragment, null);
                
        		btnFacebookLink = (LoginButton)root.findViewById(R.id.btnFacebookLink);
        		btnFacebookLink.setFragment(this);
        		
                btnChangePass = (Button)root.findViewById(R.id.btnChangePass);
                btnChangePass.setOnClickListener(this);
                btnChangePass.setEnabled(false);
                                
                etOldPass = (EditText)root.findViewById(R.id.etOldPass);
                etNewPass1 = (EditText)root.findViewById(R.id.etPassword);
                etNewPass2 = (EditText)root.findViewById(R.id.etConfirmPassword);
                
                final TextView tvPasswordMatcher = (TextView)root.findViewById(R.id.tvPasswordMatcher);
                TextWatcherAdapter textWatcher = new TextWatcherAdapter() {
                        String s1, s2, oldpass;

                        @Override
                        public void afterTextChanged(Editable e) {
                                s1 = etNewPass1.getText().toString();
                                s2 = etNewPass2.getText().toString();
                                if (!s1.isEmpty() && s1.compareTo(s2) == 0) {
                                        tvPasswordMatcher.setText(R.string.passwordsmatch);
                                        tvPasswordMatcher.setTextColor(getResources().getColor(R.color.green));
                                        btnChangePass.setEnabled(true);
                                } else {
                                        tvPasswordMatcher.setText(R.string.passwordsdonotmatch);
                                        tvPasswordMatcher.setTextColor(getResources().getColor(R.color.red));
                                        btnChangePass.setEnabled(false);
                                }

                                oldpass = etOldPass.getText().toString();
                                if (oldpass.isEmpty()) {
                                        btnChangePass.setEnabled(false);
                                }


                                // TODO: Safely clear strings from memory using some char array
                        }
                };

                etOldPass.addTextChangedListener(textWatcher);
                etNewPass1.addTextChangedListener(textWatcher);
                etNewPass2.addTextChangedListener(textWatcher);
        
                return root;
        }
        
    	@Override
    	public void onCreate(Bundle savedInstanceState) {
    	    super.onCreate(savedInstanceState);
    	    
    	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
    	    uiHelper.onCreate(savedInstanceState);
    	}
        
        public void onClick(View v) {
                assert etNewPass1.getText().toString().compareTo(etNewPass2.getText().toString()) == 0;

                final ProgressDialog pg = ProgressDialog.show(getActivity(), getResources().getString(R.string.changePass), getResources().getString(R.string.changePassLoading));

        
                String newpassword = etNewPass1.getText().toString();
                String oldpassword = etOldPass.getText().toString();
                

                RequestParams params = new RequestParams();
                final int id = getActivity().getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);
                
                params.put("user_id", Integer.toString(id));
                params.put("oldpassword", oldpassword);
                params.put("newpassword", newpassword);
                

                AsyncHttpClient client = new AsyncHttpClient();
                client.get("http://www.lol-fc.com/classmate/changepass.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                                pg.dismiss();

                                int id;
                                try {
                                        id = Integer.parseInt(response);
                                } catch (NumberFormatException e) {
                                        id = NULL_USER;
                                }

                                if (id <= NULL_USER) {
                                        AlertDialog d = new AlertDialog.Builder(getActivity()).create();
                                        d.setTitle("Error With Changing Your Password");
                                        d.setMessage("Please double check to make sure you have input all the forms correctly");
                                        d.setIcon(android.R.drawable.ic_dialog_alert);
                                        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                public void onCancel(DialogInterface dialog) {
                                                  // setResult(0);
                                                        //finish();
                                                }
                                        });

                                        d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                        //setResult(RESULT_CANCELED);
                                                        //finish();
                                                }
                                        });

                                        d.show();
                                        return;
                                }
                                else
                                {
                                        AlertDialog d = new AlertDialog.Builder(getActivity()).create();
                                        d.setTitle("Success!");
                                        d.setMessage("Your password has been successfully changed!");
                                        d.setIcon(android.R.drawable.ic_dialog_alert);
                                        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                public void onCancel(DialogInterface dialog) {
                                                  // setResult(0);
                                                        //finish();
                                                }
                                        });

                                        d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                        //setResult(RESULT_CANCELED);
                                                        //finish();
                                                }
                                        });
                                        
                                        d.show();
                                        etNewPass1.setText("");
                                        etNewPass2.setText("");
                                        etOldPass.setText("");
                                        return;
                                }

                        

                        /*        Intent i = new Intent();
                                i.putExtra(LoginActivity.INTENT_KEY_USERID, id);
                                i.putExtra(LoginActivity.INTENT_KEY_EMAIL, emailAddress);
                                i.putExtra(LoginActivity.INTENT_KEY_USERNAME, username);
                                i.putExtra(LoginActivity.INTENT_KEY_REMEMBER, cbRememberMe.isChecked());
                                setResult(RESULT_OK, i);
                                finish();
                                */
                        }
                });
        }
    
    	@Override
    	public void onResume() {
    	    super.onResume();
    	    
    	    // For scenarios where the main activity is launched and user
    	    // session is not null, the session state change notification
    	    // may not be triggered. Trigger it if it's open/closed.
    	    Session session = Session.getActiveSession();
    	    if (session != null &&
    	           (session.isOpened() || session.isClosed()) ) {
    	        onSessionStateChange(session, session.getState(), null);
    	    }
    	    uiHelper.onResume();
    	}

    	@Override
    	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	    super.onActivityResult(requestCode, resultCode, data);
    	    uiHelper.onActivityResult(requestCode, resultCode, data);
    	}

    	@Override
    	public void onPause() {
    	    super.onPause();
    	    uiHelper.onPause();
    	}

    	@Override
    	public void onDestroy() {
    	    super.onDestroy();
    	    uiHelper.onDestroy();
    	}

    	@Override
    	public void onSaveInstanceState(Bundle outState) {
    	    super.onSaveInstanceState(outState);
    	    uiHelper.onSaveInstanceState(outState);
    	}
    	
    	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    	    if (state.isOpened()) {
    	        Log.i("FACEBOOK", "Logged in...");
    	    } else if (state.isClosed()) {
    	        Log.i("FACEBOOK", "Logged out...");
    	    }
    	}
        
}