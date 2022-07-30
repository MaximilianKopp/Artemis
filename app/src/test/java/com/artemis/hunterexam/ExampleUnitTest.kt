package com.artemis.hunterexam

import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val testDateTime = "09.05.22, 00:42"

    @Test
    fun testLocalDateTimeParsing() {
        val dtf: DateTimeFormatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(
                    Locale("DE")
                )
        println("DateTimeFormatter $dtf")
        val testLdt = LocalDateTime.parse(testDateTime, dtf)
        println(testLdt)
    }
}