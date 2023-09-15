import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TimeFormat {
    //Date and time formatter
    public static LocalDateTime getDateTimeInput(Scanner GetData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = null;

        while (dateTime == null) {
            try {
                System.out.print("Enter Date and Time (yyyy-MM-dd HH:mm): ");
                String input = GetData.nextLine();
                dateTime = LocalDateTime.parse(input, formatter);
            } catch (Exception e) {
                System.out.println("Invalid date and time format. Please use yyyy-MM-dd HH:mm.");
            }
        }

        return dateTime;
    }

    //format the date and time into: 2023 June 21 06:40 PM
    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy MMMM dd hh:mm a");
        return dateTime.format(outputFormatter);
    }
}
