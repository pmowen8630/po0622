import tools.Tool;

import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class RentalAgreement {
    String toolCode;
    String toolType;
    String toolBrand;
    int rentalDays;
    LocalDate checkoutDate;
    LocalDate dueDate;
    double dailyRentalCharge;
    int chargeDays;
    double preDiscountCharge;
    double discountPercent;
    double discountAmount;
    double finalCharge;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
    NumberFormat percentFormatter = NumberFormat.getPercentInstance();

    // construct rental agreement
    public RentalAgreement (
            Tool tool,
            int rentalDayCount,
            double discount,
            LocalDate checkoutDate
    ) {
        this.toolCode = tool.getToolCode();
        this.toolType = tool.getToolType();
        this.toolBrand = tool.getBrand();
        this.rentalDays = rentalDayCount;
        this.checkoutDate = checkoutDate;
        this.dueDate = checkoutDate.plusDays(rentalDayCount);
        this.dailyRentalCharge = tool.getDailyCharge();
        this.chargeDays = getChargeDays(checkoutDate, dueDate, tool.isWeekdayCharge(), tool.isWeekendCharge(), tool.isHolidayCharge());
        this.preDiscountCharge = tool.getDailyCharge() * chargeDays;
        this.discountPercent = discount / 100;
        this.discountAmount = discountPercent * preDiscountCharge;
        this.finalCharge = preDiscountCharge - discountAmount;

    }


    // Iterate and add days of rental agreement if each day should be charged
    private int getChargeDays(LocalDate checkoutDate, LocalDate dueDate, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        int chargeDays = 0;
        for (LocalDate date = checkoutDate; date.isBefore(dueDate) || date.equals(dueDate); date = date.plusDays(1)) {
            if (isWeekend(date)) {
                if (weekendCharge)
                    chargeDays++;
            }
            else {
                if (isHoliday(date)){
                    if (holidayCharge)
                        chargeDays ++;
                }
                else {
                    if (weekdayCharge)
                        chargeDays++;
                }
            }
        }
        return chargeDays;
    }

    // return whether date is on weekend
    private static boolean isWeekend(final LocalDate checkDate) {
        DayOfWeek day = DayOfWeek.of(checkDate.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
    }

    // return whether date is holiday
    private static boolean isHoliday(final LocalDate checkDate) {
        LocalDate date = LocalDate.of(checkDate.getYear(), Month.SEPTEMBER, 1);
        TemporalAdjuster laborDay =
                TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY);

        LocalDate observedIndependenceDay = getObservedIndependenceDay(checkDate.getYear());
        return checkDate.isEqual(date.with(laborDay)) || checkDate.isEqual(observedIndependenceDay);
    }

    // Return the date Independence Day is observed on for given year
    private static LocalDate getObservedIndependenceDay(final int year) {
        LocalDate independenceDay = LocalDate.of(year, 7, 4);
        DayOfWeek day = DayOfWeek.of(independenceDay.get(ChronoField.DAY_OF_WEEK));
        if (day == DayOfWeek.SATURDAY)
            return independenceDay.minusDays(1);
        if (day == DayOfWeek.SUNDAY)
            return independenceDay.plusDays(1);
        return independenceDay;
    }

    @Override
    public String toString() {
        return "Tool Code: " + toolCode + '\n' +
                "Tool Type: " + toolType + '\n' +
                "Tool Brand: " + toolBrand + '\n' +
                "Checkout Date: " + checkoutDate.format(dateTimeFormatter) + '\n' +
                "Due Date: " + dueDate.format(dateTimeFormatter) + '\n' +
                "Daily Rental Charge: " + currencyFormatter.format(dailyRentalCharge) + '\n' +
                "Charge Days: " + chargeDays + '\n' +
                "Pre-discount Charge: " + currencyFormatter.format(preDiscountCharge) + '\n' +
                "Discount Percent: " + percentFormatter.format(discountPercent) + '\n' +
                "Discount Amount: " + currencyFormatter.format(discountAmount) + '\n' +
                "Final Charge: " + currencyFormatter.format(finalCharge);
    }
}
