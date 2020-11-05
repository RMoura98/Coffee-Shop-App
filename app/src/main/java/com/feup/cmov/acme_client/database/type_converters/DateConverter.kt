package com.feup.cmov.acme_client.database.type_converters

import androidx.room.TypeConverter
import com.feup.cmov.acme_client.utils.ShowFeedback
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(dateLong: Long): Date{
        return Date(dateLong);
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time;
    }
}