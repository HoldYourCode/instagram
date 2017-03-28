package com.holdyourcolour.instahelper.controller.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.holdyourcolour.instahelper.R;
import com.holdyourcolour.instahelper.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HoldYourColour on 28.03.2017.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>  {
    private static final String TAG = UsersAdapter.class.getSimpleName();
    private List<User> mUsers;
    private Fragment mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public UsersAdapter(Fragment context, List<User> users) {
        mContext = context;
        mUsers = users;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.mFullNameTextView.setText(user.getFullName());
        holder.mUserNameTextView.setText(user.getUsername());
        Picasso.with(mContext.getContext()).load(user.getProfilePictureURL()).into(holder.mProfilePicture);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mUsers == null){
            return 0;
        }
        return mUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mUserNameTextView, mFullNameTextView;
        ImageView mProfilePicture;
        ViewHolder(View v) {
            super(v);
            mUserNameTextView = (TextView) v.findViewById(R.id.username_textview);
            mFullNameTextView = (TextView) v.findViewById(R.id.full_name_textview);
            mProfilePicture = (ImageView) v.findViewById(R.id.profile_picture_imageview);
        }
    }
}
