package base.engineering.motosumiyoshi.sakeapp.model;

public class Product {

    private final String itemName;

    private final String itemPrice;

    private final String itemUrl;

    private final String imageURL;

    public Product(
            String itemName,
            String itemPrice,
            String imageURL,
            String itemUrl) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemUrl = itemUrl;
        this.imageURL = imageURL;
    }

    public String getItemName() {
        return itemName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemUrl() {
        return itemUrl;
    }
}
