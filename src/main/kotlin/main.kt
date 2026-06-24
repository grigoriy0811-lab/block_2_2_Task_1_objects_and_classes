// Ссылочный тип хранение данных
data class Post(
    val id: Int,               // Переменная, идентификатор поста
    val text: String,          // Переменная, текст поста
    val date: String?,         // Переменная, дата публикации
    val fromId: String,        // Переменная, идентификатор автора
    val views: Int,            // Переменная, количество просмотров
    val geo: String?,          // Переменная, геолокация
    val signerId: String,      // Переменная, идентификатор того, кто подписал пост
    val likes: Likes,          // Переменная, лайки
    val comments: Comments,    // Переменная, комментарии
    val reposts: Reposts,      // Переменная, репосты
    val attachment: Array<Attachment>? = null, // Переменная, вложения (может быть пустым)
)

data class Comment(
    val id: Int, // Идентификатор комментария
    val fromId: String, // Идентификатор автора комментария
    val text: String, // Текст комментария
    val date: String?, // Дата создания комментария (может быть null)
    val postId: Int?, // ID поста, к которому относится комментарий (может быть null)
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
data class AudioAttachment(val audio: Audio) : Attachment { // Связывает данные Audio с типом audio
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

class PostNotFoundException(message: String = "Пост с таким id не найден") : Exception(message) // Исключение если пост не найден

object WallService { // Объект одиночка (синглтон) - срабатывает в единственном экземпляре

    private var posts = emptyArray<Post>() // Приватный массив для хранения постов, в начале пустой
    private var comments = emptyArray<Comment>() // Приватный массив для хранения комментариев, в начале пустой
    private var nextId = 1 // Счетчик для генерации следующего ID нового поста, начинается с 1
    private var nextCommentId = 1 // Счетчик для генерации следующего ID нового комментария

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

    fun clear() { // Метод очистки всего WallService
        posts = emptyArray() // Очищаем массив - удаляем все сохраненные посты
        comments = emptyArray() // Очищаем массив - удаляем все комментарии
        nextId = 1 // Сбрасываем счетчик ID постов на начальное значение 1
        nextCommentId = 1 //Сбрасываем счетчик ID комментариев на начальное значение 1
    }

    fun createComment(postId: Int, comment: Comment): Comment { // Метод для создания комментария к посту
        // Проверка на существование поста с указанным ID
        var postExists = false // Переменная флаг изначально false(псст не найден)
        for (post in posts) { // Проходим по всем постам
            if (post.id == postId) { // Проверка на совпадение
                postExists = true // Если нашли меняем флаг на true
                break // Выходим из цикла, так как пост не найдем
            }
        }

        if (!postExists) { // Проверяем, не найден ли пост
            throw PostNotFoundException("Пост с id $postId не найден")
        } // Выбрасываем исключение с нашим сообщением

        // Если пост найден, создаем новый комментарий
        val newComment = comment.copy( // Создаем копию переданного комментария
            id = nextCommentId,// Присваиваем новый уникальный ID
            postId = postId // Устанавливаем ID поста к которому относится комментарий
        )

        comments += newComment // Добавляем новый комментарий в массив комментариев
        nextCommentId++ // Увеличиваем счетчик комментариев на 1
        return newComment // Возвращаем добавленный комментарий с присвоенным ID
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

    val addedPost = WallService.add(post) // Вызываем метод add для добавления поста
    println("Пост добавлен с ID: ${addedPost.id}") // Выводим сообщение с ID добавленного поста

    val comment = Comment( // Создали экземпляр класса Comment
        id = 0, // ID присваивается автоматически методом createComment(в начале указывает ноль)
        fromId = "Петр", // Идентификатор автора комментария
        text = "Классный пост про голубей!", // Текст комментария
        date = "26.06.26", // Дата создание комментария
        postId = addedPost.id // ID поста к которому относится комментарий
    )

    // Комментарий к посту с исключением
    try { // Блок try (пытаемся выполнить код)
        val newComment = WallService.createComment(addedPost.id, comment) // Вызываем метод для создания комментария
        println("Комментарий добавлен с ID: ${newComment.id}") // Выводим сообщение с ID нового комментария
        println("Текст комментария: ${newComment.text}") // Выводит текст добавленного комментария
    } catch (e: PostNotFoundException) { // Блок catch перехватывает исключение если пост не найден
        println("Ошибка: ${e.message}") // Выводим сообщение об ошибке
    }

    // Проверка с несуществующим постом(демонстрация исключения)
    try { // Блок try пытается выполнить код
        WallService.createComment(999, comment) // Пытается создать комментария к посту с id 999(Поста нет)
    } catch (e: PostNotFoundException) { // Блок catch перехватывает исключение
        println("Ошибка: ${e.message}") // Выводит сообщение об ошибке
    }

    val attachment: Attachment = VideoAttachment(
        Video(
            1,
            "Видео",
            "25.06.26",
            1000
        )
    ) // Переменная attachment с типом Attachment, в ней объект VideoAttachment
    println(
        when (attachment) { // Выводим результат с конструкцией when
            is VideoAttachment -> attachment.video.toString()
            is DocAttachment -> attachment.doc.toString()
            is LinkAttachment -> attachment.link.toString()
            is AudioAttachment -> attachment.audio.toString()
            is PhotoAttachment -> attachment.photo.toString()
            else -> println("unknown")
        }
    )
}


