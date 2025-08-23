package hero;

public class Hero_L {

    private String name;
    private int health;
    private int damage;
    private Item_L[] itemBag = new Item_L[6];

    public Hero_L(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
    }

    public Hero_L(String name, int health, int damage, int itemSlots) {
        this(name, health, damage); // call other constructor to avoid duplicated code

        // The exercise description is a bit vague for what to do if the given number of item slots
        // exceeds the range [1, 15]. This solution clamps the given size to the range boundaries.
        int itemBagSize = Math.max(1, Math.min(15, itemSlots));
        this.itemBag = new Item_L[itemBagSize];
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public String getName() {
        return name;
    }


    /**
     * Sets the given name as the new name of the hero.
     * @param name the new name, must contain at least one letter
     */
    public void setName(String name) {
        if (name != null && name.length() >= 1) {
            this.name = name;
        }
    }

    public void printInfo() {
        System.out.println("Hero '" + name + "': H: " + health + ", D: " + damage);
    }


    /**
     * Adds the given item to the item bag.
     * @param item the item to add to the item bag
     * @return <code>true</code> if there was an empty item slot in the item bag and the item
     * could be added to it, <code>false</code> otherwise
     */
    public boolean insertItem(Item_L item) {
        for (int i = 0; i < itemBag.length; i++) {
            if (itemBag[i] == null) {
                itemBag[i] = item;
                return true;
            }
        }
        return false;
    }

    public void printEquipment() {
        System.out.println("Equipment for Hero '" + name + "':");
        for (int i = 0; i < itemBag.length; i++) {
            if (itemBag[i] != null) {
                System.out.print("  * ");
                itemBag[i].printInfo();
            }
        }
    }


    /**
     * Computes the total damage of a hero by considering the additional damage of their items.
     * @return the sum of the hero's and their items' damage
     */
    public int getEquippedDamage() {
        int totalDamage = this.getDamage();

        for (int i = 0; i < itemBag.length; i++) {
            if (itemBag[i] != null) {
                totalDamage += itemBag[i].getDamage();
            }
        }

        return totalDamage;
    }


    /**
     * Performs an attack on the given hero with all equipped items
     * @param target
     */
    public void attackEquipped(Hero_L target) {
        if (target == null) {
            return;
        }
        target.health -= this.getEquippedDamage();
    }


    /**
     * Performs an attack in the given hero without any items.
     * @param target
     */
    public void attackUnequipped(Hero_L target) {
        if (target == null) {
            return;
        }
        target.health -= this.getDamage();
    }


    /**
     * Loses a random item in the item bag
     * @return the lost item or <code>null</code> if there was no item to lose in the bag
     */
    public Item_L loseItem() {
        // Count items
        int itemCounter = 0;
        for (int i = 0; i < itemBag.length; i++) {
            Item_L item = this.itemBag[i];
            if (item != null) {
                itemCounter++;
            }
        }

        if (itemCounter == 0) {
            return null;
        }

        // Determine a random item that will be lost
        int lostItemNr = (int)(Math.random() * itemCounter) + 1;

        // Find the lostItemNr-th item and delete it
        for (int i = 0; i < itemBag.length; i++) {
            Item_L item = this.itemBag[i];
            if (item != null) {
                lostItemNr--;
            }

            if (lostItemNr == 0) {
                this.itemBag[i] = null;
                return item;
            }
        }

        return null;
    }
}