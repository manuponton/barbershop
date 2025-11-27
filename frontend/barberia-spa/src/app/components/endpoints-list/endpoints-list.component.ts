import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { EndpointDescriptor } from '../../models';

@Component({
  selector: 'app-endpoints-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './endpoints-list.component.html'
})
export class EndpointsListComponent {
  @Input() endpoints: EndpointDescriptor[] = [];
}
