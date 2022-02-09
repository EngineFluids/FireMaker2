package FireMaker.Tasks;

import FireMaker.FireMaker;
import FireMaker.C;
import FireMaker.Task;
import org.powbot.api.Area;
import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.mobile.script.ScriptManager;

// Define Areas / Tiles to light fire in GE in C.java
// Area tiles, [3177, 3485], [3177, 3484], [3177, 3483] etc
// 3498 - 3481
// Walk to area
// If area is same area as last row, change area
// Light fire based on last xp drop conditional
//

public class lightFire extends Task {
    FireMaker main;
    public C c;

    public lightFire(FireMaker main) {
        super();
        super.name = "Fire";
        this.main = main;
    }

    @Override
    public boolean activate() {
        return !Bank.opened()
                && Inventory.stream().name("Tinderbox").count() == 1
                && Inventory.stream().name(C.logs).count() > 0;
    }

    @Override
    public void execute() {

        Area startZone = new Area(new Tile(3176, 3498, 0), new Tile(3178, 3482, 0));
        if (Movement.energyLevel() > 0) {
            Movement.running(true);
            Condition.wait(() -> Movement.running(true), 20, 75);
        }

//      Moving towards the random tile
        System.out.println("Stepping to Zone");
        Movement.step(startZone.getRandomTile());
        Condition.wait(() -> startZone.contains(Players.local()), c.time*2, c.attempts);

        Game.tab(Game.Tab.INVENTORY);
        Condition.wait(()-> Game.tab(Game.Tab.INVENTORY), 25, 75);

        GroundItem groundItem = GroundItems.stream().within(1).name("Ashes").nearest().first();
        if (groundItem.getTile().distanceTo(Players.local()) > 1 && !Players.local().inMotion()) {
            Movement.step(startZone.getRandomTile());
            System.out.println("Row in use, moving...");
            Condition.wait(() -> startZone.contains(Players.local()), c.time*2, c.attempts);

        }

        if (Game.tab(Game.Tab.INVENTORY)) {
            Item logs = Inventory.stream().name(C.logs).first();
            Item tinderbox = Inventory.stream().name("Tinderbox").first();
            GameObject fire = Objects.stream().within(1).id(C.fire).nearest().first();
            GroundItem l = GroundItems.stream().id(C.logID).first();

            for (int i = 0; i < Inventory.stream().name(C.logs).count(); i++) {
                if (fire.getTile().distanceTo(Players.local()) != 0 || l.getTile().distanceTo(Players.local()) != 0) {
                    if (Inventory.selectedItem().id() == -1) {
                        System.out.println("Selecting Tinderbox");
                        logs.interact("Use");
                        Condition.wait(() -> Inventory.selectedItem().id() == logs.id(), 30, 75);

                    } else if (Inventory.selectedItem().id() == logs.id()) {
                        System.out.println("Lighting Fire");
                        tinderbox.interact("Use");
                        Condition.wait(() -> tinderbox.interact("Use"), 30, 75);
                    }
                } else {
                    System.out.println("Waiting...");
                    Condition.wait(() -> (l.getTile().distanceTo(Players.local()) > 1 || fire.getTile().distanceTo(Players.local()) != 0), c.time, c.attempts*5);
                }
            }
        } else {
            ScriptManager.INSTANCE.stop();
        }
    }
}
