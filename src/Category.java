public class Category {

    private String name;
    private Integer count;

    public Category (String name){
        this.name = name;
        this.count = 0;
    }

    public Category (String name, Integer count){
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void increaseCount(){
        this.count ++;
    }

    public void decreaseCount(){
        this.count --;
    }

    public boolean equals(Object o){
        if ((o == null) || (o.getClass() != this.getClass())){
            return false;
        }
        else {
            Category obj = (Category)o;
            return (obj.getName().equals(getName()));
        }
    }


}
