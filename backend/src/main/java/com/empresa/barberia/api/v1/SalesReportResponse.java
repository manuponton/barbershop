package com.empresa.barberia.api.v1;

import java.util.List;

public record SalesReportResponse(double totalAmount, long approvedCount, long declinedCount, List<PaymentResponse> payments) {
}
