package tools;

public class Chainsaw extends Tool {
    public Chainsaw(String toolCode, String brand) {
        super(toolCode, brand);
        this.toolType = "Chainsaw";
        this.dailyCharge = 1.49;
        this.weekdayCharge = true;
        this.weekendCharge = false;
        this.holidayCharge = true;
    }
}
