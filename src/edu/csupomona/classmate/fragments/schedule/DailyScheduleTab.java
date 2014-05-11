package edu.csupomona.classmate.fragments.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.LoginActivity;
import edu.csupomona.classmate.MainActivity;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Section;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.utils.TextWatcherAdapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DailyScheduleTab extends Fragment implements Constants {

        private ViewGroup root;
        private EditText etScheduleName;
        private Button btnAddSchedule;
        private LinearLayout llAddClass;
        private LinearLayout llNoClass;
        private LinearLayout llSchedule;
        private LinearLayout llNoSchedule;
        private boolean outside_source =  false;
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                root = (ViewGroup)inflater.inflate(R.layout.schedule_daily_tab_layout, null);
                         
                final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
                
                final LinearLayout llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
                llProgressBar.setVisibility(View.VISIBLE);

                final RequestParams params = new RequestParams();
                params.put("user_id", Long.toString(USER.getID()));

                final AsyncHttpClient client = new AsyncHttpClient();
                client.get("http://www.lol-fc.com/classmate/2/getusernumschedules.php", params, new AsyncHttpResponseHandler() {
                	 @Override
                     public void onSuccess(String response) {
                		 
                		 llProgressBar.setVisibility(View.GONE);
                		 
                		 int numSchedules;
                         try {
                        	 numSchedules = Integer.parseInt(response);
                         } catch (NumberFormatException e) {
                        	 numSchedules = 0;
                         }
                         
                         if(numSchedules==0)
                         {
                        	 llNoSchedule = (LinearLayout)root.findViewById(R.id.llNoSchedule);
                        	 llNoSchedule.setVisibility(View.VISIBLE);
                        	 
                        	  etScheduleName = (EditText)root.findViewById(R.id.etScheduleName);
                        	  btnAddSchedule = (Button)root.findViewById(R.id.btnAddSchedule);
                        	  btnAddSchedule.setEnabled(false);
                              
                        	  TextWatcherAdapter scheduleNameTextWatcher = new TextWatcherAdapter() {
                                  String s1;

                                  @Override
                                  public void afterTextChanged(Editable e) {
                                          s1 = etScheduleName.getText().toString();
                       
                                          if (!s1.isEmpty()) {
                                        	  btnAddSchedule.setEnabled(true);
                                          } else {
                                        	  btnAddSchedule.setEnabled(false);
                                          }

                                          // TODO: Safely clear strings from memory using some char array
                                  }
                          };
                          
                          etScheduleName.addTextChangedListener(scheduleNameTextWatcher);                                                 
                             
                         }
                         else
                         {
                        	  client.get("http://www.lol-fc.com/classmate/2/getusernumclassestoday2.php", params, new AsyncHttpResponseHandler() {
                                  @Override
                                  public void onSuccess(String response) {
                                          llProgressBar.setVisibility(View.GONE);

                                          int numClasses;
                                          try {
                                                  numClasses = Integer.parseInt(response);
                                          } catch (NumberFormatException e) {
                                                  numClasses = 0;
                                          }

                                          if (numClasses == 0) {
                                                  
                                                  
                                                          llNoClass = (LinearLayout)root.findViewById(R.id.llNoClass);
                                                          llNoClass.setVisibility(View.VISIBLE);
                                                          
                                                  
                                          } else {
                                                  llSchedule = (LinearLayout)root.findViewById(R.id.llSchedule);
                                                  llSchedule.setVisibility(View.VISIBLE);

                                                  final LinearLayout llProgressBarClasses = (LinearLayout)llSchedule.findViewById(R.id.llProgressBarClasses);
                                                  llProgressBarClasses.setVisibility(View.VISIBLE);

                                                  RequestParams params = new RequestParams();
                                                  params.put("full", "yes");
                                                  params.put("user_id", Long.toString(USER.getID()));

                                                  AsyncHttpClient client = new AsyncHttpClient();
                                                  client.get("http://lol-fc.com/classmate/2/getuserclassestoday2.php", params, new AsyncHttpResponseHandler() {
                                                          @Override
                                                          public void onSuccess(String response) {
                                                                  setupSchedule(response);
                                                                  llProgressBarClasses.setVisibility(View.GONE);
                                                          }
                                                  });
                                          }
                                  }
                          });

                         }

                	 }
                });
                
              
                return root;
        }

        private void setupSchedule(String response) {
                List<Section> schedule = new ArrayList<Section>();
                if (1 < response.length()) {
                        try {
                                Section s;
                                JSONObject jObj;
                                JSONArray myjsonarray = new JSONArray(response);
                                for (int i = 0; i < myjsonarray.length(); i++) {
                                        jObj = myjsonarray.getJSONObject(i);
                                        s = new Section(
                                                jObj.getInt("class_id"),
                                                jObj.getString("title"),
                                                jObj.getString("time_start"),
                                                jObj.getString("time_end"),
                                                jObj.getString("weekdays"),
                                                jObj.getString("date_start"),
                                                jObj.getString("date_end"),
                                                jObj.getString("instructor"),
                                                jObj.getString("building"),
                                                jObj.getString("room"),
                                                jObj.getString("section"),
                                                jObj.getString("major_short"),
                                                jObj.getString("major_long"),
                                                jObj.getString("class_num"),
                                                jObj.getString("term")
                                        );

                                        schedule.add(s);
                                }
                        } catch (JSONException e) {
                                e.printStackTrace();
                        }
                }
                
                ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), schedule);
                
                ListView lvSchedule = (ListView)llSchedule.findViewById(R.id.lvSchedule);
                lvSchedule.setAdapter(adapter);
                lvSchedule.setOnItemClickListener(new ListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                LinearLayout llExtendedInfo = (LinearLayout)view.findViewById(R.id.llExtendedInfo);
                                llExtendedInfo.setVisibility(llExtendedInfo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                        }
                });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                switch (requestCode) {
                        case CODE_ADD_CLASS:
                                //if (resultCode != RESULT_OK) {
                                //        break;
                                //}

                        		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);

                                if (llAddClass != null) {
                                        llAddClass.setVisibility(View.GONE);
                                }

                                if (llSchedule == null) {
                                        llSchedule = (LinearLayout)root.findViewById(R.id.llSchedule);
                                }

                                llSchedule.setVisibility(View.VISIBLE);

                                final LinearLayout llProgressBarClasses = (LinearLayout)llSchedule.findViewById(R.id.llProgressBarClasses);
                                llProgressBarClasses.setVisibility(View.VISIBLE);

                                RequestParams params = new RequestParams();
                                params.put("full", "yes");
                                params.put("user_id", Long.toString(USER.getID()));

                                AsyncHttpClient client = new AsyncHttpClient();
                                client.get("http://lol-fc.com/classmate/2/getuserclassestoday2.php", params, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(String response) {
                                                setupSchedule(response);
                                                llProgressBarClasses.setVisibility(View.GONE);
                                        }
                                });

                                break;
                }
        }
}