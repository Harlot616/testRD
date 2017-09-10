package test.fugga.com.myapplication.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import test.fugga.com.myapplication.MainActivity;
import test.fugga.com.myapplication.R;

public class FragmentWebview extends Fragment {
    private WebView webContext;
    private String hitsUrl;
    private MainActivity ma;

    public FragmentWebview() {
        this.ma = (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.hitsUrl =  getArguments().getString("url");

        webContext = (WebView) view.findViewById(R.id.web_context);
        webContext.getSettings().setJavaScriptEnabled(true);
        webContext.loadUrl(hitsUrl);

        webContext.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }
}
