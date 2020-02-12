package ak.android.movieshighlight.widget

import ak.android.movieshighlight.R
import ak.android.movieshighlight.common.toast
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri

class FavoriteMovieWidget : AppWidgetProvider() {
    companion object {
        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = toUri(Intent.URI_INTENT_SCHEME).toUri()
            }

            val views = RemoteViews(context.packageName, R.layout.widget_favorite_movie).apply {
                setRemoteAdapter(R.id.stack_poster, intent)
                setEmptyView(R.id.stack_poster, R.id.tv_no_data)
            }

            intent.action = "toast"

            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT).also {
                views.setPendingIntentTemplate(R.id.stack_poster, it)
            }


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action != null) {
            if (intent.action == "toast") {
                val idx = intent.getIntExtra("extra", 0)
                context?.toast("Touched: $idx")
            }
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}