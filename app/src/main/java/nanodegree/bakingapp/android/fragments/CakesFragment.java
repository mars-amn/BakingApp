package nanodegree.bakingapp.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nanodegree.bakingapp.android.R;
import nanodegree.bakingapp.android.activities.IngredientsActivity;
import nanodegree.bakingapp.android.adapters.CakesAdapter;
import nanodegree.bakingapp.android.models.Cake;
import nanodegree.bakingapp.android.rest.API;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("WeakerAccess")
public class CakesFragment extends Fragment implements CakesAdapter.CakeListener {

    public static final String INTENT_KEY = "nanodegree.bakingapp.android.selected_cake";
    private static final String TAG = CakesFragment.class.getSimpleName();
    private static List<Cake> mCakeResponse;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.mainCakesRecyclerView)
    RecyclerView mCakesRecyclerView;
    @BindView(R.id.loadingProgressBar)
    ProgressBar loadingProgressBar;
    private CakesAdapter mAdapter;
    private Context mContext;
    private onIdlingResourceChangeListener mIdleResourceListener;

    public CakesFragment() {
    }

    @VisibleForTesting
    @NonNull
    public static CakesFragment getFragmentInstance() {
        return new CakesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View cakesView = inflater.inflate(R.layout.fragment_cakes, container, false);
        ButterKnife.bind(this, cakesView);
        initCakesAdapter();

        if (mCakeResponse != null) {
            hideLoadingIndicator();
            mAdapter.updateCakes(mCakeResponse);
        } else {
            getCakeResults();
        }

        return cakesView;
    }

    private void hideLoadingIndicator() {
        mCakesRecyclerView.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.GONE);
    }

    private void showLoadingIndicator() {
        mCakesRecyclerView.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void initCakesAdapter() {
        mContext = getContext();
        mAdapter = new CakesAdapter(mContext, new ArrayList<Cake>(0), this);
        mCakesRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mIdleResourceListener = (onIdlingResourceChangeListener) context;
        } catch (ClassCastException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void getCakeResults() {
        showLoadingIndicator();
        mIdleResourceListener.idlingResourceChangeListener(false);
        Call<List<Cake>> call = API.getCakesClient().getCakes();
        call.enqueue(new Callback<List<Cake>>() {
            @Override
            public void onResponse(Call<List<Cake>> call, Response<List<Cake>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        mCakeResponse = response.body();
                        mAdapter.updateCakes(mCakeResponse);
                        hideLoadingIndicator();
                        mIdleResourceListener.idlingResourceChangeListener(true);
                    }
                } else {
                    Log.d(TAG, "response code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Cake>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onCakeLickListener(int cakeIndex) {
        Intent selectedCake = new Intent(mContext, IngredientsActivity.class);
        selectedCake.putExtra(INTENT_KEY, mCakeResponse.get(cakeIndex));
        startActivity(selectedCake);
    }


    public interface onIdlingResourceChangeListener {
        void idlingResourceChangeListener(boolean countingIdlingResource);
    }

}