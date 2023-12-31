/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private int num1;
    private int num2;
    private int num3;
    private boolean hasSword;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, boolean hasSword) {
        this.shop = shop;
        this.hasSword = hasSword;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }
    public void setHasSword(boolean hasSword) {
        this.hasSword = hasSword;
    }


    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown(boolean easyMode) {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if(easyMode) {
                return true;
            } else if (checkItemBreak()) {
                hunter.removeItemFromKit(item);

                printMessage += "\nUnfortunately, you lost your " + item;
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        printMessage = "";
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Colors.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (shop.getHasSword()) {
                printMessage += Colors.RED + "the brawler, seeing your sword, realizes he picked a losing fight and gives you his gold" + Colors.RESET;
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(goldDiff);
            } else {
                if (Math.random() > noTroubleChance) {
                    printMessage += Colors.RED + "Okay, stranger! You proved yer mettle. Here, take my gold." + Colors.RESET;
                    printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                    hunter.changeGold(goldDiff);
                } else {
                    printMessage += Colors.RED + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + Colors.RESET;
                    printMessage += "\nYou lost the brawl and pay " + goldDiff + " gold.";
                    hunter.changeGold(-goldDiff);
                }
            }
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < (1.0/6)) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < (2.0/6)) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < (3.0/6)) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < (4.0/6)) {
            return new Terrain("Desert", "Water");
        } else if (rnd < 5.0/6){
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
    public void dig() {
        int rand = (int) (Math.random() * 2) + 1;
        if (rand == 1) {
            int rand2 = (int) (Math.random() * 20) + 1;
            System.out.println("You dug up " + rand2 + " gold!");
            hunter.changeGold(rand2);
        } else {
            System.out.println("You dug but only found dirt");
        }
    }
    public void hunt() {
        int rand = (int) (Math.random() * 4) + 1;
        if (rand == 1) {
            if (num1 >= 1) {
                System.out.println("You already collected this treasure");
            } else {
                System.out.println("You found a crown!");
                hunter.addTreasure("crown");
                num1++;
            }
        } else if (rand == 2) {
            if (num2 >= 1) {
                System.out.println("You already collected this treasure");
            } else {
                System.out.println("You found a trophy!");
                hunter.addTreasure("trophy");
                num2++;
            }
        } else if (rand == 3) {
            if (num3 >= 1) {
                System.out.println("You already collected this treasure");
            } else {
                System.out.println("You found a gem!");
                hunter.addTreasure("gem");
                num3++;
            }
        } else {
            System.out.println("You found dust");
        }
    }
}