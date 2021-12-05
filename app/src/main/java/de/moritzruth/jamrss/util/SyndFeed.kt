package de.moritzruth.jamrss.util

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.FeedException
import com.rometools.rome.io.SyndFeedInput
import de.moritzruth.jamrss.graph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request

sealed class FetchAndParseResult {
    class Success(val feed: SyndFeed): FetchAndParseResult()
    class RequestFailed(val exception: Exception): FetchAndParseResult()
    object InvalidUrl: FetchAndParseResult()
    object BadResponse: FetchAndParseResult()
    object InvalidFeed: FetchAndParseResult()
}

suspend fun fetchAndParseFeed(url: String): FetchAndParseResult = withContext(Dispatchers.IO) {
    val request = try {
        Request.Builder()
            .get()
            .url(url)
            .build()
    } catch (exception: IllegalArgumentException) {
        return@withContext FetchAndParseResult.InvalidUrl
    }

    val response = try {
        graph.okHttpClient.newCall(request).await()
    } catch (exception: Exception) {
        println(exception::class.qualifiedName)
        return@withContext FetchAndParseResult.RequestFailed(exception)
    }

    response.body!!.use { body ->
        if (response.isSuccessful) {
            val feed = try {
                SyndFeedInput().build(body.charStream())!!
            } catch (exception: IllegalArgumentException) {
                return@withContext FetchAndParseResult.InvalidFeed
            } catch (exception: FeedException) {
                return@withContext FetchAndParseResult.InvalidFeed
            }

            FetchAndParseResult.Success(feed)
        }
        else FetchAndParseResult.BadResponse
    }
}