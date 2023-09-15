import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Administrator extends Person {
    private String adminId;
    public int adminCount = 0;

    // ArrayList to store registered users
    public static final List<Administrator> registeredAdmin = new ArrayList<>();
    
    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId){
        this.adminId = adminId;
    }

    public Administrator() {
        super("", "", "");
    }

    public Administrator(String adminId, String username, String phone, String password) {
        super(username, phone, password);
        this.adminId = adminId;
    }


    @Override
    public void login() {
        // load the admin data to registeredAdmin arraylist from admin.txt
        try {
            loadAdmin();
        } catch (IOException e) {
            System.out.println("Error to load the data from admin file.");
            e.printStackTrace();
            return;
        }

        do {
            System.out.println("*====================LOGIN====================*");
            System.out.println("Enter [esc] at phone number or password to return");
            System.out.print("Enter your phone number: ");
            String phone = FitnessPlus.input.nextLine();

            // Check for esc to return
            if (phone.equalsIgnoreCase("esc")) {
                return;
            }

            // Check if phone number is in the database
            int adminIndex = -1;
            for (int i = 0; i <registeredAdmin.size(); i++) {
                if (phone.equals(registeredAdmin.get(i).getPhone())) {
                    adminIndex = i;
                    break;
                }
            }
            // phone number not in the database
            if (adminIndex == -1) {
                System.out.println("Invalid phone number");
                System.out.println("Press Enter to continue...");
                FitnessPlus.input.nextLine();
                continue;
            }

            do{
                System.out.print("Enter your password: ");
                String password = FitnessPlus.input.nextLine();

                // Check for esc to return
                if (password.equalsIgnoreCase("esc")) {
                    return;
                }

                // to check if the admin enter the same password both time
                if (!password.equals(registeredAdmin.get(adminIndex).getPassword())) {
                    System.out.println("Incorrect password");
                    System.out.println("Press Enter to continue...");
                    FitnessPlus.input.nextLine();
                    continue;
                }
                
                System.out.println("Admin located");
                System.out.println("Press Enter to continue...");
                FitnessPlus.input.nextLine();
                FitnessPlus.adminMenu();
                break;
            } while (true);
            break;
        } while (true);
    }

    //To display the class ordered by the format
    public static void displayClass(List<String> classes) {
        System.out.println("List of gym classes:");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        String format = "| %-8s | %-10s | %-30s | %-15s | %-17s | %-15s | %-15s | %-8s | %-30s |%n";
    
        System.out.format(format, "Class ID", "Class Type", "Date and Time", "Instructor Name",
                "Instructor Phone", "Class Duration", "Slot Available", "Price", "Class Description");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    
        for (String gymClass : classes) {
            String[] classDetails = gymClass.split("\\*");
            System.out.format(format, classDetails[0], classDetails[1], classDetails[2], classDetails[3],
                    classDetails[4], classDetails[5] + " mins", classDetails[6], "RM" + classDetails[7],
                    classDetails[8]);
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    //This function allow the admin to add a class
    public static void addClass(Scanner GetData) {
        System.out.println("Adding a new class");

        String classId = null;
        boolean classValid = false;
        while(!classValid){
            boolean classExist = false;

            System.out.print("Enter Class ID OR type 'esc' to return: ");
            classId = GetData.nextLine();

            if (classId.equalsIgnoreCase("esc")) {
                return; // Return to the main menu
            }

            // Check if the input matches the class id pattern
            if(classId.isEmpty() || classId.isBlank()){
                System.out.println("Invalid class id. Class id can't be blanks.");
                System.out.println("Press Enter to continue...");
                GetData.nextLine();
            }else if(!Pattern.matches("[a-zA-Z0-9]+", classId)){
                System.out.println("Invalid class id. Please enter a class id only consist of letter and number.");
                System.out.println("Press Enter to continue...");
                GetData.nextLine();
            }else{
                // To avoid the overlap of class id
                for(String gymClass : Booking.gymClasses){
                    String[] classDetails = gymClass.split("\\*");
                    if(classDetails[0].equals(classId)){
                        System.out.println("This class id already exists.");
                        System.out.println("Press Enter to continue...");
                        GetData.nextLine();
                        classExist = true;
                    }
                }
            }
            if(!classExist){
                classValid = true;
            }
            
        }

        String classType = null;
        while(true){

            System.out.print("Enter Class Type: ");
            classType = GetData.nextLine();
            // Check if the input matches the class type pattern
            if(classType.isEmpty() || classType.isBlank()){
                System.out.println("Invalid class id. Class Type can't be blanks.");
            }else if(!Pattern.matches("[a-zA-Z]+", classType)){
                System.out.println("Invalid class id. Please enter a class id only consist of letter.");
            }else{
                break;
            }
        }

        LocalDateTime dateTime = TimeFormat.getDateTimeInput(GetData);

        System.out.print("Enter Instructor Name: ");
        String instructorName = GetData.nextLine();

        String instructorPhone = null;
        while (true) {
            System.out.print("Enter Instructor Phone (01********): ");
            instructorPhone = GetData.nextLine();

            // Check if the input matches the phone number pattern
            if (Pattern.matches("^01[0-9]{8,9}$", instructorPhone)) {
                break; // Exit the loop if the input is valid
            } else {
                System.out.println("Invalid phone number. Please enter a 10 to 11 digits phone number.");
            }
        }

        int classDuration = 0;
        while (true) {
            try {
                System.out.print("Enter Class Duration(mins): ");
                classDuration = Integer.parseInt(GetData.nextLine());
                if (classDuration > 0) {
                    break;
                } else {
                    System.out.println("Please enter a positive integer for class duration.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for class duration.");
            }
        }

        int SlotAvailable = 0;
        while (true) {
            try {
                System.out.print("Enter Slot Available: ");
                SlotAvailable = Integer.parseInt(GetData.nextLine());
                if (SlotAvailable >= 0) {
                    break;
                } else {
                    System.out.println("Please enter a non-negative integer for slot available.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for slot available.");
            }
        }

        double classPrice = 0;
        while (true) {
            try {
                System.out.print("Enter Price: ");
                classPrice = Integer.parseInt(GetData.nextLine());
                if (classPrice >= 0) {
                    break;
                } else {
                    System.out.println("Please enter a non-negative price.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid price.");
            }
        }

        System.out.print("Enter Class Description: ");
        String classDesc = GetData.nextLine();

        String newClass = String.format(
                "%s*%s*%s*%s*%s*%d*%d*%.2f*%s",
                classId, classType, TimeFormat.formatDateTime(dateTime), instructorName,
                instructorPhone, classDuration, SlotAvailable, classPrice, classDesc
        );

        Booking.gymClasses.add(newClass);
        System.out.println("Class added successfully!");
        Booking.saveClass();
    }

    // This function allows admin to edit the class
    public static void editClass(Scanner GetData) {
        while (true) {
            System.out.println("Editing a class");

            System.out.print("Enter the Class ID to edit OR Type 'esc' to return: ");
            String targetClassId = GetData.nextLine();

            if (targetClassId.equalsIgnoreCase("esc")) {
                return; // Return to the main menu
            }

            int index = Booking.findClassIndex(targetClassId);
            if (index == -1) {
                System.out.println("Class not found.");
                GetData.nextLine();
            } else {
                // Display the Content before the Editing
                System.out.println("Current details:");
                List<String> DisplayEditClass = Collections.singletonList(Booking.gymClasses.get(index));
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                String format = "| %-8s | %-10s | %-30s | %-15s | %-17s | %-15s | %-15s | %-8s | %-30s |%n";

                System.out.format(format, "Class ID", "Class Type", "Date and Time", "Instructor Name",
                        "Instructor Phone", "Class Duration", "Slot Available", "Price", "Class Description");
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

                for (String gymClass : DisplayEditClass) {
                    String[] classDetails = gymClass.split("\\*");
                    System.out.format(format, classDetails[0], classDetails[1], classDetails[2], classDetails[3],
                            classDetails[4], classDetails[5] + " mins", classDetails[6], "RM" + classDetails[7],
                            classDetails[8]);
                }
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.print("Press Enter to continue...");
                FitnessPlus.input.nextLine();

                String newClassType = null;
                while (true) {
                    System.out.print("Enter new Class Type: ");
                    newClassType = GetData.nextLine();
                    // Check if the input matches the class type pattern
                    if (newClassType.isEmpty() || newClassType.isBlank()) {
                        System.out.println("Invalid class type. Class Type can't be blank.");
                    } else if (!Pattern.matches("[a-zA-Z]+", newClassType)) {
                        System.out.println("Invalid class type. Please enter a class type consisting of letters only.");
                    } else {
                        break;
                    }
                }

                LocalDateTime newDateTime = TimeFormat.getDateTimeInput(GetData);

                System.out.print("Enter new Instructor Name: ");
                String newInstructorName = GetData.nextLine();

                String newInstructorPhone = null;
                while (true) {
                    System.out.print("Enter Instructor Phone (01********): ");
                    newInstructorPhone = GetData.nextLine();

                    // Check if the input matches the phone number pattern
                    if (Pattern.matches("^01[0-9]{8,9}$", newInstructorPhone)) {
                        break; // Exit the loop if the input is valid
                    } else {
                        System.out.println("Invalid phone number. Please enter a 10 to 11 digits phone number.");
                    }
                }

                int newClassDuration = 0;
                while (true) {
                    try {
                        System.out.print("Enter class duration (mins): ");
                        newClassDuration = Integer.parseInt(GetData.nextLine());
                        if (newClassDuration >= 0) {
                            break;
                        } else {
                            System.out.println("Please enter a non-negative integer for the class duration.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid integer for the class duration.");
                    }
                }

                int newSlotAvailable = 0;
                while (true) {
                    try {
                        System.out.print("Enter Slot Available: ");
                        newSlotAvailable = Integer.parseInt(GetData.nextLine());
                        if (newSlotAvailable >= 0) {
                            break;
                        } else {
                            System.out.println("Please enter a non-negative integer for slot available.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid integer for slot available.");
                    }
                }

                double newClassPrice = 0;
                while (true) {
                    try {
                        System.out.print("Enter Price: ");
                        newClassPrice = Double.parseDouble(GetData.nextLine());
                        if (newClassPrice >= 0) {
                            break;
                        } else {
                            System.out.println("Please enter a non-negative price.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid price.");
                    }
                }

                GetData.nextLine();
                System.out.print("Enter new Class Description: ");
                String newClassDesc = GetData.nextLine();

                String editedClass = String.format(
                        "%s*%s*%s*%s*%s*%d*%d*%.2f*%s",
                        targetClassId, newClassType, TimeFormat.formatDateTime(newDateTime), newInstructorName,
                        newInstructorPhone, newClassDuration, newSlotAvailable, newClassPrice, newClassDesc
                );

                Booking.gymClasses.set(index, editedClass);
                System.out.println("Class edited successfully!");
                Booking.saveClass();
            }
        }
    }

    //This function allow the admin to remove a class
    public static void removeClass(Scanner GetData) {
        System.out.println("Removing a class");

        System.out.print("Enter the Class ID to remove: ");
        String targetClassId = GetData.nextLine();

        if(targetClassId.equals("esc")){
            return;
        }
        int index = Booking.findClassIndex(targetClassId);
        if (index == -1) {
            System.out.println("Class not found.");
            return;
        }

        System.out.println("Are you sure you want to remove this class? (Y/N): ");
        String confirmation = GetData.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {
            Booking.gymClasses.remove(index);
            System.out.println("Class removed successfully!");
            Booking.saveClass();
        } else {
            System.out.println("Class removal cancelled.");
        }
    }

    public static void loadAdmin() throws IOException {
        Administrator newAdmin = new Administrator();

        try (BufferedReader reader = new BufferedReader(new FileReader("admin.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\*");
                if (data.length == 4) {
                    newAdmin.setAdminId(data[0]);
                    newAdmin.setPhone(data[1]);
                    newAdmin.setUsername(data[2]);
                    newAdmin.setPassword(data[3]);

                    Administrator.registeredAdmin.add(newAdmin);
                    newAdmin.adminCount++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading data from file.");
            e.printStackTrace();

        }
    }
}


