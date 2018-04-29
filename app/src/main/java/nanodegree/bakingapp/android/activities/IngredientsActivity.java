package nanodegree.bakingapp.android.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nanodegree.bakingapp.android.R;
import nanodegree.bakingapp.android.fragments.CakesFragment;
import nanodegree.bakingapp.android.fragments.IngredientsFragment;
import nanodegree.bakingapp.android.fragments.RecipeFragment;
import nanodegree.bakingapp.android.models.Cake;
import nanodegree.bakingapp.android.models.Ingredient;
import nanodegree.bakingapp.android.models.Steps;
import nanodegree.bakingapp.android.provider.CakeContract;
import nanodegree.bakingapp.android.widget.CakesWidgetService;

@SuppressWarnings("WeakerAccess")
public class IngredientsActivity extends AppCompatActivity implements IngredientsFragment.StepClickListener {

    public static final String INTENT_VIDEO_URL_KEY = "nanodegree.bakingapp.android.video_url";
    public static final String INTENT_STEP_INSTRUCTIONS_KEY = "nanodegree.bakingapp.android.step_instructions";
    public static final String INTENT_DESCRIPTION_KEY = "nanodegree.bakingapp.android.short_description_key";
    private static final String DUMMY_TEXT_TO_SHARE = "this is dummy text for testing purposes";

    private static Cake cake;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.menuFab)
    FloatingActionMenu mFabMenu;

    private IngredientsFragment mIngredientsFragment;
    private FragmentManager mFragmentManager;
    private List<Ingredient> mIngredientsList;
    private List<Steps> mStepsList;
    private String mCakeName;
    private boolean isInTwoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ButterKnife.bind(this);
        Intent cakeIntent = getIntent();

        if (cakeIntent != null && cakeIntent.hasExtra(CakesFragment.INTENT_KEY)) {
            cake = cakeIntent.getParcelableExtra(CakesFragment.INTENT_KEY);
            mCakeName = cake.getName();
            mIngredientsList = cake.getIngredients();
            mStepsList = cake.getSteps();
        } else {
            Toast.makeText(this, R.string.error_upon_launch_message, Toast.LENGTH_SHORT).show();
            finish();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mCakeName);
        }

        mFragmentManager = getSupportFragmentManager();

        isInTwoPaneMode = findViewById(R.id.twoPaneLayout) != null;

        if (savedInstanceState == null) {
            initIngredientsFragment();
        }
    }

    @OnClick(R.id.shareFab)
    public void onShareIngredientsClick() {
        String ingredients = "These are the Ingredients that required to make " +
                mCakeName + " :\n " +
                getIngredientsInOnePiece();
        shareIngredients(ingredients);
    }

    private String getIngredientsInOnePiece() {
        StringBuilder ingredientToBeSaved = new StringBuilder();
        DecimalFormat quantityFormatter = new DecimalFormat("0.#");

        for (int i = 0; i < mIngredientsList.size(); i++) {
            double quantity = mIngredientsList.get(i).getQuantity();
            ingredientToBeSaved.append(quantityFormatter.format(quantity))
                    .append(" ")
                    .append(getMeasuringUnit(mIngredientsList.get(i).getMeasure()))
                    .append("of ")
                    .append(mIngredientsList.get(i).getIngredient())
                    .append("\n");
        }
        return ingredientToBeSaved.toString();
    }

    /**
     * do you think I have a problem with attention to details? 0/
     * I hope not :)
     */
    private String getMeasuringUnit(String measure) {
        String unit;
        switch (measure) {
            case "CUP":
                unit = "Cup/s ";
                break;
            case "TBLSP":
                unit = "Table spoon/s ";
                break;
            case "TSP":
                unit = "Tea spoon/s ";
                break;
            case "K":
                unit = "Kilogram/s ";
                break;
            case "G":
                unit = "Grams ";
                break;
            case "OZ":
                unit = "ounces ";
                break;
            case "UNIT":
                unit = "Units ";
                break;
            default:
                unit = "as much as you like";
        }
        return unit;
    }

    private void shareIngredients(String ingredientsToShare) {
        ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(ingredientsToShare) //(TODO) replace this parameter with DUMMY_TEXT_TO_SHARE
                .setChooserTitle(R.string.share_chooser_label)
                .startChooser();
    }

    @OnClick(R.id.addToWidgetFab)
    public void prepareTheIngredients() {
        long timeAdded = System.currentTimeMillis();
        addIngredientIntoDatabase(mCakeName, getIngredientsInOnePiece(), timeAdded);
    }

    private void addIngredientIntoDatabase(String cakeName, String ingredients, long date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CakeContract.CakesEntry.CAKE_NAME_COLUMN, cakeName);
        contentValues.put(CakeContract.CakesEntry.INGREDIENTS_COLUMN, ingredients);
        contentValues.put(CakeContract.CakesEntry.TIME_STAMP_COLUMN, date);

        Uri uri = getContentResolver().insert(CakeContract.CakesEntry.CONTENT_URI, contentValues);
        onInsertingCompleted(uri);
    }

    private void onInsertingCompleted(Uri cakeUri) {
        if (cakeUri != null) {
            Toast.makeText(this, R.string.successfully_inserted, Toast.LENGTH_SHORT).show();
            updateWidget();
            mFabMenu.close(true);
        } else {
            Toast.makeText(this, R.string.error_upon_launch_message, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateWidget() {
        CakesWidgetService.startActionUpdateWidget(this);
    }

    private void initIngredientsFragment() {
        mIngredientsFragment = new IngredientsFragment();
        mIngredientsFragment.setmIngredientsList(mIngredientsList);
        mIngredientsFragment.setmStepsList(mStepsList);
        mFragmentManager.beginTransaction()
                .replace(R.id.ingredientsFragmentContainer, mIngredientsFragment)
                .commit();
    }

    @Override
    public void onStepClickListener(Steps step) {
        if (!isInTwoPaneMode) {
            watchStepInstructions(step);
        } else {
            addStepsFragmentToActivity(step);
        }
    }

    private void addStepsFragmentToActivity(Steps step) {
        String videoURL = step.getVideoURL();
        String stepInstructions = step.getDescription();
        if (!videoURL.equals("")) {
            RecipeFragment recipeFragment = new RecipeFragment();
            recipeFragment.setmVideoURL(videoURL);
            recipeFragment.setmStepInstructions(stepInstructions);

            mFragmentManager.beginTransaction()
                    .replace(R.id.recipeFragmentContainer, recipeFragment)
                    .commit();
        } else {
            Toast.makeText(this, R.string.not_available_message, Toast.LENGTH_SHORT).show();
        }
    }

    private void watchStepInstructions(Steps step) {
        String videoURL = step.getVideoURL();
        String shortDescription = step.getShortDescription();
        String stepInstructions = step.getDescription();
        if (!videoURL.equals("")) {
            Intent recipeStepIntent = new Intent(this, RecipeActivity.class);
            recipeStepIntent.putExtra(INTENT_STEP_INSTRUCTIONS_KEY, stepInstructions);
            recipeStepIntent.putExtra(INTENT_VIDEO_URL_KEY, videoURL);
            recipeStepIntent.putExtra(INTENT_DESCRIPTION_KEY, shortDescription);
            startActivity(recipeStepIntent);
        } else {
            Toast.makeText(this, R.string.not_available_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
