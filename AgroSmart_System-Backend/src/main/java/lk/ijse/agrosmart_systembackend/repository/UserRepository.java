package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

/*    @Query(value = "select * from User", nativeQuery = true)
    List<User> getallusers();

    @Modifying
    @Query(value = "update ....",nativeQuery = true)
    void updateuser(User user);*/

}
