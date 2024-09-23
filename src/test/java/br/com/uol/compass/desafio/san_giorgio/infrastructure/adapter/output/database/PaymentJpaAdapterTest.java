package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus;
import br.com.uol.compass.desafio.san_giorgio.application.factory.model.PaymentItemFactory;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.entity.PaymentEntity;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.respository.PaymentJpaRepository;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.factory.PaymentEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PaymentJpaAdapterTest {

    @Mock
    private PaymentJpaRepository repository;

    @InjectMocks
    private PaymentJpaAdapter jpaAdapter;

    @ParameterizedTest
    @EnumSource(PaymentStatus.class)
    @DisplayName("Should save payment successfully.")
    void shouldFindInvoiceByIdSuccessfully(final PaymentStatus status) {
        var payment = switch(status) {
            case PARTIAL -> PaymentEntityFactory.createPartialPaymentEntity();
            case TOTAL -> PaymentEntityFactory.createTotalPaymentEntity();
            default -> PaymentEntityFactory.createSurplusPaymentEntity();
        };

        when(repository.save(any(PaymentEntity.class)))
                .thenReturn(payment);

        var item = switch(status) {
            case PARTIAL -> PaymentItemFactory.createPartialPaymentItem();
            case TOTAL -> PaymentItemFactory.createTotalPaymentItem();
            default -> PaymentItemFactory.createSurplusPaymentItem();
        };

        var result = jpaAdapter.save(item);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(item.getId());
        assertThat(result.getInvoice().getId()).isEqualTo(item.getInvoice().getId());
        assertThat(result.getSeller().getId()).isEqualTo(item.getSeller().getId());
        assertThat(result.getPaymentStatus()).isEqualTo(status);
        assertThat(result.getPaymentValue()).isEqualTo(item.getPaymentValue());

        verify(repository, times(1))
                .save(any(PaymentEntity.class));
    }

    @Test
    @DisplayName("Should propagate exception when unexpected error.")
    void shouldPropagateExceptionWhenUnexpectedError() {
        when(repository.save(any(PaymentEntity.class)))
                .thenThrow(new NullPointerException());

        var item = PaymentItemFactory.createPartialPaymentItem();

        var exception = assertThrows(
                NullPointerException.class,
                () -> jpaAdapter.save(item)
        );

        verify(repository, times(1))
                .save(any(PaymentEntity.class));

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNull();
    }
}
