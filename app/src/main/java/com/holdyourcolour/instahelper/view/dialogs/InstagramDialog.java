package com.holdyourcolour.instahelper.view.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holdyourcolour.instahelper.network.InstagramAPI;

/**
 * Created by HoldYourColour on 27.03.2017.
 */

public class InstagramDialog extends Dialog {

    private String mUrl;
    private OAuthDialogListener mListener;
    private ProgressDialog mProgressDialog;
    private WebView mWebView;
    private LinearLayout mContent;
    private TextView mTitle;
    private static final String TAG = InstagramDialog.class.getSimpleName();

    public InstagramDialog(Activity context, String url, OAuthDialogListener listener) {
        super(context);
        mUrl = url;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setMessage("Loading...");
        mContent = new LinearLayout(getContext());
        mContent.setOrientation(LinearLayout.VERTICAL);
        setUpTitle();
        setUpWebView();
        addContentView(mContent, new FrameLayout.LayoutParams(480, 820));
    }
    private void setUpTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mTitle = new TextView(getContext()); mTitle.setText("Instagram");
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTypeface(Typeface.DEFAULT_BOLD);
        mTitle.setBackgroundColor(Color.BLACK);
        mContent.addView(mTitle);
    }
    private void setUpWebView() {
        mWebView = new WebView(getContext());
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new OAuthWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        mContent.addView(mWebView);
    }
    private class OAuthWebViewClient extends WebViewClient {
        @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirecting URL " + url);
            if (url.startsWith(InstagramAPI.CALLBACK)) {
                String urls[] = url.split("=");
                mListener.onComplete(urls[1]);
                InstagramDialog.this.dismiss();
                return true;
            } return false;
        }
        @Override public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d(TAG, "Page error: " + description);
            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onError(description); InstagramDialog.this.dismiss();
        }
        @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "Loading URL: " + url); super.onPageStarted(view, url, favicon);
            mProgressDialog.show();
        }
        @Override public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url); String title = mWebView.getTitle(); if (title != null && title.length() > 0) {
                mTitle.setText(title);
            }
            Log.d(TAG, "onPageFinished URL: " + url);
            mProgressDialog.dismiss();
        }
    }
    public interface OAuthDialogListener {
        public abstract void onComplete(String accessToken);
        public abstract void onError(String error);
    }
}
