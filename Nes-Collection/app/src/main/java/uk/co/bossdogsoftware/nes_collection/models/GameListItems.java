package uk.co.bossdogsoftware.nes_collection.models;

public class GameListItems {

    public String name, publisher, image, group;
    public int _id, owned, cart, manual, box;

    public int getItemId() { return _id; }
    public void setItemId(int _id) { this._id = _id; } //returns the item id

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; } //returns the item name

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher;  } //returns the item quantity

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; } //returns the item name

    public int getCart() { return cart; }
    public void setCart(int cart) { this.cart = cart; }

    public int getBox() { return box; }
    public void setBox(int box) { this.box = box; }

    public int getManual() { return manual; }
    public void setManual(int manual) { this.manual = manual; }
}
