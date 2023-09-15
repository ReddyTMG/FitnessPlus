import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Booking implements HistoryShower {
    public static List<String> cart = new ArrayList<>();

    // ArrayList to store gymclasses
    public static final List<String> gymClasses = new ArrayList<>();

    // Method for making a booking
    public static void booking(String currentUserPhone) {
        gymClasses.clear();
        String option;

        loadClass();
        loadCart(currentUserPhone);
        while (true) {
            Administrator.displayClass(gymClasses);

            System.out.print("Enter options using index (or 'esc' to exit and save): ");
            option = FitnessPlus.input.nextLine();

            if (option.equalsIgnoreCase("esc")) {
                saveCart(currentUserPhone);
                gymClasses.clear();
                cart.clear();
                return;
            }

            int index = findClassIndex(option);

            if (index == -1) {
                System.out.println("Class not found.");
                FitnessPlus.input.nextLine();
            } else {
                boolean bookingExistInCart = false;

                // Check if the class exists in the cart for the current user
                for (String cartItem : cart) {
                    String[] cartDetails = cartItem.split("\\*");
                    if (cartDetails[0].equals(currentUserPhone) && (cartDetails[cartDetails.length - 1].equals("Pending") || cartDetails[cartDetails.length - 1].isEmpty())) {
                        if (Integer.parseInt(cartDetails[1]) == (index + 1)) {
                            bookingExistInCart = true;
                            break;
                        }
                    }
                }

                if (bookingExistInCart) {
                    System.out.println("You have already added this to the cart.");
                    FitnessPlus.input.nextLine();
                } else {

                    String gymClass = gymClasses.get(index);
                    String[] classDetails = gymClass.split("\\*");
                    int availableSlots = Integer.parseInt(classDetails[6]);

                    if (availableSlots > 0) {
                        cart.add(currentUserPhone + "*" + gymClass + "*Pending");
                        System.out.println("Class added to cart.");
                        FitnessPlus.input.nextLine();

                        // Decrease available slots
                        availableSlots--;
                        classDetails[6] = Integer.toString(availableSlots);

                        // Update the gymClasses list
                        gymClasses.set(index, String.join("*", classDetails));

                        saveClass();
                    } else {
                        System.out.println("Not enough slots for this class.");
                        FitnessPlus.input.nextLine();
                    }
                }
            }
        }
    }

    // Method for canceling a booking
    public static void cancelBooking(String currentUserPhone) {

        gymClasses.clear();
        loadClass();
        loadCart(currentUserPhone);

        while (true) {
            Booking booking = new Booking();
            boolean bookingExist = false;
            for (String cartitems : cart) {
                String[] cartitem = cartitems.split("\\*");
                if (cartitem[0].equals(currentUserPhone) && cartitem[cartitem.length - 1].equals("Pending"))
                    bookingExist = true;

            }
            if (!bookingExist) {
                System.out.println("No previous booking in your cart.");
                FitnessPlus.input.nextLine();
                gymClasses.clear();
                cart.clear();
                return;
            }

            booking.showHistory(currentUserPhone);
            boolean classExist = false;

            System.out.print("Enter class id to cancel (Enter \"esc\" to exit): ");
            String classId = FitnessPlus.input.nextLine();

            if (classId.equalsIgnoreCase("esc")) {
                gymClasses.clear();
                cart.clear();
                return;
            }
            // Find and remove the booking from the cart
            int cartIndexToRemove = -1;
            for (int i = 0; i < cart.size(); i++) {
                String cartItem = cart.get(i);
                String[] cartDetails = cartItem.split("\\*");
                if (cartDetails[0].equals(currentUserPhone) && cartDetails[1].equals(classId)) {
                    classExist = true;
                    cartIndexToRemove = i;
                    break;
                }
            }
            // Load and update the booking.txt file
            try (BufferedReader reader = new BufferedReader(new FileReader("booking.txt"))) {
                List<String> updatedLines = new ArrayList<>(); // To store updated lines
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\*");
                    if (parts[0].equals(currentUserPhone) && parts[1].equals(classId) && parts[parts.length - 1].equals("Pending")) {
                        classExist = true;
                        System.out.println("Class is removed successfully.");
                        FitnessPlus.input.nextLine();

                        int index = findClassIndex(classId);
                        // Update gymClasses
                        String gymClass = gymClasses.get(index);
                        String[] classDetails = gymClass.split("\\*");
                        int availableSlots = Integer.parseInt(classDetails[6]);

                        // Increase available slots
                        availableSlots++;
                        classDetails[6] = Integer.toString(availableSlots);

                        // Update the gymClasses list
                        gymClasses.set(index, String.join("*", classDetails));

                        // Remove the canceled item from the cart
                        if (cartIndexToRemove != -1) {
                            cart.remove(cartIndexToRemove);
                        }
                        saveClass();

                    } else {
                        updatedLines.add(line); // Add lines that shouldn't be removed
                    }
                }

                // Save the updated lines back to booking.txt
                try (PrintWriter writer = new PrintWriter(new FileWriter("booking.txt"))) {
                    for (String updatedLine : updatedLines) {
                        writer.println(updatedLine);
                    }
                } catch (IOException e) {
                    System.out.println("Fail to write the updated data");
                    e.printStackTrace();
                }

                if (!classExist) {
                    System.out.println("Class not found in your bookings.");
                    FitnessPlus.input.nextLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

// Method to get user bookings
    public static List<String> getUserBookings(String currentUserPhone) {
        List<String> userBookings = new ArrayList<>();
        String fileName = "booking.txt";

        // Load and display the user's bookings from booking.txt
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\*");
                if (parts.length >= 2 && parts[0].equals(currentUserPhone)) {
                    userBookings.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userBookings;
    }

    // Method to save the cart to a file
    public static void saveCart(String currentUserPhone) {
        String fileName = "booking.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (String cartThing : cart) {
                writer.println(cartThing);
            }

            System.out.println("Cart saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load the cart from a file
    public static void loadCart(String currentUserPhone) {
        try (BufferedReader reader = new BufferedReader(new FileReader("booking.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                cart.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading data from file.");
            e.printStackTrace();
        }
    }

    // Implementation of the showHistory method from the HistoryShower interface
    public void showHistory(String currentUserPhone) {
        // Load booking history for the user with currentUserPhone
        List<String> bookingHistory = BookingHistory.loadBookingHistory(currentUserPhone);

        if (bookingHistory.isEmpty()) {
            System.out.println("No booking Cart found for the user.");
            System.out.print("Press Enter to continue...");
            FitnessPlus.input.nextLine();
        } else {
            System.out.println("Booking Cart by " + currentUserPhone);
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            String format = "| %-8s | %-10s | %-30s | %-15s | %-17s | %-15s | %-15s | %-30s | %-14s |%n";

            System.out.format(format, "Class ID", "Class Type", "Date and Time", "Instructor Name",
                    "Instructor Phone", "Class Duration", "Price", "Class Description", "Payment Status");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            for (String gymClass : bookingHistory) {
                String[] classDetails = gymClass.split("\\*");
                if (classDetails[10].equals("Pending")) {
                    System.out.format(format, classDetails[1], classDetails[2], classDetails[3], classDetails[4],
                            classDetails[5], classDetails[6] + " mins", "RM" + classDetails[8],
                            classDetails[9], classDetails[10]);
                }

            }
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            bookingHistory.clear();
        }
    }

    // Method to load gym classes from a file
    public static void loadClass() {
        try (BufferedReader reader = new BufferedReader(new FileReader("gym_classes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Booking.gymClasses.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save gym classes to a file
    public static void saveClass() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("gym_classes.txt"))) {
            for (String gymClass : Booking.gymClasses) {
                writer.write(gymClass);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to find the index of a gym class by ClassID
    public static int findClassIndex(String targetClassId) {
        for (int i = 0; i < Booking.gymClasses.size(); i++) {
            String[] classDetails = Booking.gymClasses.get(i).split("\\*");
            if (classDetails[0].equals(targetClassId)) {
                return i;
            }
        }
        return -1;
    }
}