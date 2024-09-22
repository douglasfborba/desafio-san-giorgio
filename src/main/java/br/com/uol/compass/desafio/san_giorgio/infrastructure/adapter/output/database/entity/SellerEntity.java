package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.entity;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Seller;
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

import java.math.BigInteger;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table(name = "SELLER")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "SELLER_ID", nullable = false)
    private BigInteger id;

    @Column(name = "SELLER_NAME", nullable = false)
    private String name;

    @OneToMany(mappedBy = "seller", fetch = LAZY)
    private Set<PaymentEntity> payments;

    public Seller toDomain() {
        return Seller.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static SellerEntity fromDomain(final Seller domain) {
        return SellerEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .build();
    }
}
