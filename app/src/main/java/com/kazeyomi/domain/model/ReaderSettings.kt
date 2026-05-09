package com.kazeyomi.domain.model

data class ReaderSettings(
    val mode: ReadingMode = ReadingMode.VERTICAL,
    val direction: ReadingDirection = ReadingDirection.LEFT_TO_RIGHT,
    val showPageNumber: Boolean = true,
    val tapZones: TapZones = TapZones.STANDARD,
    val volumeKeys: Boolean = false,
    val keepScreenOn: Boolean = true,
    val autoMarkAsRead: Boolean = true,
    val preloadAmount: Int = 10
)

enum class ReadingMode {
    VERTICAL,
    HORIZONTAL,
    WEBTOON;

    companion object {
        fun fromString(value: String): ReadingMode = when (value.uppercase()) {
            "HORIZONTAL" -> HORIZONTAL
            "WEBTOON" -> WEBTOON
            else -> VERTICAL
        }
    }
}

enum class ReadingDirection {
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT;

    companion object {
        fun fromString(value: String): ReadingDirection = when (value.uppercase()) {
            "RIGHT_TO_LEFT" -> RIGHT_TO_LEFT
            else -> LEFT_TO_RIGHT
        }
    }
}

enum class TapZones {
    STANDARD,
    REVERSED;

    companion object {
        fun fromString(value: String): TapZones = when (value.uppercase()) {
            "REVERSED" -> REVERSED
            else -> STANDARD
        }
    }
}
