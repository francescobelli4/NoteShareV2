package app.entities;

public class ExplorerItemEntity {
    protected ExplorerItemType type;
    protected String name;

    protected ExplorerItemEntity(ExplorerItemType type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ExplorerItemType getType() {
        return type;
    }
}
