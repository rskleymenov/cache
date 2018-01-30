package cache.models;


import cache.annotations.Column;
import cache.annotations.Id;
import cache.annotations.Table;

@Table(name = "user")
public class User {
    @Id
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;

    public User() {
    }

    public User(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
