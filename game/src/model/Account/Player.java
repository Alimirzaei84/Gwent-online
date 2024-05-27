package model.Account;

import model.game.Row;

public class Player {
    private User user;
    private Row[] rows;

    public Player(User user) {
        this.user = user;
        rows = new Row[3];
        createRows();
    }

    private void createRows() {
        rows[0] = new Row(Row.RowName.FIRST);
        rows[1] = new Row(Row.RowName.SEC);
        rows[2] = new Row(Row.RowName.THIRD);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Player)) {
                return false;
            }

            return this.user.equals(((Player) obj).user);
    }
}
