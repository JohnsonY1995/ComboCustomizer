package fi.controller;

import fi.model.Category;
import fi.model.Item;
import fi.util.ItemIdArrayWrapper;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Transactional
public class ComboController {

    @Autowired
    protected SessionFactory sessionFactory;

    private static final Logger logger = LoggerFactory.getLogger(ComboController.class);
    private static final int ITEM_EXEC_TIME = 1000;

    // Retrieve items
    @RequestMapping(value = "/items", method = RequestMethod.GET, produces = "application/json")
    public List<Map<String, Object>> getItems() {
        final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Item.class);

        final Iterator<Item> results = criteria.list().iterator();

        final List<Map<String, Object>> resultList = new ArrayList<>();

        while (results.hasNext()) {
            final Map<String, Object> resultMap = new LinkedHashMap<>();
            final Item item = results.next();

            resultMap.put("item_id", item.getId());
            resultMap.put("name", item.getName());
            resultMap.put("category_id", item.getCategory().getId());
            resultMap.put("ordering", item.getCategory().getOrdering());
            resultList.add(resultMap);
        }
        return resultList;
    }

    // Retrieve categories
    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = "application/json")
    public List<Map<String, Object>> getCategories() {
        final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Category.class);
        final Iterator<Category> results = criteria.list().iterator();
        final List<Map<String, Object>> resultList = new ArrayList<>();

        while (results.hasNext()) {
            final Map<String, Object> resultMap = new LinkedHashMap<>();
            final Category category = results.next();
            resultMap.put("id", category.getId());
            resultMap.put("name", category.getName());
            resultMap.put("ordering", category.getOrdering());
            resultList.add(resultMap);
        }
        return resultList;
    }

    // Create item
    @PostMapping(value = "/items")
    public ResponseEntity createItem(@RequestParam("name") String name, @RequestParam("category_id") int categoryId) {
        final Category category = sessionFactory.getCurrentSession().get(Category.class, categoryId);
        final Item item = new Item(name, category);
        sessionFactory.getCurrentSession().save(item);
        return new ResponseEntity(Collections.singletonMap("item_id", item.getId()), HttpStatus.OK);
    }

    // Create category
    @PostMapping(value = "/categories")
    public ResponseEntity createCategory(@RequestParam("name") String name, @RequestParam("ordering") int ordering) {
        final Category category = new Category(name, ordering);
        sessionFactory.getCurrentSession().save(category);
        return new ResponseEntity(Collections.singletonMap("category_id", category.getId()), HttpStatus.OK);
    }

    // Validate combo
    private boolean isComboValid() {
        // Check item ordering

        // Check item dependencies
        final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Item.class);
        final Iterator<Item> results = criteria.list().iterator();
        while (results.hasNext()) {
            final Iterator<Item> dependenciesIterator = results.next().getItemDependencies().iterator();
            while (dependenciesIterator.hasNext()) {
                logger.info("LOL" + dependenciesIterator.next());
            }
        }


        return true;
    }

    // Execute combo and return result
    @PostMapping(value = "/execute", consumes = "application/json")
    public ResponseEntity executeCombo(@RequestBody ItemIdArrayWrapper items) throws InterruptedException {

        if (!isComboValid()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        final Iterator<Integer> itemIdIterator = items.getItems().iterator();
        final long startTime = System.currentTimeMillis();
        while (itemIdIterator.hasNext()) {
            logger.info("Running item id " + itemIdIterator.next());
            Thread.sleep(ITEM_EXEC_TIME);
        }
        final long finishTime = System.currentTimeMillis() - startTime;

        final Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("msElapsed", finishTime);
        resultMap.put("result", " is built!");
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

}
