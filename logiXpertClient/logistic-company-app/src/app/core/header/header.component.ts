import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {

  constructor(private userService: UserService) {}


  logout(): void {
    this.userService.logout().subscribe({
      next: (response) => {
        console.log(response); 
        localStorage.clear(); 
        window.location.reload();
      },
      error: (err) => {
        console.error("Logout failed:", err);
      },
    });
  }

  ngOnInit(): void {

  }

}
