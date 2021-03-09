package dev.decagon.godday.dummyblogfragments.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import dev.decagon.godday.dummyblogfragments.model.Comment
import dev.decagon.godday.dummyblogfragments.model.Post
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class MainDatabaseTest {

    private lateinit var commentdao: CommentDao
    private lateinit var postdao: PostDao
    private lateinit var db: MainDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, MainDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        commentdao = db.commentDao
        postdao = db.postDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetComment() {
        val com1 = Comment(1, null, "Godday", "g@g.com", "Hello")
        val com2 = Comment(2, null, "Obehi", "ob@gmail.com", "Testing")
        val comment = listOf(com1, com2)
        commentdao.insertAll(comment)
        val comment2 = commentdao.getLatestComment()
        assertEquals(comment2.name, "Obehi")
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun insertAndGetPost() {
        val post = Post(1, null, "Hello", "This is testing")
        postdao.insert(post)
        val post2 = postdao.getLatestPost()
        assertEquals(post2.title, "Hello")
    }
}