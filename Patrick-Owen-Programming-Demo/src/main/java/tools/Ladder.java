package tools;

public class Ladder extends Tool {
    public Ladder(String toolCode, String brand) {
        super(toolCode, brand);
        this.toolType = "Ladder";
        this.dailyCharge = 1.99;
        this.weekdayCharge = true;
        this.weekendCharge = true;
        this.holidayCharge = false;
    }
}
