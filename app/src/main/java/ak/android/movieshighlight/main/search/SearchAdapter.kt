package ak.android.movieshighlight.main.search

import ak.android.movieshighlight.BuildConfig
import ak.android.movieshighlight.R
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
import kotlinx.android.synthetic.main.list_header.view.*
import kotlinx.android.synthetic.main.list_search_items.view.*

class SearchAdapter(private val context: Context, private val items: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM_HEADER = 0
        private const val ITEM_RESULT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is String -> ITEM_HEADER
            is MovieResultsItem, is TvResultsItem -> ITEM_RESULT
            else -> throw IllegalArgumentException("Type not supported by Adapter")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_HEADER -> SearchHeaderViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.list_header,
                    parent,
                    false
                )
            )
            ITEM_RESULT -> SearchResultViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.list_search_items,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Type not supported by Adapter")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ITEM_HEADER -> {
                val headerHolder = holder as SearchHeaderViewHolder
                headerHolder.bindHeader(items[position] as String)
            }
            ITEM_RESULT -> {
                val resultHolder = holder as SearchResultViewHolder
                resultHolder.bindItem(items[position])
            }
            else -> throw IllegalArgumentException("Undefined view type (SearchAdapter.onBindViewHolder())")
        }
    }

    override fun getItemCount(): Int = items.size

    inner class SearchHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindHeader(header: String) {
            itemView.tv_search_header.text = header
        }
    }

    inner class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(item: Any) {
            var poster: String? = null
            val intent = Intent(context, DetailsActivity::class.java)

            when (item) {
                is MovieResultsItem -> {
                    poster = item.posterPath
                    itemView.tv_title.text = item.title ?: item.originalTitle
                    intent.putExtra(DetailsActivity.DATA_KEY, item)
                }
                is TvResultsItem -> {
                    poster = item.posterPath
                    itemView.tv_title.text = item.name ?: item.originalName
                    intent.putExtra(DetailsActivity.DATA_KEY, item)
                }
            }

            Picasso.get()
                .load(BuildConfig.BASE_IMAGE_URL + poster)
                .into(itemView.iv_poster)

            itemView.setOnClickListener {
                context.startActivity(intent)
            }
        }
    }
}