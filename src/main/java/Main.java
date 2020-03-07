import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    private static final int FIELD_X_SIZE = 9;
    private static final int FIELD_Y_SIZE = 9;
    private static int minesCounter = 0;

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            minesCounter = readMinesCounter(reader);
            GameField gameField = new GameField(FIELD_X_SIZE, FIELD_Y_SIZE);
            gameField.printField();
            gameCycle(reader, gameField);
        } catch (IOException e) {
            System.out.println("Something gone wrong with IO");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static int readMinesCounter(BufferedReader reader) throws IOException {
        while (true) {
            System.out.println("How many mines do you want on the field?");
            String strInput = reader.readLine();
            if (strInput.matches("^[-]?\\d{0,10}$")) {
                long tempLong = Long.parseLong(strInput);
                if (tempLong > 0) {
                    if (tempLong <= FIELD_X_SIZE * FIELD_Y_SIZE) {
                        return (int) tempLong;
                    } else {
                        System.out.println("Mines count can't exceed number of field cells!");
                    }
                } else {
                    System.out.println("Mines count should be positive!");
                }
            } else {
                System.out.println(strInput + " is not a number");
            }
        }
    }

    private static boolean checkVictory(GameField gameField) {
        return gameField.rows
                .stream()
                .flatMap(List::stream)
                .noneMatch(x -> x.checkState(FieldCellState.MINE_HIDDEN) || x.checkState(FieldCellState.BLANK_CELL_WITH_FLAG)
                ) ||
                gameField.rows.stream()
                              .flatMap(List::stream)
                              .noneMatch(x -> x.checkState(FieldCellState.BLANK_CELL) || x.checkState(FieldCellState.BLANK_CELL_WITH_FLAG));
    }

    private static void gameCycle(BufferedReader reader, GameField gameField) throws IOException {
        boolean victoryFlag = true;
        boolean firstTurn = true;
        do {
            System.out.print("Set/unset mines marks or claim a cell as free:  ");
            String strInput = reader.readLine();
            if (strInput.matches("^[1-9]\\s++[1-9]\\s++(free|mine)")) {
                String[] input = strInput.split("\\s++");
                int x = Integer.parseInt(input[0]) - 1;
                int y = Integer.parseInt(input[1]) - 1;
                String turn_type = input[2];
                if ("mine".equals(turn_type)) {
                    if (gameField.getCell(x, y).mark()) {
                        gameField.printField();
                    }
                }
                if ("free".equals(turn_type)) {
                    if (firstTurn) {
                        gameField.setMines(x, y, minesCounter);
                        firstTurn = false;
                    }
                    if (gameField.getCell(x, y).checkState(FieldCellState.MINE_HIDDEN)) {
                        gameField.rows.stream()
                                      .flatMap(List::stream)
                                      .filter(fieldCell -> fieldCell.checkState(FieldCellState.MINE_HIDDEN) || fieldCell
                                              .checkState(FieldCellState.MINE_MARKED))
                                      .forEach(fieldCell -> fieldCell.setState(FieldCellState.MINE_LOSE));
                        gameField.printField();
                        System.out.println("You stepped on a mine and failed!");
                        victoryFlag = false;
                        break;
                    } else {
                        if (gameField.getCell(x, y).getNeighbourMinesCount() == 0) {
                            gameField.recursiveCellOpen(gameField.getCell(x, y).getCoordinates());
                        } else {
                            gameField.getCell(x, y).setState(FieldCellState.NUMBER);
                        }
                        gameField.printField();
                    }
                }
            } else {
                System.out.println("Please input two numbers from 1 to 9");
            }
        } while (!checkVictory(gameField) || firstTurn);
        if (victoryFlag) {
            System.out.println("Congratulations! You found all mines!");
        }
    }

}
