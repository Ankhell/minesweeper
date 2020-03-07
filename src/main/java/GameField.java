import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameField {
    List<List<FieldCell>> rows;
    int sizeX;
    int sizeY;
    int minesCount;

    public GameField(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.rows = new ArrayList<>();
        for (int i = 0; i < sizeX; i++) {
            this.rows.add(new ArrayList<>());
            for (int j = 0; j < sizeY; j++) {
                this.rows.get(i).add(new FieldCell(FieldCellState.BLANK_CELL, j, i));
            }
        }
    }

    public void setMines(int firstX, int firstY, int minesCount) {
        this.minesCount = minesCount;
        Random random = new Random();
        for (int i = 0; i < this.minesCount; i++) {
            boolean mineSet = false;
            while (!mineSet) {
                int rndX;
                int rndY;
                do {
                    rndX = random.nextInt(this.sizeX);
                    rndY = random.nextInt(this.sizeY);
                } while (rndX == firstX && rndY == firstY);
                if (getCell(rndX, rndY).checkState(FieldCellState.BLANK_CELL)) {
                    getCell(rndX, rndY).setState(FieldCellState.MINE_HIDDEN);
                    getNeighbours(getCell(rndX,
                                          rndY
                    ).getCoordinates()).forEach(FieldCell::incrementNeighbourMinesCount);
                    mineSet = true;
                }
                if (getCell(rndX, rndY).checkState(FieldCellState.BLANK_CELL_WITH_FLAG)) {
                    getCell(rndX, rndY).setState(FieldCellState.MINE_MARKED);
                    getNeighbours(getCell(rndX,
                                          rndY
                    ).getCoordinates()).forEach(FieldCell::incrementNeighbourMinesCount);
                    mineSet = true;
                }
            }
        }
    }

    public FieldCell getCell(int x, int y) {
        return this.rows.get(y).get(x);
    }

    public FieldCell getCellUserIO(int x, int y) {
        return this.rows.get(y - 1).get(x - 1);
    }

    public void recursiveCellOpen(Coordinates coordinates) {
        int x = coordinates.getX();
        int y = coordinates.getY();
        if (getCell(x, y).checkState(FieldCellState.BLANK_CELL) || getCell(x,y).checkState(FieldCellState.BLANK_CELL_WITH_FLAG)) {
            if (getCell(x, y).getNeighbourMinesCount() == 0) {
                getCell(x, y).setState(FieldCellState.BLANK_CELL_CHECKED);
                getNeighbours(coordinates).stream()
                                          .map(FieldCell::getCoordinates)
                                          .forEach(this::recursiveCellOpen);
            } else {
                getCell(x, y).setState(FieldCellState.NUMBER);
            }
        }
    }

    public List<FieldCell> getNeighbours(Coordinates coordinates) {
        List<FieldCell> result = new ArrayList<>();
        int x = coordinates.getX();
        int y = coordinates.getY();
        int[] xDeltas;
        int[] yDeltas;

        if (x == 0) {
            xDeltas = new int[]{0, 1};
        } else if (x == sizeX - 1) {
            xDeltas = new int[]{-1, 0};
        } else {
            xDeltas = new int[]{-1, 0, 1};
        }
        if (y == 0) {
            yDeltas = new int[]{0, 1};
        } else if (y == sizeY - 1) {
            yDeltas = new int[]{-1, 0};
        } else {
            yDeltas = new int[]{-1, 0, 1};
        }


        for (int xDelta : xDeltas) {
            for (int yDelta : yDeltas) {
                if (!(xDelta == 0 && yDelta == 0)) {
                    result.add(getCell(x + xDelta, y + yDelta));
                }
            }
        }

        return result;
    }

    public void printField() {
        System.out.println(getFirstAndLastLines()[0]);
        for (int i = 0; i < this.sizeY; i++) {
            printRow(i);
        }
        System.out.println(getFirstAndLastLines()[1]);
    }

    public void showField() {
        System.out.println(getFirstAndLastLines()[0]);
        for (int i = 0; i < this.sizeY; i++) {
            printRowDebug(i);
        }
        System.out.println(getFirstAndLastLines()[1]);
    }

    private String[] getFirstAndLastLines() {
        StringBuilder firstLine = new StringBuilder(" |");
        StringBuilder lastLine = new StringBuilder("-|");
        for (int i = 1; i <= this.sizeX; i++) {
            firstLine.append(i);
            lastLine.append("-");
        }
        lastLine.append("|");
        firstLine.append("|\n").append(lastLine);
        return new String[]{firstLine.toString(), lastLine.toString()};
    }

    private void printRow(int rowIndex) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rowIndex + 1).append("|");
        for (FieldCell cell : this.rows.get(rowIndex)) {
            stringBuilder.append(cell);
        }
        stringBuilder.append("|");
        System.out.println(stringBuilder.toString());
    }

    private void printRowDebug(int rowIndex) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rowIndex + 1).append("|");
        for (FieldCell cell : this.rows.get(rowIndex)) {
            if (cell.checkState(FieldCellState.MINE_HIDDEN)) {
                stringBuilder.append('*');
            } else if (cell.neighbourMinesCount != 0) {
                stringBuilder.append(cell.neighbourMinesCount);
            } else {
                stringBuilder.append('.');
            }
        }
        stringBuilder.append("|");
        System.out.println(stringBuilder.toString());
    }

    public void changeMineMarks(){
    }
}
