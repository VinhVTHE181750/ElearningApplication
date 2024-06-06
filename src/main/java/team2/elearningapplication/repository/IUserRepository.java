package team2.elearningapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team2.elearningapplication.Enum.EnumUserStatus;
import team2.elearningapplication.entity.User;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findUserById(int id);

    Optional<User> findByUsernameAndStatus(String username, EnumUserStatus status);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndStatus(String email, EnumUserStatus status);

    @Query(value = "SELECT COUNT(*) FROM users WHERE deleted = false", nativeQuery = true)
    int getTotalUser();

}
