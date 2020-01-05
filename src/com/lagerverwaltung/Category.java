package com.lagerverwaltung;

/**
 * Category is an attribute of an item.
 */
public class Category {

    /**
     * Name of the category
     */
    private String name;

    /**
     * Count of items inside this category
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
     *
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
     * Increase the amount of items inside category by one
     */
    public void increaseCount(){
        this.count ++;
    }

    /**
     * Decrease the amount of items inside category by one
     */
    public void decreaseCount(){
        this.count --;
    }

    /**
     * Checks if given object equals this Category
     *
     * @param o another object
     * @return true if both objects are the same, else false
     */
    public boolean equals(Object o){
        if ((o == null) || (o.getClass() != this.getClass())){
            return false;
        } else {
            Category obj = (Category)o;
            return (obj.getName().equals(getName()));
        }
    }

    /**
     * Converts category into .csv compatible String
     *
     * @return .csv compatible String
     */
    public String toStringCSV(){
        return  "-1," + name + ",-1,-1,-1,-1";
    }
}
