package ak.android.movieshighlight.main.favorite.movie

import ak.android.movieshighlight.database.AppDatabase
import ak.android.movieshighlight.database.toMovieModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class FavoriteMovieViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val db = AppDatabase.getDatabase(context).getFavorite()

    val movies = liveData(Dispatchers.IO) {
        emit(db.getFavoriteMovies().toMovieModel())
    }
}
