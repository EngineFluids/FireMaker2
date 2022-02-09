package FireMaker;

import org.powbot.api.rt4.Player;
import org.powbot.api.rt4.Players;
import java.util.ArrayList;

// Area tiles, [3177, 3485], [3177, 3484], [3177, 3483] etc
// 3498 - 3481

public class C {

    public C() {
        super();
    }

    public ArrayList<String> userTaskList = new ArrayList<String>();

    public Player p() {
        return Players.local();
    }

//    Store Variables
    public int minutes = 0;
    public int startXp = 0;
    public int time = 300;
    public int attempts = 11;
    public static String logs;
    public static final int[] x = {3176, 3177, 3178};
    public static final int[] y = {3498, 3497, 3496, 3495, 3494, 3493};
//    FIND OUT FIRE OBJECT ID
    public static final int[] fire = {10433, 10660, 12796, 13337, 13881, 14169, 15156,
20000, 20001, 21620, 23046, 25155, 25156, 25465, 26185, 26186, 26575, 26576, 26577,
26578, 28791, 30021, 31798, 32297, 33311, 34682, 35810, 35811, 35812, 35912, 35913,
3775, 38427, 40728, 41316, 4265, 4266, 43146, 5249, 5499, 5981, 9735};
    public static final int[] logID = {1511, 1521};
}
