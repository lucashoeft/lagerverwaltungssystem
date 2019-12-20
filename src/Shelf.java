public class Shelf {

    private Integer id;
    private Double weight;

    public Shelf(int id, Double weight){
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weigth) {
        this.weight = weight;
    }

    // try to add item to shelf
    public boolean addToShelf(InventoryItem item, int delta){
        // if combined weight to heavy
        if (weight + (item.getWeight() * delta) > 10000000){
            return false;
        }
        else{
            // add item weight
            weight += item.getWeight() * delta;
            return true;
        }
    }

    // try to remove item from shelf
    public boolean removeFromShelf(InventoryItem item, int delta){
        // if removing to much
        if (weight - (item.getWeight() * delta) < 0){
            return false;
        }
        else{
            // removing item weight
            weight -= item.getWeight() * delta;
            /*if (weight == 0){
                //remove shelf
            }*/
            return true;
        }
    }

}
