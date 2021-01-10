package com.nehak.instagramfeed.feedUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nehak.instagramfeed.autoPlay.VideoAutoPlayHelper
import com.nehak.instagramfeed.databinding.FragmentFeedListBinding

/**
 * Create By Neha Kushwah
 */
class FeedListFragment : Fragment() {

    lateinit var binding: FragmentFeedListBinding;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedListBinding.inflate(inflater, container, false);
        return binding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Set adapter (items are being used inside adapter, you can setup in your own way*/
        val feedAdapter = FeedAdapter(requireContext())
        binding.adapter = feedAdapter

        /*Helper class to provide AutoPlay feature inside cell*/
        val videoAutoPlayHelper =
            VideoAutoPlayHelper(recyclerView = binding.recyclerView)
        videoAutoPlayHelper.startObserving();

    }
}