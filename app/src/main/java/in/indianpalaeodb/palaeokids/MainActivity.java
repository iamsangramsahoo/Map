package in.indianpalaeodb.palaeokids;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String HOME_URL = "https://www.indianpalaeodb.in/palaeokids";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(61,43,31));
        showNativeHome();
    }

    private TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setPadding(0, 10, 0, 10);
        return t;
    }

    private Button button(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        return b;
    }

    private void showNativeHome() {
        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(36, 30, 36, 36);
        root.setBackgroundColor(Color.rgb(247,241,232));
        scroll.addView(root);

        TextView brand = text("PALAEOKIDS", 13, Color.rgb(107,74,50));
        brand.setLetterSpacing(0.18f);
        root.addView(brand);

        TextView title = text("Explore Earth's past", 30, Color.rgb(20,20,20));
        title.setTypeface(null, 1);
        root.addView(title);
        root.addView(text("Real science, real fossils, real fun. Learn about fossils, pollen, rocks, sediments, geological time, ancient environments and the methods palaeoscientists use to reconstruct the past.", 17, Color.rgb(45,45,45)));

        Button open = button("Open PalaeoKids Learning Hub");
        open.setOnClickListener(v -> showWeb(HOME_URL));
        root.addView(open, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        root.addView(text("Explore", 22, Color.rgb(61,43,31)));
        String[] modules = {
                "Palaeo Game — quizzes, clues and ancient-life challenges",
                "Palaeo Art — explore deep time through creativity",
                "Palaeo Dictionary — simple explanations of scientific words",
                "Palaeo Map — discover important fossil sites across India",
                "Palaeo Picture Creator — reconstruct past worlds",
                "3D Explore — investigate palaeoscience objects and proxies",
                "Multiproxy Lab — learn how scientific instruments and proxies work"
        };
        for (String m : modules) {
            TextView card = text("•  " + m, 16, Color.rgb(25,25,25));
            card.setPadding(18, 12, 18, 12);
            root.addView(card);
        }

        root.addView(text("Offline mini-glossary", 22, Color.rgb(61,43,31)));
        root.addView(text("Fossil — preserved evidence of past life.\nPollen — microscopic grains produced by seed plants; fossil pollen helps reconstruct past vegetation.\nProxy — measurable evidence used to infer a past environmental condition.\nStratigraphy — the study of layered rocks and sediments.\nPalaeoclimate — climate conditions in Earth's past.\nRadiocarbon dating — a method used to estimate the age of organic material.", 16, Color.rgb(35,35,35)));

        Button site = button("Visit Indian Palaeo Database");
        site.setOnClickListener(v -> showWeb("https://www.indianpalaeodb.in/"));
        root.addView(site, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(scroll);
    }

    private boolean online() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        if (network == null) return false;
        NetworkCapabilities caps = cm.getNetworkCapabilities(network);
        return caps != null && (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    private void showWeb(String url) {
        if (!online()) {
            Toast.makeText(this, "No internet connection. The offline PalaeoKids home is still available.", Toast.LENGTH_LONG).show();
            return;
        }

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setBackgroundColor(Color.WHITE);

        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(6, 4, 6, 4);
        bar.setBackgroundColor(Color.rgb(61,43,31));

        Button home = button("Home");
        Button back = button("Back");
        Button refresh = button("Refresh");
        Button share = button("Share");

        home.setOnClickListener(v -> showNativeHome());
        back.setOnClickListener(v -> {
            if (webView != null && webView.canGoBack()) webView.goBack();
            else showNativeHome();
        });
        refresh.setOnClickListener(v -> {
            if (webView != null) webView.reload();
        });
        share.setOnClickListener(v -> shareCurrent());

        bar.addView(home);
        bar.addView(back);
        bar.addView(refresh);
        bar.addView(share);
        page.addView(bar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowContentAccess(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String host = uri.getHost() == null ? "" : uri.getHost();
                if (host.equals("www.indianpalaeodb.in") || host.equals("indianpalaeodb.in")) return false;
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                return true;
            }
        });

        page.addView(webView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        setContentView(page);
        webView.loadUrl(url);
    }

    private void shareCurrent() {
        String url = webView != null ? webView.getUrl() : HOME_URL;
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_TEXT, "Explore PalaeoKids: " + url);
        startActivity(Intent.createChooser(send, "Share PalaeoKids"));
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else showNativeHome();
    }
}
