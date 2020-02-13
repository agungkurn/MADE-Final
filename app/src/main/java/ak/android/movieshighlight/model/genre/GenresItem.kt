package ak.android.movieshighlight.model.genre

import ak.android.movieshighlight.database.GenreDb
import com.google.gson.annotations.SerializedName


data class GenresItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
) {
    fun toDbModel(): GenreDb? {
        return if (id != null && name != null) {
            GenreDb(id, name)
        } else {
            null
        }
    }
}