package data.source

import data.intefaces.WebsiteDataSource
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JsoupDataSource: WebsiteDataSource {
    override suspend fun fetchSite(url: String): Result<Document> {
        return try {
            Result.success(Jsoup.connect(url).get())
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}