package org.meerkatdev.redditroulette;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.Menu;

import org.meerkatdev.redditroulette.MainActivity;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.databinding.ActivitySubredditsListBinding;


public class SubredditsListActivity extends AppCompatActivity {

    private static final String TAG = SubredditsListActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private ActivitySubredditsListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MobileAds.initialize(this, "ca-app-pub-6478297406439980~4551773625");
//        MobileAds.initialize(this, initializationStatus -> {
//            Log.d(TAG, "Initialized MobileAds!");
//        });
        binding = ActivitySubredditsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        // TODO welcome username!
        binding.toolbar.setTitle(getTitle());

        binding.fab.setOnClickListener(view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        );

//        AdView mAdView = findViewById(R.id.subr_activity_ad);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        if(getResources().getBoolean(R.bool.is_tablet)) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subreddit_sorting, menu);
        return true;
    }


    public void goBackWithShame(Activity activityCtx, String reason) {
        Intent backIntent = new Intent(activityCtx, MainActivity.class);
        backIntent.putExtra("error", reason);
        startActivity(backIntent);
    }

    @Override
    public void onBackPressed() {
        goBackWithShame(this, "to not trigger download");
    }
}
