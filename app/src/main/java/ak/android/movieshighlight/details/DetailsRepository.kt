package ak.android.movieshighlight.details

import ak.android.movieshighlight.common.errLog
import ak.android.movieshighlight.database.AppDatabase
import ak.android.movieshighlight.model.genre.GenresItem
import ak.android.movieshighlight.retrofit.RetrofitFactory
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object DetailsRepository {
    private val genreList = mutableListOf<GenresItem>()

    val genres = { context: Context ->
        AppDatabase.getDatabase(context).getGenres().getGenres()
    }


    suspend fun fetchGenres(context: Context) {
        if (genreList.isEmpty()) {
            withContext(Dispatchers.IO) {
                val movieResponse = RetrofitFactory.getMovieOrTvShow().getMovieGenre()
                if (movieResponse.isSuccessful) {
                    val result = movieResponse.body()
                    result?.genres?.let {
                        genreList.addAll(it.requireNoNulls())
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
                            }
                        }
                    }
                } else {
                    errLog("error getting genres for tv")
                    throw IOException("error getting genres for tv")
                }

                genreList.forEach {
                    it.toDbModel()?.let { dbModel ->
                        AppDatabase.getDatabase(context).getGenres().insertGenres(dbModel)
                    }
                }
            }
        }
    }
}