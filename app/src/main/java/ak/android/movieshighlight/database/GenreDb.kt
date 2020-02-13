package ak.android.movieshighlight.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genre")
data class GenreDb(
    @PrimaryKey
    val id: Int,

    val name: String
)