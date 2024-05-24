import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../auth.service';
import {AuthResponse} from "../auth-response.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  public registerForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      infix: [''],
      lastName: ['', Validators.required],
      email: ['', [Validators.email, Validators.required, Validators.maxLength(64), Validators.minLength(5)]],
      password: ['', [Validators.minLength(8), Validators.maxLength(128)]],
      repeated_password: ['']
    });
  }

  public onSubmit(): void {
    this.authService
      .register(this.registerForm.value)
      .subscribe((authResponse: AuthResponse) => {
        console.log('AuthResponse: ', authResponse);
        this.router.navigate(['']);
      });
  }
}
