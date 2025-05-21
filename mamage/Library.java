import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Library {
    private List<Book> books;
    private Map<String, User> users; // 用户ID -> 用户对象
    private List<BorrowRecord> borrowRecords; // 所有借阅记录
    
    public Library() {
        this.books = new ArrayList<>();
        this.users = new HashMap<>();
        this.borrowRecords = new ArrayList<>();
    }
    
    // 添加图书
    public void addBook(Book book) {
        books.add(book);
        System.out.println("图书《" + book.getTitle() + "》已添加到图书馆。");
    }
    
    // 删除图书
    public boolean removeBook(String id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(id)) {
                Book removedBook = books.remove(i);
                System.out.println("图书《" + removedBook.getTitle() + "》已从图书馆移除。");
                return true;
            }
        }
        System.out.println("未找到ID为" + id + "的图书。");
        return false;
    }
    
    // 添加用户
    public void addUser(User user) {
        users.put(user.getId(), user);
        System.out.println("用户 " + user.getName() + " 已添加到系统。");
    }
    
    // 获取用户
    public User getUser(String userId) {
        return users.get(userId);
    }
    
    // 获取所有用户
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    // 借阅图书
    public boolean borrowBook(String userId, String bookId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("未找到ID为" + userId + "的用户。");
            return false;
        }
        
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("未找到ID为" + bookId + "的图书。");
            return false;
        }
        
        if (!book.isAvailable()) {
            System.out.println("图书《" + book.getTitle() + "》已被借出，无法借阅。");
            return false;
        }
        
        // 更新图书状态
        book.setAvailable(false);
        
        // 更新用户借阅记录
        user.borrowBook(bookId);
        
        // 创建借阅记录
        BorrowRecord record = new BorrowRecord(userId, bookId);
        borrowRecords.add(record);
        
        System.out.println("用户 " + user.getName() + " 已成功借阅图书《" + book.getTitle() + "》。");
        return true;
    }
    
    // 归还图书
    public boolean returnBook(String userId, String bookId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("未找到ID为" + userId + "的用户。");
            return false;
        }
        
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("未找到ID为" + bookId + "的图书。");
            return false;
        }
        
        if (book.isAvailable()) {
            System.out.println("图书《" + book.getTitle() + "》未被借出，无需归还。");
            return false;
        }
        
        if (!user.hasBorrowed(bookId)) {
            System.out.println("用户 " + user.getName() + " 未借阅此图书。");
            return false;
        }
        
        // 更新图书状态
        book.setAvailable(true);
        
        // 更新用户借阅记录
        user.returnBook(bookId);
        
        // 更新借阅记录
        for (BorrowRecord record : borrowRecords) {
            if (record.getUserId().equals(userId) && 
                record.getBookId().equals(bookId) && 
                !record.isReturned()) {
                record.setReturnDate(new Date());
                break;
            }
        }
        
        System.out.println("用户 " + user.getName() + " 已成功归还图书《" + book.getTitle() + "》。");
        return true;
    }
    
    // 获取用户的借阅历史
    public List<BorrowRecord> getUserBorrowHistory(String userId) {
        List<BorrowRecord> userRecords = new ArrayList<>();
        for (BorrowRecord record : borrowRecords) {
            if (record.getUserId().equals(userId)) {
                userRecords.add(record);
            }
        }
        return userRecords;
    }
    
    // 获取图书的借阅历史
    public List<BorrowRecord> getBookBorrowHistory(String bookId) {
        List<BorrowRecord> bookRecords = new ArrayList<>();
        for (BorrowRecord record : borrowRecords) {
            if (record.getBookId().equals(bookId)) {
                bookRecords.add(record);
            }
        }
        return bookRecords;
    }
    
    // 基于用户借阅历史推荐图书
    public List<Book> recommendBooks(String userId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("未找到ID为" + userId + "的用户。");
            return new ArrayList<>();
        }
        
        // 获取用户借阅过的所有图书
        Set<String> userBookIds = new HashSet<>();
        for (BorrowRecord record : borrowRecords) {
            if (record.getUserId().equals(userId)) {
                userBookIds.add(record.getBookId());
            }
        }
        
        // 如果用户没有借阅记录，返回空列表
        if (userBookIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取用户借阅过的图书的所有标签
        Set<String> userTags = new HashSet<>();
        for (String bookId : userBookIds) {
            Book book = findBookById(bookId);
            if (book != null) {
                userTags.addAll(book.getTags());
            }
        }
        
        // 基于标签匹配度推荐图书
        Map<Book, Integer> recommendations = new HashMap<>();
        for (Book book : books) {
            // 跳过用户已借阅过的图书
            if (userBookIds.contains(book.getId())) {
                continue;
            }
            
            // 计算标签匹配度
            int matchCount = 0;
            for (String tag : book.getTags()) {
                if (userTags.contains(tag)) {
                    matchCount++;
                }
            }
            
            // 如果有匹配的标签，添加到推荐列表
            if (matchCount > 0) {
                recommendations.put(book, matchCount);
            }
        }
        
        // 将推荐图书按匹配度排序
        List<Book> recommendedBooks = new ArrayList<>(recommendations.keySet());
        recommendedBooks.sort((b1, b2) -> recommendations.get(b2) - recommendations.get(b1));
        
        // 返回前5本推荐图书
        return recommendedBooks.size() > 5 ? recommendedBooks.subList(0, 5) : recommendedBooks;
    }
    
    // 按ID查找图书
    public Book findBookById(String id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }

    // 按标题查找图书
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

    // 按作者查找图书
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

    // 按标签查找图书
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

    // 显示所有图书
    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("图书馆中没有图书。");
            return;
        }
        
        System.out.println("图书馆中的所有图书：");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    // 获取所有图书
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
}