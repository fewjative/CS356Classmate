package edu.csupomona.classmate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.abstractions.Section;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.utils.TextWatcherAdapter;

public class SectionReviewActivity extends Activity implements View.OnClickListener, Constants {
	private RatingBar rbRating;
	private EditText etTitle;
	private EditText etReview;
	private Button btnSubmitReview;

	private User user;
	private Section section;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.section_review_adapter_layout);
		setResult(RESULT_CANCELED);

		user = getIntent().getParcelableExtra(INTENT_KEY_USER);
		section = getIntent().getParcelableExtra(INTENT_KEY_SECTION);

		rbRating = (RatingBar)findViewById(R.id.rbRating);
		rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				btnSubmitReview.setEnabled(true);
			}
		});

		etTitle = (EditText)findViewById(R.id.etTitle);
		etReview = (EditText)findViewById(R.id.etReview);
		TextWatcher tw = new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				btnSubmitReview.setEnabled(etTitle.getText().length() > 0 && etReview.getText().length() > 0);
			}
		};

		etTitle.addTextChangedListener(tw);
		etReview.addTextChangedListener(tw);


		btnSubmitReview = (Button)findViewById(R.id.btnSubmitReview);
		btnSubmitReview.setOnClickListener(this);
		btnSubmitReview.setEnabled(false);
	}

	public void onClick(View v) {

		final ProgressDialog loadingDialog = new ProgressDialog(this);
		loadingDialog.setTitle("Submitting Review");
		loadingDialog.setMessage("Pushing review to server.");
		loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		//loadingDialog.setCancelable(true);
		loadingDialog.show();

		RequestParams params = new RequestParams();
		params.put("class_id", Integer.toString(section.getClassID()));
		params.put("user_id", Long.toString(user.getID()));
		params.put("text", etReview.getText().toString());
		params.put("rating", Float.toString(rbRating.getRating()));
		params.put("title", etTitle.getText().toString());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://lol-fc.com/classmate/createreview.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {

				if(!loadingDialog.isShowing())
				{
					return;
				}

				loadingDialog.dismiss();

				setResult(RESULT_OK);
				finish();
			}
		});
	}
}
