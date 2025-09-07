package ru.valentin.product_manager_api.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @OneToMany(mappedBy = "category",
            cascade = CascadeType.ALL, /*уточнить*/
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.setCategory(null);
    }

    public Category() {
    }

    public Category(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;
        return id == category.id && title.equals(category.title);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(id);
        result = 31 * result + title.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Category{}";
    }
}