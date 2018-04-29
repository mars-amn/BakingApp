package nanodegree.bakingapp.android.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import nanodegree.bakingapp.android.R;
import nanodegree.bakingapp.android.fragments.RecipeFragment;

public class RecipeActivity extends AppCompatActivity {

    private String mVideoURL;
    private String mStepInstruction;
    private String mStepDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibe);

        Intent iRecipe = getIntent();

        if (iRecipe != null) {
            mVideoURL = iRecipe.getStringExtra(IngredientsActivity.INTENT_VIDEO_URL_KEY);
            mStepInstruction = iRecipe.getStringExtra(IngredientsActivity.INTENT_STEP_INSTRUCTIONS_KEY);
            mStepDescription = iRecipe.getStringExtra(IngredientsActivity.INTENT_DESCRIPTION_KEY);
        } else {
            Toast.makeText(this, R.string.error_upon_launch_message, Toast.LENGTH_SHORT).show();
            finish();
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(this,
                    R.string.connectivity_state_label, Toast.LENGTH_LONG).show();
            finish();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mStepDescription);
        }

        initRecipeFragment();

    }

    private void initRecipeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        RecipeFragment fragment = new RecipeFragment();
        fragment.setmVideoURL(mVideoURL);
        fragment.setmStepInstructions(mStepInstruction);

        fragmentManager.beginTransaction()
                .replace(R.id.recipeFragmentContainer, fragment)
                .commit();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
