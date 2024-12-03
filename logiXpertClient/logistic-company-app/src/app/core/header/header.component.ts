import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../../services/user.service';
import { MessageService } from '../../services/message.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {

  constructor(private userService: UserService, private router: Router, private messageService: MessageService) {}

  logout(): void {
    this.userService.logout().subscribe({
      next: () => {
        this.messageService.setMessage('Successfully logged out.', 'success');
        this.router.navigate(['/home']);
      },
      error: (err) => {
        const errorMessage = err.error?.message || 'An error occurred during logout.';
        this.messageService.setMessage(errorMessage, 'error');
  
        console.error('Logout failed:', err);
      },
    });
  }
  ngOnInit(): void {

  }

}
