package com.mbicycle.core_utils.date

import com.mbicycle.core_utils.extensions.*
import com.mbicycle.core_utils.static_utils.Logger
import java.lang.StringBuilder
import java.text.*
import java.util.*
import java.util.concurrent.TimeUnit

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

class DateUtils {

    companion object {

        val recentYear
            get() = getCalendarField(Calendar.YEAR)
        val recentMonth
            get() = getCalendarField(Calendar.MONTH)
        val recentDay
            get() = getCalendarField(Calendar.DAY_OF_MONTH)
        val currentHourIn24Format
            get() = getCalendarField(Calendar.HOUR_OF_DAY)
        val currentHourIn12Format
            get() = getCalendarField(Calendar.HOUR)
        val recentMinute
            get() = getCalendarField(Calendar.MINUTE)

        /**
         * Function that will return a [Date] fulfilled with an actual
         * available maximum date in the [Calendar]
         *
         * @author Ilya Kislenka
         */
        fun getActualMinimumDate(): Date {

            val calendar = getRecentCalendar()
            setActualMinimumToField(calendar,
                Calendar.YEAR)
            setActualMinimumToField(calendar,
                Calendar.MONTH)
            setActualMinimumToField(calendar,
                Calendar.DAY_OF_MONTH)
            return calendar.time
        }

        /**
         * Function that will return a recent date with 10 year appended
         *
         * @author Ilya Kislenka
         */
        fun getDateAppendingTenYears(): Date = getRecentCalendar().apply {
            set(Calendar.YEAR, get(Calendar.YEAR) + 10)
            set(Calendar.MONTH, this.getActualMaximum(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, this.getActualMaximum(Calendar.DAY_OF_MONTH))
        }.time

        /**
         * Function that will return a [Calendar] with a current date
         * within
         *
         * @author Ilya Kislenka
         */
        fun getRecentCalendar(): Calendar = Calendar.getInstance(Locale.US)

        /**
         * Function that will return a [Date] with @param yearsCount appended
         *
         * @param timeStamp it's a timestamp to years be appended
         * @param yearsCount it's an amount of years to be appended
         *
         * @author Ilya Kislenka
         */
        fun appendYearsToTimeStamp(
            timeStamp: Long,
            yearsCount: Int
        ) = getCalendarWithTimeStamp(timeStamp).apply {
            add(Calendar.YEAR, yearsCount)
        }.time

        /**
         * Function that will return a [Date] with @param monthsCount appended
         *
         * @param timeStamp it's a timestamp to months be appended
         * @param monthsCount it's an amount of months to be appended
         *
         * @author Ilya Kislenka
         */
        fun appendMonthsToTimeStamp(
            timeStamp: Long,
            monthsCount: Int
        ) = getCalendarWithTimeStamp(timeStamp).apply {
            add(Calendar.MONTH, monthsCount)
        }.time

        /**
         * Function that will return a [Date] with @param daysCount appended
         *
         * @param timeStamp it's a timestamp to days be appended
         * @param daysCount it's an amount of days to be appended
         *
         * @author Ilya Kislenka
         */
        fun addDaysToTimeStamp(timeStamp: Long,
                               daysCount: Int) = getRecentCalendar().apply {
            timeInMillis = timeStamp
            add(Calendar.DAY_OF_MONTH, daysCount)
            timeInMillis
        }

        /**
         * Function that will compare two dates by the [Calendar.MONTH] & [Calendar.YEAR]
         * fields and return a boolean result
         *
         * @param firstDate it's a first date to compare
         * @param secondDate it's a second date to compare
         *
         * @author Ilya Kislenka
         */
        fun isMonthAndYearAreEquals(
            firstDate: Date,
            secondDate: Date
        ): Boolean {

            val firstCalendar =
                getCalendarWithTimeStamp(firstDate.time)
            val firstYear = firstCalendar.get(Calendar.YEAR)
            val firstMonth = firstCalendar.get(Calendar.MONTH)

            val secondCalendar =
                getCalendarWithTimeStamp(secondDate.time)
            val secondYear = secondCalendar.get(Calendar.YEAR)
            val secondMonth = secondCalendar.get(Calendar.MONTH)

            return (firstYear == secondYear) && (firstMonth == secondMonth)
        }

        /**
         * Function that will set a hour and minute to a current [Date]
         *
         * @param hour it's a 24-hour format hour to be set inside
         * @param minute it's a minute to be set inside
         *
         * @author Ilya Kislenka
         */
        fun setTimeToCurrentDate(hour: Int, minute: Int): Date = getRecentCalendar().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }.time

        /**
         * Function that will determine whether the passed timestamp
         * belongs to a bounds of a current day
         *
         * @author Ilya Kislenka
         */
        fun isTimeStampInBoundsOfCurrentDay(timeStamp: Long) =
            timeStamp in getStartOfCurrentDayTimeStamp()..getEndCurrentDayTimeStamp()

        /**
         * Function that will return a current day timestamp
         * that was moved to a start of the day
         *
         * @author Ilya Kislenka
         */
        fun getStartOfCurrentDayTimeStamp() =
            moveTimeStampToStartOfTheDay(System.currentTimeMillis())

        /**
         * Function that will return a current day timestamp
         * that was moved to an end of the day
         *
         * @author Ilya Kislenka
         */
        fun getEndCurrentDayTimeStamp() =
            moveTimeStampToEndOfTheDay(System.currentTimeMillis())

        /**
         * Function that will return a [TimeZone.getDefault] raw offset in hours from UTC-0
         *
         * @author Ilya Kislenka
         */
        fun getTimezoneOffsetInHours() = TimeZone.getDefault().rawOffset / 1000 / 60 / 60

        /**
         * Function that will return a raw timezone offset from a UTC-0 to a device timezone
         *
         * @author Ilya Kislenka
         */
        fun getRawTimezoneOffset() = TimeUnit.HOURS.toMillis(getTimezoneOffsetInHours().toLong())

        /**
         * Function that will determine whether passed timestamp belongs to AM time offset
         *
         * @author Ilya Kislenka
         */
        fun isTimeStampIsAnteMeridiem(timeStamp: Long): Boolean {
            val daySuffix = Date(timeStamp).formatByPattern("a", Locale.US)
            return daySuffix.contains("a") || daySuffix.contains("A")
        }

        /**
         * Function that will determine whether passed timestamp belongs to PM time offset
         *
         * @author Ilya Kislenka
         */
        fun isTimeStampIsAPostMeridiem(timeStamp: Long): Boolean {
            val daySuffix = Date(timeStamp).formatByPattern("a", Locale.US)
            return daySuffix.contains("p") || daySuffix.contains("P")
        }

        /**
         * Function that will lowercase time offset from AM to am and PM to pm
         * and will return a String formatted [String]
         *
         * @author Ilya Kislenka
         */
        fun replaceDaySuffixToLowerCased(formattedTime: String): String {

            var result = formattedTime

            if (result.contains("AM"))
                result = result.replace("AM", "am")

            if (result.contains("PM"))
                result = result.replace("PM", "pm")

            return result
        }

        /**
         * Specific function that will transform a timestamp transformed to a [String]
         * which is @param dateTimeUTC0 to a [Date] and then will set it's time to a
         *
         * @param dateTimeStamp it's a regular representation of any [Date]
         * @param dateTimeUTC0 it's a time slot that was transformed to a [String]
         *
         * @author Ilya Kislenka
         */
        fun appendTimeZoneOffsetToTimeStamp(dateTimeStamp: Long,
                                            dateTimeUTC0: String): Long {
            val timeSlotTimeUTC0Long =
                parseTimeStampByPattern(
                    dateTimeUTC0,
                    addingTimeZoneOffset = false
                )
            return getCalendarWithTimeStamp(
                dateTimeStamp).apply {
                val timeCalendar =
                    getCalendarWithTimeStamp(
                        timeSlotTimeUTC0Long)
                set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
                set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
            }.timeInMillis + getRawTimezoneOffset()
        }

        /**
         * Specific function that will transform a timestamp which is @param dateTime
         * to a [Date] and then will set it's date to a
         *
         * @param dateTimeStamp it's a regular representation of any [Date]
         * @param dateTime it's a time in UTC-0
         *
         *
         * @Note We should pass a time for particular day before decreasing
         * TimeZone offset, because we are passing day and time as different
         * fields, and to move the date correctly we need a time related to the
         * date, because all the dates in format "MM-dd-yyyy" would have 12AM as time
         * and if user wanted to start an event at 6 PM the day wouldn't actually be a
         * different day after moving the time, but if time would be 12AM it almost always
         * would be a wrong day
         *
         * @author Ilya Kislenka
         */
        fun boundPickedDateByUTC0(dateTimeStamp: Long, dateTime: Long) =
            getCalendarWithTimeStamp(dateTimeStamp).apply {
                val timeCalendar =
                    getCalendarWithTimeStamp(
                        dateTime)
                set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
                set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
            }.timeInMillis - getRawTimezoneOffset()

        /**
         * Function that will take a @param timeStamp is device timezone
         * and will decrease it's [Calendar.HOUR_OF_DAY] to a timezone offset
         * from UTC-0
         *
         *@author Ilya Kislenka
         */
        fun boundPickedTimeByUTC0(timeStamp: Long): Long {
            val calendar =
                getCalendarWithTimeStamp(timeStamp)
            calendar.add(Calendar.HOUR_OF_DAY, -getTimezoneOffsetInHours())
            return calendar.timeInMillis
        }

        /**
         * Function that will return a [Calendar] instance
         * with @param timeStamp as it's time inside
         *
         * @param timeStamp it's a time stamp that will be set as
         * a time inside the [Calendar]
         *
         * @author Ilya Kislenka
         */
        fun getCalendarWithTimeStamp(timeStamp: Long) =
            getRecentCalendar().apply { timeInMillis = timeStamp }

        /**
         * Function that will return a [Calendar.MONTH] of
         * passed @param timeStamp
         *
         * @author Ilya Kislenka
         */
        fun getMonthOfTimeStamp(timeStamp: Long) =
            getRecentCalendar().apply { timeInMillis = timeStamp }.get(Calendar.MONTH)

        /**
         * Function that will take two time stamps and will
         * transform them to a [String] representation, and
         * automatically determine whether they belongs to a same day or not,
         * and it will affect the transformation process
         *
         * @sample `Aug 28 2019 12:43-2:45 AM`
         * @sample `Aug 28 2019 12:43 AM-2:45 PM`
         * @sample `Aug 28 2019 12:43 PM-2:45 AM`
         * @sample `Aug 28 2019 12:43 PM-Aug 29 2019 12:43 PM`
         *
         * @param from it's a first timestamp to transform
         * @param to it's a second timestamp to transform
         * @param locale it's a [Locale] in which the month and AM/PM will be displayed
         *
         * @author Ilya Kislenka
         */
        fun formatTimestamps(from: Long,
                             to: Long,
                             locale: Locale = Locale.US): String {

            val toCalendar =
                getCalendarWithTimeStamp(to)
            val fromCalendar =
                getCalendarWithTimeStamp(from)

            val isTimestampsBelongsToSameDay =
                fromCalendar.get(Calendar.DAY_OF_MONTH) == toCalendar.get(Calendar.DAY_OF_MONTH)

            return if (isTimestampsBelongsToSameDay) {

                val toTimeStampPostfix = toCalendar.time.formatByPattern("a", locale)
                val fromTimeStampPostfix = fromCalendar.time.formatByPattern("a", locale)

                val isTimestampsBelongsTheSamePeriod = toTimeStampPostfix == fromTimeStampPostfix

                val toPresentation = toCalendar.time.formatByPattern("hh:mm a", locale)
                val fromPresentation =
                    fromCalendar.time.formatByPattern(if (isTimestampsBelongsTheSamePeriod) "hh:mm" else "hh:mm a",
                        locale)

                StringBuilder().builder {
                    append(fromCalendar.time.formatByPattern("MMM dd yyyy,", locale))
                    append(" ")
                    append(fromPresentation)
                    append("-")
                    append(toPresentation)
                }

            } else {
                StringBuilder().builder {
                    append(fromCalendar.time.formatByPattern("MMM dd yyyy, hh:mm a", locale))
                    append("-")
                    append(toCalendar.time.formatByPattern("MMM dd yyyy, hh:mm a", locale))
                }
            }
        }

        /**
         * Function that will take two time stamps and will
         * transform them to a [String] representation, but only
         * by one date
         *
         * @Note designed to be called on a time stamps that
         * belongs to the same months and year
         *
         * @sample `Aug 28 2019` if they belongs to the same day
         * @sample `Aug 28-29, 2019` if them belongs to a different days
         *
         * @param from it's a first timestamp to transform
         * @param to it's a second timestamp to transform
         * @param locale it's a [Locale] in which the month and AM/PM will be displayed
         *
         * @author Ilya Kislenka
         */
        fun formatTimeStampsByDate(from: Long,
                                   to: Long,
                                   locale: Locale = Locale.US): String {

            val toCalendar =
                getCalendarWithTimeStamp(to)
            val fromCalendar =
                getCalendarWithTimeStamp(from)

            val isTimestampsBelongsToSameDay =
                fromCalendar.get(Calendar.DAY_OF_MONTH) == toCalendar.get(Calendar.DAY_OF_MONTH)
            return if (isTimestampsBelongsToSameDay) toCalendar.time.formatByPattern("MMM  dd, yyyy") else
                StringBuilder().builder {
                    append(fromCalendar.time.formatByPattern("MMM  dd", locale))
                    append("-")
                    append(fromCalendar.time.formatByPattern("dd, yyyy", locale))
                }
        }

        /**
         * Function that will take two time stamps and will
         * transform them to a [String] representation, but only
         * by time
         *
         * @Note designed to be called on a time stamps that
         * belongs to the same day, month and year
         *
         * @sample `10:45-11-45 AM` or `10:45-11-45 PM` if they belongs to the same time offset
         * @sample `10:45 AM-11-45 PM` if they don't belongs to the same time offset
         *
         * @param from it's a first timestamp to transform
         * @param to it's a second timestamp to transform
         * @param locale it's a [Locale] in which AM/PM postfixes will be displayed
         *
         * @author Ilya Kislenka
         */
        fun formatTimeStampsByTime(from: Long,
                                   to: Long,
                                   locale: Locale = Locale.US): String {

            val toCalendar =
                getCalendarWithTimeStamp(to)
            val fromCalendar =
                getCalendarWithTimeStamp(from)

            val toTimeStampPostfix = toCalendar.time.formatByPattern("a", locale)
            val fromTimeStampPostfix = fromCalendar.time.formatByPattern("a", locale)

            val isTimestampsBelongsTheSamePeriod = toTimeStampPostfix == fromTimeStampPostfix

            val toPresentation = toCalendar.time.formatByPattern("hh:mm a", locale)
            val fromPresentation =
                fromCalendar.time.formatByPattern(if (isTimestampsBelongsTheSamePeriod) "hh:mm" else "hh:mm a", locale)

            return StringBuilder().builder {
                append(fromPresentation)
                append("-")
                append(toPresentation)
            }
        }

        /**
         * Function that will move @param day time stamp
         * time to a start of the day time
         *
         * @author Ilya Kislenka
         */
        fun moveTimeStampToStartOfTheDay(day: Long): Long {
            return getRecentCalendar().apply {
                timeInMillis = day
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 1)
                set(Calendar.SECOND, 1)
            }.timeInMillis
        }

        /**
         * Function that will move @param day time stamp
         * time to an end of the day time
         *
         * @author Ilya Kislenka
         */
        fun moveTimeStampToEndOfTheDay(day: Long): Long {
            return getRecentCalendar().apply {
                timeInMillis = day
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }.timeInMillis
        }

        private fun setActualMinimumToField(calendar: Calendar, calendarField: Int) {
            calendar.set(calendarField, calendar.getActualMinimum(calendarField))
        }

        private fun getCalendarField(calendarField: Int) = getRecentCalendar().get(calendarField)

        /**
         * Function that will parse @param timeStamp to a
         * specified pattern, and will return 0 if it's not possible
         *
         * @param timeStamp it's a time stamp represented as [String]
         * @param pattern it's a String template to parse the timeStamp
         * @param addingTimeZoneOffset determines whether the function will
         * add a time zone offset or not
         *
         * @author Ilya Kislenka
         */
        fun parseTimeStampByPattern(
            timeStamp: String,
            pattern: String = "HH:mm:ss",
            addingTimeZoneOffset: Boolean = true
        ): Long {

            return try {

                SimpleDateFormat(pattern, Locale.US).let {
                    if (addingTimeZoneOffset)
                        it.timeZone = TimeZone.getTimeZone("UTC")
                    it.parse(timeStamp).time
                }

            } catch (ignore: ParseException) {
                Logger.logError(
                    DateUtils::class.java.simpleName,
                    "Cannot parse passed string = $timeStamp, by pattern $pattern",
                    error = ignore
                )
                return 0
            }
        }

        /**
         * Function that designed to transform the day of the week
         * from the servers UTC-0 time zone to a proper day name in
         * a device locale, by appending to time linked with the server
         * day a time zone offset
         *
         * @param dayName it's one of the days from [parseDayNumberToName]
         * @param startTime designed to be a time in the following format "HH:mm:ss"
         * @param UTC0 it's a param that will affects to appending a time zone offset or
         * decreasing it
         *
         * @return one of the days similar to [parseDayNumberToName]
         *
         * @author Ilya Kislenka
         */
        fun dayOfTheWeekIn(
            dayName: String,
            startTime: String,
            UTC0: Boolean = true
        ): String {

            val calendar = getRecentCalendar()
            val dayNumber = calendar.parseDayNameToNumber(dayName.toUpperCase(Locale.US))
            val dayTimeCalendar =
                getCalendarWithTimeStamp(
                    parseTimeStampByPattern(
                        startTime,
                        addingTimeZoneOffset = false))
            calendar.apply {
                set(Calendar.DAY_OF_WEEK, dayNumber)
                set(Calendar.MINUTE, dayTimeCalendar.get(Calendar.MINUTE))
                set(Calendar.HOUR_OF_DAY, dayTimeCalendar.get(Calendar.HOUR_OF_DAY))

                val calendarTime = calendar.timeInMillis
                val timeZoneOffset =
                    getRawTimezoneOffset()

                timeInMillis = if (UTC0) calendarTime - timeZoneOffset else calendarTime + timeZoneOffset
            }
            val resultDayNumber = calendar.get(Calendar.DAY_OF_WEEK)
            return calendar.parseDayNumberToName(resultDayNumber)
        }

        /**
         * Function that will take the day and time time stamps
         * and will combine them in a one time stamp with adding
         * timezone offset or not
         *
         * @param day it's a day timestamp from which only the [Calendar.YEAR], [Calendar.MONTH] and [Calendar.DAY_OF_MONTH] will
         * be used
         * @param time it's a time timestamp from which only the [Calendar.MINUTE] and [Calendar.HOUR_OF_DAY] will be used
         * @param addingTimeZoneOffset determines whether or not to add a timezone offset or not
         *
         * @author Ilya Kislenka
         */
        fun buildTimeStamp(
            day: Long,
            time: Long,
            addingTimeZoneOffset: Boolean = false
        ): Long {
            val timeStamp = Calendar.getInstance().apply {
                val dayCalendar =
                    getCalendarWithTimeStamp(day)
                set(Calendar.YEAR, dayCalendar.get(Calendar.YEAR))
                set(Calendar.MONTH, dayCalendar.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, dayCalendar.get(Calendar.DAY_OF_MONTH))

                val timeCalendar =
                    getCalendarWithTimeStamp(time)
                set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
                set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
            }.timeInMillis
            return if (addingTimeZoneOffset) timeStamp + getRawTimezoneOffset() else timeStamp
        }

        /**
         * Function that will determine whether the @param birthDate is
         * corresponds to a desired @param desiredAge
         *
         * @Note function will count, that even if @param birthDate
         * is exactly today but a some years ago, then [Calendar.HOUR_OF_DAY]
         * and [Calendar.MINUTE] will be counted in calculation
         *
         * @param desiredAge it's an age that you desire date to correspond
         * @param birthDate it's a timestamp of a person's birth date
         *
         * @author Ilya Kislenka
         */
        fun isAgeCorrespondsTo(desiredAge: Int,
                               birthDate: Long): Boolean {

            if (birthDate == -1L)
                return false

            val recentCalendar = getRecentCalendar()
            val birthDateCalendar = getRecentCalendar()
                .apply {
                timeInMillis = birthDate
            }
            val yearsDifference =
                recentCalendar.get(Calendar.YEAR) - birthDateCalendar.get(Calendar.YEAR)

            return if (yearsDifference == desiredAge) {

                val recentMonth = recentCalendar.get(Calendar.MONTH)
                val birthDayMonth = birthDateCalendar.get(Calendar.MONTH)

                val afterBirthday = recentMonth > birthDayMonth

                when {
                    afterBirthday -> true
                    recentMonth == birthDayMonth -> recentCalendar.get(Calendar.DAY_OF_MONTH) > birthDateCalendar.get(
                        Calendar.DAY_OF_MONTH
                    )
                    else -> false
                }
            } else
                yearsDifference > desiredAge
        }
    }

    /**
     * Function that designed to transform passed params
     * to a [Date] instance
     *
     * @Note designed to use after [showDatePickerDialog] callback returned
     * the result
     *
     * @param year the year to apply
     * @param month the month to apply
     * @param day the day to  apply
     *
     * @author Ilya Kislenka
     */
    fun toDate(
        year: Int,
        month: Int,
        day: Int
    ): Date {
        val calendar = getRecentCalendar()
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return Date(calendar.timeInMillis)
    }
}
