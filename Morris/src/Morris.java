package Morris;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Paul Krappatsch on 11.06.2017.
 */

public class Morris implements ImmutableBoard<MorrisMove> {
<<<<<<< .merge_file_a00232
    /******************************************************************
     * Fields:
     *  board[]:
     *      -> 24 elements:
     *          -> -1 => Player2 ; 0 => empty (no stones placed) ; +1 => Player1
     *
     *  Turn:
     *      -> Represents the current player, also the value used to fill the board
     *
     *  depth:
     *      -> toDo
     *  movesWithoutRemoving:
     *      -> to determintate an draw
     *          => 50 moves without removing is a Draw
     *  parent:
     *      -> Parentboard of the current parent
     *          => Rootboard got no parent
     *
     *  phase:
     *      -> phase determinate the movepossibilites of the players
     *          => phase 1: Stones can be set anywhere as long as the element of the board[] is 0
     *          => phase 2: toDo
     *          => phase 3: toDo
     *          => phase 4: toDO
     *          => phase 5: toDo
     *
     *  isFlipped:
     *      -> determinate the colors of the Stones for the toString() method
     *
     *****************************************************************/
=======
>>>>>>> .merge_file_a05588

    private int[] board = new int[24];
    private int turn = +1;
    private int depth = 0;
    private int moveswithoutremoving = 0; // used for detecting draws
    private Morris parent = null;
    private int phase = 1;
    private boolean isFlipped = false;

    public static void main(String[] args) {

        ImmutableBoard<MorrisMove> m = new Morris();
        System.out.println(m.toString());
        m = m.makeMove(new MorrisMove(0));
        m = m.makeMove(new MorrisMove(1));
        m = m.makeMove(new MorrisMove(2));
        m = m.makeMove(new MorrisMove(3));
        m = m.makeMove(new MorrisMove(4));
        m = m.makeMove(new MorrisMove(5));
        m = m.makeMove(new MorrisMove(6));
        m = m.makeMove(new MorrisMove(7));
        m = m.makeMove(new MorrisMove(8));
        m = m.makeMove(new MorrisMove(9));
        m = m.makeMove(new MorrisMove(10));
        m = m.makeMove(new MorrisMove(11));
        m = m.makeMove(new MorrisMove(16));
        m = m.makeMove(new MorrisMove(12));
        m = m.makeMove(new MorrisMove(17));
        m = m.makeMove(new MorrisMove(14));
        m = m.makeMove(new MorrisMove(20));
        m = m.makeMove(new MorrisMove(22));
        System.out.println(m.toString());
        m = m.makeMove(new MorrisMove(16, 23, 0));
        System.out.println(((Morris) m).numberOfClosedPotentialMills(new MorrisMove(5, 13)));
        System.out.println(m.toString());
        System.out.println(((Morris) m).streamMovesWithRemoves(new MorrisMove(5, 13)).count());
        m.moves().forEach(System.out::println);
        System.out.println(m.moves().size());
    }

    @Override
    public ImmutableBoard<MorrisMove> makeMove(MorrisMove morrisMove) {
        if (phase == 1) return makeMovePhasePlace(morrisMove);
        else return makeMovePhaseMoveAndJump(morrisMove);
    }

    private Morris makeMovePhasePlace(MorrisMove morrisMove) {
        Morris child = new Morris();
        child.board = Arrays.copyOf(board, 24);
        child.board[morrisMove.getTo()] = turn;
        child.turn = -turn;
        child.isFlipped = isFlipped;
        child.depth = depth + 1;
        child.parent = this;
        if (morrisMove.getRemove() != -1) {
            child.board[morrisMove.getRemove()] = 0;
            child.moveswithoutremoving = 0;
        } else {
            child.moveswithoutremoving = moveswithoutremoving + 1;
        }
        if (child.depth == 18) child.phase = 2;
        else child.phase = phase;
        return child;
    }

    private Morris makeMovePhaseMoveAndJump(MorrisMove morrisMove) {
        Morris child = new Morris();
        child.board = Arrays.copyOf(board, 24);
        child.board[morrisMove.getFrom()] = 0;
        child.board[morrisMove.getTo()] = turn;
        child.turn = -turn;
        child.isFlipped = isFlipped;
        child.depth = depth + 1;
        child.parent = this;
        if (morrisMove.getRemove() != -1) {
            child.board[morrisMove.getRemove()] = 0;
            child.moveswithoutremoving = 0;
            if (child.phase != 5 && child.checkForPhaseJump()) { // phase1 stays at 1
                if (child.phase == 2) child.phase = child.turn == 1 ? 3 : 4;
                else phase = 5;
                //else if (child.phase == 3) child.phase = child.turn == 1 ? 3 : 5; // else child.phase = 5;
                //else if (child.phase == 4) child.phase = child.turn == 1 ? 5 : 4;
            } else child.phase = phase;
        } else {
            child.phase = phase;
            child.moveswithoutremoving = moveswithoutremoving + 1;
        }
        return child;
    }

    private boolean checkForPhaseJump() {
        return Arrays.stream(board)
                .filter(n -> n == turn)
                .count() == 3;
    }

    @Override
    public ImmutableBoard<MorrisMove> undoMove() {
        return parent;
    }

<<<<<<< .merge_file_a00232
    /******************************************************************
     * streamMoves()
     *      -> chooses the stream
     *      -> depends on the phase
     *
     *****************************************************************/
=======
>>>>>>> .merge_file_a05588
    Stream<MorrisMove> streamMoves() {
        if (phase == 1) return streamMovesPhasePlace();
        else if (phase == 2 || phase == 3 && turn == -1 || phase == 4 && turn == +1)
            return streamMovesPhaseMove();
        return streamMovesPhaseJump();
    }

    private Stream<MorrisMove> streamMovesPhasePlace() {
        return IntStream.range(0, 24)
                .filter(to -> board[to] == 0)
                .mapToObj(MorrisMove::new)
                .map(this::streamMovesWithRemoves)
                .flatMap(morrisMoveStream -> morrisMoveStream);
    }

<<<<<<< .merge_file_a00232
    /******************************************************************
     *   streamMovesPhaseMove()
     *      -> iterates through the every element of the board
     *      -> filters the positions of the current player
     *      -> adds a MorrisMove if the direct neighbour is an empty Position
     *      -> adds the streamMovesWithRemoves to the stream
     *      -> combines all the mini streams into one big and returns it
     *
     *****************************************************************/
=======
>>>>>>> .merge_file_a05588
    private Stream<MorrisMove> streamMovesPhaseMove() {
        final int[][] moves = {
                {1, 7}, {0, 2, 9}, {1, 3}, {2, 11, 4}, {3, 5}, {4, 13, 6}, {5, 7}, {0, 6, 15},
                {9, 15}, {1, 8, 10, 17}, {9, 11}, {3, 10, 12, 19}, {11, 13}, {5, 12, 14, 21}, {13, 15}, {7, 8, 14, 23},
                {17, 23}, {9, 16, 18}, {17, 19}, {18, 11, 20}, {19, 21}, {13, 20, 22}, {21, 23}, {15, 16, 22}
        };

        return IntStream.range(0, 24)
                .filter(from -> board[from] == turn)
                .mapToObj(from -> Arrays.stream(moves[from])
                        .filter(to -> board[to] == 0)
                        .mapToObj(to -> new MorrisMove(from, to))
                )
                .flatMap(moveStream -> moveStream)
                .map(this::streamMovesWithRemoves)
                .flatMap(morrisMoveStream -> morrisMoveStream);
    }

<<<<<<< .merge_file_a00232

    /******************************************************************
     *  streamMovesPhaseJump():
     *      -> iterates through every element of the board
     *      -> filters all positions of the current player
     *      -> adds a MorrisMove for every free Position on the Board
     *      -> combines all the mini-streams into a big one and returns it
     *
     *****************************************************************/
=======
>>>>>>> .merge_file_a05588
    private Stream<MorrisMove> streamMovesPhaseJump() {
        return IntStream.range(0, 24)
                .filter(from -> board[from] == turn)
                .mapToObj(from -> IntStream.range(0, 24)
                        .filter(to -> board[to] == 0)
                        .mapToObj(to -> new MorrisMove(from, to))
                )
                .flatMap(morrisMoveStream -> morrisMoveStream)
                .map(this::streamMovesWithRemoves)
                .flatMap(morrisMoveStream -> morrisMoveStream);
    }

<<<<<<< .merge_file_a00232

    /******************************************************************
     * isFlipped():
     *      -> pretty much a getter-method
     *      -> returns if the board was flipped
     *
     *****************************************************************/

=======
>>>>>>> .merge_file_a05588
    @Override
    public boolean isFlipped() {
        return isFlipped;
    }

    private Stream<MorrisMove> streamMovesWithRemoves(MorrisMove morrisMove) {
        if (numberOfClosedPotentialMills(morrisMove) > 0) { // doesn't account for double mills atm
            int[] openStone = findOpenStones(-turn).toArray();
            if (openStone.length == 0) {//if all Stones are in Mills, Mills can be broken
                return IntStream.range(0, 24)
                        .filter(i -> i == -turn)
                        .mapToObj(i -> new MorrisMove(morrisMove.getFrom(), morrisMove.getTo(), i));
            }
            return Arrays.stream(openStone)
                    .mapToObj(i -> new MorrisMove(morrisMove.getFrom(), morrisMove.getTo(), i));
        }
        return Stream.of(morrisMove);
    }
<<<<<<< .merge_file_a00232

    /******************************************************************
     *
     *
     *****************************************************************/
    long numberOfClosedPotentialMills(MorrisMove move) {
        final int[][][] mills = {
               /*{{0, 1, 2,}, {0, 6, 7}},
                {{0, 1, 2}, {1, 9, 17}},
                {{0, 1, 2}, {2, 3, 4}},
                {{2, 3, 4}, {19, 11, 3}},
                {{2, 3, 4}, {6, 5, 4}},
                {{6, 5, 4}, {21, 13, 5}},
                {{6, 5, 4}, {0, 6, 7}},
                {{0, 7, 6}, {7, 15, 23}},

                {{8, 9, 10}, {14, 15, 8}},
                {{8, 9, 10}, {1, 9, 17}},
                {{8, 9, 10}, {10, 11, 12}},
                {{10, 11, 12}, {19, 11, 3}},
                {{10, 11, 12}, {14, 13, 12}},
                {{14, 13, 12}, {21, 13, 5}},
                {{14, 13, 12}, {14, 15, 8}},
                {{14, 15, 8}, {7, 15, 23}},

                {{16, 17, 18}, {16, 23, 22}},
                {{16, 17, 18}, {1, 9, 17}},
                {{16, 17, 18}, {18, 19, 20}},
                {{18, 19, 20}, {19, 11, 3}},
                {{18, 19, 20}, {22, 21, 20}},
                {{22, 21, 20}, {21, 13, 22}},
                {{22, 21, 20}, {16, 23, 22}},
                {{16, 23, 22}, {7, 15, 23}}*/

=======
    long numberOfClosedPotentialMills(MorrisMove move) {
        final int[][][] mills = {
>>>>>>> .merge_file_a05588
                {{1, 2,}, {6, 7}},
                {{0, 2}, {9, 17}},
                {{0, 1}, {3, 4}},
                {{2, 4}, {19, 11}},
                {{2, 3}, {6, 5}},
                {{6, 4}, {21, 13}},
                {{5, 4}, {0, 7}},
                {{0, 6}, {15, 23}},

                {{9, 10}, {14, 15}},
                {{8, 10}, {1, 17}},
                {{8, 9}, {11, 12}},
                {{10, 12}, {19, 3}},
                {{10, 11}, {14, 13}},
                {{14, 12}, {21, 5}},
                {{13, 12}, {15, 8}},
                {{14, 8}, {7, 23}},

                {{17, 18}, {23, 22}},
                {{16, 18}, {1, 9}},
                {{16, 17}, {19, 20}},
                {{18, 20}, {11, 3}},
                {{18, 19}, {22, 21}},
                {{22, 20}, {13, 22}},
                {{21, 20}, {16, 23}},
                {{16, 22}, {7, 15,}}
        };

        return Arrays.stream(mills[move.getTo()])
                .map(ints -> Arrays.stream(ints)
                        .map(i -> i == move.getFrom() ? 0 : board[i])
                        .filter(i -> i == turn)
                        .count()
                )
                .filter(i -> i == 2)
                .count();
    }

<<<<<<< .merge_file_a00232

    /******************************************************************
     *
     *
     *****************************************************************/
=======
>>>>>>> .merge_file_a05588
    IntStream findOpenStones(int player) { // player being the encoding for the players stones on the board, +1 or -1
        final int[][][] mills = {
                {{1, 2,}, {6, 7}},
                {{0, 2}, {9, 17}},
                {{0, 1}, {3, 4}},
                {{2, 4}, {19, 11}},
                {{2, 3}, {6, 5}},
                {{6, 4}, {21, 13}},
                {{5, 4}, {0, 7}},
                {{0, 6}, {15, 23}},

                {{9, 10}, {14, 15}},
                {{8, 10}, {1, 17}},
                {{8, 9}, {11, 12}},
                {{10, 12}, {19, 3}},
                {{10, 11}, {14, 13}},
                {{14, 12}, {21, 5}},
                {{13, 12}, {15, 8}},
                {{14, 8}, {7, 23}},

                {{17, 18}, {23, 22}},
                {{16, 18}, {1, 9}},
                {{16, 17}, {19, 20}},
                {{18, 20}, {11, 3}},
                {{18, 19}, {22, 21}},
                {{22, 20}, {13, 22}},
                {{21, 20}, {16, 23}},
                {{16, 22}, {7, 15,}}
        };

        return IntStream.range(0, 24)
                .filter(i -> board[i] == player)
                .filter(i ->
                        Arrays.stream(mills[i]) // stream mill partners
                                .map((int[] ints) -> (Arrays.stream(ints)
                                        .map(i1 -> board[i1])
                                        .filter(i1 -> i1 == player)
                                        .count()))
                                .filter(integer -> integer == 2)
                                .count() == 0);
    }

<<<<<<< .merge_file_a00232

    /******************************************************************
     *  moves():
     *      returns a list which contains all available moves for the current player on the current board
     *
     *****************************************************************/
=======
>>>>>>> .merge_file_a05588
    @Override
    public List<MorrisMove> moves() {
        return streamMoves().collect(Collectors.toList());
    }


    /******************************************************************
     *  getMove():
     *      returns a MorrisMove which holds the information to move from the current board
     *      to the child
     *
     *      returns a illegalArgumentException if no MorrisMove exists which would lead to child
     *
     *****************************************************************/
    public MorrisMove getMove(Morris child) {
        MorrisMove res = new MorrisMove();
        res.setFrom(IntStream.range(0, 24)
                .filter(i -> board[i] == turn)
                .filter(i -> child.board[i] == 0)
                .findAny()
                .orElse(-1)
        );
        res.setTo(IntStream.range(0, 24)
                .filter(i -> board[i] == 0)
                .filter(i -> child.board[i] == turn)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("no MorrisMove possible"))
        );
        res.setRemove(IntStream.range(0, 24)
                .filter(i -> board[i] == -turn)
                .filter(i -> child.board[i] == 0)
                .findAny()
                .orElse(-1)
        );
        return res;
    }

<<<<<<< .merge_file_a00232

    /******************************************************************
     *  getHistory():
     *      -> returns a List, which contains the MorrisMoves used to create the current board
     *
     *****************************************************************/
=======
>>>>>>> .merge_file_a05588
    @Override
    public List<MorrisMove> getHistory() {
        LinkedList<MorrisMove> history = new LinkedList<>();
        Morris child = this;
        while (child.parent != null) {
            history.addFirst(child.parent.getMove(child));
            child = child.parent;
        }
        return history;
    }

<<<<<<< .merge_file_a00232

    /******************************************************************
     *  toString():
     *      -> creates a textual representation of the Morrisboard
     *
     *****************************************************************/
=======
>>>>>>> .merge_file_a05588
    @Override
    public String toString() {
        final int[][] display = {
                {0, -4, -4, -4, -4, -4, 1, -4, -4, -4, -4, -4, 2},
                {-3, -2, -2, -2, -2, -2, -3, -2, -2, -2, -2, -2, -3},
                {-3, -2, 8, -4, -4, -4, 9, -4, -4, -4, 10, -2, -3},
                {-3, -2, -3, -2, -2, -2, -3, -2, -2, -2, -3, -2, -3},
                {-3, -2, -3, -2, 16, -4, 17, -4, 18, -2, -3, -2, -3},
                {7, -4, 15, -4, 23, -2, -2, -2, 19, -4, 11, -4, 3},
                {-3, -2, -3, -2, 22, -4, 21, -4, 20, -2, -3, -2, -3},
                {-3, -2, -3, -2, -2, -2, -3, -2, -2, -2, -3, -2, -3},
                {-3, -2, 14, -4, -4, -4, 13, -4, -4, -4, 12, -2, -3},
                {-3, -2, -2, -2, -2, -2, -3, -2, -2, -2, -2, -2, -3},
                {6, -4, -4, -4, -4, -4, 5, -4, -4, -4, -4, -4, 4}
        };

        char[] repr;
        if (isFlipped) repr = new char[]{'X', '.', 'O', '-', '|', ' '}; // [3] u [4] = ' ' für Spielfeld ohne Linien
        else repr = new char[]{'O', '.', 'X', '-', '|', ' '};
        return IntStream.rangeClosed(0, 10).mapToObj(row -> Arrays.stream(display[row])
                .boxed()
                .map(n -> (n < 0) ? n + 6 : board[n])
                .map(n -> repr[n + 1])
                .map(n -> Character.toString(n))
                .collect(Collectors.joining("  ")) //1-3 Felder Abstand
        ).collect(Collectors.joining("\n", "\n", "")); // prefix "\n" für jShell
    }

<<<<<<< .merge_file_a00232

    /******************************************************************
     *  isWin():
     *      returns true if:
     *          => the current player as less then 3 stones
     *          => the current player as no possible moves
     *      returns false if:
     *          => phase 1 is active
     *
     *     the opponent wins when the return value is true
     *
     *****************************************************************/
=======
>>>>>>> .merge_file_a05588
    @Override
    public boolean isWin() { //return true after winning game, player -turn wins
        return phase != 1 && (streamMoves().count() == 0 ||
                Arrays.stream(board)
                        .filter(n -> turn == n)
                        .count() < 3);
    }

<<<<<<< .merge_file_a00232

    /******************************************************************
     * isDraw():
     *      -> if a games has 50 consecutive moves without removing a stone
     *      the game ends in a draw
     *
     *****************************************************************/
=======
>>>>>>> .merge_file_a05588
    @Override
    public boolean isDraw() {
        return moveswithoutremoving >= 50;
    }

<<<<<<< .merge_file_a00232
    /******************************************************************
     *  flip():
     *      -> switch the colors of the current Board
     *
     *****************************************************************/
=======
>>>>>>> .merge_file_a05588
    @Override
    public ImmutableBoard<MorrisMove> flip() {
        Morris res = new Morris();
        res.parent = parent;
        res.turn = turn;
        res.depth = depth;
        res.isFlipped = !isFlipped;
        res.moveswithoutremoving = moveswithoutremoving;
        res.board = Arrays.copyOf(board, 24);
        return res;
    }

    //\s*(?:Turn\s*\d*\s*:)?\s*(\d+)?\s*->\s*(\d+)\s*(?::\s*(\d+))?\s* regex for Loading
    @Override
    public ImmutableBoard<MorrisMove> load(String name) {
        return load(Paths.get(name));
    }

    @Override
    public ImmutableBoard<MorrisMove> load(Path path) {
        final Pattern pattern = Pattern.compile("\\s*(?:Turn\\s*\\d*\\s*:)?\\s*(\\d+)?\\s*->\\s*(\\d+)\\s*(?::\\s*(\\d+))?\\s*");
        ImmutableBoard<MorrisMove> morris = new Morris();
        try {
            if (Files.lines(path, StandardCharsets.UTF_8)
                    .filter(s -> !s.isEmpty())
                    .limit(1L)
                    .map(s -> s.split(":")[1])
                    .map(String::trim)
                    .map(Boolean::valueOf)
                    .findAny()
                    .get()
                    ) {
                morris = morris.flip();
                System.out.println("Test");
            }
            List<MorrisMove> moves = Files.lines(path, StandardCharsets.UTF_8)
                    .filter(s -> !s.isEmpty())
                    .skip(1L)
                    .map(pattern::matcher)
                    .map(matcher -> {
                        if (!matcher.matches()) throw new IllegalArgumentException("Save was corrupted");
                        return new MorrisMove(matcher.group(1) == null ? -1 : Integer.parseInt(matcher.group(1)),
                                Integer.parseInt(matcher.group(2)),
                                matcher.group(3) == null ? -1 : Integer.parseInt(matcher.group(3)));
                    })
                    .collect(Collectors.toList());
            for (MorrisMove move : moves) {
                morris = morris.makeMove(move);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return morris;
    }

    @Override
    public ImmutableBoard<MorrisMove> save(String name) {
        return save(Paths.get(name));
    }

    @Override
    public ImmutableBoard<MorrisMove> save(Path path) {
        try (BufferedWriter out = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            int turnCounter = 1;
            out.write("isFlipped: " + String.valueOf(isFlipped) + "\n");
            for (MorrisMove move : getHistory()) {
                out.write(String.format("Turn %3d:", turnCounter++));
                if (move.getFrom() != -1) {
                    out.write(String.format("%3d", move.getFrom()));
                } else out.write("   ");
                out.write(String.format(" -> %3d", move.getTo()));
                if (move.getRemove() != -1) {
                    out.write(String.format(" : %3d", move.getRemove()));
                }
                out.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
}