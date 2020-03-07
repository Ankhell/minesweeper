public enum FieldCellState {
    MINE_HIDDEN('.'),
    MINE_MARKED('*'),
    MINE_LOSE('X'),
    BLANK_CELL('.'),
    BLANK_CELL_WITH_FLAG('*'),
    BLANK_CELL_CHECKED('/'),
    NUMBER(' ');

    private Character displayView;

    FieldCellState(Character displayView) {
        this.displayView = displayView;
    }

    public Character getDisplayView() {
        return displayView;
    }


    @Override
    public String toString() {
        return this.name();
    }
}
