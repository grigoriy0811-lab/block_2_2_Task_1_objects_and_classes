// Ссылочный тип хранение данных
data class Post(
    val id: Int,
    val text: String,
    val date: String?,
    val fromId: String,
    val views: Int,
    val geo: String?,
    val signerId: String,
    val likes: Likes,
    val comments: Comments,
    val reposts: Reposts,
    val attachment: Array<Attachment>? = null
)

//Класс с функцией проверки количество лайков
class Likes(
    count: Int, //Начальное количество лайков
    val userLikes: Boolean, // Поле: true — пользователь поставил лайк, false — не поставил
) {
    var count = count //Поле для хранения лайков
        set(value) { //Вызывается функция, если меняется поле count
            if (value >= 0) { //Если новое значение не отрицательное
                field = value //Тогда обновляем поле
            }//Если отрицательное - то ничего не делаем
        }
}

//Класс с функцией проверки количество комментариев
class Comments(
    numberOfComments: Int,//Параметр конструктора (временный)
    val canPost: Boolean, //Поле, может ли пользователь комментировать
) {
    var numberOfComments = numberOfComments //Поле для хранения количества комментариев
        set(value) { // Функция, которая срабатывает при изменении
            if (value >= 0) { // Если новое значение не отрицательное
                field = value // То обновляем поле
            }
        }
}

class Reposts(
    numberOfReposts: Int, //Параметр конструктора (временный)
    val userReposted: Boolean, //Поле, репостнул ли пользователь
) {
    var numberOfReposts = numberOfReposts // Поле, для хранения количество репостов
        set(value) { // Функция, которая срабатывает при изменении
            if (value >= 0) { // Если новое значение не отрицательное
                field = value // То обновляем поле
            }
        }
}

interface Attachment { // Общий интерфейс для вложений
    val type: String
}

data class Audio(val id: Int, val title: String, val date: String) // Класс, хранит данные аудио
data class AudioAttachment(val audio: Audio) : Attachment { // связывает данные Audio с типом audio
    override val type = "audio" // Переназначаем тип вложения
}

data class Video(val id: Int, val title: String, val date: String, val views: Int)
data class VideoAttachment(val video: Video) : Attachment {
    override val type = "video"
}

data class Photo(val id: Int, val title: String, val date: String, val comments: Comments)
data class PhotoAttachment(val photo: Photo) : Attachment {
    override val type = "photo"
}

data class Link(val id: Int, val title: String, val date: String, val text: String)
data class LinkAttachment(val link: Link) : Attachment {
    override val type = "link"
}

data class Doc(val id: Int, val title: String, val date: String, val text: String)
data class DocAttachment(val doc: Doc) : Attachment {
    override val type = "doc"
}

object WallService { // Объект одиночка (синглтон) - срабатывает в единственном экземпляре

    private var posts = emptyArray<Post>() //Поле. массив постов пустой в начале. Приватный
    private var nextId = 1 // Поле. следующий номер id для нового поста

    fun add(post: Post): Post { // Метод добавление поста
        val toDo = post.copy(id = nextId) // Создаем копию поста с новым id
        posts += toDo // Добавляем новый пост в массив
        nextId++ // Увеличиваем счетчик id на 1
        return toDo // Возвращаем созданный пост с id
    }

    fun update(updatedPost: Post): Boolean { // Метод принимает обновленный пост и возращает тру или фалс
        for ((index, post) in posts.withIndex()) { // Цикл по массиву - ищет нужный id
            if (post.id == updatedPost.id) { // Если id текущего поста совпадает с id нового поста
                posts[index] = updatedPost //Заменяет пост в массиве на новом месте по найденному индексу
                return true // Успешно обновился и выходит из метода
            }
        }
        return false // Если цикл закончился и ничего не нашел
    }

    fun clear() { // Метод очистки, ничего не возвращает
        posts = emptyArray() // Заменяем массив постов на пустой массив
        nextId = 1 // Сбрасывает счетчик id на начальное значение 1
    }
}

fun main() { // Запуск
    val post = Post(
        // Создаем пост
        3,
        "Голуби мутанты утащили кота",
        null,
        "Иван",
        1000,
        null,
        "Новости Москва",
        likes = Likes(count = 0, userLikes = false), // Объект с информацией о лайках
        comments = Comments(numberOfComments = 5, canPost = true),// Объект с комментариями
        reposts = Reposts(numberOfReposts = 10, userReposted = false), // Объект с репостами
    )
    val attachment: Attachment = VideoAttachment(Video(1,"Видео", "25.06.26", 1000))
    println(when (attachment) {
        is VideoAttachment -> attachment.video.toString()
        is DocAttachment -> attachment.doc.toString()
        is LinkAttachment -> attachment.link.toString()
        is AudioAttachment -> attachment.audio.toString()
        is PhotoAttachment -> attachment.photo.toString()
        else -> println("unknown")
    })
}


