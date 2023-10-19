package com.ns.shortsnews.video.data

import android.content.Context
import android.util.Log
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ns.shortsnews.MainApplication
import com.ns.shortsnews.cache.HlsBulkPreloadCoroutine
import com.ns.shortsnews.data.source.UserApiService
import com.ns.shortsnews.utils.AppPreference
import com.ns.shortsnews.utils.IntentLaunch
import com.player.models.VideoData
import com.videopager.data.*
import com.videopager.utils.CategoryConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class VideoDataRepositoryImpl(private val userApiService: UserApiService) : VideoDataRepository {

    override suspend fun videoData(
        id: String,
        videoFrom: String, page: Int, perPage: Int,languages:String,
    ): Flow<MutableList<VideoData>> {
        var ll = mutableListOf<MutableList<VideoData>>()

        Log.i("RequestTT","CategoryId = $id and Language = $languages.toString()")

        val llVid = withContext(Dispatchers.IO) {
            try {
                val response = when (videoFrom) {
                    CategoryConstants.CHANNEL_VIDEO_DATA -> {
                        userApiService.getChannelVideos(id)
                    }

                    CategoryConstants.BOOKMARK_VIDEO_DATA -> {
                        userApiService.getBookmarkVideos(page = page, perPage = perPage)
                    }

                    CategoryConstants.NOTIFICATION_VIDEO_DATA -> {
                        userApiService.getNotificationVideos()
                    }

                    CategoryConstants.DEFAULT_VIDEO_DATA -> {
                        userApiService.getShortsVideos(
                            category = id,
                            page = page,
                            perPage = perPage,
                            languages = languages
                        )
                    }

                    else -> {
                        userApiService.getShortsVideos(
                            category = id,
                            page = page,
                            perPage = perPage,
                            languages = languages
                        )
                    }
                }

                val precachingAllowedCount = response.data.size
                var videoUrls = Array(precachingAllowedCount) { "" }
                var videoIds = Array(precachingAllowedCount) { "" }


                val videoData = response.data
                    .mapIndexed { index, post ->
                        val width = post?.width
                        val height = post?.height
                        val aspectRatio = if (width != null && height != null) {
                            width.toFloat() / height.toFloat()
                        } else {
                            null
                        }
                        if (index < precachingAllowedCount) {
                            videoUrls[index] = post.videoUrl
                            videoIds[index] = post.id.toString()
                        }
//                        Log.i("RequestTT","Id = ${post.id}, MediaUri = ${post.videoUrl}")
                        VideoData(
                            id = post.id,
                            mediaUri = post.videoUrl,
                            previewImageUri = post.preview!!,
                            aspectRatio = aspectRatio,
                            type = post.type,
                            video_url_mp4 = post.video_url_mp4,
                            page = response.page,
                            perPage = response.perPage
                        )

                    }.filter {
                        it.mediaUri.isNotBlank()
                    }

                // Preload Video urls
                HlsBulkPreloadCoroutine.schedulePreloadWork(videoUrls, videoIds)

                Log.i("AshwaniXYZ", "VideoDataRepositoryImpl videoData Size = ${videoData.size}")

                ll.add(videoData as MutableList)
                ll
            } catch (e: Exception) {
                Log.i("RequestTT","Exception = $e")
                if(e is HttpException) {
                    val httpCode = e.code()
                    if(httpCode == 401) {
                        Log.i("Logout", "401")
                        // Logout
                        IntentLaunch.logoutInfoDialog()
                    }
                }
                ll
            }
        }

        return llVid.asFlow()
    }


    override fun like(videoId: String, position: Int): Flow<Triple<String, Boolean, Int>> = flow {
        try {
            val res = userApiService.like(videoId)
            emit(Triple(res.data.like_count, res.data.liked, position))
        } catch (ec: java.lang.Exception) {
            Log.i("kamels", "$ec")
        }

    }

    override fun save(videoId: String, position: Int): Flow<Triple<String, Boolean, Int>> = flow {
        try {
            val res = userApiService.save(videoId)
            emit(Triple(res.data.saved_count, res.data.saved, position))
        } catch (ec: java.lang.Exception) {
            Log.i("kamels", "$ec")
        }
    }


    override fun follow(channel_id: String, position: Int): Flow<Pair<Following, Int>> = flow {
        try {
            val data = userApiService.follow(channel_id)
            Log.i("getVideoInfo", "follow :: ${data.data.following} ")
            Log.i("getVideoInfo", "channel id :: ${data.data.channel_id} ")
            emit(Pair(data, position))
        } catch (ec: java.lang.Exception) {
            Log.i("kamels", "$ec")
        }

    }

    override fun comment(videoId: String, position: Int): Flow<Triple<String, Comments, Int>> =
        flow {
            try {
                val data = userApiService.comment(videoId)
                emit(Triple(videoId, data, position))
            } catch (ec: java.lang.Exception) {
                Log.i("kamels", "$ec")
            }

        }

    override fun getVideoInfo(videoId: String, position: Int): Flow<Pair<VideoInfoData, Int>> =
        flow {

            Log.i("getVideoInfo", "getVideoInfo :: $position")
            try {
                val data = userApiService.getVideoInfo(videoId)
                if (data.status) {
                    emit(Pair(data.data, position))
                    Log.i("getVideoInfo", "getVideoInfo_id :: ${data.data.id}")
                    Log.i("getVideoInfo", "getVideoInfo_following :: ${data.data.following}")
                } else {
                    emit(Pair(VideoInfoData(), position))
                    Log.i("getVideoInfo", "status :: ${data.status}")

                }

                Log.i("getVideoInfo", "Response :: ${data.status}")

            } catch (ec: java.lang.Exception) {
                Log.i("getVideoInfo", "Error :: ${ec.message}")
                emit(Pair(VideoInfoData(), position))
            }

        }

    override fun getPostComment(
        videoId: String,
        comment: String,
        position: Int
    ): Flow<Pair<PostComment, Int>> = flow {
        try {
            val body = mutableMapOf<String, String>()
            body["comment"] = comment
            val data = userApiService.getPostComment(videoId, body)
            emit(Pair(data, position))
        } catch (ec: java.lang.Exception) {
            Log.i("kamels", "$ec")
        }
    }
}
