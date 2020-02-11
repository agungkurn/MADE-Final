package ak.android.movieshighlight.model.tv

import com.google.gson.annotations.SerializedName

data class TvResponse(

    @field:SerializedName("page")
	val page: Int? = null,

    @field:SerializedName("total_pages")
	val totalPages: Int? = null,

    @field:SerializedName("results")
	val results: List<TvResultsItem?>? = null,

    @field:SerializedName("total_results")
	val totalResults: Int? = null
)