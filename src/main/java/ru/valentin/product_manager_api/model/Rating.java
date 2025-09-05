package ru.valentin.product_manager_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate", nullable = false)
    private Double rate;

    @Column(name = "count", nullable = false)
    private Integer count;

    @OneToOne(mappedBy = "rating", fetch = FetchType.LAZY)
    private Product product;

    public Rating() {
    }

    public Rating(Double rate, Integer count) {
        this.rate = rate;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating = (Rating) o;
        return id.equals(rating.id) && rate.equals(rating.rate) && count.equals(rating.count) && product.equals(rating.product);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + rate.hashCode();
        result = 31 * result + count.hashCode();
        result = 31 * result + product.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", rate=" + rate +
                ", count=" + count +
                ", product=" + product +
                '}';
    }
}
