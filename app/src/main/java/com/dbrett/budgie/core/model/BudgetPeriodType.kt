package com.dbrett.budgie.core.model

//import com.dbrett.budgie.core.data.source.local.entity.getIsoWeekNumber
//import kotlinx.datetime.LocalDate
//import kotlinx.datetime.number
//
enum class BudgetPeriodType {
    DAY,
    WEEK,
    MONTH,
    YEAR,
    CUSTOM
}
//
//public fun generatePeriodKey(
//    periodType: BudgetPeriodType,
//    from : LocalDate,
//    to   : LocalDate? = null
//) : String {
//    when (periodType) {
//        BudgetPeriodType.DAY   -> {
//            val monthNumber = from.month.number.toString().padStart(2, '0')
//            val dayStr = from.day.toString().padStart(2, '0')
//            return "DAY_${from.year}-$monthNumber-$dayStr"
//        }
//
//        BudgetPeriodType.WEEK  -> {
//            val weekNumber = getIsoWeekNumber(from)
//            return "WEEK_${from.year}-$weekNumber"
//        }
//
//        BudgetPeriodType.MONTH -> {
//            val monthNumber = from.month.number.toString().padStart(2, '0')
//            return "MONTH_${from.year}-$monthNumber"
//        }
//
//        BudgetPeriodType.YEAR  -> return "YEAR_${from.year}"
//
//        BudgetPeriodType.CUSTOM -> {
//            checkNotNull(to)
//            return "CUSTOM_${from}_${to}"
//        }
//    }
//}