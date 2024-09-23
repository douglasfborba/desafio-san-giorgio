package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database;

import br.com.uol.compass.desafio.san_giorgio.application.domain.exception.SellerNotFoundException;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.respository.SellerJpaRepository;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.factory.SellerEntityFactory;
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
class SellerJpaAdapterTest {

    @Mock
    private SellerJpaRepository repository;

    @InjectMocks
    private SellerJpaAdapter jpaAdapter;

    @Test
    @DisplayName("Should find seller by id successfully.")
    void shouldFindSellerByIdSuccessfully() {
        var seller = SellerEntityFactory.createSellerEntity();

        when(repository.findById(any(BigInteger.class)))
                .thenReturn(Optional.of(seller));

        var result = jpaAdapter.findSellerById(seller.getId());

        verify(repository, times(1))
                .findById(any(BigInteger.class));

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(seller.getId());
        assertThat(result.getName()).isEqualTo(seller.getName());
    }

    @Test
    @DisplayName("Should throw SellerNotFoundException when seller not found.")
    void shouldThrowSellerNotFoundExceptionWhenInvoiceNotFound() {
        var seller = SellerEntityFactory.createSellerEntity();

        when(repository.findById(any(BigInteger.class)))
                .thenReturn(Optional.empty());

        var exception = assertThrows(
                SellerNotFoundException.class,
                () -> jpaAdapter.findSellerById(seller.getId())
        );

        verify(repository, times(1))
                .findById(any(BigInteger.class));

        assertThat(exception).isNotNull();
        assertThat(exception.getStatus()).isEqualTo(PRECONDITION_FAILED);
        assertThat(exception.getMessage()).isEqualTo("Seller not found.");
    }
}
