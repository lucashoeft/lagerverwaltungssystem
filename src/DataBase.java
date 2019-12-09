public class DataBase {
    //public static String path;
    String path;

    public DataBase (){
        this.path = null;
    }

    public DataBase (String path){
        this.path = path;
    }

    public void set_path (String path){
        this.path = path;
    }

    public String get_path () {
        return this.path;
    }

}
