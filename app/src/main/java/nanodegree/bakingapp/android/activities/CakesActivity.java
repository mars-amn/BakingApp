package nanodegree.bakingapp.android.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import nanodegree.bakingapp.android.R;
import nanodegree.bakingapp.android.fragments.CakesFragment;


@SuppressWarnings("WeakerAccess")
public class CakesActivity extends AppCompatActivity implements CakesFragment.onIdlingResourceChangeListener {

    private static final String TAG = CakesActivity.class.getSimpleName();
    public static CountingIdlingResource mCountingIdleResource;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.losingConnectivityState)
    LinearLayout connectivityState;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.cakesFragmentContainer)
    FrameLayout cakesFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCountingIdleResource = new CountingIdlingResource(TAG);

        if (!isNetworkAvailable()) {
            showConnectivityState();
        } else if (savedInstanceState == null) {
            hideConnectivityState();
            CakesFragment fragment = new CakesFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.cakesFragmentContainer, fragment)
                    .commit();

        }


    }

    private void hideConnectivityState() {
        cakesFragmentContainer.setVisibility(View.VISIBLE);
        connectivityState.setVisibility(View.GONE);
    }

    private void showConnectivityState() {
        cakesFragmentContainer.setVisibility(View.GONE);
        connectivityState.setVisibility(View.VISIBLE);
    }

    @Override
    public void idlingResourceChangeListener(boolean countingIdlingResource) {
        if (!countingIdlingResource) {
            mCountingIdleResource.increment();
        } else {
            mCountingIdleResource.decrement();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
