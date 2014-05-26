package edu.csupomona.classmate.fragments.books;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.BookComment;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.fragments.friends.FriendRequestsAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class ViewBookFragment extends Fragment implements View.OnClickListener,Constants  {
	private long user_id;
	private long booklist_id;
	private long seller_id;
	private String title;
	private String classname;
	private String condition;
	private String price;
	TextView tvTitle;
	TextView tvClassname;
	TextView tvPrice;
	TextView tvCondition;
	EditText etComment;
	Button btnComment;
	ViewGroup ROOT;
	
	public ViewBookFragment(long user_id,long booklist_id,long seller_id, String title,String classname,String condition,String price)
	{
		this.user_id = user_id;
		this.booklist_id = booklist_id;
		this.seller_id = seller_id;
		this.title = title;
		this.classname = classname;
		this.condition = condition;
		this.price = price;
	}
	
	public ViewBookFragment()
	{
		this.user_id = 17;
		this.booklist_id = 3;
		this.seller_id = 17;
		this.title = "Jungle Book";
		this.classname = "ENG202";
		this.condition = "New";
		this.price = "3.50";
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ROOT = (ViewGroup)inflater.inflate(R.layout.books_comments_layout, null);
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		tvTitle = (TextView)ROOT.findViewById(R.id.tvTitle);
		tvTitle.setText("Book Title: " + title);
		
		tvClassname = (TextView)ROOT.findViewById(R.id.tvClassname);
		tvClassname.setText("For class: " + classname);
		
		tvCondition = (TextView)ROOT.findViewById(R.id.tvCondition);
		tvCondition.setText("In condition: " + condition);
		
		tvPrice = (TextView)ROOT.findViewById(R.id.tvPrice);
		tvPrice.setText("For this price: " + price);
		
		etComment = (EditText)ROOT.findViewById(R.id.etComment);
		btnComment = (Button)ROOT.findViewById(R.id.btnComment);
		
		btnComment.setOnClickListener(this);
		
		setUpComments();
		
		return ROOT;
	}

	private void setUpComments()
	{
		final LinearLayout llProgressBar = (LinearLayout)ROOT.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put("booklist_id", Long.toString(booklist_id));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/2/getbookcomments.php",params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				List<BookComment> comments = new LinkedList<BookComment>();

				try {
					JSONObject jObj;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						comments.add(new BookComment(
							jObj.getLong("comment_id"),
							jObj.getLong("user_id"),
							jObj.getString("username"),				
							jObj.getString("comment")
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				CommentsAdapter adapter = new CommentsAdapter(getActivity(),comments);
				ListView lvQueryResults = (ListView)ROOT.findViewById(R.id.lvQueryResults);
				if (adapter.isEmpty()) {
					TextView tvEmptyList = (TextView)ROOT.findViewById(R.id.tvEmptyList);
					tvEmptyList.setText(R.string.book_comment_list_empty);

					LinearLayout llEmptyList = (LinearLayout)ROOT.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvQueryResults.setAdapter(adapter);
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("booklist_id", Long.toString(booklist_id));
		params.put("user_id", Long.toString(user_id));
		String comment = etComment.getText().toString();
		params.put("comment", comment );
		
		final ProgressDialog loadingDialog = new ProgressDialog(getActivity());
		loadingDialog.setTitle("Adding your comment");
		loadingDialog.setMessage("Your comment is being added as we speak");
		loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadingDialog.show();
		
		System.out.println(Long.toString(booklist_id) + " " + Long.toString(user_id) + " " + comment);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/2/addbookcomment.php",params, new JsonHttpResponseHandler() {
			
			/*@Override
			public void onSuccess(String response) {
				System.out.println("response: " + response);
				if (!loadingDialog.isShowing()) {
					return;
				}

				loadingDialog.dismiss();

				int result;
				try {
					result = Integer.parseInt(response);
				} catch (NumberFormatException e) {
					result = NO_USER;
				}

				if (result <= NO_USER) {
					AlertDialog d = new AlertDialog.Builder(getActivity()).create();
					d.setTitle("Error");
					d.setMessage("Your comment could not be posted");
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
					etComment.setText("");
					setUpComments();
					return;
				}
			}
			
			public void onFailure(Throwable arg0, String arg1) {
		         Log.d("TAG", "Failure");        
		     }*/
			
		});
		
		if (!loadingDialog.isShowing()) {
	
		}

		loadingDialog.dismiss();
		
		etComment.setText("");
		setUpComments();
		
	}
}
