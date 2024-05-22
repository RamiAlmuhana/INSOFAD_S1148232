import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../models/user.model";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-profile-update',
  templateUrl: './profile-update.component.html',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    RouterLink
  ],
  styleUrls: ['./profile-update.component.scss']
})
export class ProfileUpdateComponent implements OnInit {
  public user: User;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.getUserByEmail().subscribe((user: User) => {
      this.user = user;
    });
  }

  updateUser(): void {
    this.userService.updateUser(this.user).subscribe((updatedUser: User) => {
      console.log('Gebruikersgegevens succesvol bijgewerkt:', updatedUser);
      alert('Gebruikersgegevens bijgewerk')
    }, (error) => {
      console.error('Fout bij het bijwerken van de gebruikersgegevens:', error);
    });
  }

  deleteAccount(): void {
    if (confirm('Weet je zeker dat je je account wilt verwijderen? Dit kan niet ongedaan worden gemaakt.')) {
      this.userService.deleteUser(this.user.email).subscribe(() => {
        console.log('Account succesvol verwijderd');
        alert('Account verwijderd')
      }, (error) => {
        console.error('Fout bij het verwijderen van het account:', error);
      });
    }
  }
}
