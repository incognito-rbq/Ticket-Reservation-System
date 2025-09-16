// Seat class
class Seat implements Comparable<Seat>
{
    private int number;
    private String category;
    private boolean isReserved;
    private String reservedBy = null;

    Seat(int number, String category) {
        this.number = number;
        this.category = category;
        this.isReserved = false;
    }

    public int getNumber() {
        return number;
    }
    public String getCategory() {
        return category;
    }
    public String getReservedBy() {
        return reservedBy;
    }
    public boolean isReserved() {
        return isReserved;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }
    public void setReservedBy(String reservedBy) {
        this.reservedBy = reservedBy;
    }

    @Override
    public int compareTo(Seat other) {
        int cmp = this.category.compareTo(other.category);
        return cmp != 0 ? cmp : Integer.compare(this.number, other.number);
    }

    @Override
    public String toString() {
        return "Seat Number: " + getNumber()+
                "\nCategory: " +getCategory()+
                "\nReservedBy: " +getReservedBy()+
                "\nReservation Status: " +isReserved();
    }
}

class Action
{
    String type;
    Seat seat;
    String reservedBy;

    Action(String type, Seat seat,String reservedBy)
    {
        this.type = type;
        this.seat = seat;
        this.reservedBy = reservedBy;
    }
}