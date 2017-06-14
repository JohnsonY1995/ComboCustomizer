package fi.dao;

import fi.model.Category;
import fi.model.Item;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ItemDAO {

    private static final Logger logger = LoggerFactory.getLogger(ItemDAO.class);

    @Autowired
    protected SessionFactory sessionFactory;

    public List<Item> getAllItems() {
        final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Item.class);
        criteria.addOrder(Order.asc("category"));
        return criteria.list();
    }

    public List<Category> getAllCategories() {
        final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Category.class);
        return criteria.list();
    }

    public int createCategory(final String name) {
        final Category category = new Category(name);
        sessionFactory.getCurrentSession().save(category);
        return category.getId();
    }

    public int createItem(final String name, final int categoryId) {
        final Category category = sessionFactory.getCurrentSession().get(Category.class, categoryId);
        final Item item = new Item(name, category);
        item.setCategory(category);
        sessionFactory.getCurrentSession().save(item);
        return item.getId();
    }

    public List<Item> getItemsByIds(final List<Integer> itemIds) {
        final Criterion criterion = Restrictions.in("id", itemIds);
        final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Item.class).add(criterion);
        return criteria.list();
    }

    public Item getItemById(final int id) {
        return sessionFactory.getCurrentSession().get(Item.class, id);
    }

    // Get items in the order to be executed
    public LinkedHashSet<Item> getExecuteItemsByIds(final List<Integer> executeItemIds) {
        final LinkedHashSet<Item> executeItems = new LinkedHashSet<>();
        for (Integer itemId : executeItemIds) {
            executeItems.add(getItemById(itemId));
        }
        return executeItems;
    }

}
