package com.giloguy.examcs.models;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "authors")
public class Authors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE , CascadeType.REMOVE},
            mappedBy = "authors"
    )
    @JsonBackReference
    private Set<Books> books_written = new HashSet<Books>();

    public Authors() {
    }
    public Authors(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBooks_written(Set<Books> books_written) {
        this.books_written = books_written;
    }

    public Set<Books> getBooks_written() {
        return books_written;
    }
}
