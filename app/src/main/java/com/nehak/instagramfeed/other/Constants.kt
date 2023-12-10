package com.nehak.instagramfeed.other

import com.nehak.instagramfeed.dataModels.FeedItem

/**
 * Create By Neha Kushwah
 */
interface Constants {
    companion object {
        val videoList: ArrayList<FeedItem> = ArrayList();
        init {
            var feedItem = FeedItem(0)
            feedItem.downloadUrl = "https://firebasestorage.googleapis.com/v0/b/videolist-fb641.appspot.com/o/video_0.mp4?alt=media&token=cd0fa6be-4bc3-43d0-8a59-635c57b7c7c6"
            feedItem.dimension = "640:640"
            videoList.add(feedItem)

            feedItem = FeedItem(1)
            feedItem.downloadUrl = "https://firebasestorage.googleapis.com/v0/b/videolist-fb641.appspot.com/o/video_1.mp4?alt=media&token=4323ea5c-9d32-4fc8-8a14-75f987dd24ae"
            feedItem.dimension = "640:800"
            videoList.add(feedItem)

            feedItem = FeedItem(2)
            feedItem.downloadUrl = "https://firebasestorage.googleapis.com/v0/b/videolist-fb641.appspot.com/o/video_2.mp4?alt=media&token=550d2f0f-a30e-43cb-b58a-94e80a1b8e1a"
            feedItem.dimension = "640:800"
            videoList.add(feedItem)

            feedItem = FeedItem(3)
            feedItem.downloadUrl = "https://firebasestorage.googleapis.com/v0/b/videolist-fb641.appspot.com/o/video_3.mp4?alt=media&token=750985ae-b5d9-4a5a-83d7-32bbde060ccf"
            feedItem.dimension = "640:1136"
            videoList.add(feedItem)

            feedItem = FeedItem(4)
            feedItem.downloadUrl = "https://firebasestorage.googleapis.com/v0/b/videolist-fb641.appspot.com/o/video_4.mp4?alt=media&token=e01df825-99a8-4d0e-b67d-92dcf2abcf4c"
            feedItem.dimension = "640:640"
            videoList.add(feedItem)

            feedItem = FeedItem(5)
            feedItem.downloadUrl = "https://firebasestorage.googleapis.com/v0/b/videolist-fb641.appspot.com/o/video_5.mp4?alt=media&token=ab48a304-7018-4476-8fc3-d18a0f14a326"
            feedItem.dimension = "640:1226"
            videoList.add(feedItem)

            feedItem = FeedItem(6)
            feedItem.downloadUrl = "https://firebasestorage.googleapis.com/v0/b/videolist-fb641.appspot.com/o/video_6.mp4?alt=media&token=2787c00c-93f8-43b9-876a-2511197050bc"
            feedItem.dimension = "640:640"
            videoList.add(feedItem)

            feedItem = FeedItem(7)
            feedItem.downloadUrl = "https://firebasestorage.googleapis.com/v0/b/videolist-fb641.appspot.com/o/video_7.mp4?alt=media&token=8306429b-2cea-417a-b36c-c204b72c9e4e"
            feedItem.dimension = "640:640"
            videoList.add(feedItem)

            feedItem = FeedItem(8)
            feedItem.downloadUrl = "https://firebasestorage.googleapis.com/v0/b/videolist-fb641.appspot.com/o/video_8.mp4?alt=media&token=90a85c8c-f65d-4a38-bdc6-51600f787d02"
            feedItem.dimension = "640:1136";
            videoList.add(feedItem)

            feedItem = FeedItem(9)
            feedItem.downloadUrl = "https://firebasestorage.googleapis.com/v0/b/videolist-fb641.appspot.com/o/video_9.mp4?alt=media&token=7eac8d4b-d93f-4f5e-a8d8-ae58ae75bcc9"
            feedItem.dimension = "640:800"
            videoList.add(feedItem)

            feedItem = FeedItem(10)
            feedItem.downloadUrl = "https://firebasestorage.googleapis.com/v0/b/videolist-fb641.appspot.com/o/video_10.mp4?alt=media&token=84f66fe1-a1e7-4b41-881d-13f6794e1a9a"
            feedItem.dimension = "640:800"
            videoList.add(feedItem)
        }

    }

}