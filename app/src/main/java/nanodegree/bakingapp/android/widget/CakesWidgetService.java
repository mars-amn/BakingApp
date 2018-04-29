package nanodegree.bakingapp.android.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import nanodegree.bakingapp.android.R;


@SuppressWarnings("WeakerAccess")
public class CakesWidgetService extends IntentService {

    @SuppressWarnings("WeakerAccess")
    public static final String UPDATE_WIDGET_ACTION = "nanodegree.bakingapp.android_update_widget_action";

    public CakesWidgetService() {
        super("CakesWidgetService");
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, CakesWidgetService.class);
        intent.setAction(UPDATE_WIDGET_ACTION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            String action = intent.getAction();
            if (UPDATE_WIDGET_ACTION.equals(action)) {
                handleUpdateWidgetAction();
            }
        }
    }

    private void handleUpdateWidgetAction() {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int widgetsIds[] = widgetManager.getAppWidgetIds(new ComponentName(this, IngredientsAppWidget.class));
        widgetManager.notifyAppWidgetViewDataChanged(widgetsIds, R.id.widget_grid_view);
        IngredientsAppWidget.updateCakesAppWidget(this, widgetManager, widgetsIds);
    }
}
