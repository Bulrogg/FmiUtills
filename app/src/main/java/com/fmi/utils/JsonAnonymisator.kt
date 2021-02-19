package com.fmi.utils

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive

class JsonAnonymisator {

    fun anonymize(body: String): String {
        return try {
            val jsonElementOriginal = JsonParser.parseString(body)
            val jsonElementAnonymized = getJsonElementAnonymized("", jsonElementOriginal)
            jsonElementAnonymized.toString()
        } catch (e: Exception) {
            ERROR_ANONYNIMIZATOR
        }
    }

    private fun getJsonElementAnonymized(parentKey: String, originJsonElement: JsonElement): JsonElement {
        return when (originJsonElement) {
            is JsonPrimitive -> {
                if (keysToAnonymized.contains(parentKey)) JsonPrimitive(VALUE_ANONYMIZED)
                else originJsonElement
            }
            is JsonObject -> {
                val newInnerObject = JsonObject()
                originJsonElement.keySet().forEach { originJsonElementSubKey ->
                    val originJsonElementSubElement = originJsonElement.get(originJsonElementSubKey)
                    val subJsonElementAnonymized = getJsonElementAnonymized(originJsonElementSubKey, originJsonElementSubElement)
                    newInnerObject.add(originJsonElementSubKey, subJsonElementAnonymized)
                }
                newInnerObject
            }
            is JsonArray -> {
                val newInnerArray = JsonArray()
                originJsonElement.map {
                    newInnerArray.add(getJsonElementAnonymized("", it))
                }
                newInnerArray
            }
            else -> originJsonElement
        }
    }

    companion object {
        private val keysToAnonymized = listOf("toAnonymized", "toAnonymized1", "toAnonymized2", "toAnonymized3", "toAnonymized4")
        private const val VALUE_ANONYMIZED = "***"
        private const val ERROR_ANONYNIMIZATOR = "Bad json"
    }

}