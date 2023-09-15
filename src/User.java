import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class User extends Person{
    
    private int userAge;
    private String userSex;
    private String address;
    private double weight;
    private double height;
    private double wallet;

    // ArrayList to store registered users
    public static List<User> registeredUsers = new ArrayList<>();

    // Getter and Setter methods for all attributes
    public String getAddress() {
        return address;
    }

    public int getUserAge() {
        return userAge;
    }

    public String getUserSex() {
        return userSex;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public double getWallet() {
        return wallet;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setWallet(double wallet){
        this.wallet = wallet;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }
    
    public void setHeight(double height){
        this.height = height;
    }

    // Constructor
    public User(String phone, String username, String address, int userAge, String userSex,
                double weight, double height, String password, double wallet) {
        super(username, phone, password); 
        this.address = address;
        this.userAge = userAge;
        this.userSex = userSex;
        this.weight = weight;
        this.height = height;
        this.wallet = wallet;
    }

    public User(){
        super("", "", "");
    }
    
    public void userRegister() {
        User newUser;
    
        do {
            System.out.println("\n*====================REGISTER====================*");
            System.out.println("Enter [esc] at phone number or password to return");
    
            do {
                System.out.print("Enter your phone number: ");
                String phone = FitnessPlus.input.nextLine();
    
                if (phone.equalsIgnoreCase("esc")) {
                    return;
                }
    
                if (!phone.matches("\\d+")) {
                    System.out.println("Invalid phone number");
                    System.out.println("Press Enter to continue...");
                    FitnessPlus.input.nextLine();
                    continue;
                }
    
                boolean isExist = false;
    
                try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] userData = line.split(",");
                        String userPhone = userData[0]; // Assuming phone number is at index 0
                        if (phone.equals(userPhone)) {
                            isExist = true;
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error loading data from file.");
                    e.printStackTrace();
                }
    
                if (isExist) {
                    System.out.println("\nUser already exists");
                    System.out.println("Press Enter to continue...");
                    FitnessPlus.input.nextLine();
                    return;
                }
    
                boolean validName = true; // Separate variable for username validation
    
                do {
                    System.out.print("Enter your full name: ");
                    username = FitnessPlus.input.nextLine(); // Assign the input to the username variable
    
                    validName = true; // Reset the flag before each validation loop
    
                    if (username.trim().isEmpty()) {
                        System.out.println("Username cannot be all blanks");
                        System.out.println("Press Enter to continue...");
                        FitnessPlus.input.nextLine();
                        validName = false;
                        continue;
                    }
    
                    for (int i = 0; i < username.length(); i++) {
                        char c = username.charAt(i);
                        if (!Character.isAlphabetic(c) && c != ' ') {
                            validName = false;
                            break;
                        }
                    }
    
                    if (!validName) {
                        System.out.println("Username can contain only alphabets and blanks.");
                        System.out.println("Press Enter to continue...");
                        FitnessPlus.input.nextLine();
                    } else if (!Character.isAlphabetic(username.charAt(0))) {
                        System.out.println("The first character must be an alphabet.");
                        System.out.println("Press Enter to continue...");
                        FitnessPlus.input.nextLine();
                    }
                } while (!validName);
    
                System.out.print("Enter your address: ");
                String address = FitnessPlus.input.nextLine();
    
                do {
                    System.out.print("Enter your sex [M/F]: ");
                    userSex = FitnessPlus.input.nextLine().toUpperCase();
                    if (!userSex.equals("M") && !userSex.equals("F")) {
                        System.out.println("Invalid sex");
                        System.out.println("Press Enter to continue...");
                        FitnessPlus.input.nextLine();
                    }
                } while (!userSex.equals("M") && !userSex.equals("F"));
    
                do {
                    System.out.print("Enter your age: ");
                    if (FitnessPlus.input.hasNextInt()) {
                        userAge = FitnessPlus.input.nextInt();
                        FitnessPlus.input.nextLine();
                        break;
                    } else {
                        System.out.println("Invalid age. Please enter a valid integer.");
                        System.out.println("Press Enter to continue...");
                        FitnessPlus.input.nextLine();
                        FitnessPlus.input.nextLine();
                    }
                } while (true);
    
                do {
                    System.out.print("Enter your weight [KG]: ");
                    if (FitnessPlus.input.hasNextDouble()) {
                        weight = FitnessPlus.input.nextDouble();
                        FitnessPlus.input.nextLine();
                        break;
                    } else {
                        System.out.println("Invalid weight. Please enter a valid integer.");
                        System.out.println("Press Enter to continue...");
                        FitnessPlus.input.nextLine();
                        FitnessPlus.input.nextLine();
                    }
                } while (true);
    
                do {
                    System.out.print("Enter your height [CM]: ");
                    if (FitnessPlus.input.hasNextDouble()) {
                        height = FitnessPlus.input.nextDouble();
                        FitnessPlus.input.nextLine();
                        break;
                    } else {
                        System.out.println("Invalid height. Please enter a valid integer.");
                        System.out.println("Press Enter to continue...");
                        FitnessPlus.input.nextLine();
                        FitnessPlus.input.nextLine();
                    }
                } while (true);
    
                do {
                    System.out.print("Enter your password: ");
                    password = FitnessPlus.input.nextLine();
    
                    // Check for esc to return
                    if (password.equalsIgnoreCase("esc")) {
                        return;
                    }
                    if (password.length() < 6) {
                        System.out.println("Invalid password. Please enter at least 6 characters.");
                        System.out.println("Press Enter to continue...");
                        FitnessPlus.input.nextLine();
                    } else if (!password.matches("^[a-zA-Z0-9]+$")) {
                        System.out.println("Invalid password. Password can only contain letters and digits.");
                        System.out.println("Press Enter to continue...");
                        FitnessPlus.input.nextLine();
                    } else {
                        System.out.print("Enter your password again: ");
                        String password2 = FitnessPlus.input.nextLine();
    
                        // Check if passwords match
                        if (!password.equals(password2)) {
                            System.out.println("Passwords do not match");
                            System.out.println("Press Enter to continue...");
                            FitnessPlus.input.nextLine();
                        } else {
                            break; // Valid password, exit the loop
                        }
                    }
                } while (true);
    
                // Save user's information if no error
                newUser = new User(phone, username, address, userAge, userSex, weight, height, password, 0.0);
                registeredUsers.add(newUser);
                System.out.println("\nRegistered successfully");
                System.out.println("Press Enter to continue...");
                FitnessPlus.input.nextLine();
                try {
                    saveUser();
                } catch (IOException e) {
                    System.out.println("Error saving user data to file.");
                    e.printStackTrace();
                }
                return;
            } while (true);
        } while (true);
    }
    
    
    @Override
    public void login() {
        registeredUsers.clear();

        try {
            loadUser();
        }catch (IOException e) {
            System.out.println("Error to load the data from file.");
            e.printStackTrace();
            return;
        }
    
        do {
            System.out.println("\n\n*=====================LOGIN=====================*");
            System.out.println("Enter [esc] at phone number or password to return");
            System.out.print("Enter your phone number: ");
            String phone = FitnessPlus.input.nextLine();
    
            // Check for esc to return
            if (phone.equalsIgnoreCase("esc")) {
                return;
            }
    
            // Check if phone number is in the database
            boolean isValidUser = false;
            String currentUserPhone = null;
            try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] userData = line.split(",");
                    String userPhone = userData[0];
                    if (phone.equals(userPhone)) {
                        isValidUser = true;
                        currentUserPhone = userPhone;
                        break;
                    }
                }
            } 
            
            catch (IOException e) {
                System.out.println("Error to load the data from file.");
                e.printStackTrace();
            }
    
            if (!isValidUser) {
                System.out.println("Invalid phone number");
                System.out.println("Press Enter to continue...");
                FitnessPlus.input.nextLine();
                continue;
            }
    
            String correctPassword = null;
            try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] userData = line.split(",");
                    String userPhone = userData[0];
                    String userPassword = userData[7];
                    if (phone.equals(userPhone)) {
                        correctPassword = userPassword;
                        break;
                    }
                }
            } 
            
            catch (IOException e) {
                System.out.println("Error to load the data from file.");
                e.printStackTrace();
            }
    
            if (correctPassword == null) {
                System.out.println("Error reading password from file.");
                return; // Or handle the error as appropriate
            }
    
            do {
                System.out.print("Enter your password: ");
                String password = FitnessPlus.input.nextLine();
    
                // Check for esc to return
                if (password.equalsIgnoreCase("esc")) {
                    return;
                }
    
                if (!password.equals(correctPassword)) {
                    System.out.println("Incorrect password");
                    System.out.println("Press Enter to continue...");
                    FitnessPlus.input.nextLine();
                } 
                
                else {
                    System.out.println("User located");
                    System.out.println("Press Enter to continue...");
                    FitnessPlus.input.nextLine();
                    FitnessPlus.userMenu(currentUserPhone);
                    //
                    break;
                }
            } while (true);
            return;
        } while(true);
        
    }

    public void profile(User currentUser){
        String option;

        do{
            System.out.println("*========================================*");
            System.out.println("*                Profile                 *");
            System.out.println("*----------------------------------------*");
            System.out.println("  Name: " + currentUser.getUsername());
            System.out.println("  Age: " + currentUser.getUserAge());
            System.out.println("  Address: " + currentUser.getAddress());
            System.out.println("  Phone Number: " + currentUser.getPhone());
            System.out.println("  Weight [KG]: " + currentUser.getWeight());
            System.out.println("  Height [CM]: " + currentUser.getHeight());
            System.out.print("  BMI: ");
            currentUser.bmi(currentUser);
            System.out.println("  Wallet: " + currentUser.getWallet());
            System.out.println("\n  [1] Change Password                ");
            System.out.println("  [2] Top Up Money                     ");
            System.out.println("  [3] Change Height & Weight           ");
            System.out.println("  [4] Exit                             ");
            System.out.println("*========================================*");
            System.out.print("Enter option: ");
            option = FitnessPlus.input.nextLine();

            switch(option){
                case "1":
                    boolean passwordChanged = false;
                    do {
                        System.out.print("\nEnter old password: ");
                        String oldPassword = FitnessPlus.input.nextLine();
            
                        if (!oldPassword.equals(currentUser.getPassword())) {
                            System.out.println("Incorrect password");
                            System.out.println("Press Enter to continue...");
                            FitnessPlus.input.nextLine();
                            continue; // Start the loop over if the old password is incorrect
                        }
            
                        String newPassword;
                        do {
                            System.out.print("Enter new password: ");
                            newPassword = FitnessPlus.input.nextLine();
            
                            if (newPassword.equals(currentUser.getPassword())) {
                                System.out.println("The password is the same as the old password");
                            } else if (newPassword.length() < 6) {
                                System.out.println("Invalid password. Please enter at least 6 characters");
                            } else {
                                currentUser.setPassword(newPassword);
            
                                // Save updated data to file
                                try {
                                    saveUser();
                                    System.out.println("Your password has been changed successfully");
                                    passwordChanged = true; // Set the flag to true

                                } 
                                catch (IOException e) {
                                    System.out.println("Error saving data to file.");
                                    e.printStackTrace();
                                }
                            }
                        } while (!passwordChanged && (newPassword.equals(currentUser.getPassword()) || newPassword.length() < 6));
            
                        if (passwordChanged) {
                            break; // Exit the outer loop if the password was successfully changed
                        }
            
                        System.out.println("Press Enter to continue...");
                        FitnessPlus.input.nextLine();

                    } while (true);
                    break;

                case "2":
                    do {
                        double currentWallet = currentUser.getWallet();
                        System.out.println("\nCurrent Wallet: " + currentWallet);
            
                        do {
                            System.out.print("Enter an amount for top up: ");
                            String walletInput = FitnessPlus.input.next();
            
                            // Check if the input contains only digits
                            if (walletInput.matches("\\d+")) {
                                wallet = Double.parseDouble(walletInput);
            
                                if (wallet <= 0) {
                                    System.out.println("Amount must be greater than zero.");
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a valid number.\n");
                            }
                        } while (wallet <= 0);
            
                        currentUser.setWallet(currentWallet + wallet);
            
                        // Save updated data to file
                        try {
                            saveUser();
                            System.out.println("Wallet topped up successfully");
                            System.out.println("Press Enter to continue...");
                            FitnessPlus.input.nextLine();
                            FitnessPlus.input.nextLine();
                        } catch (IOException e) {
                            System.out.println("Error saving data to file.");
                            e.printStackTrace();
                        }
                        break;
                    } while (true);
                    break;

                case "3":
                    System.out.println("\nCurrent weight [KG]: " + currentUser.getWeight());
                    do {
                        System.out.print("Enter your new weight [KG]: ");
                        if (FitnessPlus.input.hasNextDouble()){
                            weight = FitnessPlus.input.nextDouble();
                            FitnessPlus.input.nextLine();
                            currentUser.setWeight(weight);
                            break;
                        } 
                        else {
                            System.out.println("Invalid weight. Please enter a valid integer.");
                            System.out.println("Press Enter to continue...");
                            FitnessPlus.input.nextLine();
                            FitnessPlus.input.nextLine();
                        }
                    } while (true);

                    System.out.println("\nCurrent height [CM]: " + currentUser.getHeight());
                    do {
                        System.out.print("Enter your new height [CM]: ");
                        if (FitnessPlus.input.hasNextDouble()){
                            height = FitnessPlus.input.nextDouble();
                            FitnessPlus.input.nextLine();
                            currentUser.setHeight(height);
                            break;
                        } 
                        else {
                            System.out.println("Invalid height. Please enter a valid integer.");
                            System.out.println("Press Enter to continue...");
                            FitnessPlus.input.nextLine();
                            FitnessPlus.input.nextLine();
                        }
                    } while (true);

                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option. Please try again!");
                    break;
            }  
        } while(option != "3");
    } 
    // To calculate the bmi of user
    public void bmi(User currentUser){

        double weight = currentUser.getWeight();
        double height = (currentUser.getHeight() / 100); // change cm to m
        double bmi = weight / Math.pow(height, 2);
        String formattedBMI = String.format("%.2f", bmi);


        if (bmi <= 18.4){
            System.out.println(formattedBMI + ". You are underweight");
        }

        else if (bmi >= 18.5 && bmi <= 24.9){
            System.out.println(formattedBMI + ". You are normal");
        }

        else if (bmi >= 25.0 && bmi <= 39.9){
            System.out.println(formattedBMI + ". You are overweight");
        }

        else{
            System.out.println(formattedBMI + ". You are obese");
        }
    }
    // To save the data of user into user.txt
    public void saveUser() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.txt"))) {
            for (User user : registeredUsers) {
                writer.println(
                               user.getPhone() + "," +
                               user.getUsername() + "," +
                               user.getAddress() + "," +
                               user.getUserAge() + "," +
                               user.getUserSex() + "," +
                               user.getWeight() + "," +
                               user.getHeight() + "," +
                               user.getPassword() + "," +
                               user.getWallet());             
            }
            writer.close();
        } 
        
        catch (IOException e) {
            System.out.println("Error saving data to file.");
            throw e;
        }
    }
    // To load the data of user to the registeredUsers arraylist from user.txt
    public void loadUser() throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 9) {
                    phone = data[0];
                    username = data[1];
                    address = data[2];
                    userAge = Integer.parseInt(data[3]);
                    userSex = data[4];
                    weight = Double.parseDouble(data[5]);
                    height = Double.parseDouble(data[6]);
                    password = data[7];
                    wallet = Double.parseDouble(data[8]);
    
                    User newUser = new User(phone, username, address, userAge, userSex, weight, height, password, wallet);
                    registeredUsers.add(newUser);
                }
            }
        } 
        
        catch (IOException e) {
            System.out.println("Error loading data from file.");
            e.printStackTrace();
        }
    }
}
