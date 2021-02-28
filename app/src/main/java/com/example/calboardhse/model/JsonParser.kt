package com.example.callboardhse.model

import android.util.JsonReader
import java.io.InputStream

interface JsonParser<T> {
    fun parse(json: InputStream): T
    fun parse(reader: JsonReader): T
}