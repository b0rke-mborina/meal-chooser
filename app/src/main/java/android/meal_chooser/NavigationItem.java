package android.meal_chooser;

import java.io.Serializable;

public class NavigationItem implements Serializable {
    private int icon;
    private int title;

    public NavigationItem(int icon, int title) {
        this.icon = icon;
        this.title = title;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public int getTitle() {
        return title;
    }
}
