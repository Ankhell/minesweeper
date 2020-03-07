public class FieldCell {
    FieldCellState state;
    int neighbourMinesCount;
    Coordinates coordinates;

    public FieldCell(FieldCellState state, int x, int y) {
        this.state = state;
        this.neighbourMinesCount = 0;
        this.coordinates = new Coordinates(x, y);
    }

    public FieldCellState getState() {
        return state;
    }

    public void setState(FieldCellState state) {
        this.state = state;
    }

    public int getNeighbourMinesCount() {
        return neighbourMinesCount;
    }

    public void setNeighbourMinesCount(int neighbourMinesCount) {
        this.neighbourMinesCount = neighbourMinesCount;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public boolean checkState(FieldCellState state) {
        return this.state.equals(state);
    }

    public void incrementNeighbourMinesCount() {
        this.neighbourMinesCount++;
    }

    public boolean mark() {
        switch (this.state) {
            case NUMBER:
                System.out.println("There is a number here!");
                return false;
            case BLANK_CELL_CHECKED:
                System.out.println("This cell is already open!");
                return false;
            case BLANK_CELL:
                this.state = FieldCellState.BLANK_CELL_WITH_FLAG;
                break;
            case MINE_HIDDEN:
                this.state = FieldCellState.MINE_MARKED;
                break;
            case MINE_MARKED:
                this.state = FieldCellState.MINE_HIDDEN;
                break;
            case BLANK_CELL_WITH_FLAG:
                this.state = FieldCellState.BLANK_CELL;
                break;
        }
        return true;
    }

    @Override
    public String toString() {
        if (this.checkState(FieldCellState.NUMBER)) {
            return Integer.toString(this.neighbourMinesCount);
        } else {
            return this.state.getDisplayView().toString();
        }
    }
}
