package com.app.ayatulkursiislamicnew;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.MainThread;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class CommonActivity extends AppCompatActivity {

    boolean isPlaying = false;
    int currentLangIndex = 0;
    Button playButton;
    ImageView contentsView;
    MediaPlayer mp;
    SeekBar     seekVolume;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);


        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.main_screen_nav_bar_app_icon);

        currentLangIndex = 0;
        Bundle b = getIntent().getExtras();
        if (b != null) {
            currentLangIndex = b.getInt("lang");
        }

        contentsView = (ImageView)findViewById(R.id.ivContents);

        // Ads
        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) findViewById(R.id.ad_view);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        String android_id = Settings.Secure.getString(this.getContentResolver(),
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

        initSound();

        final AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        final int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekVolume = (SeekBar)findViewById(R.id.seekBar);
        seekVolume.setMax(maxVolume);
        seekVolume.setProgress(currentVolume);
        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        playButton = (Button)findViewById(R.id.btnPlay);
        playButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    pauseSound();
                }
                else {
                    playSound();
                }
            }
        });

        ImageButton leftMoveButton = (ImageButton) findViewById(R.id.btnMoveLeft);
        leftMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLangIndex > 0) {
                    updateLangWithIndex(--currentLangIndex);
                }
            }
        });

        ImageButton rightMoveButton = (ImageButton) findViewById(R.id.btnMoveRight);
        rightMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLangIndex < 2) {
                    updateLangWithIndex(++currentLangIndex);
                }
            }
        });

        updateLangWithIndex(currentLangIndex);
    }

    void updateLangWithIndex(int index) {

        String[] langs = getResources().getStringArray(R.array.language_list);
        if (index < 0 || index >= langs.length)
            return;

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        String language = langs[index];
        switch (index) {
            case 0: //arabic
                contentsView.setImageDrawable(getResources().getDrawable(R.drawable.ayatlkursi_screen_common_aya_arabic_text_image));
                break;
            case 1: // english
                contentsView.setImageDrawable(getResources().getDrawable(R.drawable.ayatlkursi_screen_common_aya_english_text_image));
                break;
            case 2: // urdu
                contentsView.setImageDrawable(getResources().getDrawable(R.drawable.ayatlkursi_screen_common_aya_urdu_text_image));
                break;
        }
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + " / " + language);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void playSound()
    {
        isPlaying = true;
        mp.start();
        playButton.setBackground(getResources().getDrawable(R.drawable.pausebutton));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void pauseSound()
    {
        isPlaying = false;
        mp.pause();
        playButton.setBackground(getResources().getDrawable(R.drawable.playbutton));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void stopSound()
    {
        isPlaying = false;
        playButton.setBackground(getResources().getDrawable(R.drawable.playbutton));
        mp.stop();
    }

    void initSound() {
        mp = MediaPlayer.create(this, R.raw.audio);
        mp.setVolume(0.5f, 0.5f);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopSound();
                initSound();
            }
        });
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(android_id).build();
        mInterstitialAd.loadAd(adRequest);
    }

    // volume
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                    seekVolume.setProgress(seekVolume.getProgress() + 1);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                    seekVolume.setProgress(seekVolume.getProgress() - 1);
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_common_layout, menu);
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
                                    (MainActivity.RateLink));
                            startActivity(intent);
                        }
                    })
                    .setPositiveButton("Later", null);
            alertDialogBuilder.show();

            return true;
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
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        stopSound();
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);

        stopSound();

        super.onBackPressed();
    }
}
