import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { FooterComponent} from './core/footer/footer.component';
import { HeaderComponent } from './core/header/header.component'; 
import { Message, MessageService } from './services/message.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, FooterComponent, HeaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'logistic-company-app';

  message: Message | null = null;

  constructor(private messageService: MessageService) {
    this.messageService.getMessage().subscribe((message) => {
      this.message = message;
    });
  }
  
}
