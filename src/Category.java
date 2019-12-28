/**
 * Category is an attribute of an item.
 *
 * @author ...
 * @version 1.0
 */
public class Category {

    /**
     * name of the category
     */
    private String name;

    /**
     * count of items inside this category
     */
    private Integer count;

    /**
     * Category Constructor
     *
     * @param name name of new category
     */
    public Category (String name){
        this.name = name;
        this.count = 0;
    }

    /**
     * Category Constructor
     * @param name name of new category
     * @param count amount of items inside this category
     */
    public Category (String name, Integer count){
        this.name = name;
        this.count = count;
    }

    /**
     * @return name of category
     */
    public String getName() {
        return name;
    }

    /**
     * @param name new name of the category
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return amount of items inside category
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count new amount of items inside category
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * increase the amount of items inside category by one
     */
    public void increaseCount(){
        this.count ++;
    }

    /**
     * decrease the amount of items inside category by one
     */
    public void decreaseCount(){
        this.count --;
    }

    /**
     * @param o another object
     * @return is the other object the same object as me?
     */
    public boolean equals(Object o){
        if ((o == null) || (o.getClass() != this.getClass())){
            return false;
        }
        else {
            Category obj = (Category)o;
            return (obj.getName().equals(getName()));
        }
    }

    public String toStringCSV(){
        String csv = "";
        // no further checks like for embedded comma, correct location encoding, ... (has to be done by caller)
        try {
            csv = "," + name +",,,,";
        }
        catch (Exception ignored) { } // don't export invalid items
        return csv;
    }



}
