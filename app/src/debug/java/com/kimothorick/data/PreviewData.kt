package com.kimothorick.data

import com.kimothorick.plashr.data.models.collection.Collection
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.models.topics.Topic
import com.kimothorick.plashr.data.models.user.User
import com.kimothorick.plashr.data.models.user.UserCollection

@Suppress("unused")
val PreviewTopic = Topic(
    id = "bo8jQKTaE0Y",
    slug = "wallpapers",
    title = "Wallpapers",
    description = "From epic drone shots to inspiring moments in nature — submit your best desktop and mobile backgrounds.\r\n\r\n",
    updatedAt = "2025-01-06T09:34:32Z",
    startsAt = "2020-04-15T00:00:00Z",
    endsAt = null,
    onlySubmissionsAfter = null,
    visibility = "featured",
    featured = true,
    totalPhotos = 16261,
    links = Topic.Links(
        self = "https://api.unsplash.com/topics/wallpapers",
        html = "https://unsplash.com/t/wallpapers",
        photos = "https://api.unsplash.com/topics/wallpapers/photos",
    ),
    status = "open",
    owners = listOf(
        Topic.Owner(
            id = "QV5S1rtoUJ0",
            updatedAt = "2024-12-23T14:37:09Z",
            username = "unsplash",
            name = "Unsplash",
            firstName = "Unsplash",
            lastName = null,
            twitterUsername = "unsplash",
            portfolioUrl = "https://www.threads.net/@unsplash",
            bio = "Behind the scenes of the team building the internet’s open library of freely useable visuals.",
            location = "Montreal, Canada",
            links = Topic.Owner.Links(
                self = "https://api.unsplash.com/users/unsplash",
                html = "https://unsplash.com/@unsplash",
                photos = "https://api.unsplash.com/users/unsplash/photos",
                likes = "https://api.unsplash.com/users/unsplash/likes",
                portfolio = "https://api.unsplash.com/users/unsplash/portfolio",
                following = "https://api.unsplash.com/users/unsplash/following",
                followers = "https://api.unsplash.com/users/unsplash/followers",
            ),
            profileImage = Topic.Owner.ProfileImage(
                small = "https://images.unsplash.com/profile-1544707963613-16baf868f301?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
                medium = "https://images.unsplash.com/profile-1544707963613-16baf868f301?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64",
                large = "https://images.unsplash.com/profile-1544707963613-16baf868f301?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128",
            ),
            instagramUsername = "unsplash",
            totalCollections = 19,
            totalLikes = 16115,
            totalPhotos = 29,
            acceptedTos = true,
        ),
    ),
    coverPhoto = Topic.CoverPhoto(
        id = "VmfEp9MEQPI",
        createdAt = "2024-11-29T11:11:38Z",
        updatedAt = "2025-01-06T09:34:31Z",
        width = 8192,
        height = 5464,
        color = "#f3d9d9",
        blurHash = "L~L|l^oJoLoL~VoeayfQnMWpayay",
        description = null,
        altDescription = "A view of a snowy mountain range from a plane",
        urls = Topic.CoverPhoto.Urls(
            raw = "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3",
            full = "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb",
            regular =
                "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q" +
                    "=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max",
            small =
                "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4" +
                    ".0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max",
            thumb =
                "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&" +
                    "q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max",
        ),
        previewPhotos = listOf(
            Topic.CoverPhoto.PreviewPhoto(
                id = "VmfEp9MEQPI",
                createdAt = "2024-11-29T11:11:38Z",
                updatedAt = "2025-01-06T09:34:31Z",
                urls = Topic.CoverPhoto.PreviewPhoto.Urls(
                    raw = "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3",
                    full =
                        "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=85&fm=jpg&" +
                            "crop=entropy&cs=srgb",
                    regular =
                        "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=80&fm=jpg&" +
                            "crop=entropy&cs=tinysrgb&w=1080&fit=max",
                    small =
                        "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=80&fm=jpg&" +
                            "crop=entropy&cs=tinysrgb&w=400&fit=max",
                    thumb =
                        "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=80&fm=jpg&" +
                            "crop=entropy&cs=tinysrgb&w=200&fit=max",
                ),
            ),
            Topic.CoverPhoto.PreviewPhoto(
                id = "dIW6Cgme7EQ",
                createdAt = "2024-12-18T14:24:16Z",
                updatedAt = "2025-01-05T13:47:58Z",
                urls = Topic.CoverPhoto.PreviewPhoto.Urls(
                    raw = "https://images.unsplash.com/photo-1622722931764-974b9dc54dc8",
                    full = "https://images.unsplash.com/photo-1622722931764-974b9dc54dc8?q=85&fm=jpg&" + "crop=entropy&cs=srgb",
                    regular =
                        "https://images.unsplash.com/photo-1622722931764-974b9dc54dc8?q=80&fm=jpg&" +
                            "crop=entropy&cs=tinysrgb&w=1080&fit=max",
                    small =
                        "https://images.unsplash.com/photo-1622722931764-974b9dc54dc8?q=80&fm=jpg&" +
                            "crop=entropy&cs=tinysrgb&w=400&fit=max",
                    thumb =
                        "https://images.unsplash.com/photo-1622722931764-974b9dc54dc8?q=80&fm=jpg&" +
                            "crop=entropy&cs=tinysrgb&w=200&fit=max",
                ),
            ),
        ),
        promotedAt = null,
        links = null,
        user = null,
    ),
    publishedAt = null,
    mediaTypes = null,
    topContributors = listOf(
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = "Rick Kimotho",
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = "Rick Kimotho",
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&" +
                        "crop=faces&bg=%23fff&h=150&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = "Rick Kimotho",
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = "Rick Kimotho",
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = "Rick Kimotho",
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces" +
                        "&bg=%23fff&h=150&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = "Rick Kimotho",
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces" +
                        "&bg=%23fff&h=150&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = "Rick Kimotho",
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces" +
                        "&bg=%23fff&h=150&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
    ),
)

@Suppress("unused")
val PreviewTopicTwo = Topic(
    id = "bo8jQKTaE0Y",
    slug = "wallpapers",
    title = "Wallpapers",
    description =
        "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. " +
            "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis," +
            " ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo," +
            " fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, " +
            "justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper" +
            " nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat",
    updatedAt = "2025-01-06T09:34:32Z",
    startsAt = "2020-04-15T00:00:00Z",
    endsAt = null,
    onlySubmissionsAfter = null,
    visibility = "featured",
    featured = true,
    totalPhotos = 16261,
    links = Topic.Links(
        self = "https://api.unsplash.com/topics/wallpapers",
        html = "https://unsplash.com/t/wallpapers",
        photos = "https://api.unsplash.com/topics/wallpapers/photos",
    ),
    status = "open",
    owners = listOf(
        Topic.Owner(
            id = "QV5S1rtoUJ0",
            updatedAt = "2024-12-23T14:37:09Z",
            username = "unsplash",
            name = "Unsplash",
            firstName = "Unsplash",
            lastName = null,
            twitterUsername = "unsplash",
            portfolioUrl = "https://www.threads.net/@unsplash",
            bio = "Behind the scenes of the team building the internet’s open library of freely useable visuals.",
            location = "Montreal, Canada",
            links = Topic.Owner.Links(
                self = "https://api.unsplash.com/users/unsplash",
                html = "https://unsplash.com/@unsplash",
                photos = "https://api.unsplash.com/users/unsplash/photos",
                likes = "https://api.unsplash.com/users/unsplash/likes",
                portfolio = "https://api.unsplash.com/users/unsplash/portfolio",
                following = "https://api.unsplash.com/users/unsplash/following",
                followers = "https://api.unsplash.com/users/unsplash/followers",
            ),
            profileImage = Topic.Owner.ProfileImage(
                small = "https://images.unsplash.com/profile-1544707963613-16baf868f301?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
                medium = "https://images.unsplash.com/profile-1544707963613-16baf868f301?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64",
                large = "https://images.unsplash.com/profile-1544707963613-16baf868f301?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128",
            ),
            instagramUsername = "unsplash",
            totalCollections = 19,
            totalLikes = 16115,
            totalPhotos = 29,
            acceptedTos = true,
        ),
    ),
    coverPhoto = Topic.CoverPhoto(
        id = "VmfEp9MEQPI",
        createdAt = "2024-11-29T11:11:38Z",
        updatedAt = "2025-01-06T09:34:31Z",
        width = 8192,
        height = 5464,
        color = "#f3d9d9",
        blurHash = "L~L|l^oJoLoL~VoeayfQnMWpayay",
        description = null,
        altDescription = "A view of a snowy mountain range from a plane",
        urls = Topic.CoverPhoto.Urls(
            raw = "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3",
            full = "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb",
            regular =
                "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy" +
                    "&cs=tinysrgb&w=1080&fit=max",
            small =
                "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy" +
                    "&cs=tinysrgb&w=400&fit=max",
            thumb =
                "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy" +
                    "&cs=tinysrgb&w=200&fit=max",
        ),
        previewPhotos = listOf(
            Topic.CoverPhoto.PreviewPhoto(
                id = "VmfEp9MEQPI",
                createdAt = "2024-11-29T11:11:38Z",
                updatedAt = "2025-01-06T09:34:31Z",
                urls = Topic.CoverPhoto.PreviewPhoto.Urls(
                    raw = "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3",
                    full =
                        "" + "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=85&fm=jpg" +
                            "&crop=entropy&cs=srgb",
                    regular =
                        "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=80&fm=jpg" +
                            "&crop=entropy&cs=tinysrgb&w=1080&fit=max",
                    small =
                        "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=80&fm=jpg" +
                            "&crop=entropy&cs=tinysrgb&w=400&fit=max",
                    thumb =
                        "https://plus.unsplash.com/premium_photo-1732736768092-43a010784507?ixlib=rb-4.0.3&q=80&fm=jpg" +
                            "&crop=entropy&cs=tinysrgb&w=200&fit=max",
                ),
            ),
            Topic.CoverPhoto.PreviewPhoto(
                id = "dIW6Cgme7EQ",
                createdAt = "2024-12-18T14:24:16Z",
                updatedAt = "2025-01-05T13:47:58Z",
                urls = Topic.CoverPhoto.PreviewPhoto.Urls(
                    raw = "https://images.unsplash.com/photo-1622722931764-974b9dc54dc8",
                    full = "https://images.unsplash.com/photo-1622722931764-974b9dc54dc8?q=85&fm=jpg&crop=entropy&cs=srgb",
                    regular =
                        "https://images.unsplash.com/photo-1622722931764-974b9dc54dc8?q=80&fm=jpg&crop=entropy&cs=tinysrgb" +
                            "&w=1080&fit=max",
                    small =
                        "https://images.unsplash.com/photo-1622722931764-974b9dc54dc8?q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400" +
                            "&fit=max",
                    thumb =
                        "https://images.unsplash.com/photo-1622722931764-974b9dc54dc8?q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200" +
                            "&fit=max",
                ),
            ),
        ),
        promotedAt = null,
        links = null,
        user = null,
    ),
    publishedAt = "2020-04-17T02:31:04Z",
    mediaTypes = null,
    topContributors = listOf(
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
    ),
)

@Suppress("unused")
val PreviewTopicNoCover = Topic(
    id = "bo8jQKTaE0Y",
    slug = "wallpapers",
    title = "Wallpapers",
    description =
        "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. " +
            "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis," +
            " ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo," +
            " fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, " +
            "justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper" +
            " nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat",
    updatedAt = "2025-01-06T09:34:32Z",
    startsAt = "2020-04-15T00:00:00Z",
    endsAt = null,
    onlySubmissionsAfter = null,
    visibility = "featured",
    featured = true,
    totalPhotos = 16261,
    links = Topic.Links(
        self = "https://api.unsplash.com/topics/wallpapers",
        html = "https://unsplash.com/t/wallpapers",
        photos = "https://api.unsplash.com/topics/wallpapers/photos",
    ),
    status = "open",
    owners = listOf(
        Topic.Owner(
            id = "QV5S1rtoUJ0",
            updatedAt = "2024-12-23T14:37:09Z",
            username = "unsplash",
            name = "Unsplash",
            firstName = "Unsplash",
            lastName = null,
            twitterUsername = "unsplash",
            portfolioUrl = "https://www.threads.net/@unsplash",
            bio = "Behind the scenes of the team building the internet’s open library of freely useable visuals.",
            location = "Montreal, Canada",
            links = Topic.Owner.Links(
                self = "https://api.unsplash.com/users/unsplash",
                html = "https://unsplash.com/@unsplash",
                photos = "https://api.unsplash.com/users/unsplash/photos",
                likes = "https://api.unsplash.com/users/unsplash/likes",
                portfolio = "https://api.unsplash.com/users/unsplash/portfolio",
                following = "https://api.unsplash.com/users/unsplash/following",
                followers = "https://api.unsplash.com/users/unsplash/followers",
            ),
            profileImage = Topic.Owner.ProfileImage(
                small = "https://images.unsplash.com/profile-1544707963613-16baf868f301?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
                medium = "https://images.unsplash.com/profile-1544707963613-16baf868f301?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64",
                large = "https://images.unsplash.com/profile-1544707963613-16baf868f301?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128",
            ),
            instagramUsername = "unsplash",
            totalCollections = 19,
            totalLikes = 16115,
            totalPhotos = 29,
            acceptedTos = true,
        ),
    ),
    coverPhoto = null,
    publishedAt = "2020-04-17T02:31:04Z",
    mediaTypes = null,
    topContributors = listOf(
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = "kimothorick",
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
    ),
)

@Suppress("unused")
val PreviewTopicIllustration = Topic(
    id = "bo8jQKTaE0Y",
    slug = "wallpapers",
    title = "Wallpapers",
    description =
        "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. " +
            "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis," +
            " ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo," +
            " fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, " +
            "justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper" +
            " nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat",
    updatedAt = "2025-01-06T09:34:32Z",
    startsAt = "2020-04-15T00:00:00Z",
    endsAt = null,
    onlySubmissionsAfter = null,
    visibility = "featured",
    featured = false,
    totalPhotos = 16261,
    links = Topic.Links(
        self = "https://api.unsplash.com/topics/wallpapers",
        html = "https://unsplash.com/t/wallpapers",
        photos = "https://api.unsplash.com/topics/wallpapers/photos",
    ),
    status = "open",
    owners = listOf(
        Topic.Owner(
            id = "QV5S1rtoUJ0",
            updatedAt = "2024-12-23T14:37:09Z",
            username = "unsplash",
            name = "Unsplash",
            firstName = "Unsplash",
            lastName = null,
            twitterUsername = "unsplash",
            portfolioUrl = "https://www.threads.net/@unsplash",
            bio = "Behind the scenes of the team building the internet’s open library of freely useable visuals.",
            location = "Montreal, Canada",
            links = Topic.Owner.Links(
                self = "https://api.unsplash.com/users/unsplash",
                html = "https://unsplash.com/@unsplash",
                photos = "https://api.unsplash.com/users/unsplash/photos",
                likes = "https://api.unsplash.com/users/unsplash/likes",
                portfolio = "https://api.unsplash.com/users/unsplash/portfolio",
                following = "https://api.unsplash.com/users/unsplash/following",
                followers = "https://api.unsplash.com/users/unsplash/followers",
            ),
            profileImage = Topic.Owner.ProfileImage(
                small = "https://images.unsplash.com/profile-1544707963613-16baf868f301?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
                medium = "https://images.unsplash.com/profile-1544707963613-16baf868f301?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64",
                large = "https://images.unsplash.com/profile-1544707963613-16baf868f301?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128",
            ),
            instagramUsername = "unsplash",
            totalCollections = 19,
            totalLikes = 16115,
            totalPhotos = 29,
            acceptedTos = true,
        ),
    ),
    coverPhoto = null,
    publishedAt = "2020-04-17T02:31:04Z",
    mediaTypes = listOf("illustration"),
    topContributors = listOf(
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
        Topic.TopContributor(
            id = null,
            updatedAt = null,
            username = null,
            name = null,
            firstName = "Rick",
            lastName = "Kimotho",
            twitterUsername = null,
            portfolioUrl = null,
            bio = null,
            location = null,
            links = null,
            profileImage = Topic.TopContributor.ProfileImage(
                small = null,
                medium =
                    "https://images.unsplash.com/profile-1690671527675-1f837910ccb2image?w=150&dpr=1&crop=faces&bg=%23fff&h=150" +
                        "&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
                large = null,
            ),
            instagramUsername = null,
            totalCollections = null,
            totalLikes = null,
            totalPhotos = null,
            acceptedTos = null,
        ),
    ),
)

@Suppress("unused")
val PreviewPhoto = Photo(
    id = "TDudZ3FIXgY",
    createdAt = "2025-02-15T08:54:24Z",
    updatedAt = "2025-02-25T18:14:28Z",
    width = 5410,
    height = 3604,
    color = "#8ca6a6",
    blurHash = "LeFi+2n%RjR%~qbIRjf6enWBofs;",
    downloads = null,
    likes = 98,
    likedByUser = false,
    description =
        "Küste im Winter in Schweden. Lorem Ipsum has been the industry's standard dummy text ever since" +
            " the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
    exif = Photo.Exif(
        make = "FUJIFILM",
        model = "GFX100S",
        name = "FUJIFILM, GFX100S",
        exposureTime = "1/125",
        aperture = "4.5",
        focalLength = "84.8",
        iso = 250,
    ),
    location = Photo.Location(
        city = "Houston",
        country = "United States of America",
        position = null,
    ),
    tags = listOf(
        Photo.Tag(title = "Winter"),
        Photo.Tag(title = "Switzerland"),
        Photo.Tag(title = "Nature"),
        Photo.Tag(title = "Iceland"),
    ),
    currentUserCollections = null,
    sponsorship = null,
    Photo.Urls(
        raw =
            "https://images.unsplash.com/photo-1739609579483-00b49437cc45?ixid=" +
                "M3wzNzEzOHwwfDF8YWxsfDF8fHx8fHx8fDE3NDA1MTM3Nzl8&ixlib=rb-4.0.3",
        full =
            "https://images.unsplash.com/photo-1739609579483-00b49437cc45?crop=entropy&cs=srgb&fm=" +
                "jpg&ixid=M3wzNzEzOHwwfDF8YWxsfDF8fHx8fHx8fDE3NDA1MTM3Nzl8&ixlib=rb-4.0.3&q=85",
        regular =
            "https://images.unsplash.com/photo-1739609579483-00b49437cc45?crop=entropy&cs=tinysrgb&" +
                "fit=max&fm=jpg&ixid=M3wzNzEzOHwwfDF8YWxsfDF8fHx8fHx8fDE3NDA1MTM3Nzl8&ixlib=rb-4.0.3&q=80&w=1080",
        small =
            "https://images.unsplash.com/photo-1739609579483-00b49437cc45?crop=entropy&cs=tinysrgb&" +
                "fit=max&fm=jpg&ixid=M3wzNzEzOHwwfDF8YWxsfDF8fHx8fHx8fDE3NDA1MTM3Nzl8&ixlib=rb-4.0.3&q=80&w=400",
        thumb =
            "https://images.unsplash.com/photo-1739609579483-00b49437cc45?crop=entropy&cs=tinysrgb&" +
                "fit=max&fm=jpg&ixid=M3wzNzEzOHwwfDF8YWxsfDF8fHx8fHx8fDE3NDA1MTM3Nzl8&ixlib=rb-4.0.3&q=80&w=200",
    ),
    links = Photo.Links(
        self = "https://api.unsplash.com/photos/a-large-body-of-water-surrounded-by-snow-covered-trees-TDudZ3FIXgY",
        html = "https://unsplash.com/photos/a-large-body-of-water-surrounded-by-snow-covered-trees-TDudZ3FIXgY",
        download = "https://unsplash.com/photos/TDudZ3FIXgY/download?ixid=M3wzNzEzOHwwfDF8YWxsfDF8fHx8fHx8fDE3NDA1MTM3Nzl8",
        downloadLocation = "https://api.unsplash.com/photos/TDudZ3FIXgY/download?ixid=M3wzNzEzOHwwfDF8YWxsfDF8fHx8fHx8fDE3NDA1MTM3Nzl8",
    ),
    user = User(
        id = "qTe7XKUQTu8",
        username = "benaja_photos",
        name = "Benaja Germann",
        portfolioUrl = "https://www.benajagermannphotos.com",
        bio =
            "Ich bin Benaja und bin 23 Jahre alt.\r\nIch liebe es die Schönheiten im leben zu fotografieren " +
                "und mich inspirieren zu lassen.",
        location = "Switzerland",
        totalLikes = 8,
        totalPhotos = 139,
        totalCollections = 2,
        instagramUsername = "benaja_photos",
        twitterUsername = null,
        profileImage = User.ProfileImage(
            small = "https://images.unsplash.com/profile-1738932038458-653f93ac2c7e?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
            medium = "https://images.unsplash.com/profile-1738932038458-653f93ac2c7e?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64",
            large = "https://images.unsplash.com/profile-1738932038458-653f93ac2c7e?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128",
        ),
        updatedAt = "2025-02-25T13:50:44Z",
        firstName = "Benaja",
        lastName = "Germann",
        downloads = null,
        social = User.Social(
            instagramUsername = "benaja_photos",
            portfolioUrl = "https://www.benajagermannphotos.com",
            twitterUsername = null,
        ),
        badge = null,
        links = User.Links(
            self = "https://api.unsplash.com/users/benaja_photos",
            html = "https://unsplash.com/@benaja_photos",
            photos = "https://api.unsplash.com/users/benaja_photos/photos",
            likes = "https://api.unsplash.com/users/benaja_photos/likes",
            portfolio = "https://api.unsplash.com/users/benaja_photos/portfolio",
        ),
        tags = null,
    ),
)

@Suppress("unused")
val PreviewCollection = Collection(
    id = "on2383767",
    title = "BIrds",
    description = null,
    publishedAt = "2025-04-28T14:18:31Z",
    lastCollectedAt = "2025-03-20T15:25:46Z",
    updatedAt = "2025-04-28T14:18:31Z",
    featured = true,
    totalPhotos = 199,
    private = false,
    shareKey = "8257b263e335c781e2569fb5741c6b80",
    links = Collection.Links(
        self = "https://api.unsplash.com/collections/2383767",
        html = "https://unsplash.com/collections/2383767/birds",
        photos = "https://api.unsplash.com/collections/2383767/photos",
        related = "https://api.unsplash.com/collections/2383767/related",
    ),
    user = User(
        id = "ctny2kwZzEY",
        updatedAt = "2024-11-04T01:52:38Z",
        username = "gerijean",
        name = "Geri Jean",
        firstName = "Geri",
        lastName = "Jean",
        instagramUsername = null,
        twitterUsername = null,
        portfolioUrl = null,
        bio = null,
        location = null,
        totalLikes = 718,
        totalPhotos = 1,
        totalCollections = 134,
        downloads = null,
        social = User.Social(
            instagramUsername = null,
            portfolioUrl = null,
            twitterUsername = null,
        ),
        profileImage = User.ProfileImage(
            small = "https://images.unsplash.com/profile-1532034266324-e506d5c8130c?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
            medium = "https://images.unsplash.com/profile-1532034266324-e506d5c8130c?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64",
            large = "https://images.unsplash.com/profile-1532034266324-e506d5c8130c?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128",
        ),
        badge = null,
        links = User.Links(
            self = "https://api.unsplash.com/users/gerijean",
            html = "https://unsplash.com/@gerijean",
            photos = "https://api.unsplash.com/users/gerijean/photos",
            likes = "https://api.unsplash.com/users/gerijean/likes",
            portfolio = "https://api.unsplash.com/users/gerijean/portfolio",
        ),
        tags = null,
    ),
    coverPhoto = Photo(
        id = "Ov7njYra5X4",
        createdAt = "2021-04-30T18:11:20Z",
        updatedAt = "2025-05-14T18:01:19Z",
        width = 3174,
        height = 4761,
        color = "#738c40",
        blurHash = "LFEWQgRQOmi{??t6kSR6GTf+Q;oe",
        downloads = null,
        likes = 14,
        likedByUser = false,
        description = null,
        exif = null,
        location = null,
        tags = null,
        currentUserCollections = null,
        sponsorship = null,
        urls = null,
        links = null,
        user = null,
    ),
    previewPhotos = listOf(
        Collection.PreviewPhoto(
            id = "Ov7njYra5X4",
            slug = "brown-bird-on-brown-tree-trunk-Ov7njYra5X4",
            createdAt = "2021-04-30T18:11:20Z",
            updatedAt = "2025-05-14T18:01:19Z",
            blurHash = "LFEWQgRQOmi{??t6kSR6GTf+Q;oe",
            assetType = "photo",
            urls = Collection.PreviewPhoto.Urls(
                raw = "https://images.unsplash.com/photo-1619806101997-a17bbb1fa2d8?ixlib=rb-4.1.0",
                full = "https://images.unsplash.com/photo-1619806101997-a17bbb1fa2d8?ixlib=rb-4.1.0&q=85&fm=jpg&crop=entropy&cs=srgb",
                regular =
                    "https://images.unsplash.com/photo-1619806101997-a17bbb1fa2d8?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=1080&fit=max",
                small =
                    "https://images.unsplash.com/photo-1619806101997-a17bbb1fa2d8?ixlib=rb-4.1.0&q=80&fm=jpg&crop=entropy" +
                        "&cs=tinysrgb&w=400&fit=max",
                thumb =
                    "https://images.unsplash.com/photo-1619806101997-a17bbb1fa2d8?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=200&fit=max",
            ),
        ),
        Collection.PreviewPhoto(
            id = "o4fi007rmTs",
            slug = "bird-in-flight-over-the-plants-o4fi007rmTs",
            createdAt = "2017-10-25T21:38:58Z",
            updatedAt = "2025-05-14T20:00:12Z",
            blurHash = "LQL;m-RiR;XA~WW@ROf69cV?aIj?",
            assetType = "photo",
            urls = Collection.PreviewPhoto.Urls(
                raw = "https://images.unsplash.com/photo-1508967497127-90082386bb59?ixlib=rb-4.1.0",
                full = "https://images.unsplash.com/photo-1508967497127-90082386bb59?ixlib=rb-4.1.0&q=85&fm=jpg&" + "crop=entropy&cs=srgb",
                regular =
                    "https://images.unsplash.com/photo-1508967497127-90082386bb59?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=1080&fit=max",
                small =
                    "https://images.unsplash.com/photo-1508967497127-90082386bb59?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=400&fit=max",
                thumb =
                    "https://images.unsplash.com/photo-1508967497127-90082386bb59?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=200&fit=max",
            ),
        ),
        Collection.PreviewPhoto(
            id = "3Okh7LBdgvc",
            slug = "a-yellow-bird-sitting-on-a-branch-of-a-tree-3Okh7LBdgvc",
            createdAt = "2022-03-19T21:28:13Z",
            updatedAt = "2025-05-14T18:22:26Z",
            blurHash = "LQL;m-RiR;XA~WW@ROf69cV?aIj?",
            assetType = "photo",
            urls = Collection.PreviewPhoto.Urls(
                raw = "https://images.unsplash.com/photo-1647725280666-bb7f94a15d69?ixlib=rb-4.1.0",
                full = "https://images.unsplash.com/photo-1647725280666-bb7f94a15d69?ixlib=rb-4.1.0&q=85&fm=jpg&" + "crop=entropy&cs=srgb",
                regular =
                    "https://images.unsplash.com/photo-1647725280666-bb7f94a15d69?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=1080&fit=max",
                small =
                    "https://images.unsplash.com/photo-1647725280666-bb7f94a15d69?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=400&fit=max",
                thumb =
                    "https://images.unsplash.com/photo-1647725280666-bb7f94a15d69?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=200&fit=max",
            ),
        ),
        Collection.PreviewPhoto(
            id = "Lej_oqHljbk",
            slug = "blue-parrot-standing-on-brown-tree-branch-Lej_oqHljbk",
            createdAt = "2017-09-24T02:50:10Z",
            updatedAt = "2025-05-14T19:59:37Z",
            blurHash = "L53bq6I7%ONMtTROo#WsInxvM{xa",
            assetType = "photo",
            urls = Collection.PreviewPhoto.Urls(
                raw = "https://images.unsplash.com/photo-1506220926022-cc5c12acdb35?ixlib=rb-4.1.0",
                full = "https://images.unsplash.com/photo-1506220926022-cc5c12acdb35?ixlib=rb-4.1.0&q=85&fm=jpg&crop=entropy&cs=srgb",
                regular =
                    "https://images.unsplash.com/photo-1506220926022-cc5c12acdb35?ixlib=rb-4.1.0&q=80&fm=jpg&crop=entropy&cs=tinysrgb" +
                        "&w=1080&fit=max",
                small =
                    "https://images.unsplash.com/photo-1506220926022-cc5c12acdb35?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=400&fit=max",
                thumb =
                    "https://images.unsplash.com/photo-1506220926022-cc5c12acdb35?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=200&fit=max",
            ),
        ),
    ),
)

@Suppress("unused")
val PreviewCollectionTwo = Collection(
    id = "on2383767",
    title = "BIrds",
    description =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut" +
            " labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
            "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate" +
            " velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, " +
            "sunt in culpa qui officia deserunt mollit anim id est laborum.",
    publishedAt = "2025-04-28T14:18:31Z",
    lastCollectedAt = "2025-03-20T15:25:46Z",
    updatedAt = "2025-04-28T14:18:31Z",
    featured = true,
    totalPhotos = 199,
    private = false,
    shareKey = "8257b263e335c781e2569fb5741c6b80",
    links = Collection.Links(
        self = "https://api.unsplash.com/collections/2383767",
        html = "https://unsplash.com/collections/2383767/birds",
        photos = "https://api.unsplash.com/collections/2383767/photos",
        related = "https://api.unsplash.com/collections/2383767/related",
    ),
    user = User(
        id = "ctny2kwZzEY",
        updatedAt = "2024-11-04T01:52:38Z",
        username = "gerijean",
        name = "Geri Jean",
        firstName = "Geri",
        lastName = "Jean",
        instagramUsername = null,
        twitterUsername = null,
        portfolioUrl = null,
        bio = null,
        location = null,
        totalLikes = 718,
        totalPhotos = 1,
        totalCollections = 134,
        downloads = null,
        social = User.Social(
            instagramUsername = null,
            portfolioUrl = null,
            twitterUsername = null,
        ),
        /* profileImage = User.ProfileImage(
             small = "https://images.unsplash.com/profile-1532034266324-e506d5c8130c?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
             medium = "https://images.unsplash.com/profile-1532034266324-e506d5c8130c?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64",
             large = "https://images.unsplash.com/profile-1532034266324-e506d5c8130c?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128",
         ),*/
        profileImage = User.ProfileImage(
            small = "",
            medium = "",
            large = "",
        ),
        badge = null,
        links = User.Links(
            self = "https://api.unsplash.com/users/gerijean",
            html = "https://unsplash.com/@gerijean",
            photos = "https://api.unsplash.com/users/gerijean/photos",
            likes = "https://api.unsplash.com/users/gerijean/likes",
            portfolio = "https://api.unsplash.com/users/gerijean/portfolio",
        ),
        tags = null,
    ),
    coverPhoto = Photo(
        id = "Ov7njYra5X4",
        createdAt = "2021-04-30T18:11:20Z",
        updatedAt = "2025-05-14T18:01:19Z",
        width = 3174,
        height = 4761,
        color = "#738c40",
        blurHash = "LFEWQgRQOmi{??t6kSR6GTf+Q;oe",
        downloads = null,
        likes = 14,
        likedByUser = false,
        description = null,
        exif = null,
        location = null,
        tags = null,
        currentUserCollections = null,
        sponsorship = null,
        urls = null,
        links = null,
        user = null,
    ),
    previewPhotos = listOf(
        Collection.PreviewPhoto(
            id = "Ov7njYra5X4",
            slug = "brown-bird-on-brown-tree-trunk-Ov7njYra5X4",
            createdAt = "2021-04-30T18:11:20Z",
            updatedAt = "2025-05-14T18:01:19Z",
            blurHash = "LFEWQgRQOmi{??t6kSR6GTf+Q;oe",
            assetType = "photo",
            urls = Collection.PreviewPhoto.Urls(
                raw = "https://images.unsplash.com/photo-1619806101997-a17bbb1fa2d8?ixlib=rb-4.1.0",
                full = "https://images.unsplash.com/photo-1619806101997-a17bbb1fa2d8?ixlib=rb-4.1.0&q=85&fm=jpg&crop=entropy&cs=srgb",
                regular =
                    "https://images.unsplash.com/photo-1619806101997-a17bbb1fa2d8?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=1080&fit=max",
                small =
                    "https://images.unsplash.com/photo-1619806101997-a17bbb1fa2d8?ixlib=rb-4.1.0&q=80&fm=jpg&crop=entropy" +
                        "&cs=tinysrgb&w=400&fit=max",
                thumb =
                    "https://images.unsplash.com/photo-1619806101997-a17bbb1fa2d8?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=200&fit=max",
            ),
        ),
        Collection.PreviewPhoto(
            id = "o4fi007rmTs",
            slug = "bird-in-flight-over-the-plants-o4fi007rmTs",
            createdAt = "2017-10-25T21:38:58Z",
            updatedAt = "2025-05-14T20:00:12Z",
            blurHash = "LQL;m-RiR;XA~WW@ROf69cV?aIj?",
            assetType = "photo",
            urls = Collection.PreviewPhoto.Urls(
                raw = "https://images.unsplash.com/photo-1508967497127-90082386bb59?ixlib=rb-4.1.0",
                full = "https://images.unsplash.com/photo-1508967497127-90082386bb59?ixlib=rb-4.1.0&q=85&fm=jpg&" + "crop=entropy&cs=srgb",
                regular =
                    "https://images.unsplash.com/photo-1508967497127-90082386bb59?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=1080&fit=max",
                small =
                    "https://images.unsplash.com/photo-1508967497127-90082386bb59?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=400&fit=max",
                thumb =
                    "https://images.unsplash.com/photo-1508967497127-90082386bb59?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=200&fit=max",
            ),
        ),
        Collection.PreviewPhoto(
            id = "3Okh7LBdgvc",
            slug = "a-yellow-bird-sitting-on-a-branch-of-a-tree-3Okh7LBdgvc",
            createdAt = "2022-03-19T21:28:13Z",
            updatedAt = "2025-05-14T18:22:26Z",
            blurHash = "LQL;m-RiR;XA~WW@ROf69cV?aIj?",
            assetType = "photo",
            urls = Collection.PreviewPhoto.Urls(
                raw = "https://images.unsplash.com/photo-1647725280666-bb7f94a15d69?ixlib=rb-4.1.0",
                full = "https://images.unsplash.com/photo-1647725280666-bb7f94a15d69?ixlib=rb-4.1.0&q=85&fm=jpg&" + "crop=entropy&cs=srgb",
                regular =
                    "https://images.unsplash.com/photo-1647725280666-bb7f94a15d69?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=1080&fit=max",
                small =
                    "https://images.unsplash.com/photo-1647725280666-bb7f94a15d69?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=400&fit=max",
                thumb =
                    "https://images.unsplash.com/photo-1647725280666-bb7f94a15d69?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=200&fit=max",
            ),
        ),
        Collection.PreviewPhoto(
            id = "Lej_oqHljbk",
            slug = "blue-parrot-standing-on-brown-tree-branch-Lej_oqHljbk",
            createdAt = "2017-09-24T02:50:10Z",
            updatedAt = "2025-05-14T19:59:37Z",
            blurHash = "L53bq6I7%ONMtTROo#WsInxvM{xa",
            assetType = "photo",
            urls = Collection.PreviewPhoto.Urls(
                raw = "https://images.unsplash.com/photo-1506220926022-cc5c12acdb35?ixlib=rb-4.1.0",
                full = "https://images.unsplash.com/photo-1506220926022-cc5c12acdb35?ixlib=rb-4.1.0&q=85&fm=jpg&crop=entropy&cs=srgb",
                regular =
                    "https://images.unsplash.com/photo-1506220926022-cc5c12acdb35?ixlib=rb-4.1.0&q=80&fm=jpg&crop=entropy&cs=tinysrgb" +
                        "&w=1080&fit=max",
                small =
                    "https://images.unsplash.com/photo-1506220926022-cc5c12acdb35?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=400&fit=max",
                thumb =
                    "https://images.unsplash.com/photo-1506220926022-cc5c12acdb35?ixlib=rb-4.1.0&q=80&fm=jpg&" +
                        "crop=entropy&cs=tinysrgb&w=200&fit=max",
            ),
        ),
    ),
)

@Suppress("unused")
val PreviewUserCollection = UserCollection(
    id = "2383767",
    title = "BIrds",
    description = null,
    publishedAt = "2025-04-28T14:18:31Z",
    lastCollectedAt = "2025-03-20T15:25:46Z",
    updatedAt = "2025-04-28T14:18:31Z",
    totalPhotos = 199,
    private = false,
    shareKey = "8257b263e335c781e2569fb5741c6b80",
    coverPhoto = UserCollection.CoverPhoto(
        id = "Ov7njYra5X4",
        width = 3174,
        height = 4761,
        color = "#738c40",
        blurHash = "LFEWQgRQOmi{??t6kSR6GTf+Q;oe",
        likes = 14,
        likedByUser = false,
        description = null,
        urls = null,
        links = null,
        user = null,
    ),
    links = UserCollection.Links(
        self = "https://api.unsplash.com/collections/2383767",
        html = "https://unsplash.com/collections/2383767/birds",
        photos = "https://api.unsplash.com/collections/2383767/photos",
        related = "https://api.unsplash.com/collections/2383767/related",
    ),
    user = UserCollection.User(
        id = "ctny2kwZzEY",
        updatedAt = "2024-11-04T01:52:38Z",
        username = "gerijean",
        name = "Geri Jean",
        portfolioUrl = null,
        bio = null,
        location = null,
        totalLikes = 718,
        totalPhotos = 1,
        totalCollections = 134,
        profileImage = UserCollection.User.ProfileImage(
            small = "https://images.unsplash.com/profile-1532034266324-e506d5c8130c?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
            medium = "https://images.unsplash.com/profile-1532034266324-e506d5c8130c?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64",
            large = "https://images.unsplash.com/profile-1532034266324-e506d5c8130c?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128",
        ),
        links = UserCollection.User.Links(
            self = "https://api.unsplash.com/users/gerijean",
            html = "https://unsplash.com/@gerijean",
            photos = "https://api.unsplash.com/users/gerijean/photos",
            likes = "https://api.unsplash.com/users/gerijean/likes",
            portfolio = "https://api.unsplash.com/users/gerijean/portfolio",
        ),
    ),
)

@Suppress("unused")
val PreviewUser = User(
    id = "uKYwEOy0h1o",
    updatedAt = "2024-02-16T22:59:01Z",
    username = "kohalloran",
    name = "Kelsey O'Halloran",
    firstName = "Kelsey",
    lastName = "O'Halloran",
    instagramUsername = null, // Top-level instagram_username from JSON
    twitterUsername = null, // Top-level twitter_username from JSON
    portfolioUrl = null, // Top-level portfolio_url from JSON
    bio = null,
    location = null,
    totalLikes = 0,
    totalPhotos = 0,
    totalCollections = 3,
    downloads = 0,
    social = User.Social(
        instagramUsername = null, // from JSON social.instagram_username
        portfolioUrl = null, // from JSON social.portfolio_url
        twitterUsername = null, // from JSON social.twitter_username
    ),
    profileImage = User.ProfileImage(
        small = "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
        medium = "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64",
        large = "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128",
    ),
    badge = null, // JSON provides "badge": null
    links = User.Links(
        self = "https://api.unsplash.com/users/kohalloran",
        html = "https://unsplash.com/@kohalloran",
        photos = "https://api.unsplash.com/users/kohalloran/photos",
        likes = "https://api.unsplash.com/users/kohalloran/likes",
        portfolio = "https://api.unsplash.com/users/kohalloran/portfolio",
    ),
    tags = User.UserTags(
        custom = listOf(
            User.UserTags.UserCustomTag(type = "search", title = "abstract"),
            User.UserTags.UserCustomTag(type = "search", title = "beach"),
            User.UserTags.UserCustomTag(type = "search", title = "forest"),
            User.UserTags.UserCustomTag(type = "search", title = "texture"),
            User.UserTags.UserCustomTag(type = "search", title = "wallpaper"),
        ),
    ),
)

@Suppress("unused")
val PreviewUser2 = User(
    id = "M0p13r7GcEc",
    updatedAt = "2025-06-04T07:46:52Z",
    username = "da_daisy",
    name = "Rita Daisy",
    firstName = "Rita",
    lastName = "Daisy",
    instagramUsername = null, // Top-level instagram_username from JSON
    twitterUsername = null, // Top-level twitter_username from JSON
    portfolioUrl = null, // Top-level portfolio_url from JSON
    bio = null,
    location = null,
    totalLikes = 0,
    totalPhotos = 31,
    totalCollections = 1,
    downloads = 5229,
    social = User.Social(
        instagramUsername = null, // from JSON social.instagram_username
        portfolioUrl = null, // from JSON social.portfolio_url
        twitterUsername = null, // from JSON social.twitter_username
    ),
    profileImage = User.ProfileImage(
        small = "https://images.unsplash.com/profile-1731736935405-c143fd4bfd79?ixlib=rb-4.1.0&crop=faces&fit=crop&w=32&h=32",
        medium = "https://images.unsplash.com/profile-1731736935405-c143fd4bfd79?ixlib=rb-4.1.0&crop=faces&fit=crop&w=64&h=64",
        large = "https://images.unsplash.com/profile-1731736935405-c143fd4bfd79?ixlib=rb-4.1.0&crop=faces&fit=crop&w=128&h=128",
    ),
    badge = null, // JSON provides "badge": null
    links = User.Links(
        self = "https://api.unsplash.com/users/da_daisy",
        html = "https://unsplash.com/@da_daisy",
        photos = "https://api.unsplash.com/users/da_daisy/photos",
        likes = "https://api.unsplash.com/users/da_daisy/likes",
        portfolio = "https://api.unsplash.com/users/da_daisy/portfolio",
    ),
    tags = User.UserTags(
        custom = listOf(
            User.UserTags.UserCustomTag(type = "search", title = "beach"),
            User.UserTags.UserCustomTag(type = "search", title = "forest"),
            User.UserTags.UserCustomTag(type = "search", title = "outdoor"),
            User.UserTags.UserCustomTag(type = "search", title = "scenery"),
            User.UserTags.UserCustomTag(type = "search", title = "summer"),
        ),
    ),
)

@Suppress("unused")
val PreviewUser3 = User(
    id = "lkesy2Bw8Mo",
    updatedAt = "2025-06-05T17:05:15Z",
    username = "mrnuclear",
    name = "ZHENYU LUO",
    firstName = "ZHENYU",
    lastName = "LUO",
    instagramUsername = null, // Top-level instagram_username from JSON
    twitterUsername = null, // Top-level twitter_username from JSON
    portfolioUrl = "https://zhenyuluo.myportfolio.com/", // Top-level portfolio_url from JSON
    bio = null,
    location = "Hong Kong", // From JSON
    totalLikes = 1,
    totalPhotos = 499,
    totalCollections = 0,
    downloads = 953211, // From JSON
    social = User.Social(
        instagramUsername = null, // from JSON social.instagram_username
        portfolioUrl = "https://zhenyuluo.myportfolio.com/", // from JSON social.portfolio_url
        twitterUsername = null, // from JSON social.twitter_username
    ),
    profileImage = User.ProfileImage(
        small = "https://images.unsplash.com/profile-1597672759738-1551440eaab3image?ixlib=rb-4.1.0&crop=faces&fit=crop&w=32&h=32",
        medium = "https://images.unsplash.com/profile-1597672759738-1551440eaab3image?ixlib=rb-4.1.0&crop=faces&fit=crop&w=64&h=64",
        large = "https://images.unsplash.com/profile-1597672759738-1551440eaab3image?ixlib=rb-4.1.0&crop=faces&fit=crop&w=128&h=128",
    ),
    badge = User.Badge(
        // Badge object is present in this JSON
        title = "Unsplash Awards 2024 Select",
        primary = true,
        slug = "unsplash-awards-select-2024",
        link = "https://awards.unsplash.com/2024/",
    ),
    links = User.Links(
        self = "https://api.unsplash.com/users/mrnuclear",
        html = "https://unsplash.com/@mrnuclear",
        photos = "https://api.unsplash.com/users/mrnuclear/photos",
        likes = "https://api.unsplash.com/users/mrnuclear/likes",
        portfolio = "https://api.unsplash.com/users/mrnuclear/portfolio",
    ),
    tags = User.UserTags(
        custom = listOf(
            User.UserTags.UserCustomTag(type = "search", title = "flower"),
            User.UserTags.UserCustomTag(type = "search", title = "forest"),
            User.UserTags.UserCustomTag(type = "search", title = "Nature"),
            User.UserTags.UserCustomTag(type = "search", title = "sky"),
            User.UserTags.UserCustomTag(type = "search", title = "technology"),
        ),
    ),
)

@Suppress("unused")
val PreviewUser4 = User(
    id = "yFaf_R9HdWQ",
    updatedAt = "2025-06-05T00:34:44Z",
    username = "lukethornton",
    name = "Luke Thornton",
    firstName = "Luke",
    lastName = "Thornton",
    instagramUsername = "imlukethornton", // Top-level instagram_username from JSON
    twitterUsername = "imlukethornton", // Top-level twitter_username from JSON
    portfolioUrl = "http://www.linkedin.com/in/lukethorntonofficial", // Top-level portfolio_url from JSON
    bio = "Managing Director at Influence Media 🇬🇧\r\n https://www.weareinfluencemedia.com", // From JSON
    location = "Hull, United Kingdom", // From JSON
    totalLikes = 0,
    totalPhotos = 702,
    totalCollections = 0,
    downloads = 502432, // From JSON
    social = User.Social(
        instagramUsername = "imlukethornton", // from JSON social.instagram_username
        portfolioUrl = "http://www.linkedin.com/in/lukethorntonofficial", // from JSON social.portfolio_url
        twitterUsername = "imlukethornton", // from JSON social.twitter_username
    ),
    profileImage = User.ProfileImage(
        small = "https://images.unsplash.com/profile-1683634928315-4d6b3ec4d1fbimage?ixlib=rb-4.1.0&crop=faces&fit=crop&w=32&h=32",
        medium = "https://images.unsplash.com/profile-1683634928315-4d6b3ec4d1fbimage?ixlib=rb-4.1.0&crop=faces&fit=crop&w=64&h=64",
        large = "https://images.unsplash.com/profile-1683634928315-4d6b3ec4d1fbimage?ixlib=rb-4.1.0&crop=faces&fit=crop&w=128&h=128",
    ),
    badge = null, // JSON provides "badge": null
    links = User.Links(
        self = "https://api.unsplash.com/users/lukethornton",
        html = "https://unsplash.com/@lukethornton",
        photos = "https://api.unsplash.com/users/lukethornton/photos",
        likes = "https://api.unsplash.com/users/lukethornton/likes",
        portfolio = "https://api.unsplash.com/users/lukethornton/portfolio",
    ),
    tags = User.UserTags(
        custom = listOf(
            User.UserTags.UserCustomTag(type = "search", title = "automotive"),
            User.UserTags.UserCustomTag(type = "search", title = "commercial"),
            User.UserTags.UserCustomTag(type = "search", title = "landscape"),
            User.UserTags.UserCustomTag(type = "search", title = "Nature"),
            User.UserTags.UserCustomTag(type = "search", title = "travel"),
        ),
    ),
)

@Suppress("unused")
val PreviewUser5 = User(
    id = "dg4S8j5TzmE",
    updatedAt = "2025-06-05T14:20:09Z",
    username = "karsten116",
    name = "Karsten Winegeart",
    firstName = "Karsten",
    lastName = "Winegeart",
    instagramUsername = "karsten116", // Top-level instagram_username from JSON
    twitterUsername = "karsten116", // Top-level twitter_username from JSON
    portfolioUrl = "http://dimelabscreative.com", // Top-level portfolio_url from JSON
    bio = "IG - @karsten116", // From JSON
    location = "Austin Texas", // From JSON
    totalLikes = 1213,
    totalPhotos = 1247,
    totalCollections = 2,
    downloads = 9602213, // From JSON
    social = User.Social(
        instagramUsername = "karsten116", // from JSON social.instagram_username
        portfolioUrl = "http://dimelabscreative.com", // from JSON social.portfolio_url
        twitterUsername = "karsten116", // from JSON social.twitter_username
    ),
    profileImage = User.ProfileImage(
        small = "https://images.unsplash.com/profile-1746726501631-71d5b03c17a0?ixlib=rb-4.1.0&crop=faces&fit=crop&w=32&h=32",
        medium = "https://images.unsplash.com/profile-1746726501631-71d5b03c17a0?ixlib=rb-4.1.0&crop=faces&fit=crop&w=64&h=64",
        large = "https://images.unsplash.com/profile-1746726501631-71d5b03c17a0?ixlib=rb-4.1.0&crop=faces&fit=crop&w=128&h=128",
    ),
    badge = User.Badge(
        // Badge object is present in this JSON
        title = "Unsplash Awards 2024 Nominee",
        primary = true,
        slug = "unsplash-awards-nominee-2024",
        link = "https://awards.unsplash.com/2024/",
    ),
    links = User.Links(
        self = "https://api.unsplash.com/users/karsten116",
        html = "https://unsplash.com/@karsten116",
        photos = "https://api.unsplash.com/users/karsten116/photos",
        likes = "https://api.unsplash.com/users/karsten116/likes",
        portfolio = "https://api.unsplash.com/users/karsten116/portfolio",
    ),
    tags = User.UserTags(
        custom = listOf(
            User.UserTags.UserCustomTag(type = "search", title = "dogs"),
            User.UserTags.UserCustomTag(type = "search", title = "fitness"),
            User.UserTags.UserCustomTag(type = "search", title = "travel"),
            User.UserTags.UserCustomTag(type = "search", title = "Portraits"),
            User.UserTags.UserCustomTag(type = "search", title = "Product"),
        ),
    ),
)

@Suppress("unused")
val PreviewUser6 = User(
    id = "sRrJOw0jZM8",
    updatedAt = "2025-06-05T14:05:09Z",
    username = "stachmann",
    name = "Richard Stachmann",
    firstName = "Richard",
    lastName = "Stachmann",
    instagramUsername = "stachmannr_", // Top-level instagram_username from JSON
    twitterUsername = null, // Top-level twitter_username from JSON
    portfolioUrl = null, // Top-level portfolio_url from JSON
    bio = "📍Budapest", // From JSON
    location = "Hungary, Budapest", // From JSON
    totalLikes = 2612,
    totalPhotos = 1645,
    totalCollections = 1,
    downloads = 674108, // From JSON
    social = User.Social(
        instagramUsername = "stachmannr_", // from JSON social.instagram_username
        portfolioUrl = null, // from JSON social.portfolio_url
        twitterUsername = null, // from JSON social.twitter_username
    ),
    profileImage = User.ProfileImage(
        small = "https://images.unsplash.com/profile-fb-1661599666-081b04e75823.jpg?ixlib=rb-4.1.0&crop=faces&fit=crop&w=32&h=32",
        medium = "https://images.unsplash.com/profile-fb-1661599666-081b04e75823.jpg?ixlib=rb-4.1.0&crop=faces&fit=crop&w=64&h=64",
        large = "https://images.unsplash.com/profile-fb-1661599666-081b04e75823.jpg?ixlib=rb-4.1.0&crop=faces&fit=crop&w=128&h=128",
    ),
    badge = User.Badge(
        // Badge object is present in this JSON
        title = "Unsplash Awards 2024 Nominee",
        primary = true,
        slug = "unsplash-awards-nominee-2024",
        link = "https://awards.unsplash.com/2024/",
    ),
    links = User.Links(
        self = "https://api.unsplash.com/users/stachmann",
        html = "https://unsplash.com/@stachmann",
        photos = "https://api.unsplash.com/users/stachmann/photos",
        likes = "https://api.unsplash.com/users/stachmann/likes",
        portfolio = "https://api.unsplash.com/users/stachmann/portfolio",
    ),
    tags = User.UserTags(
        custom = listOf(
            User.UserTags.UserCustomTag(type = "search", title = "city"),
            User.UserTags.UserCustomTag(type = "search", title = "film photography"),
            User.UserTags.UserCustomTag(type = "search", title = "Nature"),
            User.UserTags.UserCustomTag(type = "search", title = "street"),
        ),
        // The "aggregated" part of tags from JSON is not in your UserTags data class, so it's ignored.
    ),
    // The top-level "photos": [...] array from the JSON is ignored.
    // Other fields like "total_promoted_photos", "accepted_tos", "for_hire", "allow_messages",
    // "numeric_id", "meta" from the JSON are also ignored as they are not in your User data class.
)

@Suppress("unused")
val PreviewUser7 = User(
    id = "uKYwEOy0h1o",
    updatedAt = "2024-02-16T22:59:01Z",
    username = "kohalloran",
    name = "Kelsey O'Halloran",
    firstName = "Kelsey",
    lastName = "O'Halloran",
    instagramUsername = null, // Top-level instagram_username from JSON
    twitterUsername = null, // Top-level twitter_username from JSON
    portfolioUrl = null, // Top-level portfolio_url from JSON
    bio =
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy " +
            "text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
    location = null,
    totalLikes = 0,
    totalPhotos = 0,
    totalCollections = 3,
    downloads = 0,
    social = User.Social(
        instagramUsername = null, // from JSON social.instagram_username
        portfolioUrl = null, // from JSON social.portfolio_url
        twitterUsername = null, // from JSON social.twitter_username
    ),
    profileImage = User.ProfileImage(
        small = "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
        medium = "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64",
        large = "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128",
    ),
    badge = null, // JSON provides "badge": null
    links = User.Links(
        self = "https://api.unsplash.com/users/kohalloran",
        html = "https://unsplash.com/@kohalloran",
        photos = "https://api.unsplash.com/users/kohalloran/photos",
        likes = "https://api.unsplash.com/users/kohalloran/likes",
        portfolio = "https://api.unsplash.com/users/kohalloran/portfolio",
    ),
    tags = User.UserTags(
        custom = listOf(
            User.UserTags.UserCustomTag(type = "search", title = "abstract"),
            User.UserTags.UserCustomTag(type = "search", title = "beach"),
            User.UserTags.UserCustomTag(type = "search", title = "forest"),
            User.UserTags.UserCustomTag(type = "search", title = "texture"),
            User.UserTags.UserCustomTag(type = "search", title = "wallpaper"),
        ),
    ),
)

@Suppress("unused")
val PreviewUser8 = User(
    id = "uKYwEOy0h1o",
    updatedAt = "2024-02-16T22:59:01Z",
    username = "kohalloran",
    name = "Kelsey O'Halloran",
    firstName = "Kelsey",
    lastName = "O'Halloran",
    instagramUsername = null, // Top-level instagram_username from JSON
    twitterUsername = null, // Top-level twitter_username from JSON
    portfolioUrl = null, // Top-level portfolio_url from JSON
    bio = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard",
    location = null,
    totalLikes = 0,
    totalPhotos = 0,
    totalCollections = 3,
    downloads = 0,
    social = User.Social(
        instagramUsername = null, // from JSON social.instagram_username
        portfolioUrl = null, // from JSON social.portfolio_url
        twitterUsername = null, // from JSON social.twitter_username
    ),
    profileImage = User.ProfileImage(
        small = "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
        medium = "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64",
        large = "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128",
    ),
    badge = null, // JSON provides "badge": null
    links = User.Links(
        self = "https://api.unsplash.com/users/kohalloran",
        html = "https://unsplash.com/@kohalloran",
        photos = "https://api.unsplash.com/users/kohalloran/photos",
        likes = "https://api.unsplash.com/users/kohalloran/likes",
        portfolio = "https://api.unsplash.com/users/kohalloran/portfolio",
    ),
    tags = User.UserTags(
        custom = listOf(
            User.UserTags.UserCustomTag(type = "search", title = "abstract"),
            User.UserTags.UserCustomTag(type = "search", title = "beach"),
            User.UserTags.UserCustomTag(type = "search", title = "forest"),
            User.UserTags.UserCustomTag(type = "search", title = "texture"),
            User.UserTags.UserCustomTag(type = "search", title = "wallpaper"),
        ),
    ),
)
