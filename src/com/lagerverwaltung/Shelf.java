package com.lagerverwaltung;

/**
 * The Shelf class is part of the inventory and holds the weight of the inventory items that are stored in the shelf.
 * The maximum capacity is 100_000_000 decigram which are 10 tons.
 */
public class Shelf {

    /**
     * The number of the shelf
     */
    private Integer id;

    /**
     * The total weight of shelf
     */
    private int weight;

    /**
     * The constructor of the Shelf class.
     *
     * @param id the number of the new shelf
     * @param weight the total weight of the new shelf
     */
    public Shelf(int id, int weight){
        this.id = id;
        this.weight = weight;
    }

    /**
     * Checks if given object equals to this shelf.
     *
     * @param o another object
     * @return true if both objects are equal
     */
    public boolean equals(Object o){
        if ((o == null) || (o.getClass() != this.getClass())){
            return false;
        }
        else {
            Shelf obj = (Shelf)o;
            return (obj.getId() == getId());
        }
    }

    /**
     * Returns the number of the shelf.
     *
     * @return the number of the shelf
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the number of this shelf to a new number.
     *
     * @param id the new number for the shelf
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the total weight of the shelf.
     *
     * @return the weight of the shelf
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the weight of this shelf to a new weight.
     *
     * @param w the new weight of the shelf
     */
    public void setWeight(int w) {
        this.weight = w;
    }


    /**
     * Tries to add an inventory item to the shelf weight multiplied by the count.
     *
     * @param item the inventory item to be added to the shelf
     * @param count the amount of inventory items added to the shelf
     * @return true if adding was successful
      */
    public boolean addToShelf(InventoryItem item, int count){
        // if combined weight to heavy
        if (weight + (item.getWeight() * count) > 100000000){   // in 0.1g
            return false;
        }
        else{
            // add item weight
            weight += item.getWeight() * count;
            return true;
        }
    }

    /**
     * Tries to remove an inventory item from the shelf weight multiplied by the count.
     *
     * @param item the inventory item to be removed from the shelf
     * @param count the amount of inventory items removed from the shelf
     * @return true if removing was successful
      */
    public boolean removeFromShelf(InventoryItem item, int count){
        // if removing to much
        if (weight - (item.getWeight() * count) < 0){
            return false;
        }
        else{
            // removing item weight
            weight -= item.getWeight() * count;
            return true;
        }
    }

}
