package ak.android.myfavoritefilms

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_film.view.*

class FilmAdapter : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    private val BASE_POSTER_URL = "https://image.tmdb.org/t/p/w500"

    private var cursor: Cursor? = null

    fun setCursor(cursor: Cursor?) {
        this.cursor = cursor
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder =
        FilmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_film, parent, false)
        )

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        if (cursor?.moveToPosition(position) == true) {
            holder.bindItem(
                cursor?.getString(
                    cursor?.getColumnIndexOrThrow("poster") ?: 0
                ).toString(),
                cursor?.getString(
                    cursor?.getColumnIndex("title")
                        ?: cursor?.getColumnIndexOrThrow("originalTitle") ?: 0
                ).toString()
            )
        }
    }

    override fun getItemCount(): Int = cursor?.count ?: 0

    inner class FilmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(posterUrl: String, title: String) {
            itemView.tv_title.text = title
            Picasso.get()
                .load(BASE_POSTER_URL + posterUrl)
                .into(itemView.iv_poster)

            itemView.setOnClickListener {
                Toast.makeText(it.context, title, Toast.LENGTH_SHORT).show()
            }
        }
    }
}