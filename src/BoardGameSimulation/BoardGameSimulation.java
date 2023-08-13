package BoardGameSimulation;

import java.util.*;

public class BoardGameSimulation {

    public static class Player {
        String name;
        int score;
        Node currentPosition;
        int moveCount;

        public Player(String name) {
            this.name = name;
            this.score = 0;
            this.moveCount = 0;
        }

        public void move(int steps, Node endNode) {
            while (steps > 0 && currentPosition != endNode) {
                currentPosition = currentPosition.next;
                steps--;
            }
            if (currentPosition.players.size() > 0) {
                Player displacedPlayer = currentPosition.players.remove(0);
                displacedPlayer.moveBack(7);
            }
            currentPosition.players.add(this);
            score += currentPosition.value;
            moveCount++;
        }

        public void moveBack(int steps) {
            while (steps > 0 && currentPosition.prev != null) {
                currentPosition = currentPosition.prev;
                steps--;
            }
            currentPosition.players.add(this);
        }
    }

    public static class Node {
        int value;
        Node next;
        Node prev;
        List<Player> players;

        public Node(int value) {
            this.value = value;
            this.players = new ArrayList<>();
        }
    }

    public static class Board {
        Node start;
        Node end;

        public Board(int[] values) {
            Node prevNode = new Node(values[0]);
            start = prevNode;
            for (int i = 1; i < values.length; i++) {
                Node newNode = new Node(values[i]);
                prevNode.next = newNode;
                newNode.prev = prevNode;
                prevNode = newNode;
            }
            end = prevNode;
        }

        public void resetPlayers(List<Player> players) {
            for (Player player : players) {
                player.currentPosition = start;
                player.score = 0;
                player.moveCount = 0;
            }
        }

        public void printBoard() {
            Node currentNode = start;
            while (currentNode != null) {
                System.out.print(currentNode.value + " ");
                currentNode = currentNode.next;
            }
            System.out.println();
        }
    }

    public static class Game {
        List<Player> players;
        Board board;
        Random random;

        public Game(List<Player> players, int[] boardValues) {
            this.players = players;
            this.board = new Board(boardValues);
            this.random = new Random();
        }

        public void play() {
            board.resetPlayers(players);
            while (true) {
                for (Player player : players) {
                    int roll = random.nextInt(6) + 1;
                    player.move(roll, board.end);
                    if (player.currentPosition == board.end && player.score >= 44) {
                        return;
                    } else if (player.currentPosition == board.end) {
                        player.moveBack(player.moveCount);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        List<Player> players = new ArrayList<>(Arrays.asList(new Player("A"), new Player("B"), new Player("C"), new Player("D")));
        int[] boardValues = {5, 10, 8, 10, 7, 5, 9, 10, 6, 7, 10, 6, 5, 8, 9, 5, 10, 5, 9, 6, 8, 7, 10, 6, 8};

        for (int i = 1; i <= players.size(); i++) {
            List<Player> currentPlayers = players.subList(0, i);
            int totalMoves = 0;
            for (int j = 0; j < 1000; j++) {
                Game game = new Game(currentPlayers, boardValues);
                game.play();
                totalMoves += currentPlayers.get(0).moveCount;
                if ((j+1) % 100 == 0) {
                    System.out.println("After " + (j+1) + " games:");
                    game.board.printBoard();
                }
            }
            System.out.println("Average moves for player " + currentPlayers.get(0).name + ": " + (totalMoves / 1000.0));
        }
    }

}

