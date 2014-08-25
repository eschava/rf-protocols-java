package rf.protocols.debug.groups;

import rf.protocols.core.Packet;

import java.util.Arrays;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class DebugGroupsPacket implements Packet {

    private int lessThanGrouppedCount;
    private int moreThanGrouppedCount;
    private int[] groupCount;

    public DebugGroupsPacket(int groupsCount) {
        groupCount = new int[groupsCount];

        clear();
    }

    public void clear() {
        lessThanGrouppedCount = moreThanGrouppedCount = 0;
        Arrays.fill(groupCount, 0);
    }

    public void incrementGroup(int group) {
        groupCount[group]++;
    }

    public void incrementLessThanGroupped() {
        lessThanGrouppedCount++;
    }

    public void incrementMoreThanGroupped() {
        moreThanGrouppedCount++;
    }

    public int getGroupCount(int group) {
        return groupCount[group];
    }

    public int getLessThanGrouppedCount() {
        return lessThanGrouppedCount;
    }

    public int getMoreThanGrouppedCount() {
        return moreThanGrouppedCount;
    }
}
