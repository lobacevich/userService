package by.lobacevich.repository;

import by.lobacevich.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {

    int countByUserId(Long userId);

    List<PaymentCard> findByUserId(Long id);

    @Query("SELECT pc FROM PaymentCard pc JOIN FETCH pc.user WHERE pc.id = :id")
    Optional<PaymentCard> findByIdWithUser(@Param("id") Long id);

    @Modifying
    @Query("UPDATE PaymentCard pc SET pc.active = false WHERE pc.user.id = :userId")
    void deactivateByUserId(@Param("userId") Long id);

    @Modifying
    @Query(value = "UPDATE payment_cards SET active = true WHERE id = :id",
            nativeQuery = true)
    void activateCard(@Param("id") Long id);

    @Modifying
    @Query("UPDATE PaymentCard pc SET pc.active = false WHERE pc.id = :id")
    void deactivateCard(@Param("id") Long id);
}
