package fi.controller;

import fi.model.Category;
import fi.model.Item;
import fi.service.ItemService;
import fi.wrapper.ItemDependencyWrapper;
import fi.wrapper.ItemIdArrayWrapper;
import org.json.JSONObject;
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

    // Retrieve item dependencies
    @RequestMapping(value = "/itemdependencies", method = RequestMethod.GET, produces = "application/json")
    public List<Map<String, Object>> getItemDependencies() {

        itemService.getAllItemDependencies();

        return null;

        /*

        final Iterator<Category> results = itemService.getAllItemDependencies().iterator();
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
        */
    }

    // Add item dependency
    @RequestMapping(value = "/itemdependencies", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity addItemDependency(@RequestBody ItemDependencyWrapper itemDependencyWrapper) {
        itemService.addItemDependency(itemDependencyWrapper.getDepender(), itemDependencyWrapper.getDependee());
        return new ResponseEntity(HttpStatus.OK);
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
    @PostMapping(value = "/items", consumes = "application/json", produces = "application/json")
    public ResponseEntity createItem(@RequestBody String json) {
        final JSONObject jsonObject = new JSONObject(json);
        final int itemId = itemService.createItem(jsonObject.getString("name"), jsonObject.getInt("categoryId"));
        //final int itemId = itemService.createItem(item);
        return new ResponseEntity(Collections.singletonMap("item_id", itemId), HttpStatus.OK);
    }

    // Create category
    @PostMapping(value = "/categories", consumes = "application/json", produces = "application/json")
    public ResponseEntity createCategory(@RequestBody String json) {
        final int categoryId = itemService.createCategory(new JSONObject(json).getString("name"));
        return new ResponseEntity(Collections.singletonMap("category_id", categoryId), HttpStatus.OK);
    }

    // Execute combo and return result
    @PostMapping(value = "/execute", consumes = "application/json", produces = "application/json")
    public ResponseEntity executeCombo(@RequestBody ItemIdArrayWrapper items) throws InterruptedException {
        final Map<String, Object> resultMap = itemService.executeCombo(items.getItems());
        if (resultMap.get("success").equals(true)) {
            return new ResponseEntity(resultMap, HttpStatus.OK);
        } else {
            return new ResponseEntity(resultMap, HttpStatus.BAD_REQUEST);
        }
    }

}
