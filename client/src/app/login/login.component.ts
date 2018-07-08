import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {LoginRequest} from "../model/login.request.model";
import {UserService} from "../service/user.service";
declare var grecaptcha: any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  submitted: boolean = false;
  invalidLogin: boolean = false;
  captchaError: boolean = false;
  loginResponse: string;
  constructor(private formBuilder: FormBuilder, private router: Router, private userService: UserService) { }

  onSubmit() {
    this.submitted = true;
    if (this.loginForm.invalid) {
      return;
    }
    const response = grecaptcha.getResponse();
    debugger;
    if (response.length === 0) {
      this.captchaError = true;
      return;
    }
    let login = new LoginRequest();
    login.email = this.loginForm.controls.email.value;
    login.password = this.loginForm.controls.password.value;
    login.recaptchaResponse = response;
    this.userService.login(login).subscribe(data => {
      if(data.status === 200) {
        //locally store the token
        this.router.navigate(['list-user']);
      } else {
        this.invalidLogin = true;
        this.loginResponse = data.message;
      }
      grecaptcha.reset();
    });
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }



}
