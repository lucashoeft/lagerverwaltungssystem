package com.lagerverwaltung;

/**
 * The Category class represents the model of a category and holds the number of inventory items in this category.
 */
public class Category {

    /**
     * The name of the category which consists of numbers and alphabetic characters with a length between 1 and 256
     * characters.
     */
    private String name;

    /**
     * The amount of inventory items that exist with this category as a selected value.
     */
    private Integer count;

    /**
     * The constructor of the Category class.
     *
     * @param name the name of the new category
     */
    public Category (String name){
        this.name = name;
        this.count = 0;
    }

    /**
     * The constructor of the Category class.
     *
     * @param name the name of the new category
     * @param count the amount of inventory items that exist with this category as a selected value.
     */
    public Category (String name, Integer count){
        this.name = name;
        this.count = count;
    }

    /**
     * Returns the name of the category.
     *
     * @return the name of the category
     */
    public String getName() {
        return name;
    }

    /**
     * Assigns a new name to the category.
     *
     * @param name the new name of the category
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the amount of inventory items that exist with this category as a selected value.
     *
     * @return the amount of inventory items that exist with this category as a selected value
     */
    public int getCount() {
        return count;
    }

    /**
     * Assigns new value to the amount.
     *
     * @param count the new amount of inventory items that exist with this category as a selected value.
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Increases the amount of inventory items inside category by 1
     */
    public void increaseCount(){
        this.count ++;
    }

    /**
     * Decreases the amount of inventory items inside the category by 1
     */
    public void decreaseCount(){
        this.count --;
    }

    /**
     * Checks if given object equals to this category
     *
     * @param o another object
     * @return true if both objects are equal
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
     * Converts the category into a csv compatible string value
     *
     * @return the category represented as a csv row
     */
    public String toStringCSV(){
        return  "-1," + name + ",-1,-1,-1,-1";
    }
}
