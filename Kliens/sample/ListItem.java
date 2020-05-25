package sample;

import javafx.beans.property.*;

public class ListItem {
    private IntegerProperty id;
    private StringProperty iname;
    private FloatProperty quantity;
    private StringProperty unit;

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty inameProperty() {
        return iname;
    }

    public FloatProperty quantityProperty() {
        return quantity;
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public ListItem(int id, String iname, float quantity, String unit) {
        this.id = new SimpleIntegerProperty(id);
        this.iname = new SimpleStringProperty(iname);
        this.quantity = new SimpleFloatProperty(quantity);
        this.unit = new SimpleStringProperty(unit);
    }
}
