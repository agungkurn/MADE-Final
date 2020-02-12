package ak.android.movieshighlight.main.discover.movie

import ak.android.movieshighlight.common.errLog
import ak.android.movieshighlight.model.movie.MovieResultsItem
import ak.android.movieshighlight.retrofit.RetrofitFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object DiscoverMovieRepository {
    private val _movies = MutableLiveData<List<MovieResultsItem>>()
    val movies: LiveData<List<MovieResultsItem>> = _movies

    suspend fun discoverMovies() {
        withContext(Dispatchers.IO) {
            val response = RetrofitFactory.getMovieOrTvShow().fetchMovieDiscovered()

            if (response.isSuccessful) {
                val result = response.body()

                result?.results?.let {
                    _movies.postValue(it.requireNoNulls())
                }
            } else {
                errLog("${response.code()}. Error getting movie")
                throw IOException("error getting movie")
            }
        }
    }
}