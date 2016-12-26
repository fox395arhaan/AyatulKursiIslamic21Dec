package com.app.ayatulkursiislamicnew;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    final static String MoreAppLink1 = "https://play.google.com/store/apps/details?id=com.app.six6kalimaislamic&hl=en";
    final static String MoreAppLink2 = "https://play.google.com/store/apps/details?id=com.app.fourqulhindi&hl=en";
    final static String MoreAppLink3 = "https://play.google.com/store/apps/details?id=com.currencyapp.currencyconverter&hl=en";
    final static String RateLink = "https://play.google.com/store/apps/details?id=com.app.ayatulkursiislamicnew";

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.main_screen_nav_bar_app_icon);

        // Ads
        //        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getResources().getString(R.string.ads_app_id));

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) findViewById(R.id.ad_view);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(android_id)
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

        // Interstitial Ad
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstial_ad_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        // arabic
        final Button arabicButton = (Button)findViewById(R.id.btnArabic);
        arabicButton.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // pressed
                        arabicButton.setBackground(getResources().getDrawable(R.drawable.mainbutton_arabic_pressed));
                        break;
                    case MotionEvent.ACTION_UP:
                        // released
                        arabicButton.setBackground(getResources().getDrawable(R.drawable.mainbutton_arabic));
                        break;
                }
                return false;
            }
        });
        arabicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCommonActivity(0);
            }
        });

        // english
        final Button englishButton = (Button) findViewById(R.id.btnEnglish);
        englishButton.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // pressed
                        englishButton.setBackground(getResources().getDrawable(R.drawable.mainbutton_english_pressed));
                        break;
                    case MotionEvent.ACTION_UP:
                        // released
                        englishButton.setBackground(getResources().getDrawable(R.drawable.mainbutton_english));
                        break;
                }
                return false;
            }
        });
        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCommonActivity(1);
            }
        });

        // Urdu
        final Button urduButton = (Button) findViewById(R.id.btnUrdu);
        urduButton.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // pressed
                        urduButton.setBackground(getResources().getDrawable(R.drawable.mainbutton_urdu_pressed));
                        break;
                    case MotionEvent.ACTION_UP:
                        // released
                        urduButton.setBackground(getResources().getDrawable(R.drawable.mainbutton_urdu));
                        break;
                }
                return false;
            }
        });
        urduButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCommonActivity(2);
            }
        });
    }
    void gotoCommonActivity(int index)
    {
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        }
//        else {
            Intent intent = new Intent(MainActivity.this, CommonActivity.class);
            Bundle b = new Bundle();
            b.putInt("lang", index);
            intent.putExtras(b);
            startActivity(intent);
//        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(android_id).build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rate) {
            Log.d("Main", "settings");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Rate Us");
            alertDialogBuilder.setMessage("")
                    .setCancelable(true)
                    .setNegativeButton("Rate now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse
                                    (RateLink));
                            startActivity(intent);
                        }
                    })
                    .setPositiveButton("Later", null);
            alertDialogBuilder.show();

            return true;
        }
        else if (id == R.id.action_share) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Please Check out this Amazing App " + getResources().getString(R.string.app_name) + " , \n " +
                    RateLink);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
        else if (id == R.id.action_moreapp1) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse
                    (MoreAppLink1));
            startActivity(intent);
        }
        else if (id == R.id.action_moreapp2) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse
                    (MoreAppLink2));
            startActivity(intent);
        }
        else if (id == R.id.action_moreapp3) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse
                    (MoreAppLink3));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
