package com.nehak.instagramfeed.feedUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.nehak.instagramfeed.other.Extensions.Companion.findFirstVisibleItemPosition
import com.nehak.instagramfeed.other.Extensions.Companion.isAtTop
import com.nehak.instagramfeed.autoPlay.VideoAutoPlayHelper
import com.nehak.instagramfeed.databinding.FragmentFeedListBinding
import com.nehak.instagramfeed.other.Lg

/**
 * Create By Neha Kushwah
 */
class FeedListFragment : Fragment() {

    private var controlsVisibleShowHide: Boolean = false
    private val hideThreshold = 100
    private var isHeaderAlreadyHidden = false
    lateinit var binding: FragmentFeedListBinding
    private var scrolledDistance: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*Helper class to provide AutoPlay feature inside cell*/
        val videoAutoPlayHelper =
            VideoAutoPlayHelper(recyclerView = binding.recyclerView)

        /* Set adapter (items are being used inside adapter, you can setup in your own way*/
        val feedAdapter = FeedAdapter(requireContext(),object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                print("neha onScrolled horizontal")
                videoAutoPlayHelper.onScrolled(true)
            }
        })
        binding.adapter = feedAdapter

        videoAutoPlayHelper.startObserving()

        /*Helper class to provide show/hide toolBar*/
        attachScrollControlListener(binding.customToolBar, binding.recyclerView)
    }

    /**
     * This method will show hide view passed as @param -toolBar
     */
    private fun attachScrollControlListener(toolBar: View?, recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var firstVisibleItem = -1
                try {
                    firstVisibleItem = recyclerView.findFirstVisibleItemPosition()
                } catch (e: Exception) {
                    Lg.printStackTrace(e)
                }
                if (firstVisibleItem == -1) {
                    return
                }
                //show views if first item is first visible position and views are hidden
                if (firstVisibleItem == 0 && recyclerView.computeVerticalScrollOffset() < hideThreshold) {
                    if (!controlsVisibleShowHide) {
                        controlsVisibleShowHide = true
                        showTopBarWithAnim(toolBar, recyclerView, true)
                        scrolledDistance = 0
                    }
                } else {
                    if (scrolledDistance < -hideThreshold && !controlsVisibleShowHide) {
                        controlsVisibleShowHide = true
                        showTopBarWithAnim(toolBar, recyclerView, true)
                        scrolledDistance = 0
                    } else if (dy > 0/* && hideForcefully()*/ || scrolledDistance > hideThreshold && controlsVisibleShowHide) {
                        controlsVisibleShowHide = false
                        showTopBarWithAnim(toolBar, recyclerView, false)
                        scrolledDistance = 0
                    }
                }
                if (controlsVisibleShowHide && dy > 0 || !controlsVisibleShowHide && dy < 0) {
                    scrolledDistance += dy
                }
            }
        })
    }


    /***
     * Animation to show/hide
     */
    fun showTopBarWithAnim(
        toolBar: View?,
        recyclerView: RecyclerView,
        show: Boolean
    ) {
        if (show) {
            if (!isHeaderAlreadyHidden) {
                return
            }
            isHeaderAlreadyHidden = false
            toolBar?.animate()?.translationY(0f)
                ?.setInterpolator(DecelerateInterpolator(2f))
        } else {
            // To check if at the top of recycler view
            if (recyclerView.isAtTop()
            ) {
                // Its at top
                return
            }
            if (isHeaderAlreadyHidden) {
                return
            }
            isHeaderAlreadyHidden = true
            toolBar?.animate()
                ?.translationY((-toolBar.height).toFloat())
                ?.setInterpolator(AccelerateInterpolator(2F))

        }
    }
}
