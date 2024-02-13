package kz.springboot.finalProject.repositories;

import kz.springboot.finalProject.entities.ShopItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ShopItemRepository extends JpaRepository<ShopItems, Long> {

    //List<ShopItems> findAllByAmountGreaterThanOOrderByPriceDesc(int amount);

    ShopItems findByIdAndAmountGreaterThan(Long id, int amount);
}
