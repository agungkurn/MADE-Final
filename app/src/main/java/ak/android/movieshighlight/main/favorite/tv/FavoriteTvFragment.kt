package ak.android.movieshighlight.main.favorite.tv

import ak.android.movieshighlight.R
import ak.android.movieshighlight.common.hide
import ak.android.movieshighlight.common.log
import ak.android.movieshighlight.common.show
import ak.android.movieshighlight.main.FilmAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_favorite_tv.*

class FavoriteTvFragment : Fragment() {

    companion object {
        private var instance: FavoriteTvFragment? = null

        fun newInstance(): FavoriteTvFragment {
            if (instance == null) {
                instance = FavoriteTvFragment()
            }

            return instance!!
        }
    }

    private val viewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(FavoriteTvViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_favorite_tv, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_favorite_tv.setHasFixedSize(true)
        rv_favorite_tv.layoutManager = GridLayoutManager(requireContext(), 2)

        registerObserver()
    }

    private fun registerObserver() {
        // For displaying data
        log("created")
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            log("Favorite: $it")
            if (it.isEmpty()) {
                tv_no_data.show()
            } else {
                tv_no_data.hide()
                FilmAdapter(requireContext(), it).also { adapter ->
                    rv_favorite_tv.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }
}
