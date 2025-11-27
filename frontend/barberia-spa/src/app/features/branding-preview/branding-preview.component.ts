import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { BrandingConfig, SucursalResponse } from '../../models';

@Component({
  selector: 'app-branding-preview',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './branding-preview.component.html',
  styleUrls: ['./branding-preview.component.scss']
})
export class BrandingPreviewComponent {
  @Input() branch?: SucursalResponse;

  get branding(): BrandingConfig | undefined {
    return this.branch?.branding;
  }
}
