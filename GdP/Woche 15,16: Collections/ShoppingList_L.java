import java.util.*;

public class ShoppingList_L {

    private HashMap<String, HashMap<String, Integer>> groceriesAndQuantitiesPerShop = new HashMap<>();

    /**
     * Adds the item with the given quantity to the shop
     * @param shop the shop where to buy the item
     * @param item the item to buy
     * @param quantity the amount of the item that is to be bought
     */
    public void add(String shop, String item, int quantity) {

        HashMap<String, Integer> shopGroceries;

        // Retrieve the existing map of the shop or create a new one
        // Alternative, more elegant solution:
        //shopGroceries = groceriesAndQuantitiesPerShop.getOrDefault(shop, new HashMap<>());
        if (groceriesAndQuantitiesPerShop.containsKey(shop)) {
            shopGroceries = groceriesAndQuantitiesPerShop.get(shop);
        } else {
            shopGroceries = new HashMap<>();
            groceriesAndQuantitiesPerShop.put(shop, shopGroceries);
        }

        // Add the quantity to the existing quantity
        // Alternative solution with method references (not part of the GdP lecture)
        //shopGroceries.merge(item, quantity, Integer::sum);
        int oldQuantity = 0;
        if (shopGroceries.containsKey(item)) {
            oldQuantity = shopGroceries.get(item);
        }
        shopGroceries.put(item, oldQuantity + quantity);

        // Remove the item if its quantity is 0 or less
        if (shopGroceries.get(item) <= 0) {
            remove(shop, item);
        }
    }

    /**
     * Removes the item from the shopping list's shop category. If there is nothing to buy from a shop,
     * the shop itself is also removed.
     * @param shop the shop to remove the item from
     * @param item the item to remove
     */
    public void remove(String shop, String item) {

        if (!groceriesAndQuantitiesPerShop.containsKey(shop)) {
            return;
        }

        groceriesAndQuantitiesPerShop.get(shop).remove(item);

        if (groceriesAndQuantitiesPerShop.get(shop).isEmpty()) {
            groceriesAndQuantitiesPerShop.remove(shop);
        }
    }


    /**
     * Print the shopping list grouped by shop
     */
    public void print() {
        System.out.println("===================================");
        System.out.println("||         Shopping list         ||");
        System.out.println("===================================");

        for (String shop : groceriesAndQuantitiesPerShop.keySet()) {
            System.out.println("Shop '" + shop+ "':");

            HashMap<String, Integer> shoppingList = groceriesAndQuantitiesPerShop.get(shop);
            for (Map.Entry<String, Integer> entry : shoppingList.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            System.out.println();
        }
    }


    public static void main(String[] args) {
        ShoppingList_L list = new ShoppingList_L();

        list.add("dm", "Hundefutter", 10);
        list.add("Norma", "Limo", 6);
        list.add("Edeka", "Schokolade", 5);
        list.add("Edeka", "Tomaten", 10);
        list.add("Edeka", "Dose Kidneybohnen", 1);
        list.add("Edeka", "Dose Kidneybohnen", 2);
        list.print();

        list.add("Edeka", "Dose Kidneybohnen", -50);
        list.print();

        list.add("Norma", "Limo", -50);
        list.print();

        list.remove("Edeka", "Tomaten");
        list.print();
    }
}
