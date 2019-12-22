public class Shelf {

    private Integer id;
    private int weight;

    public Shelf(int id, int weight){
        this.id = id;
        this.weight = weight;
    }

    public boolean equals(Object o){
        if ((o == null) || (o.getClass() != this.getClass())){
            return false;
        }
        else {
            Shelf obj = (Shelf)o;
            return (obj.getId() == getId());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(Double w) { this.weight = w; }

    // try to add item to shelf
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

    // try to remove item from shelf
    public boolean removeFromShelf(InventoryItem item, int count){
        // if removing to much
        if (weight - (item.getWeight() * count) < 0){
            return false;
        }
        else{
            // removing item weight
            weight -= item.getWeight() * count;
            /*if (weight == 0){
                //remove shelf
            }*/
            return true;
        }
    }

}
