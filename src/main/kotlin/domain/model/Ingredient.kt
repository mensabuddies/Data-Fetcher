package domain.model

import domain.model.interfaces.Additive

data class Ingredient(
    override val name: String
) : Additive
