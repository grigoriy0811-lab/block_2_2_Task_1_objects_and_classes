data class Post(
    val id: Int,
    val text: String,
    val date: String,
    val fromId: String,
    val views: Int,
    val geo: String,
    val signerId: String,
    val likes: Likes,
    val comments: Comments,
    val reposts: Reposts,
)

class Likes(
    count: Int,
    val userLikes: Boolean,
) {
    var count = count
        set(value) {
            if (value >= 0) {
                field = value
            }
        }
}

class Comments(
    numberOfComments: Int,
    val canPost: Boolean,
) {
    var numberOfComments = numberOfComments
        set(value) {
            if (value >= 0) {
                field = value
            }
        }
}

class Reposts(
    numberOfReposts: Int,
    val userReposted: Boolean,
) {
    var numberOfReposts = numberOfReposts
        set(value) {
            if (value >= 0) {
                field = value
            }
        }
}

object WallService {

    private var posts = emptyArray<Post>()
    private var nextId = 1

    fun add(post: Post): Post {
        val toDo = post.copy(id = nextId)
        posts += toDo
        nextId++
        return toDo
    }

    fun update(updatedPost: Post): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (post.id == updatedPost.id) {
                posts[index] = updatedPost
                return true
            }
        }
        return false
    }

    fun clear() {
        posts = emptyArray()
        nextId = 1
    }
}

        fun main() {
            val post = Post(
                3,
                "Голуби мутанты утащили кота",
                "25.05.25",
                "Иван",
                1000,
                "Москва",
                "Новости Москва",
                likes = Likes(count = 0, userLikes = false),
                comments = Comments(numberOfComments = 5, canPost = true),
                reposts = Reposts(numberOfReposts = 10, userReposted = false),
            )
        }


