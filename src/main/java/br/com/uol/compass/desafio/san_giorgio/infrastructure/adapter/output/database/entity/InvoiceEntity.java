package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.entity;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Invoice;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table(name = "INVOICE")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "INVOICE_ID")
    private BigInteger id;

    @Column(name = "INVOICE_TOTAL")
    private BigDecimal total;

    @Column(name = "INVOICE_DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "invoice", fetch = LAZY)
    private Set<PaymentEntity> payments;

    public Invoice toDomain() {
        return Invoice.builder()
                .id(id)
                .total(total)
                .description(description)
                .build();
    }

    public static InvoiceEntity fromDomain(final Invoice domain) {
        return InvoiceEntity.builder()
                .id(domain.getId())
                .total(domain.getTotal())
                .description(domain.getDescription())
                .build();
    }
}
