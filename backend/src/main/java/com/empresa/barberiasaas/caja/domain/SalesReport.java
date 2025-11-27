package com.empresa.barberiasaas.caja.domain;

import java.util.List;

public record SalesReport(double totalAmount, long approvedCount, long declinedCount, List<Payment> payments) {
}
