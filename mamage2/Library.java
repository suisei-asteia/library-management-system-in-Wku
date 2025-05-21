import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Library {
    private List<Book> books;
    private Map<String, User> users; // User ID -> User object
    private List<BorrowRecord> borrowRecords; // All borrowing records
    
    public Library() {
        this.books = new ArrayList<>();
        this.users = new HashMap<>();
        this.borrowRecords = new ArrayList<>();
    }
    
    // Add book
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book '" + book.getTitle() + "' has been added to the library.");
    }
    
    // Remove book
    public boolean removeBook(String id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(id)) {
                Book removedBook = books.remove(i);
                System.out.println("Book '" + removedBook.getTitle() + "' has been removed from the library.");
                return true;
            }
        }
        System.out.println("Book with ID " + id + " not found.");
        return false;
    }
    
    // Add user
    public void addUser(User user) {
        users.put(user.getId(), user);
        System.out.println("User " + user.getName() + " has been added to the system.");
    }
    
    // Get user
    public User getUser(String userId) {
        return users.get(userId);
    }
    
    // Get all users
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    // Borrow book
    public boolean borrowBook(String userId, String bookId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("User with ID " + userId + " not found.");
            return false;
        }
        
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("Book with ID " + bookId + " not found.");
            return false;
        }
        
        if (!book.isAvailable()) {
            System.out.println("Book '" + book.getTitle() + "' is already borrowed and cannot be borrowed.");
            return false;
        }
        
        // Update book status
        book.setAvailable(false);
        
        // Update user's borrowing record
        user.borrowBook(bookId);
        
        // Create borrowing record
        BorrowRecord record = new BorrowRecord(userId, bookId);
        borrowRecords.add(record);
        
        System.out.println("User " + user.getName() + " has successfully borrowed book '" + book.getTitle() + "'.");
        return true;
    }
    
    // Return book
    public boolean returnBook(String userId, String bookId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("User with ID " + userId + " not found.");
            return false;
        }
        
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("Book with ID " + bookId + " not found.");
            return false;
        }
        
        if (book.isAvailable()) {
            System.out.println("Book '" + book.getTitle() + "' is not borrowed, no need to return.");
            return false;
        }
        
        if (!user.hasBorrowed(bookId)) {
            System.out.println("User " + user.getName() + " has not borrowed this book.");
            return false;
        }
        
        // Update book status
        book.setAvailable(true);
        
        // Update user's borrowing record
        user.returnBook(bookId);
        
        // Update borrowing record
        for (BorrowRecord record : borrowRecords) {
            if (record.getUserId().equals(userId) && 
                record.getBookId().equals(bookId) && 
                !record.isReturned()) {
                record.setReturnDate(new Date());
                break;
            }
        }
        
        System.out.println("User " + user.getName() + " has successfully returned book '" + book.getTitle() + "'.");
        return true;
    }
    
    // Get user's borrowing history
    public List<BorrowRecord> getUserBorrowHistory(String userId) {
        List<BorrowRecord> userRecords = new ArrayList<>();
        for (BorrowRecord record : borrowRecords) {
            if (record.getUserId().equals(userId)) {
                userRecords.add(record);
            }
        }
        return userRecords;
    }
    
    // Get book's borrowing history
    public List<BorrowRecord> getBookBorrowHistory(String bookId) {
        List<BorrowRecord> bookRecords = new ArrayList<>();
        for (BorrowRecord record : borrowRecords) {
            if (record.getBookId().equals(bookId)) {
                bookRecords.add(record);
            }
        }
        return bookRecords;
    }
    
    // Recommend books based on user's borrowing history
    public List<Book> recommendBooks(String userId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("User with ID " + userId + " not found.");
            return new ArrayList<>();
        }
        
        // Get all books the user has borrowed
        Set<String> userBookIds = new HashSet<>();
        for (BorrowRecord record : borrowRecords) {
            if (record.getUserId().equals(userId)) {
                userBookIds.add(record.getBookId());
            }
        }
        
        // If the user has no borrowing history, return empty list
        if (userBookIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Get all tags of books the user has borrowed
        Set<String> userTags = new HashSet<>();
        for (String bookId : userBookIds) {
            Book book = findBookById(bookId);
            if (book != null) {
                userTags.addAll(book.getTags());
            }
        }
        
        // Recommend books based on tag matching
        Map<Book, Integer> recommendations = new HashMap<>();
        for (Book book : books) {
            // Skip books the user has already borrowed
            if (userBookIds.contains(book.getId())) {
                continue;
            }
            
            // Calculate tag matching
            int matchCount = 0;
            for (String tag : book.getTags()) {
                if (userTags.contains(tag)) {
                    matchCount++;
                }
            }
            
            // If there are matching tags, add to recommendation list
            if (matchCount > 0) {
                recommendations.put(book, matchCount);
            }
        }
        
        // Sort recommended books by matching
        List<Book> recommendedBooks = new ArrayList<>(recommendations.keySet());
        recommendedBooks.sort((b1, b2) -> recommendations.get(b2) - recommendations.get(b1));
        
        // Return top 5 recommended books
        return recommendedBooks.size() > 5 ? recommendedBooks.subList(0, 5) : recommendedBooks;
    }
    
    // Find book by ID
    public Book findBookById(String id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }

    // Find book by title
    public List<Book> findBooksByTitle(String title) {
        List<Book> result = new ArrayList<>();
        String lowerCaseTitle = title.toLowerCase();
        
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(lowerCaseTitle)) {
                result.add(book);
            }
        }
        
        return result;
    }

    // Find book by author
    public List<Book> findBooksByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        String lowerCaseAuthor = author.toLowerCase();
        
        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(lowerCaseAuthor)) {
                result.add(book);
            }
        }
        
        return result;
    }

    // Find book by tag
    public List<Book> findBooksByTag(String tag) {
        List<Book> result = new ArrayList<>();
        String lowerCaseTag = tag.toLowerCase();
        
        for (Book book : books) {
            for (String bookTag : book.getTags()) {
                if (bookTag.toLowerCase().contains(lowerCaseTag)) {
                    result.add(book);
                    break;
                }
            }
        }
        
        return result;
    }

    // Display all books
    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("The library has no books.");
            return;
        }
        
        System.out.println("All books in the library:");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    // Get all books
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
}