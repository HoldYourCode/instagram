package com.holdyourcolour.instahelper.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.holdyourcolour.instahelper.R;
import com.holdyourcolour.instahelper.controller.adapters.UsersAdapter;
import com.holdyourcolour.instahelper.model.User;
import com.holdyourcolour.instahelper.network.InstagramAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class UsersListFragment extends Fragment {

    private static final String TAG = UsersListFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final int FOLLOWERS = 0;
    private static final int FOLLOWING = 1;
    private static final int UN_FOLLOWERS = 2;
    private static final int UN_ACCEPTER = 3;
    private static List<User> sFollowersList;
    private static List<User> sFollowingList;
    public static List<User> sUnFollowersList = Collections.synchronizedList(new ArrayList<User>());
    public static List<User> sUnAcceptedList = Collections.synchronizedList(new ArrayList<User>());

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public UsersListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static UsersListFragment newInstance(int sectionNumber) {
        UsersListFragment fragment = new UsersListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        int position = getArguments().getInt(ARG_SECTION_NUMBER);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        displayView(position);
        return view;
    }

    private void displayView(int number) {
        switch (number){
            case FOLLOWERS:
                Log.d(TAG, "displayView FOLLOWERS");
                InstagramAPI.getFollowers(this);
                break;
            case FOLLOWING:
                Log.d(TAG, "displayView FOLLOWING");
                InstagramAPI.getFollowing(this);
                break;
            case UN_FOLLOWERS:
                Log.d(TAG, "displayView UN_FOLLOWERS");
                if (sFollowersList != null)
                    InstagramAPI.getUnFollowers(this, sFollowersList);
                break;
            case UN_ACCEPTER:
                Log.d(TAG, "displayView UN_ACCEPTER");
                if (sFollowingList != null)
                    InstagramAPI.getUnAccepted(this, sFollowingList);
                break;
        }
    }

    public void setFollowers(List<User> followersList){
        Log.d(TAG, "setFollowers " + followersList.size());
        sFollowersList = followersList;
        UsersAdapter adapter = new UsersAdapter(this, sFollowersList);
        mRecyclerView.setAdapter(adapter);
    }

    public void setFollowing(List<User> followingList){
        Log.d(TAG, "setFollowing " + followingList.size());
        sFollowingList = followingList;
        UsersAdapter adapter = new UsersAdapter(this, sFollowingList);
        mRecyclerView.setAdapter(adapter);
    }

    public void setUnFollowers(){
        Log.d(TAG, "setUnFollowers " + sUnFollowersList.size());
        UsersAdapter adapter = new UsersAdapter(this, sUnFollowersList);
        mRecyclerView.setAdapter(adapter);
    }

    public void setUnAccepted(){
        Log.d(TAG, "setUnAccepted " + sUnAcceptedList.size());
        UsersAdapter adapter = new UsersAdapter(this, sUnAcceptedList);
        mRecyclerView.setAdapter(adapter);
    }
}