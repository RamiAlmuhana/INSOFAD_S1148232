import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../auth.service';
import {AuthRequest} from '../auth-request.model';
import {AuthResponse} from '../auth-response.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public loginForm: FormGroup;
  public maxLoginAttempts = 5;
  public loginAttempts = 0;
  public loginDisabled = false;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.email, Validators.required, Validators.maxLength(64), Validators.minLength(5)]],
      password: ['', [Validators.minLength(8), Validators.maxLength(128)]]
    });
  }

  public onSubmit(): void {
    if (this.loginDisabled) {
      return; // Login disabled, do nothing
    }

    this.authService
      .login(this.loginForm.value)
      .subscribe((authResponse: AuthResponse) => {
        console.log('AuthResponse: ', authResponse);
        this.router.navigate(['']);
      }, error => {
        // Handle login error
        console.error('Login error: ', error);
        this.loginAttempts++;

        if (this.loginAttempts >= this.maxLoginAttempts) {
          console.log('Max login attempts reached. Login disabled.');
          this.loginDisabled = true;
        }
      });
  }

  public resetAttempts(): void {
    // Reset login attempts
    this.loginAttempts = 0;
    this.loginDisabled = false;
  }
}
