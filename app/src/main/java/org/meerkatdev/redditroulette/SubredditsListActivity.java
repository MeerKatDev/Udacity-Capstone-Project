package org.meerkatdev.redditroulette;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.Menu;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PostsListActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SubredditsListActivity extends AppCompatActivity {

    private static final String TAG = SubredditsListActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddits_list);
        Activity activityCtx = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // No back button needed
        // TODO welcome username!
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        );

        if(getResources().getBoolean(R.bool.is_tablet)) {

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
//            Log.d("PostListActivity","sending arguments");
//            Bundle arguments = new Bundle();
//            arguments.putString(Tags.SUBREDDIT_NAME,
//                    getIntent().getStringExtra(Tags.SUBREDDIT_NAME));
//            arguments.putString(Tags.ACCESS_TOKEN,
//                    getIntent().getStringExtra(Tags.ACCESS_TOKEN));
//            PostsListFragment fragment = new PostsListFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_posts_list, fragment)
//                    .commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

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
