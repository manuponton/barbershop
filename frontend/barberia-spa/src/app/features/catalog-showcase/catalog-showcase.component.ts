import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CatalogItem, CatalogoResponse } from '../../models';

@Component({
  selector: 'app-catalog-showcase',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './catalog-showcase.component.html',
  styleUrls: ['./catalog-showcase.component.scss']
})
export class CatalogShowcaseComponent {
  @Input() catalogos: CatalogoResponse[] = [];
  @Input() selectedBranchId = '';
  @Output() reserveService = new EventEmitter<CatalogItem>();

  get activeItems(): CatalogItem[] {
    const match = this.catalogos.find((c) => c.sucursalId === this.selectedBranchId);
    return match?.items ?? [];
  }

  onReserve(item: CatalogItem): void {
    this.reserveService.emit(item);
  }
}
