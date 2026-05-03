//import com.example.backend.dto.PaymentResponse;
//import com.example.backend.model.User;
//import com.example.backend.service.*;
//import com.example.backend.service.impl.PaymentServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.math.BigDecimal;
//import java.util.Optional;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class PaymentDtoServiceImplTest {
//
//    @Mock // фейковый объект
//    private UserService userService;
//
//    @Mock
//    private ExchangeRateService exchangeRateService;
//
//    @Mock
//    private FeePolicyService feePolicyService;
//
//    @InjectMocks
//    private PaymentServiceImpl paymentService;
//
//    @Test
//    void shouldCalculatePaymentCorrectly() {
//        Long payerId = 1L;
//        Long recipientId = 2L;
//        PaymentDto cmd = new PaymentDto(
//                payerId,
//                recipientId,
//                new BigDecimal("100"),
//                "USD"
//        );
//
//        User payer = new User();
//        payer.setId(payerId);
//
//        User recipient = new User();
//        recipient.setId(recipientId);
//
//        when(userService.findById(payerId)).thenReturn(Optional.of(payer));
//        when(userService.findById(recipientId)).thenReturn(Optional.of(recipient));
//        when(exchangeRateService.rateForToday("USD", "RUB"))
//                .thenReturn(new BigDecimal("90"));
//        when(feePolicyService.calculateFee(any()))
//                .thenReturn(new BigDecimal("45.00"));
//
//        PaymentResponse result = paymentService.pay(cmd);
//
//        assertEquals(new BigDecimal("9000.00"), result.amountRub());
//        assertEquals(new BigDecimal("45.00"), result.fee());
//    }
//
//}
