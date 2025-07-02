package com.xut.utils

import android.util.JsonWriter

import com.xut.Constants.DEVICE_ID_KEY
import com.xut.Constants.PASS_TOKEN_KEY
import com.xut.Constants.USER_ID_KEY

import java.io.IOException
import java.io.StringWriter

object AuthUtils {
    @Throws(IOException::class)
    fun writeData(writer: JsonWriter, manager: AuthManager) {
        writer.beginObject()
        writer.name(USER_ID_KEY).value(manager.userId)
        writer.name(PASS_TOKEN_KEY).value(manager.passToken)
        writer.name(DEVICE_ID_KEY).value(manager.deviceId)
        writer.endObject()
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun asDataString(manager: AuthManager): String {
        val stringWriter = StringWriter()
        JsonWriter(stringWriter).use { writer ->
            writer.setIndent("  ")
            try {
                writeData(writer, manager)
            } catch (e: IOException) {
                writer.close()
                return e.toString()
            }
        }
        return stringWriter.toString()
            .encodeToByteArray()
            .toHexString(HexFormat {
                upperCase = true
            })
    }
}