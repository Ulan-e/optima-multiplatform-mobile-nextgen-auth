package kg.optima.mobile.android.utils

import android.content.Context
import android.util.Log
import java.io.*

private const val tag = "FileUtils"

fun readTextFile(inputStream: InputStream): String {
    val outputStream = ByteArrayOutputStream()
    val buf = ByteArray(1024)
    var len: Int
    try {
        while (inputStream.read(buf).also { len = it } != -1) {
            outputStream.write(buf, 0, len)
        }
        outputStream.close()
        inputStream.close()
    } catch (e: IOException) {
        Log.e(tag, "Error while reading text file ${e.localizedMessage}")
    }
    return outputStream.toString()
}

fun Context.saveFile(filename: String, data: Map<String, String>) {
    try {
        val filePath = File(this.filesDir, "/")
        val newFile = File(filePath, filename)
        val fileOutputStream = FileOutputStream(newFile)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)

        objectOutputStream.writeObject(data)
        objectOutputStream.close()
        fileOutputStream.close()
    } catch (exception: Exception) {
        Log.e(tag, "Error while saving file ${exception.localizedMessage}")
    }
}

fun Context.loadFile(filename: String): Map<String, String> {
    return try {
        val filePath = File(this.filesDir, "/")
        val newFile = File(filePath, filename)
        val fileInputStream = FileInputStream(newFile)
        val objectOutputStream = ObjectInputStream(fileInputStream)

        val fileData = HashMap<String, String>()
        fileData.putAll(objectOutputStream.readObject() as Map<out String, String>)
        objectOutputStream.close()
        fileInputStream.close()
        fileData
    } catch (exception: Exception) {
        Log.d(tag, "Error while reading file ${exception.localizedMessage}")
        mutableMapOf()
    }
}

fun Context.removeFile(filename: String): Boolean {
    val filePath = File(this.filesDir, "/")
    val newFile = File(filePath, filename)
    return newFile.delete()
}