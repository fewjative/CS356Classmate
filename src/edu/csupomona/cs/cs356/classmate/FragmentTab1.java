package edu.csupomona.cs.cs356.classmate;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
 
public class FragmentTab1 extends Fragment {//this is the fragment for adding a friend
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment1, container, false);
        
      final Button addFriend = (Button)rootView.findViewById(R.id.button1);
      final EditText etaddFriend = (EditText)rootView.findViewById(R.id.etaddFriend);
      addFriend.setEnabled(false);
      
      TextWatcher tw = new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				String s = etaddFriend.getText().toString();
				addFriend.setEnabled(true);
				
				if (s.isEmpty()) {
					addFriend.setEnabled(false);
					return;
				}

				
			}
		};
      
		etaddFriend.addTextChangedListener(tw);
      
      
  		addFriend.setOnClickListener(new View.OnClickListener() {


         public void onClick(View v) {
         	
         	//TextView view = (TextView)rootView.findViewById(R.id.textView2);
     
         	String userName = getActivity().getIntent().getExtras().getString(LoginActivity.KEY_USERNAME);
         	String friend = ((EditText)rootView.findViewById(R.id.etaddFriend)).getText().toString();
         	
         	AsyncHttpClient client = new AsyncHttpClient();
      		RequestParams params = new RequestParams();
      		
      		params.put("email", userName);
      		params.put("friend", friend);
      		client.get("http://www.lol-fc.com/classmate/addfriend.php", params, new AsyncHttpResponseHandler() {
      			@Override
      			public void onSuccess(String response) {
      				int id = Integer.parseInt(response);//we should definitely add some try and catch whenever we take data from server
      				
      				if (id==2) {
      					
      					AlertDialog d = new AlertDialog.Builder(getActivity()).create();
      					d.setTitle(R.string.addFriendSuccessTitle);
      					d.setMessage(getResources().getString(R.string.addFriendSuccess));
      					d.setIcon(android.R.drawable.ic_dialog_alert);
      					d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
      						public void onClick(DialogInterface dialog, int which) {
      							etaddFriend.setText("");
      							 dialog.dismiss();
      						}
      					});

      					d.show();
      				} else {
      				
      					
      					AlertDialog d = new AlertDialog.Builder(getActivity()).create();
      					d.setTitle(R.string.addFriendErrorTitle);
      					d.setMessage(getResources().getString(R.string.addFriendError));
      					d.setIcon(android.R.drawable.ic_dialog_alert);
      					d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
      						public void onClick(DialogInterface dialog, int which) {
      							 dialog.dismiss();
      						}
      					});

      					d.show();
      				}
      			}
      		});
      		
         }
     });
        return rootView;
    }
 
}