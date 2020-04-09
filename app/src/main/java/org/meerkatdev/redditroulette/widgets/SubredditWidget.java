package org.meerkatdev.redditroulette.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import androidx.preference.PreferenceManager;

import org.meerkatdev.redditroulette.PostsListActivity;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.utils.Tags;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link SubredditWidgetConfigureActivity SubredditWidgetConfigureActivity}
 */
public class SubredditWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Tags.OAUTH_DATA, Context.MODE_PRIVATE);
        String widgetText = SubredditWidgetConfigureActivity.loadTitlePref(context, appWidgetId);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.subreddit_widget);
        String subredditName = widgetText; // ???
        String accessToken = sharedPreferences.getString(Tags.ACCESS_TOKEN, "");

        Intent intent = new Intent(context, PostsListActivity.class);
        intent.putExtra(Tags.ACCESS_TOKEN, accessToken);
        intent.putExtra(Tags.SUBREDDIT_NAME, subredditName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setTextViewText(R.id.tv_widget_icon_title, subredditName);
        views.setOnClickPendingIntent(R.id.iv_widget_icon_image, pendingIntent);
        views.setOnClickPendingIntent(R.id.tv_widget_icon_title, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            SubredditWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

}

