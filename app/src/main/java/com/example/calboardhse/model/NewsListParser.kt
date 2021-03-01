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
                SECTION -> builder.section = reader.nextString()
                SUBSECTION -> builder.subsection = reader.nextString()
                TITLE -> builder.title = reader.nextString()
                ABSTRACT -> builder.abstract = reader.nextString()
                URL -> builder.newsUrl = reader.nextString()
                BYLINE -> builder.byline = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return builder.build()
    }

    private companion object {
        const val STATUS = "status"
        const val NUM_RESULTS = "num_results"
        const val RESULTS = "results"

        const val SECTION = "section"
        const val SUBSECTION = "subsection"
        const val TITLE = "title"
        const val ABSTRACT = "abstract"
        const val URL = "url"
        const val BYLINE = "byline"
    }
}
