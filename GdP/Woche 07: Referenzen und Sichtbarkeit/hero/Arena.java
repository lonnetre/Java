package hero;

public class Arena_L {

    private final static double CHANCE_DIMENSION_GAP = 0.3;
    private final static double CHANCE_LOST_ITEM = 0.2;
    private final static double CHANCE_THIEF = 0.4;
    private final static double CHANCE_SECOND_ATTACK = 0.1;


    /**
     * Lets two heroes fight each other until one is defeated.
     * @param hero1 The first hero
     * @param hero2 The second hero
     * @return the winning hero or <code>null</code> if both were defeated simultaneously.
     */
    public static Hero_L fight(Hero_L hero1, Hero_L hero2) {

        for (int round = 1; hero1.getHealth() > 0 && hero2.getHealth() > 0; round++) {
            System.out.println("Round " + round);

            // Randomly decide which hero attacks first
            Hero_L attacker1;
            Hero_L attacker2;
            if (Math.random() < 0.5) {
                attacker1 = hero1;
                attacker2 = hero2;
            } else {
                attacker1 = hero2;
                attacker2 = hero1;
            }

            System.out.println("\t" + attacker1.getName() + "'s turn!");
            playRound(attacker1, attacker2);
            if (attacker2.getHealth() <= 0) {
                return attacker1;
            }

            System.out.println("\t" + attacker2.getName() + "'s turn!");
            playRound(attacker2, attacker1);
            if (attacker1.getHealth() <= 0) {
                return attacker2;
            }
        }

        return null;
    }


    /**
     * Performs a single round in the fight by having the attacking hero attack the defending hero
     * @param attacker the attacking hero
     * @param defender the defending hero
     */
    private static void playRound(Hero_L attacker, Hero_L defender) {
        int previousHealth = defender.getHealth();

        boolean successfulAttack = performAttack(attacker, defender, true);

        // Steal item
        if (Math.random() < CHANCE_THIEF) {
            performTheft(attacker, defender);
        }

        // Second unequipped attack
        if (successfulAttack && Math.random() < CHANCE_SECOND_ATTACK) {
            System.out.println("\t" + attacker.getName() + " is getting ready for a second attack!");
            performAttack(attacker, defender, false);
        }

        int damageDealt = previousHealth - defender.getHealth();
        System.out.println("\t" + defender.getName() + ": " + previousHealth + "H - " + damageDealt + "H -> " + defender.getHealth() + "H");
    }


    /**
     * Performs the attack of the attacking hero. An item is potentially lost after the attack.
     * @param attacker the attacking hero, may lose an item after the attack
     * @param defender the defending hero, may evade the attack by a dimension jump
     * @param equipped <code>true</code> if the attacker attacks with all of their equipment
     *                 or <code>false</code> if it is an unequipped attack
     * @return <code>true</code> if the defender could be attacked or <code>false</code> otherwise
     */
    private static boolean performAttack(Hero_L attacker, Hero_L defender, boolean equipped) {

        boolean canBeAttacked = Math.random() >= CHANCE_DIMENSION_GAP;
        if (!canBeAttacked) {
            // Jump into another dimension
            System.out.println("\t" + defender.getName() + " jumped into a dimension gap and cannot be attacked!");
            return false;
        }

        if (equipped) {
            attacker.attackEquipped(defender);
        } else {
            attacker.attackUnequipped(defender);
        }

        System.out.println("\t" + attacker.getName() + " attacked " + defender.getName());

        if (Math.random() < CHANCE_LOST_ITEM) {
            // Attacker clumsily loses their item
            Item_L lostItem = attacker.loseItem();

            if (lostItem != null) {
                System.out.println("\t" + attacker.getName() + " lost item " + lostItem.getName());
            }
        }

        return true;
    }


    /**
     * Performs an item theft of the defending hero. If the thief has an empty item slot,
     * the item becomes equipment of the attacker
     * @param attacker the attacking hero who steals an item of the defender
     * @param defender the defending hero who loses an item to the attacker
     */
    private static void performTheft(Hero_L attacker, Hero_L defender) {
        Item_L stolenItem = defender.loseItem();

        if (stolenItem != null) {
            System.out.print("\t" + attacker.getName() + " stole " + defender.getName() + "'s item " + stolenItem.getName());

            // Put item in attacker's own item bag
            boolean success = attacker.insertItem(stolenItem);
            if (!success) {
                System.out.println(", but " + attacker.getName() + " could had no space left for the item");
            } else {
                System.out.println();
            }
        }
    }
}
