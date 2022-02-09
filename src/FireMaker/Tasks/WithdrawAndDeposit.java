package FireMaker.Tasks;

import FireMaker.FireMaker;
import FireMaker.Task;
import FireMaker.C;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Widgets;
import org.powbot.mobile.script.ScriptManager;

public class WithdrawAndDeposit extends Task {
    FireMaker main;
    public C c;

    public WithdrawAndDeposit(FireMaker main, C c) {
        super();
        super.name = "BankInteract";
        this.main = main;
        this.c = c;
    }

    @Override
    public boolean activate() {
        return Bank.opened() && Widgets.component(12, 20).visible();
    }

    @Override
    public void execute() {
        System.out.println("Banking");

        if (Bank.stream().name(C.logs).count()==0) {
            ScriptManager.INSTANCE.stop();
        }

        if(Inventory.stream().name("Tinderbox").count() < 1) {
            System.out.println("Emptying inventory, grabbing tinderbox");
            Bank.depositInventory();
            Condition.wait(() -> Inventory.stream().isEmpty(), c.time, c.attempts);
            Bank.withdraw("Tinderbox", 1);
            Condition.wait(() -> Inventory.stream().name("Tinderbox").count()>0, c.time, c.attempts);
        }

        if(Inventory.stream().name(C.logs).count()<1) {
            System.out.println("Withdrawing Logs");
            Bank.withdraw(C.logs, Bank.Amount.ALL);
            Condition.wait(() -> Inventory.stream().name(C.logs).count()>0, c.time, c.attempts);
        }

        Bank.close();
        Condition.wait(() -> !Bank.opened(), c.time, c.attempts);

    }
}
