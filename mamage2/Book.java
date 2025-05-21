import java.util.HashSet;
import java.util.Set;

public class Book {
    private String id;
    private String title;
    private String author;
    private String category;
    private boolean isAvailable;
    private Set<String> tags; // Tag set

    public Book(String id, String title, String author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = true;
        this.tags = new HashSet<>(); // Initialize tag set
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

    // Tag-related methods
    public Set<String> getTags() {
        return new HashSet<>(tags); // Return a copy to prevent external modification
    }
    
    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty()) {
            tags.add(tag.trim().toLowerCase()); // Normalize tag
        }
    }
    
    public void removeTag(String tag) {
        if (tag != null) {
            tags.remove(tag.trim().toLowerCase());
        }
    }
    
    // Check if a specific tag exists
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
        sb.append("Book ID: ").append(id)
          .append(", Title: ").append(title)
          .append(", Author: ").append(author)
          .append(", Category: ").append(category)
          .append(", Status: ").append(isAvailable ? "Available" : "Borrowed");
        
        if (!tags.isEmpty()) {
            sb.append(", Tags: ").append(String.join(", ", tags));
        }
        
        return sb.toString();
    }
}