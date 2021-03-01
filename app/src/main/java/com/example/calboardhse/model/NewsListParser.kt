package com.example.callboardhse.model

import android.util.JsonReader
import java.io.InputStream

class NewsListParser : JsonParser<NewsListModel> {

    override fun parse(json: InputStream): NewsListModel {
        return parse(JsonReader(json.reader()))
    }

    override fun parse(reader: JsonReader): NewsListModel {
        val builder = NewsListModel.Builder()
        reader.beginObject()
        while (reader.hasNext()) {
            builder.news = parseResults(reader)


        }
        reader.endObject()
        return builder.build()
    }

    private fun parseResults(reader: JsonReader): List<NewsModel> {
        val result = mutableListOf<NewsModel>()
        reader.beginArray()
        while (reader.hasNext()) {
            result.add(parseNews(reader))
        }
        reader.endArray()
        return result
    }

    private fun parseNews(reader: JsonReader): NewsModel {
        val builder = NewsModel.Builder()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                TITLE -> builder.title = reader.nextString()
                PLACE -> builder.place = reader.nextString()
                DATES -> builder.dates = reader.nextString()
                CURRENTDATE -> builder.currentdate = reader.nextString()
                CONTACTS -> builder.contacts = reader.nextString()
                CONTENT -> builder.content = reader.nextString()


                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return builder.build()
    }

    private companion object {

        const val TITLE = "title"
        const val PLACE = "place"
        const val DATES = "dates"
        const val CURRENTDATE = "currentdate"
        const val CONTACTS = "contacts"
        const val CONTENT = "content"


    }
}
