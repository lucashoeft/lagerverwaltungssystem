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

    public boolean addToShelf(InventoryItem item, int count){
        if (weight + (item.getWeight() * count) > 1000000){
            return false;
        }
        else{
            weight += item.getWeight() * count;
            return true;
        }
    }

    /*public boolean removeFromShelf(InventoryItem item, int count){
        if (weight - (item.getWeight() * count) < 0){
            return false;
        }
        else{
            weight -= item.getWeight() * count;
            return true;
        }
        if (weight == 0){
            //remove shelf
        }
    }*/

}
