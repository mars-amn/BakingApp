package nanodegree.bakingapp.android.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


@SuppressWarnings("WeakerAccess")
public class CakesHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Cakes.db";
    private static final int DATABASE_VERSION = 1;


    public CakesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_QUERY = "CREATE TABLE " +
                CakeContract.CakesEntry.TABLE_NAME + " ( " +
                CakeContract.CakesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CakeContract.CakesEntry.CAKE_NAME_COLUMN + " TEXT NOT NULL, " +
                CakeContract.CakesEntry.INGREDIENTS_COLUMN + " TEXT NOT NULL, " +
                CakeContract.CakesEntry.TIME_STAMP_COLUMN + " TIMESTAMP NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
