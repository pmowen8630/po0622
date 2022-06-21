/*
Patrick Owen
Programming Demo
June 20 2022
 */

import tools.*;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ToolRentalApp {
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
    private final Scanner scanner;
    private final PrintStream out;
    private final Map<String, Tool> toolCatalog;

    public ToolRentalApp(InputStream in, PrintStream out){
        scanner = new Scanner(in);
        this.out = out;
        this.out.println("Patrick Owen - Tool Rental Programming Demo\n");
        // generate tool catalog
        Map<String, Tool> toolCatalog = new HashMap<>();
        toolCatalog.put("CHNS", new Chainsaw("CHNS", "Stihl"));
        toolCatalog.put("LADW", new Ladder("LADW", "Werner"));
        toolCatalog.put("JAKD", new Jackhammer("JAKD", "DeWalt"));
        toolCatalog.put("JAKR", new Jackhammer("JAKR", "Ridgid"));
        this.toolCatalog = toolCatalog;
    }

    public void checkout() {
        out.println("~~~~~~ Checkout ~~~~~~");
        Tool tool = null;
        String toolCode = "";
        int rentalDayCount = 0;
        int discount = -1;
        LocalDate checkoutDate = null;

        // Scan for valid tool code
        boolean exception = true;
        while (exception) {
            out.println("Enter Tool Code: ");
            if (scanner.hasNextLine())
                toolCode = scanner.nextLine().toUpperCase();
            else
                exception = false;
            if (!toolCode.equals("")) {
                tool = toolCatalog.get(toolCode);
                try {
                    out.println(tool.printToolInfo() + "\n");
                    exception = false;
                }
                catch (NullPointerException e) {
                    out.println("Tool Code not found in catalog. Please try again...");
                }
            }
        }

        // Scan for valid rental day count
        if (tool!=null) {
            exception = true;
            while (exception) {
                out.println("Enter number of rental days:");
                try {
                    if (scanner.hasNextLine())
                        rentalDayCount = Integer.parseInt(scanner.nextLine());
                    else
                        exception = false;

                    if (rentalDayCount < 0)
                        throw new InvalidRentalDayCount("Please enter an integer greater than 0...");
                    exception = false;
                } catch (NumberFormatException | InvalidRentalDayCount e) {
                    out.println("Please enter an integer greater than 0...");
                }
            }
        }

        // Scan for valid discount
        if (rentalDayCount > 0) {
            exception = true;
            while (exception) {
                out.println("Enter discount (0 - 100):");
                try {
                    if (scanner.hasNextLine())
                        discount = Integer.parseInt(scanner.nextLine());
                    else
                        exception = false;
                    if (discount < 0 || discount > 100)
                        throw new InvalidDiscountPercent("Please enter an integer in the range 0 - 100...");
                    exception = false;
                } catch (NumberFormatException | InvalidDiscountPercent e) {
                    out.println("Please enter an integer in the range 0 - 100...");
                }
            }
        }

        // Scan for valid checkout date
        if (discount >= 0 && discount <= 100 ) {
            exception = true;
            while (exception) {
                out.println("Enter checkout date (MM/dd/yy):");
                try {
                    if (scanner.hasNextLine())
                        checkoutDate = sdf.parse(scanner.nextLine()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    else
                        exception = false;

                    exception = false;
                } catch (ParseException e) {
                    out.println("Please enter a valid date...");
                }
            }
        }

        if (tool!=null && rentalDayCount!=0 && discount!=-1 && checkoutDate!=null) {

            out.println("\n~~~~~~ Rental Agreement ~~~~~~");
            RentalAgreement ra = new RentalAgreement(tool, rentalDayCount, discount, checkoutDate);
            out.println(ra.toString());
        }

    }

    static class InvalidRentalDayCount extends Exception {public InvalidRentalDayCount (String e) {
        super(e);
    }};
    static class InvalidDiscountPercent extends Exception {public InvalidDiscountPercent (String e) {
        super(e);
    }};

    public static void main(String[] args) {
        ToolRentalApp toolRentalApp = new ToolRentalApp(System.in, System.out);
        toolRentalApp.checkout();
    }

}
