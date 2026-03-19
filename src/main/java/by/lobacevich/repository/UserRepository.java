package by.lobacevich.repository;

import by.lobacevich.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.paymentCards WHERE u.id = :id")
    Optional<User> findByIdWithCards(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE users SET active = true WHERE id = :id",
            nativeQuery = true)
    int activateUser(@Param("id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.id = :id")
    int deactivateUser(@Param("id") Long id);
}
