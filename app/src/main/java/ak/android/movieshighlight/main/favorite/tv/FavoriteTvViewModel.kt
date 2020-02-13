package ak.android.movieshighlight.main.favorite.tv

import ak.android.movieshighlight.database.AppDatabase
import ak.android.movieshighlight.database.toTvModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations

class FavoriteTvViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val db = AppDatabase.getDatabase(context).getFavorite()

    val movies = Transformations.map(db.getFavoriteTvSeries()) {
        it.toTvModel()
    }
}
