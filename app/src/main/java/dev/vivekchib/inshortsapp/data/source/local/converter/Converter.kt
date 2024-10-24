package dev.vivekchib.inshortsapp.data.source.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import dev.vivekchib.inshortsapp.data.model.SourceModel
import java.time.LocalDateTime
import java.time.ZoneOffset


class Converter {

    @TypeConverter
    fun fromSource(source: SourceModel): String? {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(source: String): SourceModel {
        return Gson().fromJson(source, SourceModel::class.java)
    }
}
