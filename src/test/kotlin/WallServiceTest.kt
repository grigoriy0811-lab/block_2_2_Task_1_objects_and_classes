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
            signerId = "Новости Москва"
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
                signerId = "Новости Москва"
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
            signerId = "Новости Москва"
        )
        assertFalse(WallService.update(fake))
    }
}

