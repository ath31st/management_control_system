package top.shop.shop1_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.shop.shop1_service.entity.Payment;
import top.shop.shop1_service.util.PaymentStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentUuid(String paymentUuid);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    boolean existsByPaymentUuid(String paymentUuid);

}
