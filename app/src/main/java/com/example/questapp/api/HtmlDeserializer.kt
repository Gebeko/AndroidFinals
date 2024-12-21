package com.example.questapp.api

import android.text.Html
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class HtmlDeserializer : JsonDeserializer<String> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: com.google.gson.JsonDeserializationContext?): String {
        return Html.fromHtml(json?.asString ?: "", Html.FROM_HTML_MODE_LEGACY).toString()
    }
}
