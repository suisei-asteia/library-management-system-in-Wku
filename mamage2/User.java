import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String password;
    private List<String> borrowedBookIds;
    private boolean isAdmin; // Add role field, true for admin, false for regular user
    
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.borrowedBookIds = new ArrayList<>();
        this.isAdmin = false; // Default to regular user
    }
    
    // Constructor with role parameter
    public User(String id, String name, String password, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.borrowedBookIds = new ArrayList<>();
        this.isAdmin = isAdmin;
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public List<String> getBorrowedBookIds() {
        return new ArrayList<>(borrowedBookIds);
    }
    
    // Add role-related getter
    public boolean isAdmin() {
        return isAdmin;
    }
    
    // Verify password
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    // Add borrowed book
    public void borrowBook(String bookId) {
        if (!borrowedBookIds.contains(bookId)) {
            borrowedBookIds.add(bookId);
        }
    }
    
    // Return book
    public void returnBook(String bookId) {
        borrowedBookIds.remove(bookId);
    }
    
    // Check if a book is borrowed
    public boolean hasBorrowed(String bookId) {
        return borrowedBookIds.contains(bookId);
    }
    
    @Override
    public String toString() {
        return "User ID: " + id + ", Name: " + name + 
               ", Role: " + (isAdmin ? "Admin" : "Regular User") + 
               ", Current Borrowed Books: " + borrowedBookIds.size();
    }
}