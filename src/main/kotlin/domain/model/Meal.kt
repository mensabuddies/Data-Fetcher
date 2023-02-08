package domain.model

data class Meal(
    val name: String,
    val priceStudent: Int,
    val priceGuest: Int,
    val priceEmployee: Int,
    val allergens: Set<Allergen>,
    val ingredients: Set<Ingredient>
)
