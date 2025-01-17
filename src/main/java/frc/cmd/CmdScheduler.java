package frc.cmd;

import java.util.ArrayList;
import java.util.HashMap;

import frc.cmd.Cmd.CmdStatus;

import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * A variation of WPIlib's {@link CommandScheduler} tha allows from greater contol over
 * the order in which {@link Cmd}s are run and how they cancel each other.
 * <p>
 * APIs which require WPIlib {@link Command}s can be satisfied by {@link Cmd#getWPILibCommand()},
 * which returns a {@link Command} 
 */
public class CmdScheduler {
    
    public final Object[] phases;

    public final Object[] priorities;

    public final HashMap<SubSystem0, Cmd> ssMapping = new HashMap<>();

    private final ArrayList<Cmd>[] groups;

    /**
     * @param phases an array of Objects to name possible phases in which {@link Cmd}s
     * may be run, in order from first to last
     * @param priorities an array of Objects to name possible priority levels {@link Cmd}s
     * may have, in order from highest to lowest.
     */
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

        if (cmd == null) {
            return false;
        }

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

        if (cmd == null) {
            return;
        }

        cmd.end(true);

        this.groups[this.numPriorityOf(cmd)].remove(cmd);

        cmd.status = CmdStatus.UNSCHEDULED;
    }

    /**
     * calls the {@link Cmd#init()}, {@link Cmd#exec()}, and {@link Cmd#end(boolean)} methods
     * of all currently scheduled {@link Cmd}s, with {@link Cmd}s of higher priority values
     * running first and {@link Cmd}s of the same priority value running in the order in which
     * they were scheduled by {@link Cmd#schedule()}
     */
    public void run() {

        for (var subsystem : ssMapping.keySet()) {

            if (ssMapping.get(subsystem) == null) {
                this.add(subsystem.gIdle.get());
            }
        }

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
