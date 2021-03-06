package edu.csupomona.classmate.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Instances;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Schedule;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.utils.TextWatcherAdapter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//allow for a user to upload a photo/choose a photo for their profile picture
//allow a user to make multiple schedules and choose the schedule from the settings fragment

public class SettingsFragment extends Fragment implements View.OnClickListener, Constants {
	private Button btnChangePass;
	private EditText etOldPass;
	private EditText etNewPass1;
	private EditText etNewPass2;
	private Button btnCreateSchedule;
	private Button btnSetActiveSchedule;
	private Button btnImportCalendar;
	private ImageView ivProfilePic;
	private EditText etScheduleName;
	private LinearLayout llNoSchedule;
	private LinearLayout llHasSchedule;
	private ViewGroup root;
	private Spinner sSchedule;
	private Schedule schActive = null;
	private boolean isFirst = true;
	ProgressDialog dialog = null;
	private final String upLoadServerUri = "http://www.lol-fc.com/classmate/UploadToServer.php";
	String imageFile;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		schActive = null;
		root = (ViewGroup)inflater.inflate(R.layout.settings_fragment_layout, null);

		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);

		ivProfilePic = (ImageView)root.findViewById(R.id.ivProfilePic);
		ivProfilePic.setOnClickListener(this);
		ivProfilePic.setEnabled(true);
		USER.loadAvatar(ivProfilePic);

		btnChangePass = (Button)root.findViewById(R.id.btnChangePass);
		btnChangePass.setOnClickListener(this);
		btnChangePass.setEnabled(false);

		btnCreateSchedule = (Button)root.findViewById(R.id.btnCreateSchedule);
		btnCreateSchedule.setOnClickListener(this);
		btnCreateSchedule.setEnabled(false);

		btnSetActiveSchedule = (Button)root.findViewById(R.id.btnSetActive);
		btnSetActiveSchedule.setOnClickListener(this);
		btnSetActiveSchedule.setEnabled(true);

		btnImportCalendar = (Button)root.findViewById(R.id.btnImportCalendar);
		btnImportCalendar.setOnClickListener(this);

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
				if (!s1.isEmpty() && !s2.isEmpty() && s1.equals(s2)) {
					tvPasswordMatcher.setText(R.string.passwords_match);
					tvPasswordMatcher.setTextColor(getResources().getColor(R.color.green));
					btnChangePass.setEnabled(true);
				} else {
					tvPasswordMatcher.setText(R.string.passwords_do_not_match);
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
			}
		};

		etOldPass.addTextChangedListener(textWatcher);
		etNewPass1.addTextChangedListener(textWatcher);
		etNewPass2.addTextChangedListener(textWatcher);
		etScheduleName.addTextChangedListener(scheduleNameTextWatcher);

		RequestParams params = new RequestParams();
		params.put("user_id", Long.toString(USER.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/2/getusernumschedules.php", params, new AsyncHttpResponseHandler() {
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

		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("user_id", Long.toString(USER.getID()));

		client.get("http://www.lol-fc.com/classmate/2/getuserschedules.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				System.out.println(response);
				System.out.println(USER.getID());
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
				ArrayAdapter<Schedule> adapter = new ArrayAdapter<Schedule>(getActivity(), R.layout.custom_spinner_schedule_item, schedules);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				sSchedule.setAdapter(adapter);
			}
		});
	}

	public void onClick(View v) {

		RequestParams params = new RequestParams();
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		AsyncHttpClient client = new AsyncHttpClient();

		int id = v.getId();
		if (id == R.id.ivProfilePic) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			CharSequence[] options = new CharSequence[2];
			options[0] = getResources().getString(R.string.action_camera);
			options[1] = getResources().getString(R.string.action_gallery);
			builder.setCancelable(true);
			builder.setTitle(R.string.dialog_change_profile_pic)
				   .setItems(options, new DialogInterface.OnClickListener() {
					   public void onClick(DialogInterface dialog, int which) {
						   if (which == 0) {
							   startCamera();
						   } else if (which == 1) {
							   startGallery();
						   }
					   }
				   })
				   .setNegativeButton(R.string.global_action_cancel, new DialogInterface.OnClickListener() {
					   @Override
					   public void onClick(DialogInterface dialog, int id) {
						   // ...
					   }
				   });
			builder.show();
		} else if (id == R.id.btnChangePass) {
			assert etNewPass1.getText().toString().compareTo(etNewPass2.getText().toString()) == 0;
			final ProgressDialog loadingDialog = new ProgressDialog(getActivity());
			loadingDialog.setTitle(getString(R.string.changePass));
			loadingDialog.setMessage(getString(R.string.changePassLoading));
			loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			//loadingDialog.setCancelable(false);
			loadingDialog.show();
			String newpassword = etNewPass1.getText().toString();
			String oldpassword = etOldPass.getText().toString();
			params.put("user_id", Long.toString(USER.getID()));
			params.put("oldpassword", oldpassword);
			params.put("newpassword", newpassword);
			client.get("http://www.lol-fc.com/classmate/changepass.php", params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					if (!loadingDialog.isShowing()) {
						return;
					}

					loadingDialog.dismiss();

					long id;
					try {
						id = Integer.parseInt(response);
					} catch (NumberFormatException e) {
						id = NO_USER;
					}

					if (id <= NO_USER) {
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

						d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.global_action_okay), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
			                                        //setResult(RESULT_CANCELED);
								//finish();
							}
						});

						d.show();
						return;
					} else {
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

						d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.global_action_okay), new DialogInterface.OnClickListener() {
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
		} else if (id == R.id.btnCreateSchedule) {
			System.out.println("Clicked the create schedule button");
			String scheduleName = etScheduleName.getText().toString();
			System.out.println("scheduleName: " + scheduleName);
			String firstSchedule;
			if (isFirst) {
				firstSchedule = Integer.toString(1);
			} else {
				firstSchedule = "doesnt matter";
			}
			params.put("user_id", Long.toString(USER.getID()));
			params.put("title", scheduleName);
			params.put("new", firstSchedule);
			client.get("http://www.lol-fc.com/classmate/2/adduserschedule.php", params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {

					if (isFirst) {
						btnSetActiveSchedule.setVisibility(View.VISIBLE);
						sSchedule.setVisibility(View.VISIBLE);
						isFirst = false;
					}
					setupSpinner();
					etScheduleName.setText("");

				}
			});
		} else if (id == R.id.btnSetActive) {
			System.out.println("Clicked set active");
			if (schActive != null) {
				Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_SHORT).show();

				params.put("user_id", Long.toString(USER.getID()));
				params.put("schedule_id", Integer.toString(schActive.getScheduleID()));

				client.get("http://www.lol-fc.com/classmate/2/setuserschedule.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {

						Toast.makeText(getActivity(), "Schedule is set to active, resp: " + response + " schActive: " + schActive.getScheduleID(), Toast.LENGTH_SHORT).show();
					}
				});

			}
		} else if (id == R.id.btnImportCalendar) {
			importCalendar();
		}
	}

	private void startCamera() {
		Intent camera_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	    startActivityForResult(camera_intent, CODE_CAMERA_REQUEST);
	}

	private void startGallery() {
		Intent gallery_intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(gallery_intent, CODE_GALLERY_REQUEST);
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
   	 	int serverResponseCode = 0;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

             //dialog.dismiss();

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
         		 final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
                 String urlParameters = "user_id="+Long.toString(USER.getID())+"&fname="+fileName;
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

	private void uploadPhotoToServer() {
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		dialog = ProgressDialog.show(getActivity(), "", "Uploading file...", true);

		Toast.makeText(getActivity(), "USER ID: " + Long.toString(USER.getID()), Toast.LENGTH_SHORT).show();

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
	}

	public static Bitmap bmCropMiddle(Bitmap bitmap) {
		Bitmap result;
		int smallestDimension = 0;

		if(bitmap.getWidth() == bitmap.getHeight()) {
			return bitmap;
		} else if (bitmap.getWidth() < bitmap.getHeight()) {
			smallestDimension = bitmap.getWidth();
		} else if (bitmap.getWidth() > bitmap.getHeight()) {
			smallestDimension = bitmap.getHeight();
		}
		result = ThumbnailUtils.extractThumbnail(bitmap, smallestDimension, smallestDimension);

		return result;
	}
    //==================FOR UPLOADING PICTURES

	//needs to upload server as soon as it is called
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    //System.out.println("Request Code: " +requestCode);
	    //System.out.println("Result Code: " + resultCode);
	    //System.out.println("Intent: " + data.toString());
	    if(data==null) {
	    	return;
	    }

	    switch(requestCode) {
		    case 0:
		    	System.out.println("The user pressed the back button while in the gallery");
		    	break;
		    case CODE_CAMERA_REQUEST:
		        if(resultCode == FragmentActivity.RESULT_OK) {
		            // Need to find a way to upload photo taken to server
		        	// currently using fileinputstream, but since there is no saved directory for
		        	// photos taken with camera, need to upload the data directly
		        	Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
		            thumbnail = bmCropMiddle(thumbnail);
		        	// set default profile pic to specified bitmap
		            ivProfilePic.setImageBitmap(thumbnail);
		        }
		        break;
		    case CODE_GALLERY_REQUEST:
		        try { //if data != null
	    	       Uri photoUri = data.getData();
	    	       imageFile = getRealPathFromURI(photoUri);
	    	       // Do something with the photo based on Uri
	    	       Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
	    	       thumbnail = bmCropMiddle(thumbnail);
	    	       // Load the selected image into a preview
		           ivProfilePic.setImageBitmap(thumbnail);
		           uploadPhotoToServer();
		        } catch (Exception e) {
		        	Log.i("GALLERY", e.getMessage());
		        }
		        break;
	    }
	}

	public void importCalendar() {
		//check available calendars on the device
		final String[] PROJECTION =
		    new String[] {
	            Calendars._ID,
	            Calendars.NAME,
	            Calendars.ACCOUNT_NAME,
	            Calendars.ACCOUNT_TYPE
	            };
		Cursor calCursor =
			getActivity().getContentResolver().query(
			Calendars.CONTENT_URI,
            PROJECTION,
            Calendars.VISIBLE + " = 1",
            null,
            Calendars._ID + " ASC");
		if (calCursor.moveToFirst()) {
		   do {
		      long id = calCursor.getLong(0);
		      String displayName = calCursor.getString(1);
		      String accountName = calCursor.getString(2);
		      String accountType = calCursor.getString(3);
		      System.out.println("id: " + id);
		      System.out.println("display name: " + displayName);
		      System.out.println("account name: " + accountName);
		      System.out.println("account type: " + accountType + "\n");
		      // ...
		   } while (calCursor.moveToNext());
		}



		String[] projection = new String[] { "calendar_id", "title",
                "dtstart", "dtend", "eventLocation"  };

		ContentResolver cr = getActivity().getContentResolver();
		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
		// Specify the date range you want to search for recurring
		// event instances
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(
				beginTime.get(Calendar.YEAR),
				beginTime.get(Calendar.MONTH) + 1,
				beginTime.get(Calendar.DAY_OF_MONTH),
				beginTime.get(Calendar.HOUR_OF_DAY),
				beginTime.get(Calendar.MINUTE));
		long startMillis = beginTime.getTimeInMillis();
		Calendar endTime = Calendar.getInstance();
		endTime.set(endTime.get(Calendar.YEAR), 12, 30, 0, 0);
		long endMillis = endTime.getTimeInMillis();
		ContentUris.appendId(builder, startMillis);
		ContentUris.appendId(builder, endMillis);

		Cursor cursor = cr.query(builder.build(),
                projection,
                null,
                null,
                null);

		User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		int startDay, startMonth, startYear, endDay, endMonth, endYear, startHour, startMinute, endHour, endMinute;
		String FPUBLIC = "0";
		String OPUBLIC = "0";
		String ISPRIVATE = "1";

		while(cursor.moveToNext()) {
			long id = 0;
			String title = null;
			long dtstart = 0;
			long dtend = 0;
			String eventLocation = null;

			id = cursor.getLong(0);
			title = cursor.getString(1);
			dtstart = cursor.getLong(2);
			dtend = cursor.getLong(3);
			eventLocation = cursor.getString(4);

			Calendar calendar = Calendar.getInstance();
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:m a");

			calendar.setTimeInMillis(dtstart);
			formatter = new SimpleDateFormat("dd");
			startDay = Integer.parseInt(formatter.format(calendar.getTime()));
			formatter = new SimpleDateFormat("MM");
			startMonth = Integer.parseInt(formatter.format(calendar.getTime()));
			formatter = new SimpleDateFormat("yyyy");
			startYear = Integer.parseInt(formatter.format(calendar.getTime()));
			formatter = new SimpleDateFormat("H");
			startHour = Integer.parseInt(formatter.format(calendar.getTime()));
			formatter = new SimpleDateFormat("m");
			startMinute = Integer.parseInt(formatter.format(calendar.getTime()));

			calendar.setTimeInMillis(dtend);
			formatter = new SimpleDateFormat("dd");
			endDay = Integer.parseInt(formatter.format(calendar.getTime()));
			formatter = new SimpleDateFormat("MM");
			endMonth = Integer.parseInt(formatter.format(calendar.getTime()));
			formatter = new SimpleDateFormat("yyyy");
			endYear = Integer.parseInt(formatter.format(calendar.getTime()));
			formatter = new SimpleDateFormat("H");
			endHour = Integer.parseInt(formatter.format(calendar.getTime()));
			formatter = new SimpleDateFormat("m");
			endMinute = Integer.parseInt(formatter.format(calendar.getTime()));

			System.out.println("id: " + id);
			System.out.println("title: " + title);
			System.out.println("date_start: " + startYear+"-"+startMonth+"-"+startDay);
			System.out.println("time_start: " + startHour+":"+startMinute);
			System.out.println("date_end: " + startYear+"-"+startMonth+"-"+startDay);
			System.out.println("time_end: " + endHour+":"+endMinute);
			System.out.println("location: " + eventLocation);

			RequestParams params = new RequestParams();
			params.put("user_id", Long.toString(USER.getID()));
			params.put("title", title);
			params.put("description", "Imported");
			params.put("time_start", startHour+":"+startMinute);
			params.put("time_end", endHour+":"+endMinute);
			params.put("date_start", startYear+"-"+startMonth+"-"+startDay);
			params.put("date_end", startYear+"-"+startMonth+"-"+startDay);
			params.put("weekdays", "N/A");
			params.put("fpublic", FPUBLIC);
			params.put("opublic", OPUBLIC);
			params.put("isprivate", ISPRIVATE);
			AsyncHttpClient client = new AsyncHttpClient();

			client.get("http://www.lol-fc.com/classmate/2/createevent.php", params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					System.out.println("response: " + response);

					long id;
					try {
						id = Integer.parseInt(response);
					} catch (NumberFormatException e) {
						id = NO_USER;
					}

					if (id <= NO_USER) {
						AlertDialog d = new AlertDialog.Builder(getActivity()).create();
						d.setTitle("Error With Event Creation");
						d.setMessage("We could not create the event as this time.");
						d.setIcon(android.R.drawable.ic_dialog_alert);
						d.setOnCancelListener(new DialogInterface.OnCancelListener() {
							public void onCancel(DialogInterface dialog) {
                                              // setResult(0);
								//finish();
							}
						});

						d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.global_action_okay), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
                                                    //setResult(RESULT_CANCELED);
								//finish();
							}
						});

						d.show();
						return;
					} else {
						AlertDialog d = new AlertDialog.Builder(getActivity()).create();
						d.setTitle("Success!");
						d.setMessage("The event has been created!");
						d.setIcon(android.R.drawable.ic_dialog_alert);
						d.setOnCancelListener(new DialogInterface.OnCancelListener() {
							public void onCancel(DialogInterface dialog) {
                                              // setResult(0);
								//finish();
							}
						});

						d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.global_action_okay), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
                                                    //setResult(RESULT_CANCELED);
								//finish();
							}
						});

						d.show();
						//set fields to be empty
						return;
					}

				}
			});
		}
	}

}
//http://stackoverflow.com/questions/18268880/reset-reload-fragment-container
