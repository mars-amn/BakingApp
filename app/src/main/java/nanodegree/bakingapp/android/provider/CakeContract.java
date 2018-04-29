package nanodegree.bakingapp.android.provider;

import android.net.Uri;
import android.provider.BaseColumns;


public final class CakeContract {

    public static final String AUTHORITY = "nanodegree.bakingapp.android";
    public static final String CAKES_PATH = "CakesIngredients";
    private static final Uri BASE_CONTENT = Uri.parse("content://" + AUTHORITY);

    private CakeContract() {
        throw new AssertionError("not available!");
    }

    public static class CakesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon()
                .appendPath(CAKES_PATH)
                .build();

        public static final String TABLE_NAME = "CakesIngredients";
        public static final String CAKE_NAME_COLUMN = "_cakeName";
        public static final String INGREDIENTS_COLUMN = "_ingredients";
        public static final String TIME_STAMP_COLUMN = "_time";
    }
}
