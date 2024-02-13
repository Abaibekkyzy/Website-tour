package kz.springboot.finalProject.services.impl;

import kz.springboot.finalProject.entities.Requests;
import kz.springboot.finalProject.entities.ShopItems;
import kz.springboot.finalProject.repositories.RequestRepository;
import kz.springboot.finalProject.repositories.ShopItemRepository;
import kz.springboot.finalProject.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private ShopItemRepository shopItemRepository;

    @Override
    public Requests addRequest(Requests requests) {
        return requestRepository.save(requests);
    }

    @Override
    public List<Requests> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public List<Requests> getRequestsByShopItem(ShopItems shopItems) {
        return  requestRepository.findRequestsByShopItems(shopItems);
    }

    @Override
    public Requests getRequest(Long id) {
        return requestRepository.findRequestsById(id);
    }

    @Override
    public void deleteRequest(Requests item) {
        requestRepository.delete(item);
    }

    @Override
    public Requests saveRequest(Requests item) {
        return requestRepository.save(item);
    }

    @Override
    public List<Requests> getRequestByUserId(Long id) {
        return requestRepository.findRequestsByUserId(id);
    }

    @Override
    public List<ShopItems> getAllShopItems() {
        return shopItemRepository.findAll();
    }



    @Override
    public ShopItems getShopItems(Long id) {
        return shopItemRepository.findByIdAndAmountGreaterThan(id, 0);
    }
}
