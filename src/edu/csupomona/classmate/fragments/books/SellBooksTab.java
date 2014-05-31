package edu.csupomona.classmate.fragments.books;

import static edu.csupomona.classmate.Constants.INTENT_KEY_USER;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.fragments.friends.SearchUsersAdapter;
import edu.csupomona.classmate.utils.TextWatcherAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SellBooksTab extends Fragment implements View.OnClickListener, Constants {
	
	EditText etBookTitle;
	EditText etBookClassname;
	EditText etBookCondition;
	EditText etBookPrice;
	Button btnSellBook;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ViewGroup ROOT = (ViewGroup)inflater.inflate(R.layout.books_sell_tab_layout, null);
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		etBookTitle = (EditText)ROOT.findViewById(R.id.etBookTitle);
		etBookClassname = (EditText)ROOT.findViewById(R.id.etBookClassname);
		etBookCondition = (EditText)ROOT.findViewById(R.id.etBookCondition);
		etBookPrice = (EditText)ROOT.findViewById(R.id.etBookPrice);
		btnSellBook = (Button)ROOT.findViewById(R.id.btnSellBook);
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) ROOT.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;
		
		btnSellBook.setWidth(width / 2);
		btnSellBook.setOnClickListener(this);
		return ROOT;
	}
	
	public void onClick(View v) {

		RequestParams params = new RequestParams();
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		AsyncHttpClient client = new AsyncHttpClient();

		switch (v.getId()) {
			case R.id.btnSellBook:
				//could do asserts to check if content is empty or just use a textwatcher

				final ProgressDialog loadingDialog = new ProgressDialog(getActivity());
				loadingDialog.setTitle(getString(R.string.listingBook));
				loadingDialog.setMessage(getString(R.string.listingBookLoading));
				loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				loadingDialog.show();

				String title = etBookTitle.getText().toString();
				String classname = etBookClassname.getText().toString();
				String price = etBookPrice.getText().toString();
				String condition = etBookCondition.getText().toString();

				params.put("user_id", Long.toString(USER.getID()));
				params.put("title", title);
				params.put("classname", classname);
				params.put("price", price);
				params.put("condition", condition);

				client.get("http://www.lol-fc.com/classmate/2/createbooklist.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
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
							d.setTitle("Book could not be listed");
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
							d.setMessage("Your book can now be seen on the marketplace!");
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

							etBookTitle.setText("");
							etBookClassname.setText("");
							etBookPrice.setText("");
							etBookCondition.setText("");
							return;
						}
					}
				});
				break;
		}
	}
}
