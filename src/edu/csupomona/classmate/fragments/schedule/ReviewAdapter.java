package edu.csupomona.classmate.fragments.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Review;
import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {
	public ReviewAdapter(Context context, List<Review> reviews) {
		super(context, 0, reviews);
	}

	private static class ViewHolder {
		final TextView tvReviewTitle;
		final TextView tvUser;
		final TextView tvReview;
		final RatingBar rbRating;

		ViewHolder(TextView tvReviewTitle, TextView tvUser, TextView tvReview, RatingBar rbRating) {
			this.tvReviewTitle = tvReviewTitle;
			this.tvUser = tvUser;
			this.tvReview = tvReview;
			this.rbRating = rbRating;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Review r = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		//if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.review_list_adapter_layout, null);

			TextView tvReviewTitle = (TextView)view.findViewById(R.id.tvReviewTitle);
			TextView tvUser = (TextView)view.findViewById(R.id.tvUser);
			TextView tvReview = (TextView)view.findViewById(R.id.tvReview);
			RatingBar rbRating = (RatingBar)view.findViewById(R.id.rbRating);
			view.setTag(new ViewHolder(tvReviewTitle, tvUser, tvReview, rbRating));
		//}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (r != null && holder != null) {
			if (holder.tvReviewTitle != null) {
				holder.tvReviewTitle.setText(String.format("%s by ", r.getTitle()));
			}

			if (holder.tvUser != null) {
				holder.tvUser.setText(r.getUsername());
			}

			if (holder.tvReview != null) {
				holder.tvReview.setText(r.getText());
			}

			if (holder.rbRating != null) {
				holder.rbRating.setRating(r.getRating());
			}
		}

		return view;
	}
}