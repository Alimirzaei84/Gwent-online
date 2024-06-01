package model.role;

public enum Faction {
    NORTHERN_REALMS(0),
    NILFGAARDIAN_EMPIRE(1),
    MONSTERS(2),
    SCOIA_TAEL(3),
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
