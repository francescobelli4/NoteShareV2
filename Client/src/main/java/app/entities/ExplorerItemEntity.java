package app.entities;

public class ExplorerItemEntity {
    protected String name;

    protected ExplorerItemEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
