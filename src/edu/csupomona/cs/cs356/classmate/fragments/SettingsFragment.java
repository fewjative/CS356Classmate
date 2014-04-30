package edu.csupomona.cs.cs356.classmate.fragments;

import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.Schedule;
import edu.csupomona.cs.cs356.classmate.abstractions.Term;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;
//allow for a user to upload a photo/choose a photo for their profile picture
//allow a user to make multiple schedules and choose the schedule from the settings fragment
public class SettingsFragment extends Fragment implements View.OnClickListener{
        private Button btnChangePass;
        private EditText etOldPass;
        private EditText etNewPass1;
        private EditText etNewPass2;
        private Button btnCreateSchedule;
        private Button btnSetActiveSchedule;
        private EditText etScheduleName;
        private LinearLayout llNoSchedule;
        private LinearLayout llHasSchedule;
        private ViewGroup root;
        private Spinner sSchedule;
        private Schedule schActive = null;
        private boolean isFirst = true;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        		schActive = null;
                root = (ViewGroup)inflater.inflate(R.layout.settings_fragment, null);
                
                btnChangePass = (Button)root.findViewById(R.id.btnChangePass);
                btnChangePass.setOnClickListener(this);
                btnChangePass.setEnabled(false);
                
                btnCreateSchedule = (Button)root.findViewById(R.id.btnCreateSchedule);
                btnCreateSchedule.setOnClickListener(this);
                btnCreateSchedule.setEnabled(false);
                
                btnSetActiveSchedule = (Button)root.findViewById(R.id.btnSetActive);
                btnSetActiveSchedule.setOnClickListener(this);
                btnSetActiveSchedule.setEnabled(true);
                
                etOldPass = (EditText)root.findViewById(R.id.etOldPass);
                etNewPass1 = (EditText)root.findViewById(R.id.etPassword);
                etNewPass2 = (EditText)root.findViewById(R.id.etConfirmPassword);
                etScheduleName = (EditText)root.findViewById(R.id.etScheduleName);
                
                sSchedule = (Spinner)root.findViewById(R.id.sSchedules);
                
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
                
                TextWatcherAdapter scheduleNameTextWatcher = new TextWatcherAdapter() {
                    String s1;

                    @Override
                    public void afterTextChanged(Editable e) {
                            s1 = etScheduleName.getText().toString();
         
                            if (!s1.isEmpty()) {
                                    btnCreateSchedule.setEnabled(true);
                            } else {
                            	btnCreateSchedule.setEnabled(false);
                            }

                            // TODO: Safely clear strings from memory using some char array
                    }
            };
           
            

                etOldPass.addTextChangedListener(textWatcher);
                etNewPass1.addTextChangedListener(textWatcher);
                etNewPass2.addTextChangedListener(textWatcher);
                etScheduleName.addTextChangedListener(scheduleNameTextWatcher);
             
                
                final int id = getActivity().getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);
                RequestParams params = new RequestParams();
                params.put("user_id", Integer.toString(id));

                AsyncHttpClient client = new AsyncHttpClient();
                client.get("http://www.lol-fc.com/classmate/getusernumschedules.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
   

                                int numSchedules;
                                try {
                                	numSchedules = Integer.parseInt(response);
                                } catch (NumberFormatException e) {
                                	numSchedules = 0;
                                }

                                if (numSchedules == 0) {
                                        
                                        isFirst = true;
                                        
                                } else {
                                	
                                	isFirst = false;
                                    
                                    btnSetActiveSchedule.setVisibility(View.VISIBLE);
                                    sSchedule.setVisibility(View.VISIBLE);
                  
                                    setupSpinner();
                                }
                        }
                });
        
        
                return root;
        }
        
    	private void setupSpinner() {
    		System.out.println("Setting up the spinner");
    		sSchedule.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
    			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    				schActive = (Schedule)sSchedule.getAdapter().getItem(position);
    				System.out.println("Item selected");
    			}

    			public void onNothingSelected(AdapterView<?> parent) {
    				//...
    			}
    		});

    		AsyncHttpClient client = new AsyncHttpClient();
    		RequestParams params = new RequestParams();
    		final int id = getActivity().getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);
    		params.put("user_id", Integer.toString(id));
            
    		client.get("http://www.lol-fc.com/classmate/getuserschedules.php",params, new AsyncHttpResponseHandler() {
    			@Override
    			public void onSuccess(String response) {
    				System.out.println(response);
    				System.out.println(id);
    				List<Schedule> schedules = new ArrayList<Schedule>();
    				if (1 < response.length()) {
    					try {
    						Schedule t;
    						JSONObject jObj;
    						JSONArray myjsonarray = new JSONArray(response);
    						for (int i = 0; i < myjsonarray.length(); i++) {
    							jObj = myjsonarray.getJSONObject(i);
    							t = new Schedule(
    									  jObj.getInt("schedule_id"),
    									jObj.getString("schedule_title")
    								
    							);
    							System.out.println("New Schedule");

    							schedules.add(t);
    						}
    					} catch (JSONException e) {
    						e.printStackTrace();
    					}
    				}
    				
    				System.out.println("Spinner size: " + schedules.size());
    				ArrayAdapter<Schedule> adapter = new ArrayAdapter<Schedule>(getActivity(), android.R.layout.simple_spinner_item, schedules);
    				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    				sSchedule.setAdapter(adapter);
    			}
    		});
    	}
        
        public void onClick(View v) {

        	RequestParams params = new RequestParams();
        	int id = getActivity().getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);;
        	AsyncHttpClient client = new AsyncHttpClient();
        	 
        	switch(v.getId()){
        	case R.id.btnChangePass:
                assert etNewPass1.getText().toString().compareTo(etNewPass2.getText().toString()) == 0;

                final ProgressDialog pg = ProgressDialog.show(getActivity(), getResources().getString(R.string.changePass), getResources().getString(R.string.changePassLoading));

        
                String newpassword = etNewPass1.getText().toString();
                String oldpassword = etOldPass.getText().toString();
                
               
                
                
                params.put("user_id", Integer.toString(id));
                params.put("oldpassword", oldpassword);
                params.put("newpassword", newpassword);
                
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
                break;
        	case R.id.btnCreateSchedule://change button to createScheduleFirst
        		System.out.println("Clicked the create schedule button");
        		String scheduleName = etScheduleName.getText().toString();
        		System.out.println("scheduleName: " + scheduleName);
        		String firstSchedule;
        		if(isFirst)
        		{
        			firstSchedule = Integer.toString(1);
        		}
        		else 
        		{
        			firstSchedule ="doesnt matter";
        		}

                id = getActivity().getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);
                
                params.put("user_id", Integer.toString(id));
                params.put("title",scheduleName);
                params.put("new", firstSchedule);
                
                client.get("http://www.lol-fc.com/classmate/adduserschedule.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                    	
                    	if(isFirst)
                		{
                    		 btnSetActiveSchedule.setVisibility(View.VISIBLE);
                             sSchedule.setVisibility(View.VISIBLE);
                             isFirst = false;
                		}
                    	setupSpinner();
                    	etScheduleName.setText("");
                  
                    }
            });
                
                //create schedule and set as active
        		break;
        	case R.id.btnSetActive:
        	System.out.println("Clicked set active");
        	
        	if(schActive !=null)
        	{
                Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_SHORT).show();
           
                  params.put("user_id", Integer.toString(id));
                  params.put("schedule_id",Integer.toString(schActive.getScheduleID()));
                  
                  client.get("http://www.lol-fc.com/classmate/setuserschedule.php", params, new AsyncHttpResponseHandler() {
                      @Override
                      public void onSuccess(String response) {
                      
                    	  Toast.makeText(getActivity(), "Schedule is set to active, resp: " + response + " schActive: " + schActive.getScheduleID(), Toast.LENGTH_SHORT).show();
                      }
              });

            }
        	break;
        	
        	}
        }
}
//http://stackoverflow.com/questions/18268880/reset-reload-fragment-container