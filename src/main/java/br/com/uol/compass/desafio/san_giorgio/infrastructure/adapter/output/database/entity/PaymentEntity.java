package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.entity;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table(name = "PAYMENT")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "PAYMENT_ID", nullable = false, unique = true)
    private BigInteger id;

    @Column(name = "PAYMENT_AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "PAYMENT_STATUS")
    private String status;

    @ManyToOne
    @JoinColumn(name = "INVOICE_ID")
    private InvoiceEntity invoice;

    @ManyToOne
    @JoinColumn(name = "SELLER_ID")
    private SellerEntity seller;

    public PaymentItem toDomain() {
        return PaymentItem.builder()
                .id(id)
                .invoice(invoice.toDomain())
                .seller(seller.toDomain())
                .paymentValue(amount)
                .paymentStatus(PaymentStatus.fromValue(status))
                .build();
    }

    public static PaymentEntity fromDomain(final PaymentItem domain) {
        return PaymentEntity.builder()
                .id(domain.getId())
                .invoice(InvoiceEntity.fromDomain(domain.getInvoice()))
                .seller(SellerEntity.fromDomain(domain.getSeller()))
                .amount(domain.getPaymentValue())
                .status(domain.getPaymentStatus().getValue())
                .build();
    }
}
