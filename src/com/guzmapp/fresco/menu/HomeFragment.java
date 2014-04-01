package com.guzmapp.fresco.menu;

import com.guzmapp.fresco.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class HomeFragment extends Fragment {
	private WebView web;
	
	public HomeFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        web=(WebView) rootView.findViewById(R.id.webView1);
        web.loadUrl("file:///android_asset/home.html");
        return rootView;
    }
}
