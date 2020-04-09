package org.meerkatdev.redditroulette;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import org.meerkatdev.redditroulette.databinding.ActivityPostsListBinding;

/**
 * Activity, which shows:
 * mobile:
 *      PostsListFragment
 * tablet:
 *     PostsListFragment
 *     PostViewFragment
 */
public class PostsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPostsListBinding binding
                = ActivityPostsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.detailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view ->
//                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .show()
//        );


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            // This ID represents the Home or Up button. In the case of this
//            // activity, the Up button is shown. For
//            // more details, see the Navigation pattern on Android Design:
//            //
//            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
//            //
//            navigateUpTo(new Intent(this, SubredditsListActivity.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
