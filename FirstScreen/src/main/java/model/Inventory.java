package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Created Inventory class which holds Parts(InHouse, Outsourced), and Products. This class also includes Part and Product Observable Lists. */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();



    /**
     * Add Part method adds part to the Part Observable List.
     * @param part adding part.
     * */
    public static void addPart(Part part) {
        allParts.add(part);
    }
    /**
     * Add Product method adds product to the Product Observable List.
     * @param product adding product.
     * */
    public static void addProduct(Product product) {
        allProducts.add(product);
    }


    /**
     * Look up Part method searches for a part by ID within the part observable list.
     * @param id ID of part.
     * */
    public static Part lookupPartId(int id){
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part p : allParts) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    /**
     * Look up Product method searches for a product ID within the product observable list.
     * @param id ID of product.
     * */
    public static Product lookupProductId(int id){
        ObservableList<Product> allProduct = Inventory.getAllProducts();

        for (Product p : allProduct) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Look up Part method searches for a part name within the part observable list.
     * @param partName String of Part name entered.
     * */
    public static ObservableList<Part> lookupPartName(String partName){
        ObservableList<Part> namedParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part p : allParts) {
            if(p.getName().toLowerCase().contains(partName.toLowerCase())){
                namedParts.add(p);
            }
        }
        return namedParts;
    }
    /**
     * Look up Product method searches for a product name within the product observable list.
     * @param productName String of Product name entered.
     * */
    public static ObservableList<Product> lookupProductName(String productName){
        ObservableList<Product> namedProducts = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for(Product p : allProducts) {
            if(p.getName().toLowerCase().contains(productName.toLowerCase())){
                namedProducts.add(p);
            }
        }
        return namedProducts;
    }

    /**
     * Update Part method replaces the current part data with the new data.
     * @param id ID of part.
     * @param part part name.
     * */
    public static void updatePart(int id, Part part)
    {
        int index = -1;
        for(Part Part : Inventory.getAllParts())
        {
            index++;
            if(Part.getId() == id){
                Inventory.getAllParts().set(index, part);
            }
        }
        // return void
    }
    /**
     * Update Product method replaces the current product data with the new data.
     * @param id ID of product.
     * @param product Name of Product.
     * */
    public static void updateProduct(int id, Product product)
    {
        int index = -1;

        for(Product Product : Inventory.getAllProducts())
        {
            index++;
            if(Product.getId() == id){
                Inventory.getAllProducts().set(index, product);
                return;
            }
        }
    }

    /** Delete Part method deletes the selected part from the Observable list. */
        public static boolean deletePart(Part selectedPart)
    {
        for(Part Part : Inventory.getAllParts())
        {
                Inventory.getAllParts().remove(selectedPart);
                return true;
            }
        return false;
        }
    /** Delete Product method deletes the selected product from the Observable list. */
    public static boolean deleteProduct(Product selectedProduct)
    {
        for(Product Product : Inventory.getAllProducts())
        {
            Inventory.getAllProducts().remove(selectedProduct);
            return true;
        }
        return false;
    }

    /**
     * Get all parts method retrieves all parts within the observable list.
     * @return Returns all parts in observable list.
     * */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    /**
     * Get all products method retrieves all products within the observable list.
     * @return Returns all products in Observable list.
     * */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
