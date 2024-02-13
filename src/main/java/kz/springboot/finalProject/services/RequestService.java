package kz.springboot.finalProject.services;

import kz.springboot.finalProject.entities.Requests;
import kz.springboot.finalProject.entities.ShopItems;

import java.util.List;

public interface RequestService {
    Requests addRequest(Requests requests);
    List<Requests> getAllRequests();

    List<Requests> getRequestsByShopItem(ShopItems shopItems);
    Requests getRequest(Long id);
    void deleteRequest(Requests requests);
    Requests saveRequest(Requests requests);

    List<Requests> getRequestByUserId(Long id);


    List<ShopItems> getAllShopItems();

    ShopItems getShopItems(Long id);
}
