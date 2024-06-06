package team2.elearningapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team2.elearningapplication.entity.Payment;
import team2.elearningapplication.entity.User;

import java.util.List;

public interface IPaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findPaymentByUser(User user);
}
