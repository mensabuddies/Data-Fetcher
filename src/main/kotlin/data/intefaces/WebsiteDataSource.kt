package data.intefaces

import org.jsoup.nodes.Document

interface WebsiteDataSource {
    suspend fun fetchSite(url: String): Result<Document>
}