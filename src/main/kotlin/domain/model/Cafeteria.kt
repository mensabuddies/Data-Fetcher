package domain.model

import domain.model.enums.FoodProviderType
import domain.model.enums.Location
import domain.model.interfaces.FoodProvider
import java.time.LocalDate

data class Cafeteria(
    override val name: String,
    override val type: FoodProviderType,
    override val location: Location,
    override val titleInfo: String,
    override val bodyInfo: String,
    override val linkToFoodPlan: String,
    override val linkToMoreInformation: String,
    override val description: String,
    override val address: String,
    override val openingHours: List<OpeningInfo>,
    override val menus: List<Menu>
): FoodProvider {

    override fun getMenuOfDay(date: LocalDate): Result<Menu> {
        return Result.failure(Exception("Cafeterias don't have meals!"))
    }
}
