package nanodegree.bakingapp.android.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class CakesProvider extends ContentProvider {

    private static final int INGREDIENTS = 100;
    private static UriMatcher sUriMatched = matchUris();
    private CakesHelper mCakesHelper;

    private static UriMatcher matchUris() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CakeContract.AUTHORITY, CakeContract.CAKES_PATH, INGREDIENTS);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mCakesHelper = new CakesHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase database = mCakesHelper.getReadableDatabase();
        Cursor cursor;
        switch (sUriMatched.match(uri)) {
            case INGREDIENTS:
                cursor = database.query(CakeContract.CakesEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        CakeContract.CakesEntry.TIME_STAMP_COLUMN);
                break;
            default:
                throw new UnsupportedOperationException("Not available");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not available");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = mCakesHelper.getReadableDatabase();
        Uri cakeAddress;
        switch (sUriMatched.match(uri)) {
            case INGREDIENTS:
                long id = database.insert(CakeContract.CakesEntry.TABLE_NAME,
                        null,
                        contentValues);
                if (id > 0) {
                    cakeAddress = ContentUris.withAppendedId(uri, id);
                } else {
                    throw new UnknownError("error occurred");
                }
                break;
            default:
                throw new UnsupportedOperationException("Not available");
        }
        getContext().getContentResolver().notifyChange(cakeAddress, null);
        return cakeAddress;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not available");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not available");
    }
}
