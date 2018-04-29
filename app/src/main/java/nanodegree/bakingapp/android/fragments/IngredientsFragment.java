package nanodegree.bakingapp.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nanodegree.bakingapp.android.R;
import nanodegree.bakingapp.android.adapters.IngredientsAdapter;
import nanodegree.bakingapp.android.adapters.StepsAdapter;
import nanodegree.bakingapp.android.models.Ingredient;
import nanodegree.bakingapp.android.models.Steps;


@SuppressWarnings("WeakerAccess")
public class IngredientsFragment extends Fragment implements StepsAdapter.onStepClick {

    private static final String INGREDIENTS_STATE_KEY = "nanodegree.bakingapp.android.recyclerview_ingredients";
    private static final String STEPS_STATE_KEY = "nanodegree.bakingapp.android.recyclerview_steps";
    private static final String TAG = IngredientsFragment.class.getSimpleName();
    private static List<Ingredient> mIngredientsList;
    private static List<Steps> mStepsList;

    @BindView(R.id.ingredientsRecyclerView)
    RecyclerView mIngredientsRecyclerView;

    @BindView(R.id.stepsRecyclerView)
    RecyclerView mStepsRecyclerView;

    private Context mContext;
    private StepClickListener mStepSelectedListener;
    private Parcelable mStepsRecyclerViewState;
    private Parcelable mIngredientsRecyclerViewState;

    public IngredientsFragment() {
    }

    @VisibleForTesting
    public static IngredientsFragment getFragmentInstance() {
        return new IngredientsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ingredientsView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, ingredientsView);
        mContext = getContext();

        setupStepsAndIngredientsViews();

        return ingredientsView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mStepsRecyclerViewState = savedInstanceState.getParcelable(STEPS_STATE_KEY);
            mIngredientsRecyclerViewState = savedInstanceState.getParcelable(INGREDIENTS_STATE_KEY);
        }

    }

    private void setupStepsAndIngredientsViews() {
        StepsAdapter mStepsAdapter = new StepsAdapter(mContext, getmStepsList(), this);
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        IngredientsAdapter mIngredientsAdapter = new IngredientsAdapter(mContext, getmIngredientsList());
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        mStepsRecyclerView.setHasFixedSize(true);
        restoreRecyclerViewsState();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mStepsRecyclerViewState = mStepsRecyclerView.getLayoutManager().onSaveInstanceState();
        mIngredientsRecyclerViewState = mIngredientsRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(STEPS_STATE_KEY, mStepsRecyclerViewState);
        outState.putParcelable(INGREDIENTS_STATE_KEY, mIngredientsRecyclerViewState);
    }

    private void restoreRecyclerViewsState() {
        if (mStepsRecyclerViewState != null && mIngredientsRecyclerViewState != null) {
            mStepsRecyclerView.getLayoutManager().onRestoreInstanceState(mStepsRecyclerViewState);
            mIngredientsRecyclerView.getLayoutManager().onRestoreInstanceState(mIngredientsRecyclerViewState);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mStepSelectedListener = (StepClickListener) context;
        } catch (ClassCastException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onStepClickListener(Steps step) {
        mStepSelectedListener.onStepClickListener(step);
    }

    private List<Steps> getmStepsList() {
        return mStepsList;
    }

    public void setmStepsList(List<Steps> mStepsList) {
        IngredientsFragment.mStepsList = mStepsList;
    }

    private List<Ingredient> getmIngredientsList() {
        return mIngredientsList;
    }

    public void setmIngredientsList(List<Ingredient> mIngredientsList) {
        IngredientsFragment.mIngredientsList = mIngredientsList;
    }

    public interface StepClickListener {
        void onStepClickListener(Steps step);
    }
}
