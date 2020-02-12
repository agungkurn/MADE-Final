package ak.android.movieshighlight.notification

import ak.android.movieshighlight.R
import ak.android.movieshighlight.common.log
import ak.android.movieshighlight.model.movie.MovieResultsItem
import ak.android.movieshighlight.retrofit.RetrofitFactory
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class NewReleaseWorker(private val appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result = coroutineScope {
        val todayDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Calendar.getInstance().time)

        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitFactory.getMovieOrTvShow().getTodayReleaseMovie(todayDate)
            }

            if (response.isSuccessful) {
                response.body()?.results?.let {
                    showNotification(it.requireNoNulls())
                }
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            log("Error getting today released movie: ${e.localizedMessage}")
            Result.retry()
        }
    }

    private fun showNotification(content: List<MovieResultsItem>) {
        val notificationReceiver = NotificationReceiver()
        val title = content.size.toString() + " " + appContext.getString(R.string.new_release)
        if (content.isNotEmpty()) {
            val contentStr = content.map { it.title }.joinToString(separator = "\n")

            notificationReceiver
                .showNotification(
                    appContext,
                    title,
                    contentStr,
                    NotificationReceiver.ID_NEW_RELEASE
                )
        }
    }
}