package com.ns.news.presentation.activity.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.ns.news.data.db.Section
import com.ns.news.databinding.FragmentArticleNdWidgetBinding
import com.ns.news.domain.asMergedLoadStates
import com.ns.news.presentation.adapter.ArticleNdWidgetAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter


class HomeArticleNdWidgetFragment : Fragment() {

    private val adapter = ArticleNdWidgetAdapter()
    private val viewModel: ArticleNdWidgetViewModel by viewModels { ArticleNdWidgetViewModelFactory }

    private var _binding: FragmentArticleNdWidgetBinding? = null
    private lateinit var section: Section

    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        section = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable(ARG_SECTION, Section::class.java)!!
        } else {
            arguments?.getParcelable(ARG_SECTION)!!
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleNdWidgetBinding.inflate(inflater, container, false)
        val root = binding.root
        binding.recyclerView.adapter = adapter
        requestPaginationApi()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeToRefresh()
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }


    /**
     * Making API Pagination API Request to get Section's Article & Widget
     */
    private fun requestPaginationApi() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getArticleNdWidget(sectionId = section.sectionId, url = section.api)
                .collectLatest {
                    adapter.submitData(it)
                    Log.i("Ashwani", "Response :: ArticleNdWidget")
                }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collect { loadStates ->
                binding.swipeRefresh.isRefreshing = loadStates.mediator?.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                // Use a state-machine to track LoadStates such that we only transition to
                // NotLoading from a RemoteMediator load if it was also presented to UI.
                .asMergedLoadStates()
                // Only emit when REFRESH changes, as we only want to react on loads replacing the
                // list.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                // Scroll to top is synchronous with UI updates, even if remote load was triggered.
                .collect { binding.recyclerView.scrollToPosition(0) }
        }

    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION = "section"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(section: Section): HomeArticleNdWidgetFragment {
            return HomeArticleNdWidgetFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SECTION, section)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}