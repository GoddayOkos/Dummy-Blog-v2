package dev.decagon.godday.dummyblogfragments.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.decagon.godday.dummyblogfragments.model.Comment
import dev.decagon.godday.dummyblogfragments.model.Post

/**
 * This is our MainDatabase class. The database consists of two tables/entities
 * which are used for storing posts and comments separately
 */
@Database(entities = [Comment::class, Post::class], version = 1, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {

    abstract val commentDao: CommentDao
    abstract val postDao: PostDao

    companion object {
        /**
         *  Singleton pattern to prevents multiple instances of database opening at the
         *  same time.
         */
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getInstance(context: Context): MainDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        MainDatabase::class.java,
                        "post_comment_record")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}