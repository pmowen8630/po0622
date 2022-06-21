package tools;

public class Jackhammer extends Tool {
    public Jackhammer(String toolCode, String brand) {
        super(toolCode, brand);
        this.toolType = "Chainsaw";
        this.dailyCharge = 2.99;
        this.weekdayCharge = true;
        this.weekendCharge = false;
        this.holidayCharge = false;
    }
}
