import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class LibraryManagementSystem {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null; // Current logged-in user

    public static void main(String[] args) {
        // Add some sample books and users
        initializeLibrary();
        
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                displayLoginMenu();
                int choice = getUserChoice();
                
                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    case 3:
                        System.out.println("Thank you for using the Library Management System. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid selection, please try again.");
                }
            } else {
                // Display different menus based on user role
                if (currentUser.isAdmin()) {
                    displayAdminMenu();
                } else {
                    displayUserMenu();
                }
                
                int choice = getUserChoice();
                
                if (currentUser.isAdmin()) {
                    running = handleAdminChoice(choice);
                } else {
                    running = handleUserChoice(choice);
                }
            }
            
            // Pause after each operation
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void displayLoginMenu() {
        System.out.println("\n===== Library Management System - Login =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit System");
        System.out.print("Please select an option (1-3): ");
    }
    
    // Display admin menu
    private static void displayAdminMenu() {
        System.out.println("\n===== Library Management System - Admin Menu =====");
        System.out.println("Current user: " + currentUser.getName() + " (Admin)");
        System.out.println("1. Add Book");
        System.out.println("2. Remove Book");
        System.out.println("3. Search Book");
        System.out.println("4. Display All Books");
        System.out.println("5. Tag Management");
        System.out.println("6. View All Users");
        System.out.println("7. Logout");
        System.out.println("8. Exit System");
        System.out.print("Please select an option (1-8): ");
    }
    
    // Display regular user menu
    private static void displayUserMenu() {
        System.out.println("\n===== Library Management System - User Menu =====");
        System.out.println("Current user: " + currentUser.getName());
        System.out.println("1. Search Book");
        System.out.println("2. Display All Books");
        System.out.println("3. Borrow Book");
        System.out.println("4. Return Book");
        System.out.println("5. View Borrow History");
        System.out.println("6. Get Book Recommendations");
        System.out.println("7. Logout");
        System.out.println("8. Exit System");
        System.out.print("Please select an option (1-8): ");
    }
    
    // Handle admin menu choices
    private static boolean handleAdminChoice(int choice) {
        switch (choice) {
            case 1:
                addBook();
                break;
            case 2:
                removeBook();
                break;
            case 3:
                searchBook();
                break;
            case 4:
                library.displayAllBooks();
                break;
            case 5:
                manageBookTags();
                break;
            case 6:
                displayAllUsers();
                break;
            case 7:
                currentUser = null; // Logout
                System.out.println("Successfully logged out.");
                break;
            case 8:
                System.out.println("Thank you for using the Library Management System. Goodbye!");
                return false;
            default:
                System.out.println("Invalid selection!");
        }
        return true;
    }
    
    // Handle regular user menu choices
    private static boolean handleUserChoice(int choice) {
        switch (choice) {
            case 1:
                searchBook();
                break;
            case 2:
                library.displayAllBooks();
                break;
            case 3:
                borrowBook();
                break;
            case 4:
                returnBook();
                break;
            case 5:
                viewBorrowHistory();
                break;
            case 6:
                getBookRecommendations();
                break;
            case 7:
                currentUser = null; // Logout
                System.out.println("Successfully logged out.");
                break;
            case 8:
                System.out.println("Thank you for using the Library Management System. Goodbye!");
                return false;
            default:
                System.out.println("Invalid selection!");
        }
        return true;
    }
    
    // Add method to display all users (admin only)
    private static void displayAllUsers() {
        System.out.println("\n===== System User List =====");
        List<User> users = library.getAllUsers();
        
        if (users.isEmpty()) {
            System.out.println("There are no users in the system.");
            return;
        }
        
        System.out.println("All users in the system:");
        for (User user : users) {
            System.out.println(user);
        }
    }
    
    private static int getUserChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return choice;
        } catch (NumberFormatException e) {
            return -1; // Return invalid selection
        }
    }
    
    private static void addBook() {
        System.out.println("\n===== Add New Book =====");
        
        System.out.print("Please enter book ID: ");
        String id = scanner.nextLine();
        
        // Check if ID already exists
        if (library.findBookById(id) != null) {
            System.out.println("Error: Book with ID " + id + " already exists!");
            return;
        }
        
        System.out.print("Please enter book title: ");
        String title = scanner.nextLine();
        
        System.out.print("Please enter book author: ");
        String author = scanner.nextLine();
        
        System.out.print("Please enter book category: ");
        String category = scanner.nextLine();
        
        Book newBook = new Book(id, title, author, category);
        library.addBook(newBook);
    }
    
    private static void removeBook() {
        System.out.println("\n===== Delete Book =====");
        System.out.print("Please enter the ID of the book to delete: ");
        String id = scanner.nextLine();
        library.removeBook(id);
    }
    
    private static void searchBook() {
        boolean searching = true;
        
        while (searching) {
            System.out.println("\n===== Search Book =====");
            System.out.println("1. Search by ID");
            System.out.println("2. Search by Title");
            System.out.println("3. Search by Author");
            System.out.println("4. Search by Tag");
            System.out.println("5. Return to Main Menu");
            System.out.print("Please select search method (1-5): ");
            
            int searchChoice = getUserChoice();
            
            if (searchChoice == 5) {
                searching = false;
                continue;
            }
            
            List<Book> results = null;
            boolean searchSuccess = false;
            
            switch (searchChoice) {
                case 1:
                    System.out.print("Please enter book ID: ");
                    String id = scanner.nextLine();
                    Book book = library.findBookById(id);
                    if (book != null) {
                        System.out.println("Found book:");
                        System.out.println(book);
                        searchSuccess = true;
                    } else {
                        System.out.println("No book found with ID " + id);
                    }
                    break;
                    
                case 2:
                    System.out.print("Please enter book title: ");
                    String title = scanner.nextLine();
                    results = library.findBooksByTitle(title);
                    searchSuccess = displaySearchResults(results, "Title contains '" + title + "'");
                    break;
                    
                case 3:
                    System.out.print("Please enter book author: ");
                    String author = scanner.nextLine();
                    results = library.findBooksByAuthor(author);
                    searchSuccess = displaySearchResults(results, "Author contains '" + author + "'");
                    break;
                    
                case 4:
                    System.out.print("Please enter book tag: ");
                    String tag = scanner.nextLine();
                    results = library.findBooksByTag(tag);
                    searchSuccess = displaySearchResults(results, "Tag is '" + tag + "'");
                    break;
                    
                default:
                    System.out.println("Invalid selection!");
            }
            
            // If search fails, ask user if they want to try again
            if (!searchSuccess && searchChoice >= 1 && searchChoice <= 4) {
                System.out.print("\nDo you want to change search criteria and try again? (Y/N): ");
                String choice = scanner.nextLine().trim().toUpperCase();
                if (!choice.startsWith("Y")) {
                    searching = false;
                }
            } else if (searchSuccess) {
                // Search successful, ask if they want to continue searching
                System.out.print("\nDo you want to continue searching for other books? (Y/N): ");
                String choice = scanner.nextLine().trim().toUpperCase();
                if (!choice.startsWith("Y")) {
                    searching = false;
                }
            }
        }
    }
    
    // Modify displaySearchResults method to return boolean indicating whether results were found
    private static boolean displaySearchResults(List<Book> results, String criteria) {
        if (results.isEmpty()) {
            System.out.println("No books found " + criteria + ".");
            return false;
        } else {
            System.out.println("Found " + results.size() + " books " + criteria + ":");
            for (Book book : results) {
                System.out.println(book);
            }
            return true;
        }
    }
    
    private static void borrowBook() {
        System.out.println("\n===== Borrow Book =====");
        System.out.print("Please enter the ID of the book to borrow: ");
        String bookId = scanner.nextLine();
        
        library.borrowBook(currentUser.getId(), bookId);
    }
    
    private static void returnBook() {
        System.out.println("\n===== Return Book =====");
        System.out.print("Please enter the ID of the book to return: ");
        String bookId = scanner.nextLine();
        
        library.returnBook(currentUser.getId(), bookId);
    }
    
    private static void manageBookTags() {
        System.out.println("\n===== Tag Management =====");
        System.out.print("Please enter book ID: ");
        String id = scanner.nextLine();
        
        Book book = library.findBookById(id);
        if (book == null) {
            System.out.println("No book found with ID " + id);
            return;
        }
        
        boolean managing = true;
        while (managing) {
            System.out.println("\nBook Information: " + book);
            
            Set<String> tags = book.getTags();
            System.out.println("\nCurrent Tags: " + (tags.isEmpty() ? "None" : String.join(", ", tags)));
            
            System.out.println("\n1. Add Tag");
            System.out.println("2. Remove Tag");
            System.out.println("3. Return to Main Menu");
            System.out.print("Please select an option (1-3): ");
            
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    System.out.print("Please enter tags to add (separate multiple tags with commas): ");
                    String tagsToAdd = scanner.nextLine();
                    String[] tagArray = tagsToAdd.split(",");
                    for (String tag : tagArray) {
                        book.addTag(tag);
                        System.out.println("Added tag: " + tag.trim());
                    }
                    break;
                    
                case 2:
                    if (tags.isEmpty()) {
                        System.out.println("No tags to remove.");
                        break;
                    }
                    
                    System.out.print("Please enter tag to remove: ");
                    String tagToRemove = scanner.nextLine();
                    if (book.hasTag(tagToRemove)) {
                        book.removeTag(tagToRemove);
                        System.out.println("Removed tag: " + tagToRemove);
                    } else {
                        System.out.println("No tag: " + tagToRemove);
                    }
                    break;
                    
                case 3:
                    managing = false;
                    break;
                    
                default:
                    System.out.println("Invalid selection!");
            }
        }
    }
    
    private static void viewBorrowHistory() {
        System.out.println("\n===== Borrow History =====");
        List<BorrowRecord> history = library.getUserBorrowHistory(currentUser.getId());
        
        if (history.isEmpty()) {
            System.out.println("You have no borrow history.");
            return;
        }
        
        System.out.println("Your borrow history:");
        for (BorrowRecord record : history) {
            Book book = library.findBookById(record.getBookId());
            String bookTitle = book != null ? book.getTitle() : "Unknown Book";
            
            System.out.println("Book: " + bookTitle + 
                              ", Borrow Date: " + record.getBorrowDate() + 
                              (record.isReturned() ? ", Return Date: " + record.getReturnDate() : ", Status: Not Returned"));
        }
    }
    
    private static void getBookRecommendations() {
        System.out.println("\n===== Book Recommendations =====");
        List<Book> recommendations = library.recommendBooks(currentUser.getId());
        
        if (recommendations.isEmpty()) {
            System.out.println("No book recommendations available. Please borrow some books first.");
            return;
        }
        
        System.out.println("Based on your borrow history, here are book recommendations for you:");
        for (int i = 0; i < recommendations.size(); i++) {
            Book book = recommendations.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + " - " + book.getAuthor() + 
                              " [Tags: " + String.join(", ", book.getTags()) + "]");
        }
    }
    
    private static void initializeLibrary() {
        // Add sample books
        Book book1 = new Book("B001", "Java Programming Thought", "Bruce Eckel", "Programming");
        book1.addTag("Programming");
        book1.addTag("java");
        book1.addTag("Classic");
        library.addBook(book1);
        
        Book book2 = new Book("B002", "Introduction to Algorithms", "Thomas H. Cormen", "Computer Science");
        book2.addTag("Algorithm");
        book2.addTag("Computer Science");
        book2.addTag("Classic");
        library.addBook(book2);
        
        Book book3 = new Book("B003", "The Three-Body Problem", "Liu Cixin", "Science Fiction");
        book3.addTag("Science Fiction");
        book3.addTag("Chinese Literature");
        book3.addTag("Award-winning Work");
        library.addBook(book3);
        
        Book book4 = new Book("B004", "Alive", "Yu Hua", "Literature");
        book4.addTag("Chinese Literature");
        book4.addTag("Contemporary Literature");
        library.addBook(book4);
        
        Book book5 = new Book("B005", "Design Patterns", "Erich Gamma", "Software Engineering");
        book5.addTag("Programming");
        book5.addTag("Design");
        book5.addTag("Software Engineering");
        library.addBook(book5);

        // Add users, and set the first user as admin
        library.addUser(new User("U001", "Zhang San", "password1", true)); // Admin
        library.addUser(new User("U002", "Li Si", "password2")); // Regular user
        library.addUser(new User("U003", "Wang Wu", "password3")); // Regular user
    }

    private static void login() {
        System.out.println("\n===== User Login =====");
        System.out.print("Please enter user ID: ");
        String userId = scanner.nextLine();
        
        User user = library.getUser(userId);
        if (user != null) {
            System.out.print("Please enter password: ");
            String password = scanner.nextLine();
            
            if (user.checkPassword(password)) {
                currentUser = user;
                System.out.println("Login successful! Welcome back, " + user.getName() + 
                                  (user.isAdmin() ? " (Admin)" : "") + ".");
            } else {
                System.out.println("Incorrect password, please try again.");
            }
        } else {
            System.out.println("User ID does not exist, please register or check input.");
        }
    }

    private static void register() {
        System.out.println("\n===== User Registration =====");
        System.out.print("Please enter user ID: ");
        String userId = scanner.nextLine();
        
        if (library.getUser(userId) != null) {
            System.out.println("That user ID already exists, please choose another ID.");
            return;
        }
        
        System.out.print("Please enter user name: ");
        String userName = scanner.nextLine();
        
        System.out.print("Please enter password: ");
        String password = scanner.nextLine();
        
        System.out.print("Please enter password again: ");
        String confirmPassword = scanner.nextLine();
        
        if (!password.equals(confirmPassword)) {
            System.out.println("The passwords do not match, registration failed.");
            return;
        }
        
        // New registered user defaults to regular user
        User newUser = new User(userId, userName, password, false);
        library.addUser(newUser);
        currentUser = newUser;
        System.out.println("Registration successful! Welcome, " + userName + ".");
    }
}