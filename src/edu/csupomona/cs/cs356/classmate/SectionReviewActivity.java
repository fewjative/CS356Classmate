package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import static android.app.Activity.RESULT_OK;
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
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import static edu.csupomona.cs.cs356.classmate.LoginActivity.INTENT_KEY_USERID;
import static edu.csupomona.cs.cs356.classmate.SectionDetailsActivity.INTENT_KEY_SECTION;
import edu.csupomona.cs.cs356.classmate.abstractions.Section;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

public class SectionReviewActivity extends Activity implements View.OnClickListener {
	private RatingBar rbRating;
	private EditText etTitle;
	private EditText etReview;
	private Button btnSubmitReview;

	private Section section;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.section_review_layout);
		setResult(RESULT_CANCELED);

		section = getIntent().getParcelableExtra(INTENT_KEY_SECTION);
		id = getIntent().getIntExtra(INTENT_KEY_USERID, NULL_USER);

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
		final ProgressDialog pg = ProgressDialog.show(this, "Submitting Review", "Pushing review to server.");

		RequestParams params = new RequestParams();
		params.put("class_id", Integer.toString(section.getClassID()));
		params.put("user_id", Integer.toString(id));
		params.put("text", etReview.getText().toString());
		params.put("rating", Float.toString(rbRating.getRating()));
		params.put("title", etTitle.getText().toString());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://lol-fc.com/classmate/createreview.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				pg.dismiss();
				setResult(RESULT_OK);
				finish();
			}
		});
	}
}
