package fi.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "ITEM")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="category", nullable=false)
    private Category category;

    // Item dependency mapping where a depender depends on a dependee
    @ManyToMany
    @JoinTable(name = "ITEM_DEPENDENCY", joinColumns = {
            @JoinColumn(name = "depender", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "dependee", referencedColumnName = "id", nullable = false)})
    private Collection<Item> itemDependencies;

    public Item() {}

    public Item(String name, Category category) {
        this.name = name;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Collection<Item> getItemDependencies() {
        return itemDependencies;
    }

    public void setItemDependencies(Collection<Item> itemDependencies) {
        this.itemDependencies = itemDependencies;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}