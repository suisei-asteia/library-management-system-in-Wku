import java.util.HashSet;
import java.util.Set;

public class Book {
    private String id;
    private String title;
    private String author;
    private String category;
    private boolean isAvailable;
    private Set<String> tags; // 新增标签集合

    public Book(String id, String title, String author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = true;
        this.tags = new HashSet<>(); // 初始化标签集合
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // 新增标签相关方法
    public Set<String> getTags() {
        return new HashSet<>(tags); // 返回副本以防止外部修改
    }
    
    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty()) {
            tags.add(tag.trim().toLowerCase()); // 标准化标签
        }
    }
    
    public void removeTag(String tag) {
        if (tag != null) {
            tags.remove(tag.trim().toLowerCase());
        }
    }
    
    // 检查是否有特定标签
    public boolean hasTag(String tag) {
        String lowerCaseTag = tag.toLowerCase();
        for (String t : tags) {
            if (t.toLowerCase().equals(lowerCaseTag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("图书ID: ").append(id)
          .append(", 标题: ").append(title)
          .append(", 作者: ").append(author)
          .append(", 类别: ").append(category)
          .append(", 状态: ").append(isAvailable ? "可借阅" : "已借出");
        
        if (!tags.isEmpty()) {
            sb.append(", 标签: ").append(String.join(", ", tags));
        }
        
        return sb.toString();
    }
}