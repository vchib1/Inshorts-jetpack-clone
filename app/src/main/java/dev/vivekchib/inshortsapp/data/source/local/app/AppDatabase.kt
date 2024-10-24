package dev.vivekchib.inshortsapp.data.source.local.app

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.vivekchib.inshortsapp.data.model.ArticleModel
import dev.vivekchib.inshortsapp.data.source.local.converter.Converter

@Database(entities = [ArticleModel::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val bookmarkDao: BookmarkDao
}