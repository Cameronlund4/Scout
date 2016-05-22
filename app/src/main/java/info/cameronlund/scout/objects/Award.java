package info.cameronlund.scout.objects;

import android.support.annotation.NonNull;

public class Award implements Comparable<Award> {
    String name = "";
    int count = 0;

    public Award(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(@NonNull Award another) {
        if (count > another.getCount()) {
            return -1;
        } else if (count < another.getCount()) {
            return 1;
        }
        return 0;
    }
}
