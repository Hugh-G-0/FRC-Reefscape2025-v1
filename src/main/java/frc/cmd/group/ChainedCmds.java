package frc.cmd.group;

import java.util.HashSet;
import java.util.Set;

import frc.cmd.Cmd;
import frc.cmd.SubSystem0;

public class ChainedCmds extends Cmd{
    
    private final Cmd[] members;

    protected int idx = 0;

    public ChainedCmds(Cmd... members) {

        super(members[0].scheduler, members[0].phase, members[0].priority, (SubSystem0[])allReqsOf(members).toArray());

        this.members = members;
    }

    @Override
    public void init() {
        this.members[0].init();
    }

    @Override
    public boolean exec() {
        boolean b = this.members[this.idx].exec();

        if (b) {
            this.members[this.idx].end(false);

            if (this.idx == this.members.length - 1) {
                return true;
            }

            ++this.idx;

            this.members[this.idx].init();

            return this.exec();
        }

        return false;
    }

    @Override
    public void end(boolean wasInterupted) {
        this.members[this.idx].end(wasInterupted);
    }

    private static Set<SubSystem0> allReqsOf(Cmd... cmds) {
        HashSet<SubSystem0> reqs = new HashSet<>();

        for (var c : cmds) {
            reqs.retainAll(c.reqs);
        }

        return reqs;
    }
}
