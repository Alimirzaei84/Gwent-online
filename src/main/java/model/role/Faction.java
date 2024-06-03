package model.role;

public enum Faction {
    REALMS(0),
    NILFGAARD(1),
    MONSTERS(2),
    SCOIATAEL(3),
    SKELLIGE(4),
    ALL(-1);

    final int index;
    Faction(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
