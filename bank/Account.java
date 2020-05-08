package bank;

class Account
{
    // Attributes to need in this Kata
    private String name;
    private int balance;
    private int threshold;
    private boolean isBlocked;

    // Constructor of our attributes
    public Account(String name, int balance, int threshold, boolean isBlocked)
    {
        this.name=name;
        this.balance=balance;
        this.threshold=threshold;
        this.isBlocked=isBlocked;
    }

    // Methods of our constructor
    public String getName()
    {
        return name;
    }

    public int getBalance()
    {
        return balance;
    }

    public int getThreshold()
    {
        return threshold;
    }

    public boolean getIsBlocked()
    {
        return isBlocked;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setBalance(int balance)
    {
        this.balance = balance;
    }

    public void setThreshold(int threshold)
    {
        this.threshold = threshold;
    }

    public void setIsBlocked(boolean isBlocked)
    {
        this.isBlocked = isBlocked;
    }

    public String toString()
    {

        String result = "";
        result += this.name + " | ";
        result += this.balance + " | ";
        result += this.threshold + " | ";
        result += this.isBlocked + "\n";

        return result;
    }
}
