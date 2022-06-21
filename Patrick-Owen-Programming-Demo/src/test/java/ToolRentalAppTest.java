import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tools.Chainsaw;
import tools.Jackhammer;
import tools.Ladder;
import tools.Tool;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
class InvalidRentalDayCount extends Exception {public InvalidRentalDayCount (String e) {
    super(e);
}};
class InvalidDiscountPercent extends Exception {public InvalidDiscountPercent (String e) {
    super(e);
}};

class ToolRentalAppTest {

    @ParameterizedTest
    @MethodSource("invalidRentalDayTestParams")
    public void shouldPrintInvalidRentalDay(String input) throws Exception {
        String userInput = String.format(input,
                System.lineSeparator(),
                System.lineSeparator(),
                System.lineSeparator());
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);
        ToolRentalApp.main(null); // call the main method

        InvalidRentalDayCount exception = assertThrows(
                InvalidRentalDayCount.class,
                () -> { throw new InvalidRentalDayCount("Please enter an integer greater than 0..."); }
        );
        assertEquals("Please enter an integer greater than 0...", exception.getMessage());

    }
    private static Stream<Arguments> invalidRentalDayTestParams() {
        return Stream.of(
                Arguments.of("CHNS%s-2%s10%s9/3/15"),
                Arguments.of("LADW%sxxxxx%s25%s9/3/15")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDiscountTestParams")
    public void shouldPrintInvalidDiscount(String input) throws Exception {
        String userInput = String.format(input,
                System.lineSeparator(),
                System.lineSeparator(),
                System.lineSeparator());
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);
        ToolRentalApp.main(null); // call the main method

        InvalidDiscountPercent exception = assertThrows(
                InvalidDiscountPercent.class,
                () -> { throw new InvalidDiscountPercent("Please enter an integer in the range 0 - 100..."); }
        );
        assertEquals("Please enter an integer in the range 0 - 100...", exception.getMessage());

    }
    private static Stream<Arguments> invalidDiscountTestParams() {
        return Stream.of(
                Arguments.of("CHNS%s5%s101%s9/3/15"),
                Arguments.of("LADW%s5%s-3%s9/3/15"),
                Arguments.of("JAKR%s6%sxxxx%s9/3/15")
        );
    }

    @ParameterizedTest
    @MethodSource("toolCatalogTestParams")
    public void shouldPrintToolInfo(String toolCode, String output) throws Exception {
        Map<String, Tool> toolCatalog = new HashMap<>();
        toolCatalog.put("CHNS", new Chainsaw("CHNS", "Stihl"));
        toolCatalog.put("LADW", new Ladder("LADW", "Werner"));
        toolCatalog.put("JAKD", new Jackhammer("JAKD", "DeWalt"));
        toolCatalog.put("JAKR", new Jackhammer("JAKR", "Ridgid"));


        // checkout output
        Assertions.assertEquals(output,toolCatalog.get(toolCode).printToolInfo());
    }

    private static Stream<Arguments> toolCatalogTestParams() {
        return Stream.of(
                Arguments.of("CHNS",
                        "\tTool Code: CHNS\n" +
                        "\tTool Type: Chainsaw\n" +
                        "\tBrand: Stihl\n" +
                        "\tDaily Charge: $1.49\n" +
                        "\tWeekday Charge: Yes\n" +
                        "\tWeekend Charge: No\n" +
                        "\tHoliday Charge: Yes"),
                Arguments.of("LADW",
                        "\tTool Code: LADW\n" +
                        "\tTool Type: Ladder\n" +
                        "\tBrand: Werner\n" +
                        "\tDaily Charge: $1.99\n" +
                        "\tWeekday Charge: Yes\n" +
                        "\tWeekend Charge: Yes\n" +
                        "\tHoliday Charge: No"),
                Arguments.of("JAKD",
                        "\tTool Code: JAKD\n" +
                        "\tTool Type: Chainsaw\n" +
                        "\tBrand: DeWalt\n" +
                        "\tDaily Charge: $2.99\n" +
                        "\tWeekday Charge: Yes\n" +
                        "\tWeekend Charge: No\n" +
                        "\tHoliday Charge: No"),
                Arguments.of("JAKR",
                        "\tTool Code: JAKR\n" +
                        "\tTool Type: Chainsaw\n" +
                        "\tBrand: Ridgid\n" +
                        "\tDaily Charge: $2.99\n" +
                        "\tWeekday Charge: Yes\n" +
                        "\tWeekend Charge: No\n" +
                        "\tHoliday Charge: No")
        );
    }

    @ParameterizedTest
    @MethodSource("rentalAgreementTestParams")
    public void shouldPrintRentalAgreement(String input, String output) throws Exception {
        String userInput = String.format(input,
                System.lineSeparator(),
                System.lineSeparator(),
                System.lineSeparator(),
                System.lineSeparator());
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);

        String expected = output;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        ToolRentalApp.main(null); // call the main method

        String[] lines = baos.toString().split(System.lineSeparator());
        String actual = lines[lines.length-1];

        // checkout output
        Assertions.assertEquals(expected,actual);
    }

    private static Stream<Arguments> rentalAgreementTestParams() {
        return Stream.of(
                Arguments.of("LADW%s3%s10%s7/2/20%s",
                        "Tool Code: LADW\n" +
                                "Tool Type: Ladder\n" +
                                "Tool Brand: Werner\n" +
                                "Checkout Date: 07/02/20\n" +
                                "Due Date: 07/05/20\n" +
                                "Daily Rental Charge: $1.99\n" +
                                "Charge Days: 3\n" +
                                "Pre-discount Charge: $5.97\n" +
                                "Discount Percent: 10%\n" +
                                "Discount Amount: $0.60\n" +
                                "Final Charge: $5.37"),
                Arguments.of("CHNS%s5%s25%s7/2/15%s",
                        "Tool Code: CHNS\n" +
                                "Tool Type: Chainsaw\n" +
                                "Tool Brand: Stihl\n" +
                                "Checkout Date: 07/02/15\n" +
                                "Due Date: 07/07/15\n" +
                                "Daily Rental Charge: $1.49\n" +
                                "Charge Days: 4\n" +
                                "Pre-discount Charge: $5.96\n" +
                                "Discount Percent: 25%\n" +
                                "Discount Amount: $1.49\n" +
                                "Final Charge: $4.47"),
                Arguments.of("JAKD%s6%s0%s9/3/15%s",
                        "Tool Code: JAKD\n" +
                                "Tool Type: Chainsaw\n" +
                                "Tool Brand: DeWalt\n" +
                                "Checkout Date: 09/03/15\n" +
                                "Due Date: 09/09/15\n" +
                                "Daily Rental Charge: $2.99\n" +
                                "Charge Days: 4\n" +
                                "Pre-discount Charge: $11.96\n" +
                                "Discount Percent: 0%\n" +
                                "Discount Amount: $0.00\n" +
                                "Final Charge: $11.96"),
                Arguments.of("JAKR%s9%s0%s7/2/15%s",
                        "Tool Code: JAKR\n" +
                                "Tool Type: Chainsaw\n" +
                                "Tool Brand: Ridgid\n" +
                                "Checkout Date: 07/02/15\n" +
                                "Due Date: 07/11/15\n" +
                                "Daily Rental Charge: $2.99\n" +
                                "Charge Days: 6\n" +
                                "Pre-discount Charge: $17.94\n" +
                                "Discount Percent: 0%\n" +
                                "Discount Amount: $0.00\n" +
                                "Final Charge: $17.94"),
                Arguments.of("JAKR%s4%s50%s7/2/20%s",
                        "Tool Code: JAKR\n" +
                                "Tool Type: Chainsaw\n" +
                                "Tool Brand: Ridgid\n" +
                                "Checkout Date: 07/02/20\n" +
                                "Due Date: 07/06/20\n" +
                                "Daily Rental Charge: $2.99\n" +
                                "Charge Days: 2\n" +
                                "Pre-discount Charge: $5.98\n" +
                                "Discount Percent: 50%\n" +
                                "Discount Amount: $2.99\n" +
                                "Final Charge: $2.99")
        );
    }

}