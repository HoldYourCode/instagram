package com.holdyourcolour.instahelper.model;

/**
 * Created by HoldYourColour on 27.03.2017.
 */

public class User {
    private String mFullName;
    private String mProfilePictureURL;
    private String mUsername;
    private String mId;

    public User(String fullName, String profilePictureURL, String username, String id) {
        mFullName = fullName;
        mProfilePictureURL = profilePictureURL;
        mUsername = username;
        mId = id;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public String getProfilePictureURL() {
        return mProfilePictureURL;
    }

    public void setProfilePicture(String profilePictureURL) {
        mProfilePictureURL = profilePictureURL;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "mFullName='" + mFullName + '\'' +
                ", mProfilePictureURL='" + mProfilePictureURL + '\'' +
                ", mUsername='" + mUsername + '\'' +
                ", mId='" + mId + '\'' +
                '}';
    }
}

