package nanodegree.bakingapp.android.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import nanodegree.bakingapp.android.R;
import nanodegree.bakingapp.android.provider.CakeContract;


public class GridRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridViewWidgetRemoteViewsFactory(this.getApplicationContext());
    }

    class GridViewWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Cursor mCursor;
        private Context mContext;

        public GridViewWidgetRemoteViewsFactory(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) mCursor.close();

            mCursor = getContentResolver().query(CakeContract.CakesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    CakeContract.CakesEntry.TIME_STAMP_COLUMN);
        }

        @Override
        public void onDestroy() {
            if (mCursor != null) {
                mCursor.close();
            }
        }

        @Override
        public int getCount() {
            if (mCursor == null) {
                return 0;
            } else {
                return mCursor.getCount();
            }
        }

        @Override
        public RemoteViews getViewAt(int i) {
            if (mCursor == null || mCursor.getCount() == 0) {
                return null;
            }

            mCursor.moveToPosition(i);
            int cakeNameIndex = mCursor.getColumnIndex(CakeContract.CakesEntry.CAKE_NAME_COLUMN);
            int ingredientsIndex = mCursor.getColumnIndex(CakeContract.CakesEntry.INGREDIENTS_COLUMN);

            String cakeName = mCursor.getString(cakeNameIndex);
            String ingredients = mCursor.getString(ingredientsIndex);

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_app_widget);

            remoteViews.setTextViewText(R.id.widgetCakeNameTextView, cakeName);
            remoteViews.setTextViewText(R.id.widgetIngredientText, ingredients);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
