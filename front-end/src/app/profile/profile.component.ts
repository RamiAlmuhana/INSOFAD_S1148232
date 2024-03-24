import {Component, OnInit} from '@angular/core';
import {Product} from "../models/product.model";
import {UserService} from "../services/user.service";
import {User} from "../models/user.model";
import {CommonModule} from "@angular/common";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  public user: User;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService
      .getUserByEmail()
      .subscribe((user: User) => {
        this.user = user;
      });
  }

}
