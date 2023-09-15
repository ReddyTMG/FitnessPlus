import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Payment {
    public static void payment(User currentUser) {
        // Retrieve the user's cart information
        List<String> userBookings = Booking.getUserBookings(currentUser.getPhone());
        Booking booking = new Booking();

        if (userBookings.isEmpty()) {
            System.out.println("No bookings found for the current user.");
            System.out.print("Press Enter to continue...");
            FitnessPlus.input.nextLine();
        } else {
            double totalPrice = calculateTotalPrice(userBookings);
            if(totalPrice == 0){
                System.out.println("No bookings in the cart.");
                FitnessPlus.input.nextLine();
            }else{
                System.out.println("Payment: " + currentUser.getPhone());
                booking.showHistory(currentUser.getPhone());

                // Calculate the total price of the current user's booking lists
                
                System.out.println("Total Price: RM" + totalPrice);

                // Ask the user if they want to continue with payment
                System.out.print("Do you want to continue with payment? (y/n): ");
                String choice = FitnessPlus.input.nextLine();
                System.out.println();

                if (choice.equalsIgnoreCase("y")) {
                    if(currentUser.getWallet() >= totalPrice){
                        // Deduct the total price from the user's wallet
                        double newWalletBalance = currentUser.getWallet() - totalPrice;
                        currentUser.setWallet(newWalletBalance);
                        try {
                            currentUser.saveUser();
                        }
                        catch (IOException e) {
                            System.out.println("Error saving user data to file.");
                            e.printStackTrace();
                        }

                        // Update booking status to "Paid" 
                        updateBookingStatus(userBookings,currentUser.getPhone());

                        // Generate an invoice and save it to invoice.txt
                        generateInvoice(currentUser, userBookings);

                        System.out.println("Payment successful!");
                        FitnessPlus.input.nextLine();
                        Booking.cart.clear();
                    }else{
                        // Not enough money to pay
                        System.out.println("Balance is not enough.");
                        FitnessPlus.input.nextLine();
                    }
                } else {
                    System.out.println("Payment canceled.");
                    FitnessPlus.input.nextLine();
                }
            }
        }
    }

    // Calculate the total price of pending bookings
    private static double calculateTotalPrice(List<String> userBookings) {
        double totalPrice = 0;
        for (String booking : userBookings) {
            String[] parts = booking.split("\\*");
            if (parts.length >= 8 && parts[parts.length -1].equals("Pending")) {
                double price = Double.parseDouble(parts[8]);
                totalPrice += price;
            }
        }
        return totalPrice;
    }

    // Update the payment status of bookings to "Paid"
    public static void updateBookingStatus(List<String> userBookings, String currentUserPhone) {
        String fileName = "booking.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            List<String> updatedBookings = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\*");
                if (parts.length >= 11) {
                    String bookingPhone = parts[0];
                    String paymentStatus = parts[10];
                    // Check if the phone matches the current user and payment status is "Pending"
                    if (bookingPhone.equals(currentUserPhone) && paymentStatus.equals("Pending")) {
                        // Update the payment status to "Paid"
                        parts[10] = "Paid";
                    }
                }
                updatedBookings.add(String.join("*", parts));
            }

            // Rewrite the entire file with updated bookings
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                for (String updatedBooking : updatedBookings) {
                    writer.println(updatedBooking);
                }
            } catch (IOException e) {
                System.out.println("Error writing updated data to file.");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Error reading data from file.");
            e.printStackTrace();
        }
    }

    // Generate an invoice and display it
    private static void generateInvoice(User currentUser, List<String> userBookings) {
        double totalClassPrice = 0.0;
        int totalClasses = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());

        System.out.println("-------------------------------------------------Receipt--------------------------------------------------");
        System.out.println("ClassId       ClassName       Price(RM)       Quantity         Start Time                   Duration(mins)");

        for (String booking : userBookings) {
            String[] parts = booking.split("\\*");
            if(parts[parts.length -1].equals("Pending")){
                String classId = parts[1];
                String className = parts[2];
                double price = Double.parseDouble(parts[8]);
                int quantity = 1; // Since it's one booking
                String startTime = parts[3];
                int duration = Integer.parseInt(parts[6]);

                String format = "%-13s %-15s %-15s %-16s %-28s %-10s %n";

                System.out.format(format, classId, className, price, quantity, startTime, duration);

                totalClassPrice += price;
                totalClasses++;
            }
        }

        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.printf("Total class%66s:%16d%n", "", totalClasses);
        System.out.printf("Total Price%66s:               RM%.2f%n", "", totalClassPrice);

        double totalPrice = calculateTotalPrice(userBookings);

        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.printf("Total Payment%64s:               RM%.2f%n", "", totalPrice);
        System.out.println(timestamp);
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

}