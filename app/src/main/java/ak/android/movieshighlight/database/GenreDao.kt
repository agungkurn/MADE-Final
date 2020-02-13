package ak.android.movieshighlight.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GenreDao {
    @Query("SELECT * FROM genre")
    fun getGenres(): LiveData<List<GenreDb>>

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = GenreDb::class)
    fun insertGenres(genre: GenreDb)
}