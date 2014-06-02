package edu.csupomona.classmate.fragments.books;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Book;
import edu.csupomona.classmate.abstractions.User;
import java.util.List;

public class AdminBookAdapter extends ArrayAdapter<Book> implements View.OnClickListener {
	private final User USER;

	public AdminBookAdapter(Context context, User user, List<Book> books) {
		super(context, 0, books);
		this.USER = user;
	}

	private static class ViewHolder {
		final ImageView ivAvatar;
		final TextView tvTitle;
		final ImageButton btnViewBook;
		final ImageButton btnDeleteBook;

		ViewHolder(ImageView ivAvatar,  TextView tvTitle,ImageButton btnViewBook,ImageButton btnDeleteBook) {
			this.ivAvatar = ivAvatar;
			this.tvTitle = tvTitle;
			this.btnViewBook = btnViewBook;
			this.btnDeleteBook = btnDeleteBook;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("getView called");
		Book f = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		//if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.book_admin_item_layout, null);
			ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
			TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
			ImageButton btnViewBook = (ImageButton)view.findViewById(R.id.btnViewBook);
			ImageButton btnDeleteBook = (ImageButton)view.findViewById(R.id.btnDeleteBook);

			view.setTag(new ViewHolder(ivAvatar, tvTitle, btnViewBook,btnDeleteBook));

			tvTitle.setSelected(true);

			btnViewBook.setTag(f);
			btnViewBook.setOnClickListener(this);
			btnViewBook.setVisibility(View.VISIBLE);

			btnDeleteBook.setTag(f);
			btnDeleteBook.setOnClickListener(this);
			btnDeleteBook.setVisibility(View.VISIBLE);
		//}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (f != null && holder != null) {

			if(holder.tvTitle !=null)
			{
				holder.tvTitle.setText(f.getTitle());
				System.out.println("title: "+ f.getTitle());
			}

			if(holder.btnViewBook !=null)
			{
				holder.btnViewBook.setVisibility(View.VISIBLE);
			}

			if(holder.btnDeleteBook !=null)
			{
				holder.btnDeleteBook.setVisibility(View.VISIBLE);
			}

			if (holder.ivAvatar != null) {
				holder.ivAvatar.setVisibility(View.VISIBLE);
				f.loadAvatar(holder.ivAvatar);
			}
		}

		return view;
	}

	public void onClick(View v) {
		Book r = (Book)v.getTag();
		switch (v.getId()) {
			case R.id.btnViewBook:
				viewBook(r);
				break;
			case R.id.btnDeleteBook:
				deleteBook(r);
				break;
		}
	}

	private void deleteBook(final Book b)
	{
		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_USERID, Long.toString(USER.getID()));
		params.put("booklist_id", Long.toString(b.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_REMOVEBOOK, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(b);
			}
		});
	}

	private void viewBook(final Book b) {
		   if (getContext() instanceof FragmentActivity) {

               long id = USER.getID();
               Fragment newFragment = new edu.csupomona.classmate.fragments.books.ViewBookFragment(id,b.getID(),b.getSellerID(),b.getTitle(),b.getClassname(),b.getCondition(),b.getPrice());
               FragmentActivity activity = ((FragmentActivity)getContext());
               FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();
               t.replace(R.id.flContentFrame, newFragment);
               t.addToBackStack(null);

               t.commit();
           }
	}
}
