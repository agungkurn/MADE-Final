package ak.android.movieshighlight.main.discover.movie

import ak.android.movieshighlight.R
import ak.android.movieshighlight.common.hide
import ak.android.movieshighlight.common.show
import ak.android.movieshighlight.main.discover.FilmAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {
    companion object {
        private var instance: MovieFragment? = null

        fun newInstance(): MovieFragment {
            if (instance == null) {
                instance = MovieFragment()
            }

            return instance!!
        }
    }

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(MovieViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movie, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        swipe_movies.isRefreshing = true
        viewModel.discoverMovies()
        swipe_movies.setOnRefreshListener {
            viewModel.discoverMovies()
        }

        rv_movies.setHasFixedSize(true)
        rv_movies.layoutManager = GridLayoutManager(requireContext(), 2)

        registerObserver()
    }

    private fun registerObserver() {
        // For error state
        viewModel.isError.observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                tv_retry.show()
                swipe_movies.isRefreshing = false
            } else {
                tv_retry.hide()
            }
        })

        // For displaying data
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            FilmAdapter(
                requireContext(),
                it
            ).also { adapter ->
                rv_movies.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            swipe_movies.isRefreshing = false
        })
    }
}
