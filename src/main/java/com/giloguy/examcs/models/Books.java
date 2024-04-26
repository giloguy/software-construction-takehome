package com.giloguy.examcs.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import java.util.HashSet;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 250)
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE , CascadeType.REMOVE}
    )
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )

    @JsonManagedReference
    private Set<Authors> authors = new HashSet<Authors>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonManagedReference
    private Set<Category> categories = new HashSet<Category>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "books_orders",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    @JsonBackReference
    private Set<Orders> orders = new HashSet<Orders>();
    
    private Long price;
    
    public Books() {
    }
    public Books(String title, String description, Set<Authors> authors, Set<Category> categories) {
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Authors> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Authors> authors) {
        this.authors = authors;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void addAuthor(Authors author) {
        this.authors.add(author);
        author.getBooks_written().add(this);
    }

    public void removeAuthor(Authors author) {
        this.authors.remove(author);
        author.getBooks_written().remove(this);
    }


    public void addCategory(Category category) {
        this.categories.add(category);
        category.getBooks().add(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getBooks().remove(this);
    }
    public Set<Orders> getOrders() {
        return orders;
    }

    public void setOrders(Set<Orders> orders) {
        this.orders = orders;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
