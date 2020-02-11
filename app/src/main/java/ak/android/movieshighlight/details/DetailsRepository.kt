package ak.android.movieshighlight.details

import ak.android.movieshighlight.common.errLog
import ak.android.movieshighlight.model.genre.GenresItem
import ak.android.movieshighlight.retrofit.RetrofitFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object DetailsRepository {
    private val genreList = mutableListOf<GenresItem>()

    private val _genres = MutableLiveData<List<GenresItem>>()
    val genres: LiveData<List<GenresItem>> = _genres


    suspend fun fetchGenres() {
        if (genreList.isEmpty()) {
            withContext(Dispatchers.IO) {
                val movieResponse = RetrofitFactory.getMovieOrTvShow().getMovieGenre()
                if (movieResponse.isSuccessful) {
                    val result = movieResponse.body()
                    result?.genres?.let {
                        genreList.addAll(it.requireNoNulls())
                        _genres.postValue(genreList)
                    }
                } else {
                    errLog("error getting genres for movie")
                    throw IOException("error getting genres for movie")
                }

                val tvResponse = RetrofitFactory.getMovieOrTvShow().getTvSeriesGenre()
                if (tvResponse.isSuccessful) {
                    val result = tvResponse.body()
                    result?.genres?.let {
                        it.requireNoNulls().forEach { fetchedGenre ->
                            // Check if the genres if available
                            val available = genreList.contains(fetchedGenre)
                            if (!available) {
                                genreList.add(fetchedGenre)
                                _genres.postValue(genreList)
                            }
                        }
                    }
                } else {
                    errLog("error getting genres for tv")
                    throw IOException("error getting genres for tv")
                }
            }
        }
    }
}