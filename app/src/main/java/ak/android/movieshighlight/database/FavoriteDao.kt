package ak.android.movieshighlight.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite WHERE category='movie'")
    fun getFavoriteMovies(): LiveData<List<FilmInfo>>

    @Query("SELECT * FROM favorite WHERE category='tv'")
    fun getFavoriteTvSeries(): LiveData<List<FilmInfo>>

    @Query("SELECT * FROM favorite WHERE id=:id")
    fun getFavoriteById(id: Int): LiveData<List<FilmInfo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addToFavorite(filmInfo: FilmInfo)

    @Query("DELETE FROM favorite WHERE id=:id")
    fun removeFromFavorite(id: Int)
}