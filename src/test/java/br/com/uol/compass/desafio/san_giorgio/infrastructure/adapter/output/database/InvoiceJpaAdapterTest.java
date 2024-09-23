package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database;

import br.com.uol.compass.desafio.san_giorgio.application.domain.exception.InvoiceNotFoundException;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.respository.InvoiceJpaRepository;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.factory.InvoiceEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@ExtendWith(SpringExtension.class)
class InvoiceJpaAdapterTest {

    @Mock
    private InvoiceJpaRepository repository;

    @InjectMocks
    private InvoiceJpaAdapter jpaAdapter;

    @Test
    @DisplayName("Should find invoice by id successfully.")
    void shouldFindInvoiceByIdSuccessfully() {
        var inventory = InvoiceEntityFactory.createInvoiceEntity();

        when(repository.findById(any(BigInteger.class)))
                .thenReturn(Optional.of(inventory));

        var result = jpaAdapter.findByInvoiceId(inventory.getId());

        verify(repository, times(1))
                .findById(any(BigInteger.class));
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(inventory.getId());
        assertThat(result.getTotal()).isEqualTo(inventory.getTotal());
        assertThat(result.getDescription()).isEqualTo(inventory.getDescription());

    }

    @Test
    @DisplayName("Should throw InvoiceNotFoundException when invoice not found.")
    void shouldThrowInvoiceNotFoundExceptionWhenInvoiceNotFound() {
        var inventory = InvoiceEntityFactory.createInvoiceEntity();

        when(repository.findById(any(BigInteger.class)))
                .thenReturn(Optional.empty());

        var exception = assertThrows(
                InvoiceNotFoundException.class,
                () -> jpaAdapter.findByInvoiceId(inventory.getId())
        );

        verify(repository, times(1))
                .findById(any(BigInteger.class));

        assertThat(exception).isNotNull();
        assertThat(exception.getStatus()).isEqualTo(PRECONDITION_FAILED);
        assertThat(exception.getMessage()).isEqualTo("Invoice not found.");
    }
}
