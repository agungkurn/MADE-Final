package ak.android.movieshighlight.main.discover

import ak.android.movieshighlight.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_discover.*

class DiscoverFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_discover, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabTitle = resources.getStringArray(R.array.tab_title)
        vp_discover.adapter = DiscoverPagerAdapter(childFragmentManager, tabTitle)

        tab_discover.setupWithViewPager(vp_discover)
    }
}
