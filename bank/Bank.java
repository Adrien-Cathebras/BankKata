//Adrien CATHEBRAS
// Classe : 2ESGIA1
// All Tests Passed
package bank;


import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;


public class Bank {

    // Connection strings to the mysql database / Used for this Kata
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mysql?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final String TABLE_NAME = "accounts";

    private Connection c;

    public Bank()
    {
        initDb();
        createTableAccount();
    }

    // Initialization of our Database
    private void initDb()
    {
        try
        {
            Class.forName(JDBC_DRIVER);
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Opened database successfully");

        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Closing our Database
    public void closeDb()
    {
        try
        {
            c.close();
        }
        catch (SQLException e)
        {
            System.out.println("Could not close the database : " + e);
        }
    }

    // Creation of the account table, by default the user's account is not blocked.
    private void createTableAccount()
    {
        try (Statement statement = c.createStatement())
        {
            statement.executeUpdate("CREATE TABLE " + TABLE_NAME + "(\n" +
                    "name VARCHAR(255) NOT NULL," +
                    "balance INT NOT NULL," +
                    "threshold INT NOT NULL," +
                    "isBlocked BOOLEAN NOT NULL DEFAULT false)");
        }
        catch (Exception e)
        {
            System.out.println("The account table has been created." + e);
        }
    }

    //Permits the deletion of the accounts table from the database.
    void dropAllTables() {
        try (Statement s = c.createStatement()) {
            s.executeUpdate(
                    "DROP TABLE " + "accounts" );
        } catch (Exception e) {
            System.out.println("La table à été effacé" + e);
        }
    }

    // Allows us to know if the user already exists in base
    private Account checkNewAcount(String name)
    {
        Account account;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE name='"+ name +"';";
        try (PreparedStatement s = c.prepareStatement(query)){
            ResultSet r = s.executeQuery();
            if(r.next())
            {
                return new Account(r.getString(1), r.getInt(2), r.getInt(3), r.getBoolean(4));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Creation of a new user account that will be added to our database
    public void createNewAccount(String name, int balance, int threshold)
    {
        if (threshold <= 0) {
            try (Statement s = c.createStatement())
            {
                s.executeUpdate("INSERT INTO " + TABLE_NAME + " " +
                        "(name, balance, threshold, isBlocked) " +
                        "VALUES " +
                        "('" + name + "','" + balance + "','" + threshold + "',false)");
                System.out.println("Congratulations, your account has been successfully created.");
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }}

    }

    //Allows us to display all the accounts present with all the information in the database.
    public String printAllAccounts()
    {
        String query = "SELECT * FROM " + TABLE_NAME;
        //StringBuilder allows you to concatenate strings in an optimized way.
        StringBuilder builder =  new StringBuilder();

        try (PreparedStatement s = c.prepareStatement(query))
        {
            ResultSet r = s.executeQuery();
            while (r.next())
            {
                builder.append((new Account(r.getString(1), r.getInt(2), r.getInt(3), r.getBoolean(4))).toString());
            }
        }
        catch (Exception e)
        {
            System.out.println("Here are all the accounts.");
        }

        return builder.toString();
    }


    public void changeBalanceByName(String name, int balanceModifier) { //Update de la bdd en changeant le solde du compte
        // TODO
        Account ac = checkNewAcount(name);

        int newBalance = ac.getBalance() + balanceModifier;
        if (newBalance >= ac.getThreshold()) {
            if (ac.getIsBlocked() == false) {
                ac.setBalance(newBalance);
                try (Statement s = c.createStatement()) {
                    s.executeUpdate("UPDATE " + TABLE_NAME + " SET " +
                            " balance = '" + ac.getBalance() + "' WHERE name = '" + name + "'");
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            } else {
                System.out.println("This balance is below the threshold");
            }}
    }



    // Allows us to block the user's account
    public void blockAccount(String name)
    {
        try (Statement s = c.createStatement())
        {
            s.executeUpdate("UPDATE " + TABLE_NAME + " SET " +
                    " isBlocked = true WHERE name = '"+name+"'");
        }
        catch (Exception e)
        {
            System.out.println("The account has been locked.");
        }
    }


    // For testing purpose
    String getTableDump() {
        String query = "select * from " + TABLE_NAME;
        String res = "";

        try (PreparedStatement s = c.prepareStatement(query)) {
            ResultSet r = s.executeQuery();

            // Getting nb colmun from meta data
            int nbColumns = r.getMetaData().getColumnCount();

            // while there is a next row
            while (r.next()){
                String[] currentRow = new String[nbColumns];

                // For each column in the row
                for (int i = 1 ; i <= nbColumns ; i++) {
                    currentRow[i - 1] = r.getString(i);
                }
                res += Arrays.toString(currentRow);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return res;
    }
}