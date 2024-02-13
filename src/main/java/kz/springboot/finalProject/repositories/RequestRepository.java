package kz.springboot.finalProject.repositories;

import kz.springboot.finalProject.entities.Requests;
import kz.springboot.finalProject.entities.ShopItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RequestRepository extends JpaRepository<Requests, Long> {

    Requests findRequestsById(Long id);

    List<Requests> findRequestsByUserId(Long id);

    List<Requests> findRequestsByShopItems(ShopItems shopItems);
}
