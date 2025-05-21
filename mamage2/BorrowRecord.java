import java.util.Date;

public class BorrowRecord {
    private String userId;
    private String bookId;
    private Date borrowDate;
    private Date returnDate;
    
    public BorrowRecord(String userId, String bookId) {
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = new Date(); // Current time as borrow date
        this.returnDate = null; // Null if not returned
    }
    
    // Getters
    public String getUserId() {
        return userId;
    }
    
    public String getBookId() {
        return bookId;
    }
    
    public Date getBorrowDate() {
        return borrowDate;
    }
    
    public Date getReturnDate() {
        return returnDate;
    }
    
    // Setters
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    // Check if returned
    public boolean isReturned() {
        return returnDate != null;
    }
    
    @Override
    public String toString() {
        return "User ID: " + userId +
               ", Book ID: " + bookId +
               ", Borrow Date: " + borrowDate +
               (returnDate != null ? ", Return Date: " + returnDate : ", Status: Not Returned");
    }
}