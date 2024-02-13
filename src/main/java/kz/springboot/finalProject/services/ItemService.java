package kz.springboot.finalProject.services;

import kz.springboot.finalProject.entities.Countries;
import kz.springboot.finalProject.entities.ShopItems;

import java.util.List;

public interface ItemService {
    ShopItems addItem(ShopItems item);
    List<ShopItems> getAllItems();
    ShopItems getItem(Long id);
    void deleteItem(ShopItems item);
    ShopItems saveItem(ShopItems item);


    List<Countries> getAllCountries();
    Countries addCountry(Countries country);
    Countries saveCountry(Countries country);
    Countries getCountry(Long id);
}
