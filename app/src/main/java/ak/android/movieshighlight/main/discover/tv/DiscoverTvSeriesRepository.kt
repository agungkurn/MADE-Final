package ak.android.movieshighlight.main.discover.tv

import ak.android.movieshighlight.common.errLog
import ak.android.movieshighlight.model.tv.TvResultsItem
import ak.android.movieshighlight.retrofit.RetrofitFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object DiscoverTvSeriesRepository {
    private val _tvSeries = MutableLiveData<List<TvResultsItem>>()
    val tvSeries: LiveData<List<TvResultsItem>> = _tvSeries

    suspend fun discoverTvSeries() {
        withContext(Dispatchers.IO) {
            val response = RetrofitFactory.getMovieOrTvShow().fetchTvShowDiscovered()

            if (response.isSuccessful) {
                val result = response.body()

                result?.results?.let {
                    _tvSeries.postValue(it.requireNoNulls())
                }
            } else {
                errLog("${response.code()}. Error getting TV series")
                throw IOException("Error getting TV series")
            }
        }
    }
}