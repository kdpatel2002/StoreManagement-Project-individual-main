import java.io.*;
import java.util.*;

class Product {
    private String name;
    private int id;
    private double price;
    private int quantity;

    public Product(String name, int id, double price, int quantity) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product [name=" + name + ", id=" + id + ", price=" + price + ", quantity=" + quantity + "]";
    }
}

interface StoreOperations {
    void addProduct(Product product);

    void updateProduct(int id, Product updatedProduct);

    void deleteProduct(int id);

    void displayProducts();

    void saveToFile(String fileName);

    void loadFromFile(String fileName);
}

class StoreManagement implements StoreOperations {
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayDeque<Product> recentChanges = new ArrayDeque<>();

    @Override
    public void addProduct(Product product) {
        products.add(product);
        recentChanges.addFirst(product);
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    @Override
    public void updateProduct(int id, Product updatedProduct) {

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                products.set(i, updatedProduct);
                recentChanges.addFirst(updatedProduct);
                break;
            }
        }
    }

    @Override
    public void deleteProduct(int id) {
        products.removeIf(product -> product.getId() == id);
        recentChanges.addFirst(new Product("Deleted", id, 0.0, 0));
    }

    @Override
    public void displayProducts() {
        if (this.products.isEmpty()) {
            System.out.println("\nNo products available\n");
        }
        for (Product product : products) {
            System.out.println(product);
        }
    }

    @Override
    public void saveToFile(String fileName) {
        try (FileWriter fw = new FileWriter(fileName);
                BufferedWriter bw = new BufferedWriter(fw)) {
            for (Product product : products) {
                bw.write(product.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    @Override
    public void loadFromFile(String fileName) {
        products.clear();
        try (FileReader fr = new FileReader(fileName);
                BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                // Parse the line and create a Product object
                String[] productData = line.substring(line.indexOf('[') + 1, line.indexOf(']')).split(", ");
                String name = productData[0].split("=")[1];
                int id = Integer.parseInt(productData[1].split("=")[1]);
                double price = Double.parseDouble(productData[2].split("=")[1]);
                int quantity = Integer.parseInt(productData[3].split("=")[1]);

                Product product = new Product(name, id, price, quantity);
                products.add(product);
            }
        } catch (IOException e) {
            System.err.println("Error loading data from file: " + e.getMessage());
        }
    }
}

public class Main {
    
    static void startProg(StoreManagement storeManagement){
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("----------------------Welcome to store management system---------------------");
        int choice;
        do {
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("1. Add Product");
            System.out.println("2. Display Added Products");
            System.out.println("3. Update Product");
            System.out.println("4. Remove Product");
            System.out.println("5. Save to File");
            System.out.println("6. Display Products from File");
            System.out.println("7. Exit");
            System.out.println("--------------------------------------------------");
            System.out.print("Enter : ");

            choice = sc.nextInt();

            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Enter Product Details : ");
                    System.out.print("Name : ");
                    String name = sc.nextLine();
                    System.out.print("Id : ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Price : ");
                    double price = sc.nextDouble();
                    System.out.print("Quantity : ");
                    int quantity = sc.nextInt();
                    storeManagement.addProduct(new Product(name, id, price, quantity));
                    System.out.println("\nProduct Added Successfully\n");
                    break;
                case 2:
                    System.out.println("--------------------------------------------------");
                    System.out.println("---------------Products in Stock------------------");
                    System.out.println("--------------------------------------------------");
                    storeManagement.displayProducts();
                    break;
                case 3:
                    if (storeManagement.getProducts().isEmpty()) {
                        System.out.println("\nNo products available\n");
                        break;
                    }
                    storeManagement.displayProducts();
                    System.out.println("Enter Id to update Product ");
                    int key = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter Updated Product Details : ");
                    System.out.print("Name : ");
                    String updatedName = sc.nextLine();
                    System.out.print("Price : ");
                    double updatedPrice = sc.nextDouble();
                    System.out.print("Quantity : ");
                    int updatedQuantity = sc.nextInt();
                    Product updatedProduct = new Product(updatedName, key, updatedPrice, updatedQuantity);
                    storeManagement.updateProduct(key, updatedProduct);
                    System.out.println("\nProduct updated Successfully\n");
                    break;
                case 4:
                    storeManagement.displayProducts();
                    System.out.println("Enter Id to remove product");
                    storeManagement.deleteProduct(sc.nextInt());
                    System.out.println("\nProduct Removed Successfully\n");
                    break;
                case 5:
                    storeManagement.saveToFile("products.txt");
                    System.out.println("\nProducts saved to file: products.txt\n");
                    break;
                case 6:
                    StoreManagement newStoreManagement = new StoreManagement();
                    newStoreManagement.loadFromFile("products.txt");
                    System.out.println("\nProducts loaded from file : ");
                    newStoreManagement.displayProducts();
                    break;
                case 7:
                    System.out.println("--------------------------------------------------");
                    System.out.println("-------------------THANK YOU----------------------");
                    System.out.println("--------------------------------------------------");
                    break;
                default:
                    System.out.println("Invalid input");
            }
        } while (choice != 7);
        sc.close();
    }
    public static void main(String[] args) {
        // Creating instance of StoreManagement

        StoreManagement storeManagement = new StoreManagement();
        // to not make main function too long
        startProg(storeManagement);

        
    }
}