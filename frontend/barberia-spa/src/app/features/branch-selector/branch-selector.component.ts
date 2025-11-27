import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SucursalResponse } from '../../models';

@Component({
  selector: 'app-branch-selector',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './branch-selector.component.html',
  styleUrls: ['./branch-selector.component.scss']
})
export class BranchSelectorComponent {
  @Input() branches: SucursalResponse[] = [];
  @Input() selectedBranchId = '';
  @Output() branchChange = new EventEmitter<string>();

  onSelect(id: string): void {
    this.branchChange.emit(id);
  }
}
