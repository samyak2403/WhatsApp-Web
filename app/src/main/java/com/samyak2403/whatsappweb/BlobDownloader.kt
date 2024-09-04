/*
 * Created by Samyak kamble on 9/4/24, 11:23 PM
 *  Copyright (c) 2024 . All rights reserved.
 *  Last modified 9/4/24, 11:23 PM
 */

package com.samyak2403.whatsappweb


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class BlobDownloader(private val context: Context) {

    companion object {
        const val JsInstance = "Downloader"
        private var lastDownloadBlob = ""
        private var lastDownloadTime: Long = 0
        private const val sameFileDownloadTimeout = 100L

        @JvmStatic
        fun getBase64StringFromBlobUrl(blobUrl: String): String {
            return if (blobUrl.startsWith("blob")) {
                """
                javascript: var xhr = new XMLHttpRequest();
                xhr.open('GET', '$blobUrl', true);
                xhr.responseType = 'blob';
                xhr.onload = function(e) {
                    if (this.status == 200) {
                        var blobFile = this.response;
                        var reader = new FileReader();
                        reader.readAsDataURL(blobFile);
                        reader.onloadend = function() {
                            base64data = reader.result;
                            $JsInstance.getBase64FromBlobData(base64data);
                        }
                    }
                };
                xhr.send();
                """
            } else {
                "javascript: console.log('It is not a Blob URL');"
            }
        }
    }

    @JavascriptInterface
    @Throws(IOException::class)
    fun getBase64FromBlobData(base64Data: String) {
        Log.d(MainActivity.DEBUG_TAG, "Download triggered ${System.currentTimeMillis()}")
        lastDownloadTime = System.currentTimeMillis()

        if (System.currentTimeMillis() - lastDownloadTime < sameFileDownloadTimeout) {
            Log.d(MainActivity.DEBUG_TAG, "Download within sameFileDownloadTimeout")
            if (lastDownloadBlob == base64Data) {
                Log.d(MainActivity.DEBUG_TAG, "Blobs match, ignoring download")
            } else {
                Log.d(MainActivity.DEBUG_TAG, "Blobs do not match, downloading")
                lastDownloadBlob = base64Data
                convertBase64StringToFileAndStoreIt(base64Data)
            }
        }
    }

    @SuppressLint("SimpleDateFormat") // SDF is just fine for filename
    @Throws(IOException::class)
    private fun convertBase64StringToFileAndStoreIt(base64File: String) {
        val notificationId = System.currentTimeMillis().toInt()
        val strings = base64File.split(",").toTypedArray()
        var extension = MimeTypes.lookupExt(strings[0]) ?: "." + strings[0].substringAfter('/').substringBefore(';')

        val currentDateTime = SimpleDateFormat("yyyyMMdd-hhmmss").format(Date())
        val dlFileName = "WAWTG_$currentDateTime$extension"
        val dlFilePath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), dlFileName)
        val fileAsBytes = Base64.decode(base64File.replaceFirst(strings[0], ""), Base64.DEFAULT)
        FileOutputStream(dlFilePath, false).use { os ->
            os.write(fileAsBytes)
            os.flush()
        }

        if (dlFilePath.exists()) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                val apkURI = FileProvider.getUriForFile(context, "${context.packageName}.provider", dlFilePath)
                setDataAndType(apkURI, MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val pendingIntent = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            val notificationChannelId = "Downloads"

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(notificationChannelId, "name", NotificationManager.IMPORTANCE_LOW)
                notificationManager.createNotificationChannel(notificationChannel)
                Notification.Builder(context, notificationChannelId)
                    .setContentText(String.format(context.getString(R.string.notification_text_saved_as), dlFileName))
                    .setContentTitle(context.getString(R.string.notification_title_tap_to_open))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(android.R.drawable.stat_notify_chat)
                    .build()
            } else {
                NotificationCompat.Builder(context, notificationChannelId)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(String.format(context.getString(R.string.notification_text_saved_as), dlFileName))
                    .setContentText(context.getString(R.string.notification_title_tap_to_open))
                    .build()
            }
            notificationManager.notify(notificationId, notification)
            Toast.makeText(context, R.string.toast_saved_to_downloads_folder, Toast.LENGTH_SHORT).show()
        }
    }
}
