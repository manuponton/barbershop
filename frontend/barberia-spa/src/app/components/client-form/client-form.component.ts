import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClientPayload, SucursalResponse } from '../../models';

@Component({
  selector: 'app-client-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './client-form.component.html'
})
export class ClientFormComponent implements OnChanges {
  @Input() submitting = false;
  @Input() resetKey = 0;
  @Input() branches: SucursalResponse[] = [];
  @Input() selectedBranchId = '';
  @Output() submitClient = new EventEmitter<ClientPayload>();

  readonly clientForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    birthday: ['', Validators.required],
    sucursalId: ['', Validators.required]
  });

  constructor(private readonly fb: FormBuilder) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['resetKey'] && !changes['resetKey'].firstChange) {
      this.clientForm.reset();
    }

    if (changes['selectedBranchId'] && this.selectedBranchId && !this.clientForm.dirty) {
      this.clientForm.patchValue({ sucursalId: this.selectedBranchId });
    }
  }

  onSubmit(): void {
    if (this.clientForm.invalid) {
      this.clientForm.markAllAsTouched();
      return;
    }

    const payload = this.clientForm.getRawValue();
    this.submitClient.emit(payload as ClientPayload);
  }
}
