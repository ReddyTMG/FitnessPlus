import java.util.Scanner;
import java.io.IOException;
public class FitnessPlus {
    
    public static Scanner input = new Scanner(System.in);
    public static void main(String [] args){
        User newUser = new User();
        String option;
        
                try {
                    newUser.loadUser();
                } 
                
                catch (IOException e) {
                    System.out.println("Error to load the data from file.");
                    e.printStackTrace();
                    return;
                }
        do {
            System.out.print("""
                \n
                *========================================*
                *   Welcome to Fitness+ Booking System   *
                *----------------------------------------*
                *   [1] User                             *
                *   [2] Admin                            *
                *   [3] Exit                             *
                *========================================*
                Enter option:\s""");
            option = input.nextLine();

            switch (option) {
                case "1" -> userInterface();
                case "2" -> adminInterface();
                case "3" -> System.exit(0);
                default -> System.out.println("Invalid option. Please try again!");
            }
        } while (option != "3");
        input.close();
    }

    public static void userInterface(){
        User newUser = new User();
        String option;

        do {
            System.out.print("""
                \n
                *========================================*
                *   Welcome to Fitness+ Booking System   *
                *----------------------------------------*
                *   [1] Login                            *
                *   [2] Register                         *
                *   [3] Esc                              *
                *========================================*
                Enter option:\s""");
            option = input.nextLine();

            switch (option) {
                case "1" -> newUser.login();
                case "2" -> newUser.userRegister();
                case "3" -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again!");
            }
        } while (option != "3");
        input.close();
    }

    public static void adminInterface(){
        Administrator newAdmin = new Administrator();
        String option;
 
        do {
            System.out.print("""
                \n
                *========================================*
                *   Welcome to Fitness+ Booking System   *
                *----------------------------------------*
                *   [1] Login                            *
                *   [2] Esc                              *
                *========================================*
                Enter option:\s""");
            option = input.nextLine();

            switch (option) {
                case "1" -> newAdmin.login();
                case "2" -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again!");
            }
        } while (!option.equals("3"));
        input.close();
    }

    public static void userMenu(String currentUserPhone){
        String option;
        BookingHistory history = new BookingHistory();
        // Find the current user by phone number
        User currentUser = null;
        for (User user : User.registeredUsers) {
            if (user.getPhone().equals(currentUserPhone)) {
                currentUser = user;       
                break; // Exit the loop once the user is found
            }
        }

        do {
            System.out.print("""
                \n
                *========================================*
                *   User Menu                            *
                *----------------------------------------*
                *   [1] Booking                          *
                *   [2] Cancel Booking                   *
                *   [3] Booking History                  *
                *   [4] Payment                          *
                *   [5] Profile                          *
                *   [6] Exit                             *
                *========================================*
                Enter option:\s""");
            option = input.nextLine();

            switch (option) {
                case "1" -> Booking.booking(currentUserPhone);
                case "2" -> Booking.cancelBooking(currentUserPhone);
                case "3" -> history.showHistory(currentUserPhone);
                case "4" -> Payment.payment(currentUser);
                case "5" -> currentUser.profile(currentUser);
                case "6" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid input. Please try again");
            }
        } while (true);
    }

    public static void adminMenu() {
        Booking.loadClass(); //This reload the Gym classes from the file
        int choice;
        do {
            System.out.print("""
                    \n
                    *========================================*
                    *   Admin Menu                           *
                    *----------------------------------------*
                    *   [1] Display classes                  *
                    *   [2] Add class                        *
                    *   [3] Edit class                       *
                    *   [4] Remove class                     *
                    *   [5] Exit                             *
                    *========================================*
                    Enter option:\s""");
            choice = input.nextInt();
            input.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> Administrator.displayClass(Booking.gymClasses);
                case 2 -> Administrator.addClass(input);
                case 3 -> Administrator.editClass(input);
                case 4 -> Administrator.removeClass(input);
                case 5 -> {
                    Booking.saveClass();
                    Booking.gymClasses.clear();
                    System.out.println("Exiting...");
                    return;
                }
                default -> {
                    System.out.println("Invalid choice, please try again.");
                    input.nextLine();
                }
            }
        } while (choice != 5);
    }
}