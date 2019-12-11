public class Database {
    //public static String path;
    String path;

    public Database(){
        this.path = null;
    }

    public Database(String path){
        this.path = path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

}
