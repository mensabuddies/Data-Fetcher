package domain.model

import domain.model.interfaces.Additive

data class Allergen(
    override val name: String
) : Additive
