package cinema;

import cinema.exceptions.CoordinatesOutOfBorderException;
import cinema.exceptions.SeatAlreadyPurchasedException;

import java.util.Scanner;

public class Cinema {

    private static final int firstPrice = 10;
    private static final int secondPrice = 8;

    private static Seat[][] room;

    private static int rows;
    private static int seats;
    private static int purchasedTickets;
    private static int totalSeats;
    private static int currentIncome;
    private static int totalIncome;

    private static boolean isRunning;

    public static void main(String[] args) {
        getSizeOfRoom();
        initialize();

        while (isRunning) {
            printMenu();
            takeAction(getUserInput());
        }
    }

    private static void initialize() {
        room = new Seat[rows][seats];
        isRunning = true;
        purchasedTickets = 0;
        totalSeats = rows * seats;
        currentIncome = 0;
        totalIncome = calculateTotalIncome();

        int totalSeats = rows * seats;
        if (totalSeats <= 60) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < seats; j++) {
                    room[i][j] = Seat.FIRST_HALF_SEAT;
                }
            }
        } else {
            int firstHalfOfRows = rows / 2;
            int secondHalfOfRows = rows - firstHalfOfRows - 1;
            for (int i = 0; i < firstHalfOfRows; i++) {
                for (int j = 0; j < seats; j++) {
                    room[i][j] = Seat.FIRST_HALF_SEAT;
                }
            }

            for (int i = secondHalfOfRows; i < rows; i++) {
                for (int j = 0; j < seats; j++) {
                    room[i][j] = Seat.SECOND_HALF_SEAT;
                }
            }
        }
    }

    private static void takeAction(int userInput) {
        switch (userInput) {
            case 1:
                drawScheme();
                break;
            case 2:
                buyTicket();
                break;
            case 3:
                showStatistics();
                break;
            case 0:
                exit();
                break;
        }
    }

    private static void showStatistics() {
        System.out.printf("\nNumber of purchased tickets: %d\n", getNumberOfPurchasedTickets());
        System.out.printf("Percentage: %.2f%s\n", getPercentage(), "%");
        System.out.printf("Current income: $%d\n", getCurrentIncome());
        System.out.printf("Total income: $%d\n\n", getTotalIncome());
    }

    private static int getTotalIncome() {
        return totalIncome;
    }

    private static int getCurrentIncome() {
        return currentIncome;
    }

    private static double getPercentage() {
        return (double) purchasedTickets / totalSeats * 100;
    }

    private static int getNumberOfPurchasedTickets() {
        return purchasedTickets;
    }

    private static void exit() {
        isRunning = false;
    }

    private static void printMenu() {
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
    }

    private static void buyTicket() {
        boolean isCorrect = false;
        int chosenRow = -1;
        int chosenSeat = -1;

        do {
            try {
                System.out.println("Enter a row number:");
                chosenRow = getUserInput() - 1;
                System.out.println("Enter a seat number in that row:");
                chosenSeat = getUserInput() - 1;

                // Throws SeatAlreadyPurchasedException and CoordinatesOutOfBorderException
                checkUserCoordinates(chosenRow, chosenSeat);

                isCorrect = true;
            } catch (SeatAlreadyPurchasedException e) {
                System.out.println("\nThat ticket has already been purchased!\n");
            } catch (CoordinatesOutOfBorderException e) {
                System.out.println("\nWrong input!\n");
            }
        } while (!isCorrect);

        int price = getPrice(room[chosenRow][chosenSeat]);
        System.out.printf("\nTicket price: $%d\n\n", price);
        room[chosenRow][chosenSeat] = Seat.BOUGHT;
        purchasedTickets++;
        currentIncome += price;
    }

    private static void checkUserCoordinates(int row, int seat) throws SeatAlreadyPurchasedException, CoordinatesOutOfBorderException {
        if (row < 0 || row >= rows || seat < 0 || seat >= seats) {
            throw new CoordinatesOutOfBorderException();
        }

        if (isSeatPurchased(room[row][seat])) {
            throw new SeatAlreadyPurchasedException();
        }
    }

    private static boolean isSeatPurchased(Seat seat) {
        return seat == Seat.BOUGHT;
    }

    private static int getPrice(Seat seat) {
        return seat == Seat.FIRST_HALF_SEAT ? firstPrice : secondPrice;
    }

    private static String parse(Seat seat) {
        switch (seat) {
            case FIRST_HALF_SEAT:
            case SECOND_HALF_SEAT:
                return "S";
            case BOUGHT:
                return "B";
            default:
                return null;
        }
    }

    private static void getSizeOfRoom() {
        System.out.println("Enter the number of rows:");
        rows = getUserInput();
        System.out.println("Enter the number of seats in each row:");
        seats = getUserInput();
    }

    private static int calculateTotalIncome() {
        if (totalSeats <= 60) {
            return totalSeats * 10;
        } else {
            int firstHalfOfRows = rows / 2;
            int secondHalfOfRows = rows - firstHalfOfRows;
            return seats * firstHalfOfRows * 10 + seats * secondHalfOfRows * 8;
        }
    }

    private static int getUserInput() {
        Scanner scanner = new Scanner(System.in);

        return scanner.nextInt();
    }

    private static void drawScheme() {
        System.out.println("\nCinema:");

        System.out.print(" ");
        for (int i = 1; i <= seats; i++) {
            System.out.print(" " + i);
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            System.out.print(i + 1);
            for (int j = 0; j < seats; j++) {
                System.out.print(" " + parse(room[i][j]));
            }
            System.out.println();
        }

        System.out.println();
    }
}