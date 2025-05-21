import java.util.Date;

public class BorrowRecord {
    private String userId;
    private String bookId;
    private Date borrowDate;
    private Date returnDate;
    
    public BorrowRecord(String userId, String bookId) {
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = new Date(); // 当前时间作为借阅时间
        this.returnDate = null; // 未归还时为null
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
    
    // 检查是否已归还
    public boolean isReturned() {
        return returnDate != null;
    }
    
    @Override
    public String toString() {
        return "用户ID: " + userId +
               ", 图书ID: " + bookId +
               ", 借阅日期: " + borrowDate +
               (returnDate != null ? ", 归还日期: " + returnDate : ", 状态: 未归还");
    }
}