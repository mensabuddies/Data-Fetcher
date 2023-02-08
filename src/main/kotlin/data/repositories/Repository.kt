package data.repositories

import data.intefaces.ApplicationRepository
import data.intefaces.WebsiteDataSource

class Repository(
    override val websiteDataSource: WebsiteDataSource
) : ApplicationRepository {

}