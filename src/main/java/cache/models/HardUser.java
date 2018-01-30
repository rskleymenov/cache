package cache.models;

import cache.annotations.Column;
import cache.annotations.Id;
import cache.annotations.Table;

@Table(name = "hardUser")
public class HardUser {
    @Id
    private long id;
    @Column(name = "age")
    private int age;
    @Column(name = "price")
    private Double price;

    public HardUser(long id, int age, Double price) {
        this.id = id;
        this.age = age;
        this.price = price;
    }
}
