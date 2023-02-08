package domain.model.interfaces

import domain.model.Menu
import domain.model.OpeningInfo
import domain.model.enums.FoodProviderType
import domain.model.enums.Location
import java.time.LocalDate

interface FoodProvider {
    val name: String
    val type: FoodProviderType
    val location: Location

    // TODO: Make own info class?
    val titleInfo: String
    val bodyInfo: String
    val linkToFoodPlan: String
    val linkToMoreInformation: String
    val description: String
    val address: String
    // ----

    val openingHours: List<OpeningInfo>
    val menus: List<Menu>

    fun getMenuOfDay(date: LocalDate): Result<Menu>


}