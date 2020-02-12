package ak.android.movieshighlight.main.favorite.movie

import ak.android.movieshighlight.database.FavoriteDatabase
import ak.android.movieshighlight.database.toMovieModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations

class FavoriteMovieViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val db = FavoriteDatabase.getDatabase(context).getFavorite()

    val movies = Transformations.map(db.getFavoriteMovies()) {
        it.toMovieModel()
    }
}
