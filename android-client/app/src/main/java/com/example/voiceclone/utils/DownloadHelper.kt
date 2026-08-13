package com.example.voiceclone.utils

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream

object DownloadHelper {
    /**
     * Save a WAV file into the user's Downloads folder (MediaStore) so it appears in Files/Downloads.
     * - context: app Context
     * - sourceFile: the File on internal storage to copy from
     * - filename: desired display name, e.g. "voiceclone_output.wav"
     *
     * Works on Android Q+ using MediaStore; on older devices it attempts to write to external storage.
     */
    fun saveWavToDownloads(context: Context, sourceFile: File, filename: String) {
        val resolver = context.contentResolver

        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, filename)
            put(MediaStore.Downloads.MIME_TYPE, "audio/wav")
            // Mark as pending on Android Q+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Downloads.IS_PENDING, 1)
            }
        }

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Files.getContentUri("external")
        }

        val itemUri = resolver.insert(collection, values) ?: return

        resolver.openOutputStream(itemUri).use { os ->
            FileInputStream(sourceFile).use { fis ->
                fis.copyTo(os!!)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.clear()
            values.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(itemUri, values, null, null)
        }
    }
}
