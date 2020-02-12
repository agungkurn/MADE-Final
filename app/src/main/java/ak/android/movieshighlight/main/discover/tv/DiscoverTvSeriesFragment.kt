package ak.android.movieshighlight.main.discover.tv

import ak.android.movieshighlight.R
import ak.android.movieshighlight.common.hide
import ak.android.movieshighlight.common.show
import ak.android.movieshighlight.main.FilmAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_discover_tv.*

class DiscoverTvSeriesFragment : Fragment() {
    companion object {
        private var instance: DiscoverTvSeriesFragment? = null

        fun newInstance(): DiscoverTvSeriesFragment {
            if (instance == null) {
                instance = DiscoverTvSeriesFragment()
            }

            return instance!!
        }
    }

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DiscoverTvSeriesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_discover_tv, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        swipe_tv.isRefreshing = true
        viewModel.discoverTvSeries()
        swipe_tv.setOnRefreshListener {
            viewModel.discoverTvSeries()
        }

        rv_tv.setHasFixedSize(true)
        rv_tv.layoutManager = GridLayoutManager(requireContext(), 2)

        registerObserver()
    }

    private fun registerObserver() {
        // For error state
        viewModel.isError.observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                tv_retry.show()
                swipe_tv.isRefreshing = false
            } else {
                tv_retry.hide()
            }
        })

        // For displaying data
        viewModel.tvSeries.observe(viewLifecycleOwner, Observer {
            FilmAdapter(
                requireContext(),
                it
            ).also { adapter ->
                rv_tv.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            swipe_tv.isRefreshing = false
        })
    }
}
