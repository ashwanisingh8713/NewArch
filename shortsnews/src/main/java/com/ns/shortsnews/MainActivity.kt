package com.ns.shortsnews

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import coil.imageLoader
import com.exo.players.ExoAppPlayerFactory
import com.exo.ui.ExoAppPlayerViewFactory
import com.ns.shortsnews.adapters.CategoryAdapter
import com.ns.shortsnews.databinding.ActivityMainBinding
import com.ns.shortsnews.user.data.network.NetService
import com.ns.shortsnews.user.data.repository.VideoCategoryRepositoryImp
import com.ns.shortsnews.user.domain.usecase.video_category.VideoCategoryUseCase
import com.ns.shortsnews.user.ui.viewmodel.VideoCategoryViewModel
import com.ns.shortsnews.user.ui.viewmodel.VideoCategoryViewModelFactory
import com.ns.shortsnews.video.data.VideoDataRepository
import com.videopager.ui.VideoPagerFragment
import com.videopager.vm.SharedEventViewModel
import com.videopager.vm.SharedEventViewModelFactory
import com.videopager.vm.VideoPagerViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), onProfileItemClick{
    private lateinit var binding: ActivityMainBinding
    private lateinit var caAdapter: CategoryAdapter

    private val sharedEventViewModel: SharedEventViewModel by viewModels { SharedEventViewModelFactory }

    private val netService = NetService()
    private val videoRepository = VideoCategoryRepositoryImp(netService.createRetrofit())
    private val videoCategoryUseCase = VideoCategoryUseCase(videoRepository)

    private val userViewModelFactory = VideoCategoryViewModelFactory().apply {
        inject(
            videoCategoryUseCase
        )
    }

    private val videoCategoryViewModel: VideoCategoryViewModel by viewModels { userViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.profileIcon.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        videoCategoryViewModel.loadVideoCategory()
        showCategory()
//        youtubeExtractor()
    }

    /**
     * It listens category item click
     */
    override fun itemclick(shortsType: String, position:Int, size:Int) {
        loadHomeFragment(shortsType)
    }


    /**
     * Loads Home Fragment
     */
    private fun loadHomeFragment(shortsType: String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, makeVideoPagerInstance(shortsType))
        ft.commit()
    }

    /**
     * Creates VideoPagerFragment Instance
     */
    private fun makeVideoPagerInstance(shortsType: String): VideoPagerFragment {
        val vpf =  VideoPagerFragment(
            viewModelFactory = { owner ->
                VideoPagerViewModelFactory(
                    repository = VideoDataRepository(),
                    appPlayerFactory = ExoAppPlayerFactory(
                        context = this@MainActivity
                    )
                ).create(owner)
            },
            appPlayerViewFactory = ExoAppPlayerViewFactory(),
            imageLoader = this@MainActivity.imageLoader,
            shortsType = shortsType
        )
        return vpf
    }

    private fun showCategory() {
        lifecycleScope.launch() {
            videoCategoryViewModel.videoCategorySuccessState.filterNotNull().collectLatest {
                // Setup recyclerView
                caAdapter = CategoryAdapter(itemList = it.videoCategories, itemListener = this@MainActivity)
                binding.recyclerView.adapter = caAdapter
                val defaultCate = it.videoCategories[0]
                loadHomeFragment(defaultCate.id)
            }
        }
    }

    private fun youtubeExtractor() {
        object : YouTubeExtractor(this) {
            override fun onExtractionComplete(
                sparseArray: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                if (sparseArray != null) {
                    // 18	mp4	audio/video	360p
                    // 22	mp4	audio/video	720p
                    // 134	mp4	video	360p
                    // 140	m4a	audio	128k
                    val itag = 18
                    loadHomeFragment(sparseArray.get(itag).getUrl())
                }
            }

        }.extract("https://www.youtube.com/watch?v=01omBMDKkDs")
//        }.extract("https://www.youtube.com/shorts?v=uZnWUZW1hQo")
    }


}