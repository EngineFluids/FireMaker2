package FireMaker;

import FireMaker.Tasks.WithdrawAndDeposit;
import FireMaker.Tasks.lightFire;
import org.powbot.api.Area;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.api.script.*;
import org.powbot.mobile.script.ScriptManager;

import javax.swing.text.html.Option;
import java.util.ArrayList;

@ScriptManifest(name= "FireMaker", description = "Make Fires",
version = "1.0", category = ScriptCategory.Firemaking)

@ScriptConfiguration(
        name = "Select Logs",
        description = "Which log do you want to burn?",
        defaultValue = "Logs",
        allowedValues = {"Logs", "Oak logs"},
        optionType = OptionType.STRING,
        enabled = true,
        visible = true
)

@ScriptConfiguration(
        name = "Minutes",
        description = "How many minutes do you want to run?",
        defaultValue = "",
        optionType = OptionType.INTEGER,
        enabled = true,
        visible = true
)

public class FireMaker extends AbstractScript {
    public C c = new C();
    private ArrayList<Task> taskList = new ArrayList<Task>();
    long currentRuntime = ScriptManager.INSTANCE.getRuntime(true);
    public long lastActivated = 0;
    public boolean xpConfirmed;

    public void onStart() {
        C.logs = (String) getOption("Select Logs");

        if (Game.loggedIn() && ! xpConfirmed) {
            xpConfirmed = true;
            c.startXp = Skills.experience(Constants.SKILLS_FIREMAKING);
        }

        Area area = new Area(new Tile(3143, 3511, 0), new Tile(3185, 3469, 0));
        if (area.contains(Players.local())) {
            c.minutes = (Integer) getOption("Minutes") * 60000;
            taskList.add(new WithdrawAndDeposit(this, c));
            taskList.add(new lightFire(this));
        } else {
            ScriptManager.INSTANCE.stop();
        }
    }

    @Override
    public void poll() {
        if (currentRuntime - lastActivated > c.minutes) {
            ScriptManager.INSTANCE.stop();
        }

        for (Task t : taskList) {
            if (t.activate()) {
                t.execute();
                if (ScriptManager.INSTANCE.isStopping()) {
                    break;
                }
            }
        }
    }
}
