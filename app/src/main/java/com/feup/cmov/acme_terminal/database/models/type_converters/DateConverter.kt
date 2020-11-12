package com.feup.cmov.acme_terminal.database.models.type_converters

import java.util.*

class DateConverter {
    fun toDate(dateLong: Long): Date{
        return Date(dateLong);
    }

    fun fromDate(date: Date): Long {
        return date.time;
    }
}