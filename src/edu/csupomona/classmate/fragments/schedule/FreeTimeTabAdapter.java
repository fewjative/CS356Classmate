package edu.csupomona.classmate.fragments.schedule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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





import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.fragments.ScheduleFragment;
import edu.csupomona.classmate.fragments.schedule.*;

import java.util.List;

public class FreeTimeTabAdapter extends ArrayAdapter<User> implements View.OnClickListener {
        public FreeTimeTabAdapter(Context context, List<User> friends) {
                super(context, 0, friends);
        }

        private static class ViewHolder {
                final ImageView avatar;
                final TextView tvItemTextUsername;
                final ImageButton btnCompare;
 

                ViewHolder(ImageView avatar, TextView tvItemTextUsername, ImageButton btnCompare) {
                        this.avatar = avatar;
                        this.tvItemTextUsername = tvItemTextUsername;
                        this.btnCompare = btnCompare;
                }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                User f = getItem(position);
                ViewHolder holder = null;
                View view = convertView;

                if (view == null) {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.freetime_list_list_item, null);

                        ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
                        TextView tvItemTextUsername = (TextView)view.findViewById(R.id.tvItemTextUsername);
                        ImageButton btnCompare = (ImageButton)view.findViewById(R.id.btnCompare);
                        view.setTag(new ViewHolder(ivAvatar, tvItemTextUsername, btnCompare));

                        tvItemTextUsername.setSelected(true);

                        btnCompare.setTag(f);
                        btnCompare.setOnClickListener(this);

                 
                }

                Object tag = view.getTag();
                if (tag instanceof ViewHolder) {
                        holder = (ViewHolder)tag;
                }

                if (f != null && holder != null) {
                        if (holder.tvItemTextUsername != null) {
                                SpannableStringBuilder builder = new SpannableStringBuilder();

                                SpannableString username = new SpannableString(f.getUsername());
                                username.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                builder.append(username);

                                SpannableString email = new SpannableString(String.format(" (%s)", f.getEmail()));
                                email.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.cppgold)), 0, email.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                builder.append(email);

                                holder.tvItemTextUsername.setText(builder);
                        }

                        if (holder.avatar != null) {
                                holder.avatar.setVisibility(View.VISIBLE);
                                f.loadAvatar(holder.avatar);
                        }
                }

                return view;
        }

        public void onClick(View v) {
                User r = (User)v.getTag();
                switch (v.getId()) {
                        case R.id.btnCompare:
                                compareSchedule(r);
                                break;
                }
        }

        public void compareSchedule(final User f) {
                                
            	if (getContext() instanceof FragmentActivity) {
        			Fragment newFragment = new FreeTimeScheduleFragment(f.getID());
        			FragmentActivity activity = ((FragmentActivity)getContext());
        			FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();
        			t.replace(R.id.flContentFrame, newFragment);
        			t.addToBackStack(null);
        			t.commit();
        		}
                
              
        }
}