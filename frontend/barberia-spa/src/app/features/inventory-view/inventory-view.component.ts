import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProductPayload, ProductResponse, SalesProjection, StockSnapshot } from '../../models';

@Component({
  selector: 'app-inventory-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inventory-view.component.html',
  styleUrl: './inventory-view.component.scss'
})
export class InventoryViewComponent {
  @Input() products: ProductResponse[] = [];
  @Input() stockReport: StockSnapshot[] = [];
  @Input() salesReport: SalesProjection[] = [];
  @Input() submitting = false;

  @Output() refreshInventory = new EventEmitter<void>();
  @Output() createProduct = new EventEmitter<ProductPayload>();
  @Output() registerPurchase = new EventEmitter<{ productId: string; quantity: number; unitPrice: number; note?: string }>();
  @Output() registerSale = new EventEmitter<{ productId: string; quantity: number; unitPrice: number; note?: string }>();

  productForm: ProductPayload = {
    name: '',
    category: 'Retail',
    price: 0,
    stock: 0
  };

  movement = {
    productId: '',
    quantity: 1,
    unitPrice: 0,
    note: ''
  };

  submitProduct(): void {
    this.createProduct.emit({ ...this.productForm });
    this.productForm = { name: '', category: 'Retail', price: 0, stock: 0 };
  }

  submitPurchase(): void {
    this.registerPurchase.emit({ ...this.movement });
    this.movement = { ...this.movement, quantity: 1, note: '' };
  }

  submitSale(): void {
    this.registerSale.emit({ ...this.movement });
    this.movement = { ...this.movement, quantity: 1, note: '' };
  }
}
