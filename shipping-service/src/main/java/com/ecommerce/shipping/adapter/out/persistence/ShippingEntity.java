package com.ecommerce.shipping.adapter.out.persistence;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.ecommerce.shipping.domain.model.ShippingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shippings", uniqueConstraints = {
	@UniqueConstraint(name = "uk_shipping_order_id", columnNames = { "order_id" })
})
@Getter
@Setter
@NoArgsConstructor
public class ShippingEntity {

	@Id
	@Column(nullable = false)
	private UUID id;

	@Column(name = "order_id", nullable = false)
	private Long orderId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private ShippingStatus status;

	@Column(name = "tracking_number")
	private String trackingNumber;

	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	@PrePersist
	void prePersist() {
		OffsetDateTime now = OffsetDateTime.now();
		if (createdAt == null) {
			createdAt = now;
		}
		updatedAt = now;
	}

	@PreUpdate
	void preUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}
