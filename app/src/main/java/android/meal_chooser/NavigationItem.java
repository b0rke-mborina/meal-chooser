package android.meal_chooser;

import java.io.Serializable;

/**
 * NavigationItem class represents item of navigation in the application.
 */
public class NavigationItem implements Serializable {
    /**
     * Id of the icon drawable for the navigation item.
     */
    private int icon;

    /**
     * Id of the title string for the navigation item.
     */
    private int title;

    /**
     * Parameterized constructor.
     *
     * @param icon Icon drawable id of the new navigation item.
     * @param title Title string id of the new navigation item.
     */
    public NavigationItem(int icon, int title) {
        this.icon = icon;
        this.title = title;
    }

    /**
     * Icon drawable id setter.
     *
     * @param icon New icon drawable id of the navigation item.
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }

    /**
     * Title string id setter.
     *
     * @param title New title string id of the navigation item.
     */
    public void setTitle(int title) {
        this.title = title;
    }

    /**
     * Icon drawable id getter.
     *
     * @return Icon drawable id of the navigation item.
     */
    public int getIcon() {
        return icon;
    }

    /**
     * Title string id getter.
     *
     * @return Title string id of the navigation item.
     */
    public int getTitle() {
        return title;
    }
}
