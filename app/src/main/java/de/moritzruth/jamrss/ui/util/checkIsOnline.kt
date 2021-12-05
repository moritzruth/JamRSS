package de.moritzruth.jamrss.ui.util

import de.moritzruth.jamrss.graph
import de.moritzruth.jamrss.util.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.CacheControl
import okhttp3.Request

private val REQUEST = Request.Builder().url("https://detectportal.firefox.com/").cacheControl(CacheControl.FORCE_NETWORK).build()

suspend fun checkIsOnline(): Boolean = withContext(Dispatchers.IO) {
    val response = try {
        graph.okHttpClient.newCall(REQUEST).await()
    } catch (e: Exception) {
        return@withContext false
    }

    if (response.code == 200) {
        @Suppress("BlockingMethodInNonBlockingContext")
        response.body!!.string() == "success\n"
    } else false
}