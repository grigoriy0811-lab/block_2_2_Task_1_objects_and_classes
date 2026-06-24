import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WallServiceTest {
    @BeforeEach
    fun setUp() {
        WallService.clear()
    }

    @Test
    fun add() {
        val post = Post(
            id = 3,
            text = "Голуби мутанты утащили кота",
            date = "25.05.26",
            fromId = "Иван",
            views = 1000,
            geo = "Москва",
            signerId = "Новости Москва",
            likes = Likes(0, false),
            comments = Comments(0, false),
            reposts = Reposts(0, false),
        )

        val result = WallService.add(post)
        assertNotEquals(0, result.id)
    }

    @Test
    fun update() {
        val post = WallService.add(
            Post(
                id = 3,
                text = "Голуби мутанты утащили кота",
                date = "25.05.26",
                fromId = "Иван",
                views = 1000,
                geo = "Москва",
                signerId = "Новости Москва",
                likes = Likes(0, false),
                comments = Comments(0, false),
                reposts = Reposts(0, false),
            )
        )
        val update = post.copy(text = "new")
        assertTrue(WallService.update(update))
    }

    @Test
    fun `update two`() {
        val fake = Post(
            id = 999,
            text = "Голуби мутанты утащили кота",
            date = "25.05.26",
            fromId = "Иван",
            views = 1000,
            geo = "Москва",
            signerId = "Новости Москва",
            likes = Likes(0, false),
            comments = Comments(0, false),
            reposts = Reposts(0, false),
        )
        assertFalse(WallService.update(fake))
    }

    @Test
    fun createComment() {
        val post = Post(
            id = 0,
            text = "Класс",
            date = "25.05.26",
            fromId = "Иван",
            views = 100,
            geo = "Москва",
            signerId = "Новости Москва",
            likes = Likes(0, false),
            comments = Comments(0, false),
            reposts = Reposts(0, false),
        )
        val addedPost = WallService.add(post)

        val comment = Comment(
            id = 0,
            fromId = "Петя",
            text = "Одобряю",
            date = "25.05.26",
            postId = addedPost.id,
        )

        WallService.createComment(addedPost.id, comment)
    }

    @Test
    fun shouldThrow() {
        val comment = Comment(
            id = 0,
            fromId = "Петя",
            text = "Такой пост не существует",
            date = "25.05.26",
            postId = 999
        )

        assertThrows(PostNotFoundException::class.java) {
            WallService.createComment(999, comment)
        }
    }
}

