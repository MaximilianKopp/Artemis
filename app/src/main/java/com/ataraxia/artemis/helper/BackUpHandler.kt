package com.ataraxia.artemis.helper

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File

class BackUpHandler {

    companion object {

        fun saveFile(context: Context): File {
            val file = File(
                context.getExternalFilesDir(
                    Environment.DIRECTORY_DOCUMENTS
                ), ""
            )
            if (!file.mkdirs()) {
                Log.e("Test", "Directory not created")
            }
            return file
        }
    }
}