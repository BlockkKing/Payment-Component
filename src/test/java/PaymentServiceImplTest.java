import com.example.backend.dto.PaymentRequest;
import com.example.backend.dto.PaymentResponse;
import com.example.backend.mapper.PaymentMapper;
import com.example.backend.model.Fee;
import com.example.backend.model.Payment;
import com.example.backend.model.User;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.service.*;
import com.example.backend.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// мокаем внешние зависимости
// не мокаем бизнес логику
// цеопчка - ARRANGE, ACT, ASSERT
//.thenAnswer(invocation -> invocation.getArgument(0));
//возвращает тот объект,который реально пришёл в save()

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock // фейковый объект
    private UserService userService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private FeePolicyService feePolicyService;

    @Mock
    private FeeService feeService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;
    //mockito ничего не делает с void метод

    private PaymentMapper paymentMapper = new PaymentMapper();

    private PaymentServiceImpl paymentService;
    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(
                userService,
                exchangeRateService,
                feePolicyService,
                paymentRepository,
                feeService,
                eventPublisher,
                paymentMapper
        );
    }

    @Test
    void shouldCalculatePaymentCorrectly() {
        Long payerId = 1L;
        Long recipientId = 2L;
        PaymentRequest request = new PaymentRequest(
                new BigDecimal("100"),
                "USD",
                recipientId,
                payerId
        );

        User payer = new User();
        payer.setId(payerId);

        User recipient = new User();
        recipient.setId(recipientId);

        Payment payment = new Payment(); //подготовка оружения
        Fee fee = new Fee();
        fee.setAmount(new BigDecimal("45.00"));

        when(userService.findById(payerId)).thenReturn(payer);
        when(userService.findById(recipientId)).thenReturn(recipient);
        when(exchangeRateService.getExchangeRate("USD", "RUB"))
                .thenReturn(new BigDecimal("90"));
        when(feePolicyService.calculateFee(any()))
                .thenReturn(new BigDecimal("45.00"));
        when(paymentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(feeService.createFee(any(), any(), any())).thenReturn(fee);
        PaymentResponse result = paymentService.pay(request);

        System.out.println("Amount РУБ: " + result.getAmountRub());
        System.out.println("Fee = " + result.getFee());

        assertEquals(new BigDecimal("9000.00"), result.getAmountRub());
        assertEquals(new BigDecimal("45.00"), result.getFee());
    }
}
