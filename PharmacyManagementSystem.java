import java.sql.*;
import java.util.Scanner;

public class PharmacyManagementSystem {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/pharmacy_db";
    private static final String USER = "root";
    private static final String PASS = "your MYSQL password@";

    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    // ----USER CLASS ----
    static class User {
        private final int id;
        private final String username;
        private final String role;

        public User(int id, String username ,String role) {
            this.id = id;
            this.username = username;
            this.role = role;
        }
        public String getRole() {return role;}
        public String getUsername() {return username; }
    }

    //----medicine class----
    static class Medicine {
        private final int id;
        private final String name;
        private final double price;
        private final double costPrice;
        private final int quantity;
        private final Date expiryDate;

        public Medicine (int id, String name ,double price ,double costPrice, int quantity, Date expiryDate) {
            this.id = id;
            this.name = name;
            this.price = price ;
            this.costPrice = costPrice;
            this.quantity = quantity;
            this.expiryDate = expiryDate;
        }
        @Override
        public String toString() {
            return String.format("ID: %d | Name :%s | Price: $%.2f | Cost: $%.2f | Qty: %d | Exp: %s ",
                    id ,name ,price ,costPrice , quantity ,expiryDate);
        }
    }

    static class Customer {
        private final int id ;
        private final String name;
        private final String phone;
        private final String email;
        private final Date createdAt;

        public Customer(int id, String name, String phone, String email, Date createdAt) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.createdAt = createdAt;
        }
        public int getId() {return id;}
        public String getName() {return name;}

        @Override
        public String toString() {
            return String.format("ID: %d | Name: %s | Phone: %s | Email: %s ", id, name,  phone ,email);
        }
    }

    //----Database Connection-----
    private static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER , PASS);
        }catch (SQLException e) {
            System.err.println("✖️ Database connection failed; " +e.getMessage());
            return null;
        }
    }

    //------login system--------
    private static User authenticate() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        String sql = "SELECT id ,username, role FROM users WHERE username= ? AND password =?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2,password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("✅ Login successfull!");
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"));
            }else {
                System.out.println("✖️ Invalid credentials.");
            }
        }catch (SQLException e) {
            System.err.println("Login error: " +e.getMessage());
        }
        return null;
    }

    //---------Main menu-----------
    private static void showMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("--------------------PHARMACY MANAGEMENT SYSTEM--------------------");
            System.out.println("=".repeat(40));
            System.out.printf("🧑🏻‍🦱 Logged in: %s (%s)\n" , currentUser.getUsername(), currentUser.getRole());
            System.out.println("=".repeat(40));

            System.out.println("1. Add Medicine" );
            if (currentUser.getRole().equals("manager")){
                System.out.println("2. Update Medicine");
                System.out.println("3. Delete Medicine");
            }
            System.out.println("4. View Stock");
            System.out.println("5. Search Medicine");
            System.out.println("6. Add Customer");
            System.out.println("7. Record Sale");
            System.out.println("8.Logout");

            int choice = getInt("choose an option: ");

            try{
                switch(choice) {
                    case 1 -> addMedicine();
                    case 2 -> {
                        if (currentUser.getRole().equals("manager")) {
                            updateMedicine();
                        } else {
                            invalid();
                        }
                    }
                    case 3 -> {
                        if (currentUser.getRole().equals("manager")) {
                            deleteMedicine();
                        } else {
                            invalid();
                        }
                    }


                    case 4 -> viewStock();
                    case 5 -> searchMedicine();
                    case 6 -> addCustomer();
                    case 7 -> recordSale();
                    case 8 -> {
                        System.out.println("👋🏻 Logged out. Goodbye!");
                        return;
                    }
                    default -> System.out.println("✖️ Invalid choice. Try again.");
                }
            }catch (Exception e) {
                System.err.println("⚠️ Operation failed: " +e.getMessage());
            }

        }
    }
    private static void invalid() {
        System.out.println("✖️ Access deined. Managers only.");
    }

    // ----add medicine -----

    private static void addMedicine() {
        System.out.print("Medicine Name: ");
        String name = scanner.nextLine();
        double price = getDouble("Selling Price: ");
        double costPrice = getDouble("Cost Price: ");
        int quantity = getInt("Quantity: ");
        System.out.print("Expiry Date (YYYY-MM-DD) ");
        String expiry = scanner.nextLine();

        String sql = "INSERT INTO medicines (name, price, cost_price ,quantity, expiry_date) VALUES (?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setDouble(3, costPrice);
            stmt.setInt(4, quantity);
            stmt.setDate(5,Date.valueOf(expiry));
            stmt.executeUpdate();
            System.out.println("✅ Medicine added successfully!");

        }catch (SQLException e) {
            System.err.println("✖️ Failed to add medicine: " +e.getMessage());
        }
    }
    // ---------update medicine-----
    private static void updateMedicine() {
        int id = getInt("Enter Medicine ID to update: ");
        double price = getDouble("New Selling Price: ");
        int quantity = getInt("New Quantity: ");

        String sql = "UPDATE medicines SET price = ? , quantity = ? , updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, price);
            stmt.setInt(2, quantity);
            stmt.setInt(3, id);
            int rows  = stmt.executeUpdate();
            System.out.println( rows >0 ? "✅ Medicine updated." : "✖️Medicine not found.");

        }catch (SQLException e) {
            System.err.println("✖️ updaete failed: " + e.getMessage());
        }
    }

    // ---------delete medicine ----------
    private static void deleteMedicine() {
        int id = getInt("Enter Medicine ID to delete: ");
        String sql = "DELETE FROM medicines WHERE id= ?";
        try (Connection conn =getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows>0 ? "✅ Medicine deleted." : "✖️ Not found.");

        }catch (SQLException e) {
            System.err.println("✖️ Delete failed: " +e.getMessage());
        }
    }

    private static void viewStock(){
        String sql = "SELECT *FROM medicines ORDER BY name";
        boolean found = false;
        try (Connection conn= getConnection();
              Statement stmt = conn.createStatement();
              ResultSet rs = stmt.executeQuery(sql)) {

              while (rs.next()){
                  found = true;
                  Medicine med= new Medicine(
                          rs.getInt("id"),
                          rs.getString("name"),
                          rs.getDouble("price"),
                          rs.getDouble("cost_price"),
                          rs.getInt("quantity"),
                          rs.getDate("expiry_date")
                  );
                  System.out.println(med);
                  if (rs.getInt("quantity")<10) {
                      System.out.println("⚠️ LOW STOCK ALERT!");
                  }
              }
              if (!found) System.out.println("💊 No medicine in stock.");

        }catch (SQLException e ) {
            System.err.println("✖️ view stock failed: " + e.getMessage());
        }
    }

    // ----------------search medicine ------------
    private static void searchMedicine(){
        System.out.println("Search medicine by name: ");
        String keyword = scanner.nextLine();
        String sql = "SELECT  * FROM medicines WHERE name  LIKE ?";
        try(Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(new Medicine(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getDouble("cost_price"),
                        rs.getInt("quantity"),
                        rs.getDate("expiry_date")
                ));
            }
            if (!found) System.out.println("🔍 No medicine found.");

        }catch (SQLException e) {
            System.err.println("✖️ Search failed: " + e.getMessage());
        }
    }

    // --------------Add customers -----------
    private static void addCustomer() {
        System.out.println("Customer Name: ");
        String name = scanner.nextLine();
        System.out.println("Phone: ");
        String phone = scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO customers (name, phone ,email) VALUES (?,?,?)";
        try(Connection conn = getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, name) ;
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.executeUpdate();
            System.out.println("✅ Customer added successfully!");

        }catch (SQLException e) {
            System.err.println("✖️ Failed to add customer: " +e.getMessage());
        }
    }

    //-------------Record sale with transaction--------------
    private static void recordSale() {
        int medId = getInt("Medicine ID: ");
        int custId = getInt("Customer ID: ");
        int qty = getInt("Quantity: ");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            conn = getConnection();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement("SELECT price , cost_price ,quantity FROM medicines WHERE id = ?");
            stmt.setInt(1,medId);
            rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("✖️ Medicine not found." );
                return ;
            }

            double price = rs.getDouble("price");
            double costPrice = rs.getDouble("cost_price");
            int availableQty = rs.getInt("quantity");

            if (availableQty <qty) {
                System.out.println("✖️ Insufficient stock! Available: " + availableQty);
                return;
            }
            double totalPrice = price *qty;
            double profit = (price - costPrice)*qty;

            //Insert sale ----------
            stmt = conn.prepareStatement("INSERT INTO sales (medicine_id, customer_id, quantity_sold, total_price) VALUES (?,?,?,?)");
            stmt.setInt(1, medId);
            stmt.setInt(2, custId);
            stmt.setInt(3, qty);
            stmt.setDouble(4, totalPrice);
            stmt.executeUpdate();

            //-------------Update stock ----------
            stmt = conn.prepareStatement("UPDATE medicines SET quantity= quantity-? WHERE id = ?");
            stmt.setInt(1, qty);
            stmt.setInt(2,medId);
            stmt.executeUpdate();
            conn.commit();
            System.out.printf("✅ Sale recorded! Revenue: $%.2f | Profit: $%.2f\n", totalPrice,profit);

        }catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch ( SQLException ex) { ex.printStackTrace(); }
                }
            System.err.println("✖️ Sale failed: " +e.getMessage());
        }finally {
            closeQuietly(rs,stmt, conn);
        }
    }

    //-------------helper methods-----------
    private static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            }catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a valid number.");
            }
        }

    }


    private static double getDouble(String prompt) {
        while(true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            }catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a valid number.");
            }
        }
    }

    private static void closeQuietly(AutoCloseable...closables) {
        for(AutoCloseable c: closables) {
            if (c != null) {
                try{c.close(); } catch (Exception ignored){}
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("💊💊 ------------- Welcome to PHARMACY MANAGEMENT SYSTEM ------------- 💊💊" );


        while (currentUser == null) {
            currentUser = authenticate();
        }

        showMenu();
    }
}