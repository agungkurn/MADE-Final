package ak.android.movieshighlight.main

import ak.android.movieshighlight.BuildConfig
import ak.android.movieshighlight.R
import ak.android.movieshighlight.database.FilmInfo
import ak.android.movieshighlight.details.DetailsActivity
import ak.android.movieshighlight.model.movie.MovieResultsItem
import ak.android.movieshighlight.model.tv.TvResultsItem
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_film.view.*

class FilmAdapter(private val context: Context, private val films: List<Any?>) :
    RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder =
        FilmViewHolder(LayoutInflater.from(context).inflate(R.layout.list_film, parent, false))

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bindItem(films[position])
    }

    override fun getItemCount(): Int = films.size

    inner class FilmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(film: Any?) {
            var data: FilmInfo? = null

            when (film) {
                is MovieResultsItem -> {
                    data = film.toDatabaseModel()
                }
                is TvResultsItem -> {
                    data = film.toDatabaseModel()
                }
            }

            itemView.tv_title.text = data?.title
            Picasso.get()
                .load(BuildConfig.BASE_IMAGE_URL + data?.poster)
                .into(itemView.iv_poster)

            itemView.setOnClickListener {
                Intent(context, DetailsActivity::class.java).also {
                    it.putExtra(DetailsActivity.DATA_KEY, data)
                    context.startActivity(it)
                }
            }
        }
    }
}