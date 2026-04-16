package com.kazeyomi.domain.model

data class ReaderSettings(
    val mode: ReadingMode = ReadingMode.VERTICAL,
    val direction: ReadingDirection = ReadingDirection.LEFT_TO_RIGHT,
    val showPageNumber: Boolean = true,
    val tapZones: TapZones = TapZones.STANDARD,
    val volumeKeys: Boolean = true,
    val keepScreenOn: Boolean = true,
    val autoMarkAsRead: Boolean = true,
    val autoMarkPageThreshold: Int = 90,
    val preloadAmount: Int = 5,
    val backgroundColor: Long = 0xFF000000
)

enum class ReadingMode {
    VERTICAL,
    HORIZONTAL_SINGLE,
    HORIZONTAL_CONTINUOUS,
    WEBTOON
}

enum class ReadingDirection {
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT
}

enum class TapZones {
    STANDARD,
    REVERSED,
    LEFT_SIDE_ONLY,
    RIGHT_SIDE_ONLY
}
