package top.shop.shop1_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.shop.shop1_service.entity.Payment;
import top.shop.shop1_service.service.PaymentService;
import top.shop.shop1_service.util.PaymentStatus;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@RequiredArgsConstructor
public class PaymentSchedulerConfig {
    private final PaymentService paymentService;
    private final MongoTemplate mongoTemplate;
    private static final CopyOnWriteArrayList<Payment> payments = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void init() {
        payments.addAll(paymentService.getPaymentsByStatus(PaymentStatus.UNPAID));
    }

    @Scheduled(fixedDelay = 5000)
    private void checkAndUpdateOrderStatus() {
        if (payments.isEmpty()) return;
        payments.stream()
                .filter(this::checkIsExpiredPayment)
                .forEach(p -> {
                    p.setPaymentStatus(PaymentStatus.EXPIRED);
                    p.setPaymentDate(LocalDateTime.now());
                    mongoTemplate.save(p);
                });
    }

    @EventListener
    public void updatePaymentsList(Payment payment) {
        if (!payments.contains(payment) && payment.getPaymentStatus().equals(PaymentStatus.UNPAID)) {
            payments.add(payment);
        } else if (payments.contains(payment) && !payment.getPaymentStatus().equals(PaymentStatus.UNPAID)) {
            payments.remove(payment);
        }
    }

    private boolean checkIsExpiredPayment(Payment payment) {
        return LocalDateTime.now().isAfter(payment.getPaymentDate().plusMinutes(payment.getMinutesBeforeExpiration()));
    }

}
