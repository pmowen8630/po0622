package tools;
import lombok.Getter;
import java.text.NumberFormat;

@Getter
public abstract class Tool {

    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    String toolCode;
    String toolType;
    String brand;
    double dailyCharge;
    boolean weekdayCharge;
    boolean weekendCharge;
    boolean holidayCharge;

    public Tool(String toolCode, String brand) {
        this.toolCode = toolCode;
        this.brand = brand;
    };

    public String printToolInfo() {
        return String.format("\tTool Code: %s\n\tTool Type: %s\n\tBrand: %s\n\tDaily Charge: %s\n\tWeekday Charge: %s\n\tWeekend Charge: %s\n\tHoliday Charge: %s",
                toolCode,
                toolType,
                brand,
                formatter.format(dailyCharge),
                weekdayCharge ? "Yes": "No",
                weekendCharge ? "Yes": "No",
                holidayCharge ? "Yes": "No"
                );
    };
}


