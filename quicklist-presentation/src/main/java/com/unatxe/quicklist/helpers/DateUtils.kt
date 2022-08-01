package com.unatxe.quicklist.helpers

import android.content.Context
import com.unatxe.quicklist.R
import java.util.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

object DateUtils {

    fun formatCardListDate(dateTime: DateTime, context: Context): String {
        if (dateTime.isToday()) {
            return context.getString(R.string.todayAt, dateTime.toString("HH:mm"))
        }
        val formatter: DateTimeFormatter = DateTimeFormat.forPattern("d MMMM, yyyy")
        formatter.withLocale(Locale.getDefault())
        return dateTime.toString(formatter)
    }

    fun DateTime.isToday(): Boolean {
        return this.toLocalDate().isEqual(DateTime.now().toLocalDate())
    }
}
