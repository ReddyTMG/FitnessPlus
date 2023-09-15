import java.io.*;
import java.util.*;

public class BookingHistory {
    // Method to display booking history for a user
    public void showHistory(String currentUserPhone) {
        // Load booking history for the user with currentUserPhone
        List<String> bookingHistory = loadBookingHistory(currentUserPhone);

        if (bookingHistory.isEmpty()) {
            System.out.println("No booking history found for the user.");
            System.out.print("Press Enter to continue...");
            FitnessPlus.input.nextLine();
        } else {
            System.out.println("Booking History by " + currentUserPhone);
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            String format = "| %-8s | %-10s | %-30s | %-15s | %-17s | %-15s | %-15s | %-30s | %-14s |%n";

            System.out.format(format, "Class ID", "Class Type", "Date and Time", "Instructor Name",
                    "Instructor Phone", "Class Duration", "Price", "Class Description", "Payment Status");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            for (String gymClass : bookingHistory) {
                String[] classDetails = gymClass.split("\\*");
                System.out.format(format, classDetails[1], classDetails[2], classDetails[3], classDetails[4],
                        classDetails[5], classDetails[6] + " mins", "RM" + classDetails[8],
                        classDetails[9], classDetails[10]);
            }
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            System.out.print("Press Enter to continue...");
            FitnessPlus.input.nextLine();
            bookingHistory.clear();
        }
    }

    // Method to load booking history from a file based on currentUserPhone
    public static List<String> loadBookingHistory(String currentUserPhone) {
        List<String> userBookingHistory = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("booking.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookingData = line.split("\\*");
                if (bookingData.length >= 10 && bookingData[0].equals(currentUserPhone)) {
                    userBookingHistory.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userBookingHistory;
    }
}
