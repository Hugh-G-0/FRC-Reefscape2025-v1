package frc.cmd;

import java.util.ArrayList;
import java.util.HashMap;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.cmd.Cmd.CmdStatus;

public class CmdScheduler {
    
    public final Object[] phases;

    public final Object[] priorities;

    public final HashMap<SubSystem0, Cmd> ssMapping = new HashMap<>();

    private final ArrayList<Cmd>[] groups;

    @SuppressWarnings("unchecked")
    public CmdScheduler(Object[] phases, Object[] priorities) {
        this.phases = phases;
        this.priorities = priorities;

        groups = new ArrayList[phases.length];
    }

    /* package-private */ void add(SubSystem0 ss) {
        ssMapping.put(ss, null);
    }

    /* package-private */ boolean add(Cmd cmd) {

        if (cmd.status == CmdStatus.QUEUED) {
            return true;
        }
        else if (cmd.status == CmdStatus.RUNNING) {
            cmd.status = CmdStatus.QUEUED;
            return true;
        }

        for (var i : cmd.reqs) {
            if (this.prioritize(this.ssMapping.get(i), cmd)) {
                return false;
            }
        }

        for (var i : cmd.reqs) {
            this.cancel(this.ssMapping.get(i));
        }

        this.groups[this.numPriorityOf(cmd)].add(cmd);

        return true;
    }

    /** packge-private */ void cancel(Cmd cmd) {
        cmd.end(true);

        this.groups[this.numPriorityOf(cmd)].remove(cmd);

        cmd.status = CmdStatus.UNSCHEDULED;
    }

    public void run() {

        for (var g : this.groups) {
            for (var c : g) {

                if (c.status == CmdStatus.QUEUED) {

                    c.init();

                    c.status = CmdStatus.RUNNING;
                }

                if (c.exec()) {
                    c.end(false);

                    c.status = CmdStatus.FINALIZING;
                }
            }
            g.removeIf((cmd) -> (cmd.status == CmdStatus.FINALIZING));
        }
    }

    private boolean prioritize(Cmd a, Cmd b) {

        if (b == null) {
            return true;
        }
        else if (a == null) {
            return false;
        }
        
        for (var i : priorities) {
            if (i == a.priority) {
                return true;
            }
            else if (i == b) {
                return false;
            }
        }
        return false; // impossible but required
    }

    private int numPriorityOf(Cmd cmd) {
        
        for (int i = 0; i < this.priorities.length; ++i) {
            if (this.priorities[i] == cmd.priority) {
                return i;
            }
        }
        return -1;
    }
}
