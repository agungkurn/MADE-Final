package ak.android.movieshighlight.main.discover

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ak.android.movieshighlight.R
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_discover.*

class DiscoverFragment : Fragment() {

    companion object {
        private var instance: DiscoverFragment? = null

        fun newInstance(): DiscoverFragment {
            if (instance == null) {
                instance = DiscoverFragment()
            }

            return instance!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_discover, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabTitle = resources.getStringArray(R.array.tab_title)
        vp_discover.adapter = DiscoverFragmentPagerAdapter(childFragmentManager, tabTitle)

        tab_discover.setupWithViewPager(vp_discover)
    }

}
