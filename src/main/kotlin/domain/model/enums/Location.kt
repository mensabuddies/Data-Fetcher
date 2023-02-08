package domain.model.enums

import java.util.*

enum class Location(private val value: String) {
    WUERZBURG("W\u00fcrzburg"),
    BAMBERG("Bamberg"),
    SCHWEINFURT("Schweinfurt"),
    ASCHAFFENBURG("Aschaffenburg"),
    INVALID("Invalid");

    val valueFormatted: String
        get() = value
            .lowercase(Locale.getDefault())
            .replace("\u00e4", "ae")
            .replace("\u00f6", "oe")
            .replace("\u00fc", "ue")
            .replace("\u00df", "ss")
}