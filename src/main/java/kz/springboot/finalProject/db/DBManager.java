package kz.springboot.finalProject.db;

import java.util.ArrayList;

public class DBManager {
    private  static ArrayList<Items> items = new ArrayList<>();

    static {
        items.add(new Items(1L, "IPhone 11Pro", 300000));
        items.add(new Items(2L, "IPhone 12", 400000));
        items.add(new Items(3L, "IPhone 13Pro", 450000));
    }
    private static Long id = 4L;
    public static ArrayList<Items> getItems(){
        return items;
    }

    public static void addItem(Items item){
        item.setId(id);
        items.add(item);
        id++;

    }

    public static Items getItem(Long id) {
        for(Items it: items){
            if(it.getId()==id){
                return it;
            }
        }
        return null;
    }
}
