package nanodegree.bakingapp.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nanodegree.bakingapp.android.R;

public class SplashActivity extends AppCompatActivity {

    /**
     * The launcher icon, default icon that's used all over the app
     * are created, designed and published by this great designer
     * on uplabs.com
     *
     * @see <a href="https://www.uplabs.com/posts/strawberry-cake-familly">https://www.uplabs.com</a>
     * <p>
     * also the icons used in measuring units
     * are optained from this site
     * @see <a href="https://materialdesignicons.com/">https://materialdesignicons.com/</a>
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent i = new Intent(this, CakesActivity.class);
        startActivity(i);
        finish();
    }
}
