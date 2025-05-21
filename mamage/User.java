import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String password;
    private List<String> borrowedBookIds;
    private boolean isAdmin; // 添加角色字段，true表示管理员，false表示普通用户
    
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.borrowedBookIds = new ArrayList<>();
        this.isAdmin = false; // 默认为普通用户
    }
    
    // 添加带角色参数的构造函数
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
    
    // 添加角色相关的getter
    public boolean isAdmin() {
        return isAdmin;
    }
    
    // 验证密码
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    // 添加借阅图书
    public void borrowBook(String bookId) {
        if (!borrowedBookIds.contains(bookId)) {
            borrowedBookIds.add(bookId);
        }
    }
    
    // 归还图书
    public void returnBook(String bookId) {
        borrowedBookIds.remove(bookId);
    }
    
    // 检查是否借阅了某本书
    public boolean hasBorrowed(String bookId) {
        return borrowedBookIds.contains(bookId);
    }
    
    @Override
    public String toString() {
        return "用户ID: " + id + ", 姓名: " + name + 
               ", 角色: " + (isAdmin ? "管理员" : "普通用户") + 
               ", 当前借阅图书数: " + borrowedBookIds.size();
    }
}