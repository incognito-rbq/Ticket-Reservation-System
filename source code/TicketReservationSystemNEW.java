import java.util.*;
import java.io.*;



class TicketSystem
{
    private final TreeSet<Seat> seats = new TreeSet<>();
    private final HashMap<Integer, Seat> seatMap = new HashMap<>();
    private final Stack<Action> bookingStack = new Stack<>();
    private final Stack<Action> cancellationStack = new Stack<>();

    private final HashMap<String, CategoryNode> categoryMap = new HashMap<>();

    public TicketSystem()
    {
        categoryMap.put("VIP", new CategoryNode("VIP", 1000, 100));
        categoryMap.put("Economy", new CategoryNode("Economy", 500, 400));

        int seatNumber = 1;
        for (CategoryNode category : categoryMap.values()) {
            for (int i = 0; i < category.getAvailableSeats(); i++) {
                addSeat(seatNumber++, category.getName());
            }
        }
    }

    private void addSeat(int number, String category) {
        Seat seat = new Seat(number, category);
        seatMap.put(number, seat);
        seats.add(seat);
    }

    public void reserveSeat(String customerName, String categoryName)
    {
        for (Seat seat : seats)
        {
            if (seat.getCategory().equals(categoryName) && !seat.isReserved())
            {
                seat.setReserved(true);
                seat.setReservedBy(customerName);
                bookingStack.push(new Action("BOOK", seat, seat.getReservedBy()));
                categoryMap.get(categoryName).decrementAvailableSeats();
                System.out.println("Seat " + seat.getNumber() + " in category " + categoryName + " reserved for " + customerName);
                return;
            }
        }
        System.out.println("No available seats in category: " + categoryName);
    }

    public void cancelSeat(int number) {
        Seat seat = seatMap.get(number);
        if (seat != null && seat.isReserved()) {
            cancellationStack.push(new Action("CANCEL", seat, seat.getReservedBy()));
            seat.setReserved(false);
            seat.setReservedBy(null);
            categoryMap.get(seat.getCategory()).incrementAvailableSeats();
            System.out.println("Reservation for seat " + number + " cancelled.");
        } else {
            System.out.println("Seat not reserved.");
        }
    }

    public void undoBooking() {
        if (!bookingStack.isEmpty()) {
            Action last = bookingStack.pop();
            last.seat.setReserved(false);
            last.seat.setReservedBy(null);
            categoryMap.get(last.seat.getCategory()).incrementAvailableSeats();
            System.out.println("Undo booking: Seat " + last.seat.getNumber() + " is now available.");
        } else {
            System.out.println("No booking to undo.");
        }
    }

    public void undoCancellation()
    {
        if (!cancellationStack.isEmpty())
        {
            Action last = cancellationStack.pop();
            last.seat.setReserved(true);
            last.seat.setReservedBy(last.reservedBy);
            categoryMap.get(last.seat.getCategory()).decrementAvailableSeats();
            System.out.println("Undo cancellation: Seat " + last.seat.getNumber() + " is now reserved again.");
        } else {
            System.out.println("No cancellation to undo.");
        }
    }

    public void lookupSeat(int number) {
        Seat seat = seatMap.get(number);
        if (seat != null) {
            System.out.println(seat);
        } else {
            System.out.println("Seat not found.");
        }
    }

    public void printSeats() {
        for (Seat seat : seats) {
            System.out.println(seat);
        }
    }

    public void printCategories() {
        for (CategoryNode category : categoryMap.values()) {
            int available = 0;
            for (Seat seat : seats) {
                if (seat.getCategory().equals(category.getName()) && !seat.isReserved()) {
                    available++;
                }
            }
            System.out.println("Category: " + category.getName() + ", Price: " + category.getPrice() + ", Available: " + available);
        }
    }

    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Seat seat : seats) {
                writer.write(seat.getNumber() + "," + seat.getCategory() + "," + seat.isReserved() + "," + (seat.getReservedBy() != null ? seat.getReservedBy() : ""));
                writer.newLine();
            }
            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
        catch (Exception e) { // Catch parsing errors
            System.out.println("Parsing error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadFromFile(String filename) {
        // Clear existing data
        seats.clear();
        seatMap.clear();
        bookingStack.clear();
        cancellationStack.clear();


        for (CategoryNode category : categoryMap.values()) {
            category.setAvailableSeats(0);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int number = Integer.parseInt(parts[0]);
                String category = parts[1];
                boolean reserved = Boolean.parseBoolean(parts[2]);
                String reservedBy = (parts.length > 3 && !parts[3].isEmpty()) ? parts[3] : null;

                // Create seat
                Seat seat = new Seat(number, category);
                seat.setReserved(reserved);
                seat.setReservedBy(reservedBy);

                // Add to collections
                seats.add(seat);
                seatMap.put(number, seat);
            }

            // Recalculate available seats for each category
            for (CategoryNode category : categoryMap.values()) {
                int availableCount = 0;
                for (Seat seat : seats) {
                    if (seat.getCategory().equals(category.getName()) && !seat.isReserved()) {
                        availableCount++;
                    }
                }
                category.setAvailableSeats(availableCount);
            }

            System.out.println("Data loaded from " + filename);
            System.out.println("Total seats loaded: " + seats.size());

        } catch (IOException e) {
            System.out.println("Error loading data from file '" + filename + "': " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing seat data: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        TicketSystem system = new TicketSystem();
//        String[] names = {"Alice", "Bob", "Charlie", "Diana", "Edward", "Fiona", "George", "Hannah", "Ivy", "Jack"};
//        int vipSeats = 100;
//        int economySeats = 400;
//        int seatNumber = 1;
//
//        // Add and reserve 100 VIP seats
//        for (int i = 0; i < vipSeats; i++)
//        {
//            system.addSeat(seatNumber, "VIP");
//            String name = names[seatNumber % names.length];
//            system.reserveSeat(name, "VIP");
//            seatNumber++;
//        }
//
//        // Add and reserve 400 Economy seats
//        for (int i = 0; i < economySeats; i++)
//        {
//            system.addSeat(seatNumber, "Economy");
//            String name = names[seatNumber % names.length];
//            system.reserveSeat(name, "Economy");
//            seatNumber++;
//        }



        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(" ===== Ticket Reservation Menu =====");
            System.out.println("1. Reserve Seat");
            System.out.println("2. Cancel Seat");
            System.out.println("3. Undo Booking");
            System.out.println("4. Undo Cancellation");
            System.out.println("5. Lookup Seat");
            System.out.println("6. View All Seats");
            System.out.println("7. View Category Summary");
            System.out.println("8. Save Data");
            System.out.println("9. Load Data");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter customer name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter category (VIP/Economy): ");
                    String category = scanner.nextLine();
                    system.reserveSeat(name, category);
                    break;
                case 2:
                    System.out.print("Enter seat number to cancel: ");
                    int cancelSeat = scanner.nextInt();
                    system.cancelSeat(cancelSeat);
                    break;
                case 3:
                    system.undoBooking();
                    break;
                case 4:
                    system.undoCancellation();
                    break;
                case 5:
                    System.out.print("Enter seat number to lookup: ");
                    int lookup = scanner.nextInt();
                    system.lookupSeat(lookup);
                    break;
                case 6:
                    system.printSeats();
                    break;
                case 7:
                    system.printCategories();
                    break;
                case 8:
                    system.saveToFile("seats.txt");
                    break;
                case 9:
                    system.loadFromFile("seats.txt");
                    break;
                case 0:
                    System.out.println("Exiting system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
