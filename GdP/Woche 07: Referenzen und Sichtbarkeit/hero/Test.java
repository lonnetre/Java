package hero;

public class Test_L {
    public static void main(String[] args) {
        Hero_L hero1 = new Hero_L("Gertrude", 100, 23, 10);
        hero1.insertItem(new Item_L("Eisenschwert", 7));
        hero1.printInfo();
        hero1.printEquipment();

        Hero_L hero2 = new Hero_L("Waldemar", 112, 15);
        hero2.printInfo();

        Hero_L winner = Arena_L.fight(hero1, hero2);
        if (winner != null) {
            System.out.println(winner.getName() + " has won!");
        }
    }
}
