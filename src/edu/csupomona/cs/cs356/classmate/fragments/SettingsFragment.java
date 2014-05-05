package edu.csupomona.cs.cs356.classmate.fragments;

import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.SessionState;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.Schedule;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;


//allow for a user to upload a photo/choose a photo for their profile picture
//allow a user to make multiple schedules and choose the schedule from the settings fragment
//TODO: prevent a user from clicking the save button if a photo has not been selected

public class SettingsFragment extends PictureHandlerFragment implements View.OnClickListener{
        private Button btnChangePass;
        private Button btnSavePhoto;
        private ProfilePictureView settingsProfilePicture;
        private TextView displayName;
        private TextView displayID; 
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
        private boolean isFbLoggedIn = false;
        
        private int serverResponseCode = 0;
        ProgressDialog dialog = null;
        String upLoadServerUri = null;
        String upLoadServerUriFB = null;
        
        String imageFile;
    	
        private UiLifecycleHelper uiHelper;
        private Session.StatusCallback callback = new Session.StatusCallback() {
	        @Override
	        public void call(Session session, SessionState state, Exception exception) {
	    	    	onSessionStateChange(session, state, exception);
	        }
    	};
        
    	public boolean isFbLoggedIn() {
    		return isFbLoggedIn;
    	}
    	
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        		super.onCreateView(inflater, container, savedInstanceState);
        		schActive = null;
                root = (ViewGroup)inflater.inflate(R.layout.settings_fragment, null);
                final long id = getActivity().getIntent().getLongExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);
        		
                btnChangePass = (Button)root.findViewById(R.id.btnChangePass);
                btnChangePass.setOnClickListener(this);
                btnChangePass.setEnabled(false);
                
                btnCreateSchedule = (Button)root.findViewById(R.id.btnCreateSchedule);
                btnCreateSchedule.setOnClickListener(this);
                btnCreateSchedule.setEnabled(false);
                
                btnSetActiveSchedule = (Button)root.findViewById(R.id.btnSetActive);
                btnSetActiveSchedule.setOnClickListener(this);
                btnSetActiveSchedule.setEnabled(true);
                
                btnSavePhoto = (Button)root.findViewById(R.id.btnSavePhoto);
                btnSavePhoto.setOnClickListener(this);
                btnSavePhoto.setEnabled(true);
                
                // facebook stuff
        		settingsProfilePicture = (ProfilePictureView)root.findViewById(R.id.settingsProfilePicture);
        		// set default profile pic to specified bitmap
        		settingsProfilePicture.setDefaultProfilePicture(getBitmap(R.drawable.ic_action_person));
        		
        		Thread thread = new Thread(new Runnable(){
        		    @Override
        		    public void run() {
        		        try {
        		        	System.out.println("async runnable");
                         	Drawable drawable = LoadImageFromWebOperations("http://www.lol-fc.com/classmate/uploads/"+Long.toString(id)+".jpg");
                    		
                    		if(drawable !=null)
                    			settingsProfilePicture.setDefaultProfilePicture(drawableToBitmap(drawable));
        		        } catch (Exception e)
        		        {
        		            e.printStackTrace();
        		        }
        		        
        		        try {
        		        	System.out.println("async runnable");
                         	Drawable drawable = LoadImageFromWebOperations("http://www.lol-fc.com/classmate/uploads/"+Long.toString(id)+".png");
                    		
                    		if(drawable !=null)
                    			settingsProfilePicture.setDefaultProfilePicture(drawableToBitmap(drawable));
        		        } catch (Exception e)
        		        {
        		            e.printStackTrace();
        		        }
        		        
        		        try {
        		        	System.out.println("async runnable");
                         	Drawable drawable = LoadImageFromWebOperations("http://www.lol-fc.com/classmate/uploads/default.png");
                    		
                    		if(drawable !=null)
                    			settingsProfilePicture.setDefaultProfilePicture(drawableToBitmap(drawable));
        		        } catch (Exception e)
        		        {
        		            e.printStackTrace();
        		        }
        		    }
        		});

        		thread.start(); 
        		       		
        		settingsProfilePicture.setOnClickListener(this);
        		settingsProfilePicture.setEnabled(true);
        		
        		displayName = (TextView)root.findViewById(R.id.displayName);
                displayID = (TextView)root.findViewById(R.id.displayID);
        		displayName.setText(null);
        		displayID.setText(null);
        		
        		upLoadServerUri = "http://www.lol-fc.com/classmate/UploadToServer.php";
        		upLoadServerUriFB = "http://www.lol-fc.com/classmate/UploadToServerFB.php";
                //-----------------
                
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
             
                
                RequestParams params = new RequestParams();
                params.put("user_id", Long.toString(id));

                AsyncHttpClient client = new AsyncHttpClient();
                client.get("http://www.lol-fc.com/classmate/getusernumschedules.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                                int numSchedules;
                                try {
                                	numSchedules = Integer.parseInt(response);
                                } catch (NumberFormatException e) {
                                	numSchedules = 0;
                                }//because we are using ints and FBOOK needs a long, it is checking for the wrong user_id and thus finding schedules returns -1

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
        
        private Drawable LoadImageFromWebOperations(String url)
        {
	        try
	        {
		        InputStream is = (InputStream) new URL(url).getContent();
		        Drawable d = Drawable.createFromStream(is, "src name");
		        if(d!=null)
		        	System.out.println("The Drawable is not null!");
		        return d;
	        }catch (Exception e) {
		        System.out.println("Exc="+e);
		        return null;
	        }
        }
        
        public static Bitmap drawableToBitmap (Drawable drawable) {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable)drawable).getBitmap();
            }

            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap); 
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        }
        
    	@Override
    	public void onCreate(Bundle savedInstanceState) {
    	    super.onCreate(savedInstanceState);
    	    
    	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
    	    uiHelper.onCreate(savedInstanceState);
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
    		final long id = getActivity().getIntent().getLongExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);
    		params.put("user_id", Long.toString(id));
            
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
        	long id = getActivity().getIntent().getLongExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);;
        	AsyncHttpClient client = new AsyncHttpClient();
        	 
        	switch(v.getId()){
        	//profile picture clicked
        	case R.id.settingsProfilePicture:
        		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                CharSequence camera = getResources().getString(R.string.action_camera);
                CharSequence gallery = getResources().getString(R.string.action_gallery);
                builder.setCancelable(true).
                        setItems(new CharSequence[] {camera, gallery}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                	startCamera();
                                } else if (i == 1) {
                                    startGallery();
                                }
                            }
                        });
                builder.show();  
        		break;
        	
        	case R.id.btnChangePass:
                assert etNewPass1.getText().toString().compareTo(etNewPass2.getText().toString()) == 0;

                final ProgressDialog pg = ProgressDialog.show(getActivity(), getResources().getString(R.string.changePass), getResources().getString(R.string.changePassLoading));

                String newpassword = etNewPass1.getText().toString();
                String oldpassword = etOldPass.getText().toString();
                
                params.put("user_id", Long.toString(id));
                params.put("oldpassword", oldpassword);
                params.put("newpassword", newpassword);
                
                client.get("http://www.lol-fc.com/classmate/changepass.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                                pg.dismiss();

                                long id;
                                try {
                                        id = Long.parseLong(response);
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

                id = getActivity().getIntent().getLongExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);
                
                params.put("user_id", Long.toString(id));
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
           
                  params.put("user_id", Long.toString(id));
                  params.put("schedule_id",Integer.toString(schActive.getScheduleID()));
                  
                  client.get("http://www.lol-fc.com/classmate/setuserschedule.php", params, new AsyncHttpResponseHandler() {
                      @Override
                      public void onSuccess(String response) {
                      
                    	  Toast.makeText(getActivity(), "Schedule is set to active, resp: " + response + " schActive: " + schActive.getScheduleID(), Toast.LENGTH_SHORT).show();
                      }
              });

            }
        	break;
        	case R.id.btnSavePhoto:
        		System.out.println("Clicked the save button");
        		
        		 dialog = ProgressDialog.show(getActivity(), "", "Uploading file...", true);
                 
                 new Thread(new Runnable() {
                         public void run() {
                              getActivity().runOnUiThread(new Runnable() {
                                     public void run() {
                                         System.out.println("uploading started.....");
                                     }
                                 });                      
                            
                              uploadFile(imageFile);
                                                       
                         }
                       }).start();        
                 
        		
        	break;
        	}
        }
        
        //==================FOR UPLOADING PICTURES
        //
        public int uploadFile(String sourceFileUri) {
            
            System.out.println(sourceFileUri);
            String fileName = sourceFileUri;
    
            HttpURLConnection conn = null;
            DataOutputStream dos = null;  
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024; 
            File sourceFile = new File(sourceFileUri); 
             
            if (!sourceFile.isFile()) {
                 
                 dialog.dismiss(); 
                  
                 Log.e("uploadFile", "Source File not exist :");
                  
                 getActivity().runOnUiThread(new Runnable() {
                     public void run() {
                         System.out.println("Source File not exist :");
                     }
                 }); 
                  
                 return 0;
              
            }
            else
            {
                 try { 
                	  int id = getActivity().getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);
                      String urlParameters = "user_id="+Integer.toString(id)+"&fname="+fileName;
                       // open a URL connection to the Servlet
                     FileInputStream fileInputStream = new FileInputStream(sourceFile);
                     URL url = new URL(upLoadServerUri+"?"+urlParameters);
                      
                     // Open a HTTP  connection to  the URL
                     conn = (HttpURLConnection) url.openConnection(); 
                     conn.setDoInput(true); // Allow Inputs
                     conn.setDoOutput(true); // Allow Outputs
                     conn.setUseCaches(false); // Don't use a Cached Copy
                     conn.setRequestMethod("POST");
                     conn.setRequestProperty("Connection", "Keep-Alive");
                     conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                     conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                     conn.setRequestProperty("uploaded_file", fileName); 
                   
                    // conn.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
                      
                     dos = new DataOutputStream(conn.getOutputStream());
                                      
                     dos.writeBytes(twoHyphens + boundary + lineEnd); 
                    // dos.writeBytes("Content-Disposition: form-data; name="+uploaded_file+";filename=\""+ fileName + """ + lineEnd);
                   
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);		 
                    dos.writeBytes(lineEnd);
            
                     // create a buffer of  maximum size
                     bytesAvailable = fileInputStream.available(); 
            
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     buffer = new byte[bufferSize];
            
                     // read file and write it into form...
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                        
                     while (bytesRead > 0) {
                          
                       dos.write(buffer, 0, bufferSize);
                       bytesAvailable = fileInputStream.available();
                       bufferSize = Math.min(bytesAvailable, maxBufferSize);
                       bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                        
                      }
            
                     // send multipart form data necesssary after file data...
                     dos.writeBytes(lineEnd);
                     dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                     
                     //SEND EXTRA PARAMS
                     //dos.writeBytes(urlParameters);
            
                     // Responses from the server (code and message)
                     serverResponseCode = conn.getResponseCode();
                     String serverResponseMessage = conn.getResponseMessage();
                       
                     Log.i("uploadFile", "HTTP Response is : "
                             + serverResponseMessage + ": " + serverResponseCode);
                      
                     if(serverResponseCode == 200){
                          
                         getActivity().runOnUiThread(new Runnable() {
                              public void run() {
                                   
                                  String msg = "File Upload Completed.";
                                   
                                  System.out.println(msg);
                                  Toast.makeText(getActivity(), "File Upload Complete.", 
                                               Toast.LENGTH_SHORT).show();
                              }
                          });                
                     }    
                      
                     //close the streams //
                     fileInputStream.close();
                     dos.flush();
                     dos.close();
                       
                } catch (MalformedURLException ex) {
                     
                    dialog.dismiss();  
                    ex.printStackTrace();
                     
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            System.out.println("MalformedURLException Exception : check script url.");
                            Toast.makeText(getActivity(), "MalformedURLException", 
                                                                Toast.LENGTH_SHORT).show();
                        }
                    });
                     
                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
                } catch (Exception e) {
                     
                    dialog.dismiss();  
                    e.printStackTrace();
                     
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                        	System.out.println("Got Exception : see logcat ");
                            Toast.makeText(getActivity(), "Got Exception : see logcat ", 
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("Upload file to server Exception", "Exception : "
                                                     + e.getMessage(), e);  
                }
                dialog.dismiss();       
                return serverResponseCode; 
                 
             } // End else block 
           } 
        //=============================================================
        
      //==================FOR UPLOADING PICTURES
        //
        public int uploadFileFacebook(File fbSourceFile) {
            
          
            HttpURLConnection conn = null;
            DataOutputStream dos = null;  
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024; 
            File sourceFile = fbSourceFile; 
             
            if (!sourceFile.isFile()) {
                                   
                 Log.e("uploadFile", "Source File not exist :");
                  
                 getActivity().runOnUiThread(new Runnable() {
                     public void run() {
                         System.out.println("Source File not exist :");
                     }
                 }); 
                  
                 return 0;
              
            }
            else
            {
                 try { 
                	  int id = getActivity().getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);
                      String urlParameters = "user_id="+Integer.toString(id)+"&fname="+sourceFile.toURI();
                       // open a URL connection to the Servlet
                     FileInputStream fileInputStream = new FileInputStream(sourceFile);
                     URL url = new URL(upLoadServerUriFB+"?"+urlParameters);
                      
                     // Open a HTTP  connection to  the URL
                     conn = (HttpURLConnection) url.openConnection(); 
                     conn.setDoInput(true); // Allow Inputs
                     conn.setDoOutput(true); // Allow Outputs
                     conn.setUseCaches(false); // Don't use a Cached Copy
                     conn.setRequestMethod("POST");
                     conn.setRequestProperty("Connection", "Keep-Alive");
                     conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                     conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                     conn.setRequestProperty("uploaded_file", sourceFile.toURI().toString()); 
                   
                    // conn.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
                      
                     dos = new DataOutputStream(conn.getOutputStream());
                                      
                     dos.writeBytes(twoHyphens + boundary + lineEnd); 
                    // dos.writeBytes("Content-Disposition: form-data; name="+uploaded_file+";filename=\""+ fileName + """ + lineEnd);
                   
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ sourceFile.toURI() + "\"" + lineEnd);		 
                    dos.writeBytes(lineEnd);
            
                     // create a buffer of  maximum size
                     bytesAvailable = fileInputStream.available(); 
            
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     buffer = new byte[bufferSize];
            
                     // read file and write it into form...
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                        
                     while (bytesRead > 0) {
                          
                       dos.write(buffer, 0, bufferSize);
                       bytesAvailable = fileInputStream.available();
                       bufferSize = Math.min(bytesAvailable, maxBufferSize);
                       bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                        
                      }
            
                     // send multipart form data necesssary after file data...
                     dos.writeBytes(lineEnd);
                     dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                     
                     //SEND EXTRA PARAMS
                     //dos.writeBytes(urlParameters);
            
                     // Responses from the server (code and message)
                     serverResponseCode = conn.getResponseCode();
                     String serverResponseMessage = conn.getResponseMessage();
                       
                     Log.i("uploadFile", "HTTP Response is : "
                             + serverResponseMessage + ": " + serverResponseCode);
                      
                     if(serverResponseCode == 200){
                          
                         getActivity().runOnUiThread(new Runnable() {
                              public void run() {
                                   
                                  String msg = "File Upload Completed.";
                                   
                                  System.out.println(msg);
                                  
                                  File f = new File("/mnt/sdcard/delete.png");
                                  
                                  if(f.exists())
                            	  {
                            	  f.delete();//delete photo once it was been uploaded to our server
                            	  System.out.println("File has been deleted");
                            	   }
                                  //Toast.makeText(getActivity(), "File Upload Complete.", Toast.LENGTH_SHORT).show();
                              }
                          });                
                     }    
                      
                     //close the streams //
                     fileInputStream.close();
                     dos.flush();
                     dos.close();
                       
                } catch (MalformedURLException ex) {
                     
                    ex.printStackTrace();
                     
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            System.out.println("MalformedURLException Exception : check script url.");
                            Toast.makeText(getActivity(), "MalformedURLException", 
                                                                Toast.LENGTH_SHORT).show();
                        }
                    });
                     
                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
                } catch (Exception e) {
                     
                    e.printStackTrace();
                     
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                        	System.out.println("Got Exception : see logcat ");
                            Toast.makeText(getActivity(), "Got Exception : see logcat ", 
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("Upload file to server Exception", "Exception : "
                                                     + e.getMessage(), e);  
                }      
                return serverResponseCode; 
                 
             } // End else block 
           } 
        //=============================================================
        
        // methods required for facebook stuff------------
    	@Override
    	public void onResume() {
    	    super.onResume();
    	    
    	    // For scenarios where the main activity is launched and user
    	    // session is not null, the session state change notification
    	    // may not be triggered. Trigger it if it's open/closed.
    	    Session session = Session.getActiveSession();
    	    if (session != null && (session.isOpened() || session.isClosed()) ) {
    	        onSessionStateChange(session, session.getState(), null);
    	    }
    	    uiHelper.onResume();
    	}

    	@Override
    	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	    super.onActivityResult(requestCode, resultCode, data);
    	    uiHelper.onActivityResult(requestCode, resultCode, data);
    	    //System.out.println("Request Code: " +requestCode);
    	    //System.out.println("Result Code: " + resultCode);
    	    //System.out.println("Intent: " + data.toString());
    	    if(data==null)return;
    	    
    	    switch(requestCode) {
    	    //display picture captured in ProfilePictureView
    	    case 0:
    	    	System.out.println("The user pressed the back button while in the gallery");
    	    	break;
    	    case CODE_CAMERA_REQUEST:
    	        if(resultCode == getActivity().RESULT_OK) {
    	           Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
    	           // set default profile pic to specified bitmap
    	           settingsProfilePicture.setDefaultProfilePicture(thumbnail);
    	           ImageView iv = (ImageView)root.findViewById(R.id.imageView1);
    	           iv.setImageBitmap(thumbnail);
    	        }
    	        break;
    	    case CODE_GALLERY_REQUEST:
    	        try { //if data != null
	    	       Uri photoUri = data.getData();
	    	       imageFile =getRealPathFromURI(photoUri);
	    	       // Do something with the photo based on Uri
	    	       Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
	    	       // Load the selected image into a preview
	    	       settingsProfilePicture.setDefaultProfilePicture(thumbnail);
	    	       ImageView iv = (ImageView)root.findViewById(R.id.imageView1);
    	           iv.setImageBitmap(thumbnail);
    	        } catch (Exception e) {
    	        	Log.i("GALLERY", e.getMessage());
    	        }
    	        break;
    	    }
    	}
    	
    	 private String getRealPathFromURI(Uri contentURI) {
    		    Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
    		    if (cursor == null) { // Source is Dropbox or other similar local file path
    		        return contentURI.getPath();
    		    } else { 
    		        cursor.moveToFirst(); 
    		        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
    		        return cursor.getString(idx); 
    		    }
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
    	    	Request.newMeRequest(session, new Request.GraphUserCallback() {
    				@Override
    				public void onCompleted(GraphUser user, Response response) {
    					//set facebook details(picture, name, id) in the layout if logged in
    					if (user != null) {
    						isFbLoggedIn = true;
    		                settingsProfilePicture.setProfileId(user.getId());
//    		                ImageView fbImage = ((ImageView)settingsProfilePicture.getChildAt( 0));
//    		                Bitmap bitmap = ((BitmapDrawable) fbImage.getDrawable()).getBitmap();
    		                displayName.setText(user.getName());
    		                displayID.setText(user.getId());
    		                
    		                System.out.println("Attempting to upload fbook photo");
    		        		
    		        		 //dialog = ProgressDialog.show(getActivity(), "", "Uploading facebook profile img...", true);
    		                 
    		                 new Thread(new Runnable() {
    		                         public void run() {
    		                              getActivity().runOnUiThread(new Runnable() {
    		                                     public void run() {
    		                                         System.out.println("uploading started.....");
    		                                     }
    		                                 });                      
    		                            
    		                              String filename = "delete.png";
    		                              File f = new File(Environment.getExternalStorageDirectory().getPath()+"/"+filename);
    		                              if(f.exists())f.delete();
    		                              
    		                              try
    		                              {
    		                            	  SystemClock.sleep(7000);
    		                            	  FileOutputStream out = new FileOutputStream(f);
    		                            	  View content = root.findViewById(R.id.settingsProfilePicture);
        		                              content.setDrawingCacheEnabled(true);
        		                              Bitmap bitmap = content.getDrawingCache();
        		                              
        		                              System.out.println(bitmap.getHeight() + " " + bitmap.getWidth());
        		                              bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        		                              out.flush();
        		                              out.close();
        		                              System.out.println(f.getAbsolutePath());
        		                              uploadFileFacebook(f);
        		                                      		                                  		                            	  
    		                              }catch(Exception e)
    		                              {
    		                            	  e.printStackTrace();
    		                              }
    		                            
    		                                  //File file = new File(bitmap + ".png"); 
    		                              
    		                                                      
    		                         }
    		                       }).start();     
    		                
    					}
    				}
    	        }).executeAsync();
    	    //if not logged into facebook then set all the layout details to null	
    	    } else if (state.isClosed()) {
    	        Log.i("FACEBOOK", "Logged out...");
    	        isFbLoggedIn = false;
    	        
    	    }
    	}
    	//---------------------------------------
}
//http://stackoverflow.com/questions/18268880/reset-reload-fragment-container
//http://androidexample.com/Upload_File_To_Server_-_Android_Example/index.php?view=article_discription&aid=83&aaid=106
//http://www.androidpeople.com/android-load-image-url-example
//http://stackoverflow.com/questions/541966/how-do-i-do-a-lazy-load-of-images-in-listview
//http://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
