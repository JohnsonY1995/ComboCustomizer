package fi.controller;

import fi.model.Category;
import fi.model.Item;
import fi.service.ItemService;
import fi.util.ItemIdArrayWrapper;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
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

    private static final Logger logger = LoggerFactory.getLogger(ComboController.class);

    @Autowired
    ItemService itemService;

    // Retrieve items
    @RequestMapping(value = "/items", method = RequestMethod.GET, produces = "application/json")
    public List<Map<String, Object>> getItems() {
        final Iterator<Item> iterator = itemService.getAllItems().iterator();
        final List<Map<String, Object>> resultList = new ArrayList<>();
        while (iterator.hasNext()) {
            final Map<String, Object> resultMap = new LinkedHashMap<>();
            final Item item = iterator.next();
            resultMap.put("item_id", item.getId());
            resultMap.put("name", item.getName());
            resultMap.put("category_id", item.getCategory().getId());
            //resultMap.put("ordering", item.getCategory().getOrdering());
            resultList.add(resultMap);
        }
        return resultList;
    }

    // Retrieve categories
    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = "application/json")
    public List<Map<String, Object>> getCategories() {
        final Iterator<Category> results = itemService.getAllCategories().iterator();
        final List<Map<String, Object>> resultList = new ArrayList<>();
        while (results.hasNext()) {
            final Map<String, Object> resultMap = new LinkedHashMap<>();
            final Category category = results.next();
            resultMap.put("id", category.getId());
            resultMap.put("name", category.getName());
            //resultMap.put("ordering", category.getOrdering());
            resultList.add(resultMap);
        }
        return resultList;
    }

    // Create item
    @PostMapping(value = "/items")
    public ResponseEntity createItem(@RequestParam("name") String name, @RequestParam("category_id") int categoryId) {
        final int itemId = itemService.createItem(name, categoryId);
        return new ResponseEntity(Collections.singletonMap("item_id", itemId), HttpStatus.OK);
    }

    // Create category
    @PostMapping(value = "/categories")
    public ResponseEntity createCategory(@RequestParam("name") String name) {
        final int categoryId = itemService.createCategory(name);
        return new ResponseEntity(Collections.singletonMap("category_id", categoryId), HttpStatus.OK);
    }


    // Execute combo and return result
    @PostMapping(value = "/execute", consumes = "application/json")
    public ResponseEntity executeCombo(@RequestBody ItemIdArrayWrapper items) throws InterruptedException {

        final Map<String, Object> resultMap = itemService.executeCombo(items.getItems());
        if (resultMap != null) {
            return new ResponseEntity(resultMap, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
