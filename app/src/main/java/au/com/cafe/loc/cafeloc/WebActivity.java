package au.com.cafe.loc.cafeloc;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import static au.com.cafe.loc.cafeloc.R.id.webview;

/**
 * Created by priju.jacobpaul on 21/12/2016.
 */

public class WebActivity extends AppCompatActivity {

    public static final String BUNDLE_URL_ID = "url";

    @BindView(webview)
    WebView mWebView;

    private ProgressDialog mProgressDialog;
    private final String TAG = WebActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.layout_webactivity);
        ButterKnife.bind(this);

        mWebView.getSettings().setJavaScriptEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                Log.d(TAG, "Progress: " + progress);

                if (mProgressDialog == null) {
                    mProgressDialog = ProgressDialog.show(WebActivity.this, getString(R.string.app_name), getString(R.string.loading), true);
                }

                if (mProgressDialog.isShowing() && progress == 100) {
                    mProgressDialog.dismiss();
                }

            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebActivity.this, "Oops! " + description, Toast.LENGTH_SHORT).show();
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });

        if (getIntent() != null) {
            mWebView.loadUrl(getIntent().getStringExtra(BUNDLE_URL_ID));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
