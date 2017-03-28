package com.holdyourcolour.instahelper.network;

import android.util.Log;

import com.holdyourcolour.instahelper.database.InstagramSession;
import com.holdyourcolour.instahelper.model.User;
import com.holdyourcolour.instahelper.view.activities.LoginActivity;
import com.holdyourcolour.instahelper.view.activities.MainActivity;
import com.holdyourcolour.instahelper.view.fragments.UsersListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by HoldYourColour on 27.03.2017.
 */

public class InstagramAPI {
    private static final String TAG = InstagramAPI.class.getSimpleName();

    private static final String CLIENT_ID = "06b246a5d1de49bcaad4089b0b82467a";
    private static final String CLIENT_SECRET = "55932bfe52b94151b6f463ab8b98f829";
    public static final String CALLBACK = "http://www.slickremix.com/gammablog";
    public static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/"
            + "?client_id="
            + CLIENT_ID
            + "&redirect_uri="
            + CALLBACK
            + "&response_type=code&display=touch&scope=likes+comments+relationships+follower_list";

    public static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";
    private static final int TAG_FOLLOWERS = 101;
    private static final int TAG_FOLLOWING = 102;
    private static List<String> sRequestUnFollowersCountList, sRequestUnAcceptedCountList;

    private OkHttpClient mClient;
    private static InstagramAPI sInstance;

    private InstagramAPI(){
        mClient = new OkHttpClient();
    }

    public static InstagramAPI getInstance(){
        if (sInstance == null)
            sInstance = new InstagramAPI();
        return sInstance;
    }

    public static void getUserInformation(MainActivity activity){
        InstagramSession session = InstagramSession.getInstance(activity);
        Request request = new Request.Builder().url(API_URL + "/users/" +
                session.getId() + "/?access_token=" + session.getAccessToken()).build();
        getInstance().mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "getUserInformation onFailure", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "getUserInformation onResponse " + result);
            }
        });
    }

    private static void addUserToList(final int tag, final UsersListFragment fragment, InstagramSession session, final User user){
        Request request = new Request.Builder().url(API_URL + "/users/" + user.getId() + "/relationship?access_token=" +
                session.getAccessToken()).build();
        getInstance().mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "getFollowers onFailure", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "getFollowers onResponse " + result);
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject data = jsonObj.getJSONObject("data");
                    String incomingStatus = data.getString("incoming_status");
                    String outgoingStatus = data.getString("outgoing_status");
                    switch (tag){
                        case TAG_FOLLOWERS:
                            Log.d(TAG, "TAG_FOLLOWERS sRequestUnFollowersCountList " + sRequestUnFollowersCountList.size());
                            sRequestUnFollowersCountList.remove(user.getId());
                            Log.d(TAG, "TAG_FOLLOWERS sRequestUnFollowersCountList " + sRequestUnFollowersCountList.size());
                            if (incomingStatus.equalsIgnoreCase("none")){
                                Log.d(TAG, "TAG_FOLLOWERS none");
                                UsersListFragment.sUnFollowersList.add(user);
                                if(sRequestUnFollowersCountList.size() == 0){
                                    Log.d(TAG, "TAG_FOLLOWERS size=0");
                                    fragment.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d(TAG, "TAG_FOLLOWERS run()");
                                            fragment.setUnFollowers();
                                        }
                                    });
                                }
                            }
                            break;
                        case TAG_FOLLOWING:
                            Log.d(TAG, "TAG_FOLLOWING sRequestUnAcceptedCountList " + sRequestUnAcceptedCountList.size());
                            sRequestUnAcceptedCountList.remove(user.getId());
                            Log.d(TAG, "TAG_FOLLOWING sRequestUnAcceptedCountList " + sRequestUnAcceptedCountList.size());
                            if (outgoingStatus.contentEquals("none")){
                                UsersListFragment.sUnAcceptedList.add(user);
                                if(sRequestUnAcceptedCountList.size() == 0){
                                    fragment.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            fragment.setUnAccepted();
                                        }
                                    });
                                }
                            }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getUnFollowers(UsersListFragment fragment, List<User> followersList) {
        InstagramSession session = InstagramSession.getInstance(fragment.getContext());
        sRequestUnFollowersCountList = Collections.synchronizedList(new ArrayList<String>());
        for (User user : followersList){
            sRequestUnFollowersCountList.add(user.getId());
            addUserToList(TAG_FOLLOWERS, fragment, session, user);
        }
    }

    public static void getUnAccepted(UsersListFragment fragment, List<User> followingList) {
        InstagramSession session = InstagramSession.getInstance(fragment.getContext());
        sRequestUnAcceptedCountList = Collections.synchronizedList(new ArrayList<String>());
        for (User user : followingList){
            sRequestUnAcceptedCountList.add(user.getId());
            addUserToList(TAG_FOLLOWING, fragment, session, user);
        }
    }

    public static void getFollowers(final UsersListFragment fragment){
        InstagramSession session = InstagramSession.getInstance(fragment.getContext());
        Request request = new Request.Builder().url(API_URL +"/users/self/followed-by?access_token=" +
                session.getAccessToken()).build();
        getInstance().mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "getFollowers onFailure", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "getFollowers onResponse " + result);
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray arr = jsonObj.getJSONArray("data");
                    Log.d(TAG, "FOLLOWED BY COUNT: " + arr.length());
                    final List<User> list = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++){
                        String id = ((JSONObject)arr.get(i)).getString("id");
                        String username = ((JSONObject)arr.get(i)).getString("username");
                        String profilePicture = ((JSONObject)arr.get(i)).getString("profile_picture");
                        String fullName = ((JSONObject)arr.get(i)).getString("full_name");
                        list.add(new User(fullName, profilePicture, username, id));
                    }
                    fragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fragment.setFollowers(list);
                        }
                    });
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getFollowing(final UsersListFragment fragment){
        InstagramSession session = InstagramSession.getInstance(fragment.getContext());
        Request request = new Request.Builder().url(API_URL +"/users/self/follows?access_token=" +
                session.getAccessToken()).build();
        getInstance().mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "getFollowing onFailure", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "getFollowing onResponse " + result);
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray arr = jsonObj.getJSONArray("data");
                    Log.d(TAG, "FOLLOWING COUNT: " + arr.length());
                    final List<User> list = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++){
                        String id = ((JSONObject)arr.get(i)).getString("id");
                        String username = ((JSONObject)arr.get(i)).getString("username");
                        String profilePicture = ((JSONObject)arr.get(i)).getString("profile_picture");
                        String fullName = ((JSONObject)arr.get(i)).getString("full_name");
                        list.add(new User(fullName, profilePicture, username, id));
                    }
                    fragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fragment.setFollowing(list);
                        }
                    });
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getAccessToken(final LoginActivity loginActivity, String code) {
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("redirect_uri", CALLBACK)
                .add("grant_type", "authorization_code")
                .add("code", code)
                .build();
        Request request = new Request.Builder().url(TOKEN_URL).post(formBody).build();
        getInstance().mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "getAccessToken onFailure",e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "getAccessToken onResponse " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject user = jsonObject.getJSONObject("user");
                    String accessToken = jsonObject.getString("access_token");
                    String userName = user.getString("username");
                    String fullName = user.getString("full_name");
                    String id = user.getString("id");
                    String profilePicture = user.getString("profile_picture");

                    // save to preferences
                    InstagramSession session = InstagramSession.getInstance(loginActivity);
                    session.storeAccessToken(accessToken, id, userName, fullName, profilePicture);
                    // start main activity
                    loginActivity.goToMainActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
