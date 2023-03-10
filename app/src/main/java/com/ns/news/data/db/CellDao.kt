package com.ns.news.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CellDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cells: List<Cell>)

    @Query("SELECT * FROM Cell WHERE sectionId = :sectionId")
    fun articleBySectionId(sectionId: String): PagingSource<Int, Cell>

    @Query("SELECT * FROM Cell WHERE sectionId = :sectionId")
    fun getAllArticlesBySectionId(sectionId: String): Flow<List<Cell>>

    @Query("SELECT * FROM Cell WHERE sectionId = :sectionId AND cellType = :articleCellType")
    fun getArticles(sectionId: String, articleCellType: String): Flow<List<Cell>>

    @Query("SELECT * FROM Cell WHERE sectionId = :sectionId AND type = :type")
    fun getWidgetArticles(sectionId: String, type: String): Flow<List<Cell>>

    @Query("DELETE FROM Cell WHERE sectionId = :sectionId")
    suspend fun deleteBySectionId(sectionId: String)

//    @Query("SELECT MAX(indexInResponse) + 1 FROM CellsItem WHERE sectionId = :sectionId")
//    suspend fun getNextIndexInSubreddit(sectionId: String): Int
}
