package ak.android.movieshighlight.main.search

import ak.android.movieshighlight.common.errLog
import ak.android.movieshighlight.model.movie.MovieResultsItem
import ak.android.movieshighlight.model.tv.TvResultsItem
import ak.android.movieshighlight.retrofit.RetrofitFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object SearchRepository {
    private val _movieResults = MutableLiveData<List<MovieResultsItem>>()
    val movieResults = _movieResults

    private val _tvSeriesResults = MutableLiveData<List<TvResultsItem>>()
    val tvSeriesResult: LiveData<List<TvResultsItem>> = _tvSeriesResults

    suspend fun searchMovie(keyword: String) {
        withContext(Dispatchers.IO) {
            val response = RetrofitFactory.getMovieOrTvShow().searchMovie(keyword)

            if (response.isSuccessful) {
                val result = response.body()

                result?.results?.let {
                    _movieResults.postValue(it.requireNoNulls())
                }
            } else {
                errLog("${response.code()}. Error getting movie search result")
                throw IOException("error getting movie search result")
            }
        }
    }

    suspend fun searchTvSeries(keyword: String) {
        withContext(Dispatchers.IO) {
            val response = RetrofitFactory.getMovieOrTvShow().searchTvSeries(keyword)

            if (response.isSuccessful) {
                val result = response.body()

                result?.results?.let {
                    _tvSeriesResults.postValue(it.requireNoNulls())
                }
            } else {
                errLog("${response.code()}. Error getting tv series search result")
                throw IOException("error getting tv series search result")
            }
        }
    }
}