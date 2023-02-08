package domain.model

import java.time.DayOfWeek

data class OpeningInfo(
    val dayOfWeek: DayOfWeek,
    val openingAt: String,
    val getAMealTill: String,
    val closingAt: String
)
