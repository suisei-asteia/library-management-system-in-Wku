import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class LibraryManagementSystem {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null; // 当前登录用户

    public static void main(String[] args) {
        // 添加一些示例图书和用户
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
                        System.out.println("感谢使用图书管理系统，再见！");
                        running = false;
                        break;
                    default:
                        System.out.println("无效选择，请重新输入。");
                }
            } else {
                // 根据用户角色显示不同的菜单
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
            
            // 在每个操作后暂停
            if (running) {
                System.out.println("\n按回车键继续...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void displayLoginMenu() {
        System.out.println("\n===== 图书管理系统 - 登录 =====");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 退出系统");
        System.out.print("请选择操作 (1-3): ");
    }
    
    // 显示管理员菜单
    private static void displayAdminMenu() {
        System.out.println("\n===== 图书管理系统 - 管理员菜单 =====");
        System.out.println("当前用户: " + currentUser.getName() + " (管理员)");
        System.out.println("1. 添加图书");
        System.out.println("2. 删除图书");
        System.out.println("3. 搜索图书");
        System.out.println("4. 显示所有图书");
        System.out.println("5. 标签管理");
        System.out.println("6. 查看所有用户");
        System.out.println("7. 登出");
        System.out.println("8. 退出系统");
        System.out.print("请选择操作 (1-8): ");
    }
    
    // 显示普通用户菜单
    private static void displayUserMenu() {
        System.out.println("\n===== 图书管理系统 - 用户菜单 =====");
        System.out.println("当前用户: " + currentUser.getName());
        System.out.println("1. 搜索图书");
        System.out.println("2. 显示所有图书");
        System.out.println("3. 借阅图书");
        System.out.println("4. 归还图书");
        System.out.println("5. 查看借阅历史");
        System.out.println("6. 获取图书推荐");
        System.out.println("7. 登出");
        System.out.println("8. 退出系统");
        System.out.print("请选择操作 (1-8): ");
    }
    
    // 处理管理员的菜单选择
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
                currentUser = null; // 登出
                System.out.println("已成功登出。");
                break;
            case 8:
                System.out.println("感谢使用图书管理系统，再见！");
                return false;
            default:
                System.out.println("无效选择！");
        }
        return true;
    }
    
    // 处理普通用户的菜单选择
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
                currentUser = null; // 登出
                System.out.println("已成功登出。");
                break;
            case 8:
                System.out.println("感谢使用图书管理系统，再见！");
                return false;
            default:
                System.out.println("无效选择！");
        }
        return true;
    }
    
    // 添加显示所有用户的方法（仅管理员可用）
    private static void displayAllUsers() {
        System.out.println("\n===== 系统用户列表 =====");
        List<User> users = library.getAllUsers();
        
        if (users.isEmpty()) {
            System.out.println("系统中没有用户。");
            return;
        }
        
        System.out.println("系统中的所有用户：");
        for (User user : users) {
            System.out.println(user);
        }
    }
    
    private static int getUserChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return choice;
        } catch (NumberFormatException e) {
            return -1; // 返回无效选择
        }
    }
    
    private static void addBook() {
        System.out.println("\n===== 添加新图书 =====");
        
        System.out.print("请输入图书ID: ");
        String id = scanner.nextLine();
        
        // 检查ID是否已存在
        if (library.findBookById(id) != null) {
            System.out.println("错误：ID为" + id + "的图书已存在！");
            return;
        }
        
        System.out.print("请输入图书标题: ");
        String title = scanner.nextLine();
        
        System.out.print("请输入图书作者: ");
        String author = scanner.nextLine();
        
        System.out.print("请输入图书类别: ");
        String category = scanner.nextLine();
        
        Book newBook = new Book(id, title, author, category);
        library.addBook(newBook);
    }
    
    private static void removeBook() {
        System.out.println("\n===== 删除图书 =====");
        System.out.print("请输入要删除的图书ID: ");
        String id = scanner.nextLine();
        library.removeBook(id);
    }
    
    private static void searchBook() {
        boolean searching = true;
        
        while (searching) {
            System.out.println("\n===== 搜索图书 =====");
            System.out.println("1. 按ID搜索");
            System.out.println("2. 按标题搜索");
            System.out.println("3. 按作者搜索");
            System.out.println("4. 按标签搜索");
            System.out.println("5. 返回主菜单");
            System.out.print("请选择搜索方式 (1-5): ");
            
            int searchChoice = getUserChoice();
            
            if (searchChoice == 5) {
                searching = false;
                continue;
            }
            
            List<Book> results = null;
            boolean searchSuccess = false;
            
            switch (searchChoice) {
                case 1:
                    System.out.print("请输入图书ID: ");
                    String id = scanner.nextLine();
                    Book book = library.findBookById(id);
                    if (book != null) {
                        System.out.println("找到图书：");
                        System.out.println(book);
                        searchSuccess = true;
                    } else {
                        System.out.println("未找到ID为" + id + "的图书。");
                    }
                    break;
                    
                case 2:
                    System.out.print("请输入图书标题: ");
                    String title = scanner.nextLine();
                    results = library.findBooksByTitle(title);
                    searchSuccess = displaySearchResults(results, "标题包含 '" + title + "'");
                    break;
                    
                case 3:
                    System.out.print("请输入图书作者: ");
                    String author = scanner.nextLine();
                    results = library.findBooksByAuthor(author);
                    searchSuccess = displaySearchResults(results, "作者包含 '" + author + "'");
                    break;
                    
                case 4:
                    System.out.print("请输入图书标签: ");
                    String tag = scanner.nextLine();
                    results = library.findBooksByTag(tag);
                    searchSuccess = displaySearchResults(results, "标签为 '" + tag + "'");
                    break;
                    
                default:
                    System.out.println("无效选择！");
            }
            
            // 如果搜索失败，询问用户是否要重新搜索
            if (!searchSuccess && searchChoice >= 1 && searchChoice <= 4) {
                System.out.print("\n是否要更改搜索条件重新搜索？(Y/N): ");
                String choice = scanner.nextLine().trim().toUpperCase();
                if (!choice.startsWith("Y")) {
                    searching = false;
                }
            } else if (searchSuccess) {
                // 搜索成功，询问是否继续搜索
                System.out.print("\n是否要继续搜索其他图书？(Y/N): ");
                String choice = scanner.nextLine().trim().toUpperCase();
                if (!choice.startsWith("Y")) {
                    searching = false;
                }
            }
        }
    }
    
    // 修改显示搜索结果的方法，返回布尔值表示是否找到结果
    private static boolean displaySearchResults(List<Book> results, String criteria) {
        if (results.isEmpty()) {
            System.out.println("未找到" + criteria + "的图书。");
            return false;
        } else {
            System.out.println("找到" + results.size() + "本" + criteria + "的图书：");
            for (Book book : results) {
                System.out.println(book);
            }
            return true;
        }
    }
    
    private static void borrowBook() {
        System.out.println("\n===== 借阅图书 =====");
        System.out.print("请输入要借阅的图书ID: ");
        String bookId = scanner.nextLine();
        
        library.borrowBook(currentUser.getId(), bookId);
    }
    
    private static void returnBook() {
        System.out.println("\n===== 归还图书 =====");
        System.out.print("请输入要归还的图书ID: ");
        String bookId = scanner.nextLine();
        
        library.returnBook(currentUser.getId(), bookId);
    }
    
    private static void manageBookTags() {
        System.out.println("\n===== 标签管理 =====");
        System.out.print("请输入图书ID: ");
        String id = scanner.nextLine();
        
        Book book = library.findBookById(id);
        if (book == null) {
            System.out.println("未找到ID为" + id + "的图书。");
            return;
        }
        
        boolean managing = true;
        while (managing) {
            System.out.println("\n图书信息: " + book);
            
            Set<String> tags = book.getTags();
            System.out.println("\n当前标签: " + (tags.isEmpty() ? "无" : String.join(", ", tags)));
            
            System.out.println("\n1. 添加标签");
            System.out.println("2. 删除标签");
            System.out.println("3. 返回主菜单");
            System.out.print("请选择操作 (1-3): ");
            
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    System.out.print("请输入要添加的标签(多个标签用逗号分隔): ");
                    String tagsToAdd = scanner.nextLine();
                    String[] tagArray = tagsToAdd.split(",");
                    for (String tag : tagArray) {
                        book.addTag(tag);
                        System.out.println("已添加标签: " + tag.trim());
                    }
                    break;
                    
                case 2:
                    if (tags.isEmpty()) {
                        System.out.println("该图书没有标签可删除。");
                        break;
                    }
                    
                    System.out.print("请输入要删除的标签: ");
                    String tagToRemove = scanner.nextLine();
                    if (book.hasTag(tagToRemove)) {
                        book.removeTag(tagToRemove);
                        System.out.println("已删除标签: " + tagToRemove);
                    } else {
                        System.out.println("该图书没有标签: " + tagToRemove);
                    }
                    break;
                    
                case 3:
                    managing = false;
                    break;
                    
                default:
                    System.out.println("无效选择！");
            }
        }
    }
    
    private static void viewBorrowHistory() {
        System.out.println("\n===== 借阅历史 =====");
        List<BorrowRecord> history = library.getUserBorrowHistory(currentUser.getId());
        
        if (history.isEmpty()) {
            System.out.println("您还没有任何借阅记录。");
            return;
        }
        
        System.out.println("您的借阅历史：");
        for (BorrowRecord record : history) {
            Book book = library.findBookById(record.getBookId());
            String bookTitle = book != null ? book.getTitle() : "未知图书";
            
            System.out.println("图书：" + bookTitle + 
                              "，借阅日期：" + record.getBorrowDate() + 
                              (record.isReturned() ? "，归还日期：" + record.getReturnDate() : "，状态：未归还"));
        }
    }
    
    private static void getBookRecommendations() {
        System.out.println("\n===== 图书推荐 =====");
        List<Book> recommendations = library.recommendBooks(currentUser.getId());
        
        if (recommendations.isEmpty()) {
            System.out.println("暂时没有适合您的图书推荐，请先借阅一些图书。");
            return;
        }
        
        System.out.println("根据您的借阅历史，为您推荐以下图书：");
        for (int i = 0; i < recommendations.size(); i++) {
            Book book = recommendations.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + " - " + book.getAuthor() + 
                              " [标签: " + String.join(", ", book.getTags()) + "]");
        }
    }
    
    private static void initializeLibrary() {
        // 添加示例图书
        Book book1 = new Book("B001", "Java编程思想", "Bruce Eckel", "编程");
        book1.addTag("编程");
        book1.addTag("java");
        book1.addTag("经典");
        library.addBook(book1);
        
        Book book2 = new Book("B002", "算法导论", "Thomas H. Cormen", "计算机科学");
        book2.addTag("算法");
        book2.addTag("计算机科学");
        book2.addTag("经典");
        library.addBook(book2);
        
        Book book3 = new Book("B003", "三体", "刘慈欣", "科幻小说");
        book3.addTag("科幻");
        book3.addTag("中国文学");
        book3.addTag("获奖作品");
        library.addBook(book3);
        
        Book book4 = new Book("B004", "活着", "余华", "文学");
        book4.addTag("中国文学");
        book4.addTag("当代文学");
        library.addBook(book4);
        
        Book book5 = new Book("B005", "设计模式", "Erich Gamma", "软件工程");
        book5.addTag("编程");
        book5.addTag("设计");
        book5.addTag("软件工程");
        library.addBook(book5);

        // 添加用户，并设置第一个用户为管理员
        library.addUser(new User("U001", "张三", "password1", true)); // 管理员
        library.addUser(new User("U002", "李四", "password2")); // 普通用户
        library.addUser(new User("U003", "王五", "password3")); // 普通用户
    }

    private static void login() {
        System.out.println("\n===== 用户登录 =====");
        System.out.print("请输入用户ID: ");
        String userId = scanner.nextLine();
        
        User user = library.getUser(userId);
        if (user != null) {
            System.out.print("请输入密码: ");
            String password = scanner.nextLine();
            
            if (user.checkPassword(password)) {
                currentUser = user;
                System.out.println("登录成功！欢迎回来，" + user.getName() + 
                                  (user.isAdmin() ? " (管理员)" : "") + "。");
            } else {
                System.out.println("密码错误，请重试。");
            }
        } else {
            System.out.println("用户ID不存在，请先注册或检查输入。");
        }
    }

    private static void register() {
        System.out.println("\n===== 用户注册 =====");
        System.out.print("请输入用户ID: ");
        String userId = scanner.nextLine();
        
        if (library.getUser(userId) != null) {
            System.out.println("该用户ID已存在，请选择其他ID。");
            return;
        }
        
        System.out.print("请输入用户姓名: ");
        String userName = scanner.nextLine();
        
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        
        System.out.print("请再次输入密码: ");
        String confirmPassword = scanner.nextLine();
        
        if (!password.equals(confirmPassword)) {
            System.out.println("两次输入的密码不一致，注册失败。");
            return;
        }
        
        // 新注册的用户默认为普通用户
        User newUser = new User(userId, userName, password, false);
        library.addUser(newUser);
        currentUser = newUser;
        System.out.println("注册成功！欢迎，" + userName + "。");
    }
}