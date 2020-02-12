package ak.android.movieshighlight.widget

import ak.android.movieshighlight.BuildConfig
import ak.android.movieshighlight.R
import ak.android.movieshighlight.common.errLog
import ak.android.movieshighlight.database.FavoriteDatabase
import ak.android.movieshighlight.database.FilmInfo
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.squareup.picasso.Picasso
import java.io.IOException

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val db = FavoriteDatabase.getDatabase(context).getFavorite()

    private val favs = mutableListOf<FilmInfo>()

    override fun onCreate() {}

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() {
        favs.addAll(db.getFavoriteMovies())
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_item)

        try {
            val bitmap = Picasso.get()
                .load(BuildConfig.BASE_IMAGE_URL + favs[position].poster)
                .get()

            remoteViews.setImageViewBitmap(R.id.iv_poster, bitmap)
        } catch (e: IOException) {
            errLog(e.localizedMessage)
        }

        Intent().putExtras(bundleOf("extra" to position)).also {
            remoteViews.setOnClickFillInIntent(R.id.iv_poster, it)
        }

        return remoteViews
    }

    override fun getCount(): Int = favs.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}

}