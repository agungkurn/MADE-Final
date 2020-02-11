package ak.android.movieshighlight.main.search

import ak.android.movieshighlight.R
import ak.android.movieshighlight.common.hide
import ak.android.movieshighlight.common.log
import ak.android.movieshighlight.common.show
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_search.*

class SearchResultFragment : Fragment() {
    companion object {
        private var instance: SearchResultFragment? = null

        fun newInstance(): SearchResultFragment {
            if (instance == null) {
                instance = SearchResultFragment()
            }

            return instance!!
        }
    }

    private val viewModel by lazy {
        ViewModelProvider
            .AndroidViewModelFactory(requireActivity().application)
            .create(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rv_search.setHasFixedSize(true)
        registerObserver()
    }

    private fun registerObserver() {
        // For error state
        viewModel.isError.observe(viewLifecycleOwner, Observer { error ->
            pb_search.hide()
            if (error) {
                tv_retry.show()
            } else {
                tv_retry.hide()
            }
        })

        // For displaying data
        viewModel.searchResults.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                SearchAdapter(requireContext(), it).also { adapter ->
                    rv_search.adapter = adapter
                    adapter.notifyDataSetChanged()
                }

                pb_search.hide()
                tv_retry.hide()
                rv_search.show()
                log("Content search: $it")
            }
        })
    }
}
