package hero;

public class Item_L {
    private static int idCounter = 0;

    private String name;
    private int damage;
    private final int ID;

    public Item_L(String name, int damage){
        this.name = name;

        if (damage < 0 || damage > 100) {
            this.damage = (int)(Math.random() * 101);
        } else {
            this.damage = damage;
        }

        ID = idCounter++;
    }

    public String getName(){
        return name;
    }

    public int getDamage(){
        return damage;
    }

    public void printInfo(){
        System.out.println("Item '" + name + "': +" + damage + " D");
    }
}
