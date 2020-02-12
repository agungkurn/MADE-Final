package ak.android.movieshighlight.details

import ak.android.movieshighlight.BuildConfig
import ak.android.movieshighlight.R
import ak.android.movieshighlight.common.show
import ak.android.movieshighlight.database.FilmInfo
import ak.android.movieshighlight.model.movie.MovieResultsItem
import ak.android.movieshighlight.model.tv.TvResultsItem
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*

class DetailsActivity : AppCompatActivity() {
    companion object {
        const val DATA_KEY = "film_info"
    }

    private val viewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(application).create(DetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showData()
    }

    private fun showData() {
        intent.extras?.let {
            when (val sourceData = it.getParcelable<Parcelable>(DATA_KEY)) {
                // From Discover
                is FilmInfo -> {
                    showInfo(sourceData)
                }
                // From Search
                is MovieResultsItem -> {
                    showInfo(sourceData.toDatabaseModel())
                }
                is TvResultsItem -> {
                    showInfo(sourceData.toDatabaseModel())
                }
            }
        }
    }

    private fun showInfo(data: FilmInfo) {
        val request = Picasso.get().load(BuildConfig.BASE_IMAGE_URL + data.banner)
        request.into(iv_banner)

        Picasso.get()
            .load(BuildConfig.BASE_IMAGE_URL + data.poster)
            .into(iv_poster)

        viewModel.generateActionBarColor(request) { color ->
            app_bar.setBackgroundColor(color)
        }

        supportActionBar?.title = data.title ?: data.originalTitle

        tv_rate.text = data.rate.toString()
        tv_year.text = data.dateReleased.substring(0..3)
        tv_overview.text = data.overview

        if (data.adult) {
            tv_adult.show()
        }

        data.genreId.split(", ").map { it.toInt() }.let {
            viewModel.genres(it.requireNoNulls()).observe(this) { genres ->
                genres.forEach { genre ->
                    chip_genre.addView(
                        Chip(chip_genre.context).apply {
                            text = genre
                            isClickable = false
                        }
                    )
                }
            }
        }

        checkIfFavorite(data)
    }

    private fun checkIfFavorite(data: FilmInfo) {
        viewModel.isFavorite(data.id).observe(this, Observer { isFavorite ->
            if (isFavorite) {
                // Is already favorite
                fab_favorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_black_24dp))
                fab_favorite.setOnClickListener {
                    viewModel.removeFromFavorite(data.id)
                }
            } else {
                // Not the favorite
                fab_favorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_black_24dp))
                fab_favorite.setOnClickListener {
                    viewModel.addToFavorite(data)
                }
            }
        })
    }
}
