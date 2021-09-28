package uk.co.pixoveeware.nes_collection;

/**
 * Created by Wildheart on 06/06/2016.
 */
public class NesItems {


    String name, publisher, synopsis, image, genre, region, group, subgenre, developer, year, currency;
    int _id, owned, cart, manual, box, pal_a_cart, pal_a_box, pal_a_manual, pal_b_cart, pal_b_manual,
            pal_b_box, ntsc_cart, ntsc_box, ntsc_manual, favourite, wishlist, finished;
     int flagAustralia, flagAustria, flagBenelux, flagFrance, flagGermany, flagIreland, flagItaly, flagScandinavia, flagSpain, flagSwitzerland, flagUK, flagUS ;
    double pal_a_cost, pal_b_cost, ntsc_cost;

    public int getItemId() { return _id; }
    public void setItemId(int _id) { this._id = _id; } //returns the item id

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; } //returns the item name

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher;  } //returns the item quantity

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; } //returns the item name

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; } //returns the item name

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }//returns the item department

    public String getSubgenre() {return subgenre;}
    public void setSubgenre(String subgenre) {this.subgenre = subgenre;}

    public String getDeveloper() {return developer;}
    public void setDeveloper(String developer) {this.developer = developer;}

    public String getYear() {return year;}
    public void setYear(String year) {this.year = year;}

    public int getOwned() { return owned; }
    public void setOwned(int owned) { this.owned = owned; } //returns the item id

    public int getCart() { return cart; }
    public void setCart(int cart) { this.cart = cart; } //returns the item id

    public int getManual() { return manual; }
    public void setManual(int manual) { this.manual = manual; } //returns the item id

    public int getBox() { return box; }
    public void setBox(int box) { this.box = box; } //returns the item id

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }//returns the item department

    public int getCartPalA() { return pal_a_cart; }
    public void setCartPalA(int pal_a_cart) { this.pal_a_cart = pal_a_cart; } //returns the item id

    public int getManualPalA() { return pal_a_manual; }
    public void setManualPalA(int pal_a_manual) { this.pal_a_manual = pal_a_manual; } //returns the item id

    public int getBoxPalA() { return pal_a_box; }
    public void setBoxPalA(int pal_a_box) { this.pal_a_box = pal_a_box; } //returns the item id

    public int getCartPalB() { return pal_b_cart; }
    public void setCartPalB(int pal_b_cart) { this.pal_b_cart = pal_b_cart; } //returns the item id

    public int getManualPalB() { return pal_b_manual; }
    public void setManualPalB(int pal_b_manual) { this.pal_b_manual = pal_b_manual; } //returns the item id

    public int getBoxPalB() { return pal_b_box; }
    public void setBoxPalB(int pal_b_box) { this.pal_b_box = pal_b_box; } //returns the item id

    public int getCartNtsc() { return ntsc_cart; }
    public void setCartNtsc(int ntsc_cart) { this.ntsc_cart = ntsc_cart; } //returns the item id

    public int getManualNtsc() { return ntsc_manual; }
    public void setManualNtsc(int ntsc_manual) { this.ntsc_manual = ntsc_manual; } //returns the item id

    public int getBoxNtsc() { return ntsc_box; }
    public void setBoxNtsc(int ntsc_box) { this.ntsc_box = ntsc_box; } //returns the item id

    public int getFavourite() { return favourite; }
    public void setFavourite(int favourite) { this.favourite = favourite; }

    public int getWishlist() { return wishlist; }
    public void SetWishlist(int wishlist) { this.wishlist = wishlist; } //returns the item id

    public int getFinished() { return finished; }
    public void SetFinished(int finished) { this.finished = finished; } //returns the item id

    public double getPalACost() { return pal_a_cost; }
    public void setPalACost(double pal_a_cost) { this.pal_a_cost = pal_a_cost; } //returns the item id

    public double getPalBCost() { return pal_b_cost; }
    public void setPalBCost(double pal_b_cost) { this.pal_b_cost = pal_b_cost; } //returns the item id

    public double getNtscCost() { return ntsc_cost; }
    public void setNtscCost(double ntsc_cost) { this.ntsc_cost = ntsc_cost; } //returns the item id

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }//returns the item department

    public int getAustralia() { return flagAustralia; }
    public void setAustralia(int flagAustralia) { this.flagAustralia = flagAustralia; } //returns the item id

    public int getAustria() { return flagAustria; }
    public void setAustria(int flagAustria) { this.flagAustria = flagAustria; } //returns the item id

    public int getBenelux() { return flagBenelux; }
    public void setBenelux(int flagBenelux) { this.flagBenelux = flagBenelux; } //returns the item id

    public int getFrance() { return flagFrance; }
    public void setFrance(int flagFrance) { this.flagFrance = flagFrance; } //returns the item id

    public int getGermany() { return flagGermany; }
    public void setGermany(int flagGermany) { this.flagGermany = flagGermany; } //returns the item id

    public int getIreland() { return flagIreland; }
    public void setIreland(int flagIreland) { this.flagIreland = flagIreland; } //returns the item id

    public int getItaly() { return flagItaly; }
    public void setItaly(int flagItaly) { this.flagItaly = flagItaly; } //returns the item id

    public int getScandinavia() { return flagAustria; }
    public void setScandinavia(int flagScandinavia) { this.flagScandinavia = flagScandinavia; } //returns the item id

    public int getSpain() { return flagAustralia; }
    public void setSpain(int flagSpain) { this.flagSpain = flagSpain; } //returns the item id

    public int getSwitzerland() { return flagAustria; }
    public void setSwitzerland(int flagSwitzerland) { this.flagSwitzerland = flagSwitzerland; } //returns the item id

    public int getUK() { return flagUK; }
    public void setUK(int flagUK) { this.flagUK = flagUK; } //returns the item id

    public int getUS() { return flagUS; }
    public void setUS(int flagUS) { this.flagUS = flagUS; } //returns the item id

}
