package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest;

import br.com.uol.compass.desafio.san_giorgio.application.domain.exception.InvoiceNotFoundException;
import br.com.uol.compass.desafio.san_giorgio.application.domain.exception.SellerNotFoundException;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Payment;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus;
import br.com.uol.compass.desafio.san_giorgio.application.factory.model.PaymentFactory;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.factory.request.PaymentRequestFactory;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.ConfirmPaymentUseCase;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.handler.ControllerExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@TestInstance(PER_CLASS)
@ExtendWith(SpringExtension.class)
class PaymentControllerTest {
    private static final String PATH = "/v1/payment";

    private static MockMvc mockMvc;
    private static ObjectMapper mapper;

    @InjectMocks
    private PaymentController controller;

    @Mock
    private ConfirmPaymentUseCase useCase;

    @BeforeAll
    void setUp() {
        mapper = new ObjectMapper();
        mockMvc = standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
                .setControllerAdvice(new ControllerExceptionHandler())
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @AfterEach
    void tearDown() {
        reset(useCase);
    }

    @ParameterizedTest
    @EnumSource(PaymentStatus.class)
    @DisplayName("Should confirm payments successfully.")
    void shouldConfirmPaymentsSuccessfully(final PaymentStatus status) throws Exception {
        var payment = switch(status) {
            case PARTIAL -> PaymentFactory.createPartialPayment();
            case TOTAL -> PaymentFactory.createTotalPayment();
            default -> PaymentFactory.createSurplusPayment();
        };

        var item = payment.getPaymentItems().get(0)
                .toBuilder()
                .paymentStatus(status)
                .build();

        payment = payment.toBuilder()
                .paymentItems(List.of(item))
                .build();

        when(useCase.confirm(any(Payment.class)))
                .thenReturn(payment);

        var request = switch(status) {
            case PARTIAL -> PaymentRequestFactory.createPartialPaymentRequest();
            case TOTAL -> PaymentRequestFactory.creatTotalPaymentRequest();
            default -> PaymentRequestFactory.creatSurplusPaymentRequest();
        };

        var items = payment.getPaymentItems();

        mockMvc.perform(put(PATH)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sellerId").value(payment.getSeller().getId()))
                .andExpect(jsonPath("$.paymentItems").isArray())
                .andExpect(jsonPath("$.paymentItems").value(hasSize(1)))
                .andExpect(jsonPath("$.paymentItems[0].invoiceId").value(items.get(0).getInvoice().getId().intValue()))
                .andExpect(jsonPath("$.paymentItems[0].paymentValue").value(items.get(0).getPaymentValue().doubleValue()))
                .andExpect(jsonPath("$.paymentItems[0].paymentStatus").value(status.name()))
                .andReturn();

        verify(useCase, times(1))
                .confirm(any(Payment.class));
    }

    @Test
    @DisplayName("Should confirm payments successfully when returned status is null.")
    void shouldConfirmPaymentsSuccessfullyWhenReturnStatusIsNull() throws Exception {
        var payment = PaymentFactory.createPayment();

        when(useCase.confirm(any(Payment.class)))
                .thenReturn(payment);

        var request = PaymentRequestFactory.createPartialPaymentRequest();
        var items = payment.getPaymentItems();

        mockMvc.perform(put(PATH)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sellerId").value(payment.getSeller().getId()))
                .andExpect(jsonPath("$.paymentItems").isArray())
                .andExpect(jsonPath("$.paymentItems").value(hasSize(1)))
                .andExpect(jsonPath("$.paymentItems[0].invoiceId").value(items.get(0).getInvoice().getId().intValue()))
                .andExpect(jsonPath("$.paymentItems[0].paymentValue").value(items.get(0).getPaymentValue().doubleValue()))
                .andExpect(jsonPath("$.paymentItems[0].paymentStatus").doesNotExist())
                .andReturn();

        verify(useCase, times(1))
                .confirm(any(Payment.class));
    }

    @Test
    @DisplayName("Should return error when required payment field is not null.")
    void shouldReturnErrorWhenRequiredPaymentFieldIsNull() throws Exception {
        var request = PaymentRequestFactory.createPartialPaymentRequestWithNullSeller();

        mockMvc.perform(put(PATH)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value(PATH))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value(hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value("sellerId"))
                .andExpect(jsonPath("$.errors[0].message").value("The field sellerId is required."))
                .andReturn();

        verify(useCase, never())
                .confirm(any(Payment.class));
    }

    @Test
    @DisplayName("Should return error when required payment item field is not null.")
    void shouldReturnErrorWhenRequiredPaymentItemFieldIsNull() throws Exception {
        var request = PaymentRequestFactory.createPartialPaymentRequestWithNullValue();

        mockMvc.perform(put(PATH)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value(PATH))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value(hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value("paymentItems[0].paymentValue"))
                .andExpect(jsonPath("$.errors[0].message").value("The field paymentValue is required."))
                .andReturn();

        verify(useCase, never())
                .confirm(any(Payment.class));
    }

    @Test
    @DisplayName("Should return error when invoice not found.")
    void shouldReturnErrorWhenInvoiceNotFound() throws Exception {
        when(useCase.confirm(any(Payment.class)))
                .thenThrow(new InvoiceNotFoundException());

        var request = PaymentRequestFactory.createPartialPaymentRequest();

        mockMvc.perform(put(PATH)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(412))
                .andExpect(jsonPath("$.path").value(PATH))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value(hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message").value("Invoice not found."))
                .andReturn();

        verify(useCase, times(1))
                .confirm(any(Payment.class));
    }

    @Test
    @DisplayName("Should return error when seller not found.")
    void shouldReturnErrorWhenSellerNotFound() throws Exception {
        when(useCase.confirm(any(Payment.class)))
                .thenThrow(new SellerNotFoundException());

        var request = PaymentRequestFactory.createPartialPaymentRequest();

        mockMvc.perform(put(PATH)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(412))
                .andExpect(jsonPath("$.path").value(PATH))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value(hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message").value("Seller not found."))
                .andReturn();

        verify(useCase, times(1))
                .confirm(any(Payment.class));
    }

    @Test
    @DisplayName("Should return error when unexpected error happens.")
    void shouldReturnErrorWhenUnexpectedErrorHappens() throws Exception {
        when(useCase.confirm(any(Payment.class)))
                .thenThrow(new NullPointerException());

        var request = PaymentRequestFactory.createPartialPaymentRequest();

        mockMvc.perform(put(PATH)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.path").value(PATH))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value(hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message").value("Error to process request."))
                .andReturn();

        verify(useCase, times(1))
                .confirm(any(Payment.class));
    }
}
