import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FeatureCard } from '../../models';

@Component({
  selector: 'app-feature-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './feature-map.component.html'
})
export class FeatureMapComponent {
  @Input() features: FeatureCard[] = [];
}
