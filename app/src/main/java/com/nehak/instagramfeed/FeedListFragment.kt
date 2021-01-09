package com.nehak.instagramfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.nehak.instagramfeed.databinding.FragmentFeedListBinding

class FeedListFragment : Fragment() {

    lateinit var binding : FragmentFeedListBinding;

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

        val feedAdapter = FeedAdapter()
        binding.adapter = feedAdapter
    }
}