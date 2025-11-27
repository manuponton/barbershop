import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CashSessionResponse, PaymentPayload, PaymentResponse, SalesReport } from '../../models';

@Component({
  selector: 'app-sales-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './sales-view.component.html',
  styleUrl: './sales-view.component.scss'
})
export class SalesViewComponent {
  @Input() session: CashSessionResponse | null = null;
  @Input() salesReport: SalesReport | null = null;
  @Input() processing = false;

  @Output() openCash = new EventEmitter<number>();
  @Output() closeCash = new EventEmitter<number>();
  @Output() sendPayment = new EventEmitter<PaymentPayload>();
  @Output() refreshSales = new EventEmitter<void>();

  openingAmount = 50;
  closingAmount = 0;
  payment: PaymentPayload = { description: 'Venta mostrador', amount: 0, method: 'CASH' };

  submitOpen(): void {
    this.openCash.emit(this.openingAmount);
  }

  submitClose(): void {
    this.closeCash.emit(this.closingAmount);
  }

  submitPayment(): void {
    this.sendPayment.emit({ ...this.payment });
    this.payment = { ...this.payment, amount: 0, description: 'Venta mostrador' };
  }

  protected readonly methods = ['CASH', 'CARD', 'WALLET'];
}
