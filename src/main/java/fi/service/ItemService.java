package fi.service;

import fi.dao.ItemDAO;
import fi.model.Category;
import fi.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
    private static final int ITEM_EXEC_TIME = 1000;

    @Autowired
    ItemDAO itemDAO;

    public List<Item> getAllItems() {
        return itemDAO.getAllItems();
    }

    public List<Category> getAllCategories() {
        return itemDAO.getAllCategories();
    }

    public int createCategory(final String name) {
        return itemDAO.createCategory(name);
    }

    public int createItem(final String name, final int categoryId) {
        return itemDAO.createItem(name, categoryId);
    }

    public Map<String,Object> executeCombo(final List<Integer> executeItemIds) throws InterruptedException {
        final LinkedHashSet<Item> executeItems = itemDAO.getExecuteItemsByIds(executeItemIds);
        if (!isComboValid(executeItems)) {
            return null;
        }

        // Execute Items in order
        final Iterator<Integer> itemIdIterator = executeItemIds.iterator();
        final long startTime = System.currentTimeMillis();
        String executionResult = "";
        while (itemIdIterator.hasNext()) {
            final Item executeItem = itemDAO.getItemById(itemIdIterator.next());
            executionResult += executeItem.getName() + " + ";
            logger.info("Running " + executeItem);
            logger.info("Sleeping for " + ITEM_EXEC_TIME + "ms");
            Thread.sleep(ITEM_EXEC_TIME);
        }
        executionResult = executionResult.substring(0, executionResult.length() - 3);
        final long msElapsed = System.currentTimeMillis() - startTime;

        final Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", "Your pair of shoes was built: " + executionResult);
        resultMap.put("msElapsed", msElapsed);

        logger.info("Result: " + executionResult);
        logger.info("Time elapsed (ms): " + msElapsed);

        return resultMap;
    }


    // Validate combo
    private boolean isComboValid(final LinkedHashSet<Item> items) {
        // Traverse through ordered list of selected items
        final Set<Item> passedItems = new HashSet<>();
        final Iterator<Item> itemsIterator = items.iterator();
        while (itemsIterator.hasNext()) {
            final Item item = itemsIterator.next();
            passedItems.add(item);

            // Check item dependencies
            final Iterator<Item> dependenciesIterator = item.getItemDependencies().iterator();
            while (dependenciesIterator.hasNext()) {
                final Item dependeeItem = dependenciesIterator.next();
                logger.debug("Dependency: " + item + " depends on " + dependeeItem);
                if (!passedItems.contains(dependeeItem)) {
                    logger.error("Dependency not satisfied: " + item + " requires " + dependeeItem);
                    return false;
                }
            }
        }
        return true;
    }


}
