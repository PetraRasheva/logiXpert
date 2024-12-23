import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../../services/user.service';
import { MessageService } from '../../services/message.service';
import { ElementRef, Renderer2 } from '@angular/core';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {

  constructor(private userService: UserService, 
              private router: Router, 
              private messageService: MessageService,
              private el: ElementRef, // За директен достъп до HTML елементите на компонента
              private renderer: Renderer2 // За безопасна манипулация на DOM
  ) {}

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


  toggleMenu(): void {
    const navContainer = this.el.nativeElement.querySelector('.nav-container');
    const headerContainer = this.el.nativeElement.querySelector('.header-container');

    if (navContainer.classList.contains('active')) {
      this.renderer.removeClass(navContainer, 'active');
      this.renderer.setStyle(headerContainer, 'margin-bottom', '0');
    } else {
      this.renderer.addClass(navContainer, 'active');
      this.renderer.setStyle(headerContainer, 'margin-bottom', `${navContainer.scrollHeight}px`);
    }
  }

}
