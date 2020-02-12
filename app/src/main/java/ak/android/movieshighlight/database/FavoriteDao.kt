package ak.android.movieshighlight.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    /**
     * We don't use LiveData here because it will be observed by StackRemoteViewsFactory,
     * which is not a LifecycleOwner
     */
    @Query("SELECT * FROM favorite WHERE category='movie'")
    fun getFavoriteMovies(): List<FilmInfo>

    @Query("SELECT * FROM favorite WHERE category='tv'")
    fun getFavoriteTvSeries(): LiveData<List<FilmInfo>>

    @Query("SELECT * FROM favorite WHERE id=:id")
    fun getFavoriteById(id: Int): LiveData<List<FilmInfo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addToFavorite(filmInfo: FilmInfo)

    @Query("DELETE FROM favorite WHERE id=:id")
    fun removeFromFavorite(id: Int)
}