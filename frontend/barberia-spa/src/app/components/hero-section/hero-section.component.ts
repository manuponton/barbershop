import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-hero-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './hero-section.component.html'
})
export class HeroSectionComponent {
  @Input() title = '';
  @Input() requestSuccess = '';
  @Input() requestError = '';
}
