package android.meal_chooser;

public class NavigationItem {
    private int icon;
    private int title;

    NavigationItem(int icon, int title) {
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
