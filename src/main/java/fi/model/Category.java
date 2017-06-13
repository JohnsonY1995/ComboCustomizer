package fi.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

//    @Column(name = "ordering", nullable = false)
//    private int ordering;

    @OneToMany(mappedBy="category", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private Set<Item> items;

    public Category() { }

    public Category(String name) {
        this.name = name;
        //this.ordering = ordering;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public int getOrdering() {
//        return ordering;
//    }
//
//    public void setOrdering(int ordering) {
//        this.ordering = ordering;
//    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                //", ordering=" + ordering +
                '}';
    }
}