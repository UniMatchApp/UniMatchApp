package com.ulpgc.uniMatch.data.infrastructure.mocks

import com.ulpgc.uniMatch.data.domain.models.ChatPreviewData

object ChatPreviewDataMock {
    fun createChatPreviewDataMocks(count: Int = 10): List<ChatPreviewData> {
        val names = listOf(
            "Mike",
            "Sam Sulek",
            "Togi",
            "Cbum",
            "Falete",
            "Borja Escalona",
            "Gorlok the Destroyer",
            "Masmas",
            "Ramon Dino",
            "Wesley Vissers"
        )

        val messages = listOf("Hello, how are you?", "I'm fine, thank you", "Hi", "Hello")

        val profileImages = listOf(
            "https://instagram.fmad7-1.fna.fbcdn.net/v/t51.29350-15/457137147_3291206554344001_6895839650662015791_n.jpg?stp=dst-jpg_e35&efg=eyJ2ZW5jb2RlX3RhZyI6ImltYWdlX3VybGdlbi4xNDQweDE1NzMuc2RyLmYyOTM1MC5kZWZhdWx0X2ltYWdlIn0&_nc_ht=instagram.fmad7-1.fna.fbcdn.net&_nc_cat=101&_nc_ohc=nSXhud78dG4Q7kNvgF5UCM8&_nc_gid=674dc25a055c4c4abbfa440802088281&edm=AP4sbd4BAAAA&ccb=7-5&ig_cache_key=MzQ0NDY2NDAyMjM4MjA3MDY2Nw%3D%3D.3-ccb7-5&oh=00_AYAcUbynH8z9Mr3Uej4pDgP3P9L7FO77YyqwYmLED0eVRQ&oe=67185D38&_nc_sid=7a9f4b",
            "https://instagram.fmad7-1.fna.fbcdn.net/v/t51.29350-15/442444586_1478909159498266_6992815285227869420_n.jpg?stp=dst-jpg_e35&efg=eyJ2ZW5jb2RlX3RhZyI6ImltYWdlX3VybGdlbi4xNDQweDExNTAuc2RyLmYyOTM1MC5kZWZhdWx0X2ltYWdlIn0&_nc_ht=instagram.fmad7-1.fna.fbcdn.net&_nc_cat=110&_nc_ohc=P3TG6rixnb4Q7kNvgFGHEDw&_nc_gid=c971a15cbf6b414ab894dc1056dab634&edm=ALQROFkBAAAA&ccb=7-5&ig_cache_key=MzM2NDIyMjQwNzkxMTk5NzYwMQ%3D%3D.3-ccb7-5&oh=00_AYBQLOjmNDdP6b3geNKTfdQsUhrAd3mXoIIKx20nCKOAhA&oe=67185CB2&_nc_sid=fc8dfb",
            "https://i1.sndcdn.com/artworks-fWuZ5PmvUkpoQCoE-EVXfZw-t500x500.jpg",
            "https://instagram.fmad7-1.fna.fbcdn.net/v/t39.30808-6/459921900_530906936125793_9130248871768812162_n.jpg?stp=c0.0.768.960a_dst-jpg_e35&efg=eyJ2ZW5jb2RlX3RhZyI6ImltYWdlX3VybGdlbi43Njl4OTYwLnNkci5mMzA4MDguZGVmYXVsdF9pbWFnZSJ9&_nc_ht=instagram.fmad7-1.fna.fbcdn.net&_nc_cat=105&_nc_ohc=hnWWfeAPOA4Q7kNvgGW23ka&_nc_gid=417e804cccfa4b2e86fa124b8f2d5269&edm=ALQROFkAAAAA&ccb=7-5&ig_cache_key=MzQ2MzAwNTI2ODgzMzkwOTI2OA%3D%3D.3-ccb7-5&oh=00_AYBi_dnvkacy1ZDTVaKr0yEbCotQ1oVC0XbeQYdwF_Pv-w&oe=67187F79&_nc_sid=fc8dfb",
            "https://album.mediaset.es/eimg/2023/03/04/falete_d2d4.jpg?w=1200&h=900",
            "https://image.europafm.com/clipping/cmsimages01/2022/08/15/4BFF7A00-9A76-4D79-8271-B056A41AA0BA/borja-escalona-video-grabado-vigo_104.jpg?crop=183,183,x45,y0&width=1200&height=1200&optimize=low&format=webply",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3VgCckkON-EgnIgCrJI27VFXv21sCSThDYw&s",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRKQyxv1Spoim74tijSrO7S_e1wLzEr7NTlbQ&s",
            "https://instagram.fmad7-1.fna.fbcdn.net/v/t51.29350-15/460697703_1881515442340303_3937794735801524553_n.jpg?stp=dst-jpg_e35&efg=eyJ2ZW5jb2RlX3RhZyI6ImltYWdlX3VybGdlbi4xNDQweDE0NDAuc2RyLmYyOTM1MC5kZWZhdWx0X2ltYWdlIn0&_nc_ht=instagram.fmad7-1.fna.fbcdn.net&_nc_cat=107&_nc_ohc=sjKibK6CXRQQ7kNvgHFKZ-g&_nc_gid=50defd06a40243e2b65942dbd37c2d75&edm=ALQROFkBAAAA&ccb=7-5&ig_cache_key=MzQ2MTMzODQzNTg3NDk1OTg2NA%3D%3D.3-ccb7-5&oh=00_AYAHHkgBx-bpYLabaF3d7NBbXoZvQRLuEDfEDF2UITHyOw&oe=67188BEA&_nc_sid=fc8dfb",
            "https://instagram.fmad7-1.fna.fbcdn.net/v/t51.29350-15/460596195_1572801773671703_3204819900031198295_n.jpg?stp=dst-jpg_e35&efg=eyJ2ZW5jb2RlX3RhZyI6ImltYWdlX3VybGdlbi4xNDQweDE0NDAuc2RyLmYyOTM1MC5kZWZhdWx0X2ltYWdlIn0&_nc_ht=instagram.fmad7-1.fna.fbcdn.net&_nc_cat=111&_nc_ohc=qGu8tb4WzHYQ7kNvgEF7wP0&_nc_gid=50defd06a40243e2b65942dbd37c2d75&edm=ALQROFkBAAAA&ccb=7-5&ig_cache_key=MzQ2MTMzODQzNTg5OTkyMjA5Mw%3D%3D.3-ccb7-5&oh=00_AYB9B0SIl8TTvSvfAyIRWNpOvz5AZ4ydimgyeBK31dCP4g&oe=67185F28&_nc_sid=fc8dfb"
        )

        val availableIndices = (names.indices).shuffled().take(count)
        return availableIndices.map { index ->
            val name = names[index]
            val message = messages.random()

            val currentTime = System.currentTimeMillis()
            val randomOffset = (1..86400).random()
            val time = currentTime - (randomOffset * 1000)

            val unreadMessagesCount = (0..5).random()
            val profileImage = profileImages[index]

            ChatPreviewData(
                id = index.toString(),
                userName = name,
                lastMessage = message,
                lastMessageTime = time,
                unreadMessagesCount = unreadMessagesCount,
                profileImageUrl = profileImage
            )
        }
    }

    fun searchChatPreviewDataMocks(query: String): List<ChatPreviewData> {
        return createChatPreviewDataMocks().filter {
            it.userName.contains(query, ignoreCase = true)
        }
    }
}