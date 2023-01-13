package top.shop.shop1_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.shop.shop1_service.entity.Payment;
import top.shop.shop1_service.service.DiscountService;
import top.shop.shop1_service.service.PaymentService;
import top.shop.shop1_service.service.event.PaymentEvent;
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
    private final DiscountService discountService;
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
                    paymentService.updatePaymentStatus(p, PaymentStatus.EXPIRED);
                    if (p.getPrivateDiscount() != null) {
                        discountService.updateAppliedStatusDiscount(p.getPrivateDiscount(), false);
                    }
                    if (p.getCommonDiscount() != null) {
                       discountService.increaseAvailableNumber(p.getCommonDiscount(), 1);
                    }
                });
        payments.removeIf(p -> p.getPaymentStatus().equals(PaymentStatus.EXPIRED));
    }

    @EventListener
    public void updatePaymentsList(PaymentEvent event) {
        Payment payment = (Payment) event.getSource();

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
