package com.lagerverwaltung;

/**
 * A shelf is an attribute of an item.
 * Multiple items can be stored in the same shelf.
 * The maximum weight of a shelf is 10 tons.
 *
 * @author ...
 */
public class Shelf {

    /**
     * Shelf number
     */
    private Integer id;

    /**
     * Total weight of shelf
     */
    private int weight;

    /**
     * Constructor for a new shelf
     *
     * @param id shelf number of new shelf
     * @param weight total weight of new shelf
     */
    public Shelf(int id, int weight){
        this.id = id;
        this.weight = weight;
    }

    /**
     * Compare function
     *
     * @param o compared object
     * @return is the object a Shelf and has the same Shelf ID ?
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
     * Getter for shelf identification number
     *
     * @return shelf identification number
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for shelf identification number
     *
     * @param id new identification number
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return weight of shelf
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @param w new weight of shelf
     */
    public void setWeight(int w) {
        this.weight = w;
    }


    /**
     * Try to add item to shelf, fails if total weight is exceeded
     *
     * @param item item to be added to the shelf
     * @param count amount of items added to the shelf
     * @return true if adding is successful, else false
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
     * Try to remove object from shelf, fails if there aren't enough items
     *
     * @param item item to be removed from the shelf
     * @param count count of item to be removed from the shelf
     * @return true if removing was successful else false
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
