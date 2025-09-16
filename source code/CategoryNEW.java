class CategoryNode
{
    private String name;
    private double price;
    private int availableSeats;

    public CategoryNode(String name, double price, int availableSeats)
    {
        this.name = name;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    public String getName()
    {
        return name;
    }
    public double getPrice()
    {
        return price;
    }
    public int getAvailableSeats()
    {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats)
    {
        this.availableSeats = availableSeats;
    }
    public void decrementAvailableSeats()
    {
        this.availableSeats--;
    }
    public void incrementAvailableSeats()
    {
        this.availableSeats++;
    }
}
