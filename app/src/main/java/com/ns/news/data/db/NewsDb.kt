package com.ns.news.data.db

import android.content.Context
import androidx.room.*
import com.ns.news.domain.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Database(
    entities = [Section::class, Cell::class, SectionPageRemote::class, TableRead::class, Bookmark::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class NewsDb : RoomDatabase() {
    companion object {
        val moshi: Moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        private val roomConverter = DataConverter(moshi)

        fun create(context: Context): NewsDb {
            val databaseBuilder =
                Room.databaseBuilder(context, NewsDb::class.java, "news.db").addTypeConverter(roomConverter)

            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun cellItems(): CellDao
    abstract fun remotePage(): SectionPageRemoteDao
    abstract fun sectionDao(): SectionDao
    abstract fun readDao(): ReadDao
    abstract fun bookmarkDao(): BookmarkDao
}


