package ak.android.movieshighlight.model.genre

import com.google.gson.annotations.SerializedName


data class GenreResponse(

	@field:SerializedName("genres")
	val genres: List<GenresItem?>? = null
)