import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { AdminAuthService } from '../../services/admin-auth.service';

@Component({
  selector: 'app-home-page',
  standalone: false,
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css',
})
export class HomePageComponent implements OnInit, OnDestroy {
  private readonly weddingDate = new Date(2027, 3, 10, 0, 0, 0);
  private countdownInterval: ReturnType<typeof setInterval> | null = null;

  countdownDays = '0';
  countdownHours = '00';
  countdownMinutes = '00';
  countdownSeconds = '00';

  constructor(
    public readonly adminAuthService: AdminAuthService,
    private readonly cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.updateCountdown();
    this.countdownInterval = setInterval(() => this.updateCountdown(), 1000);
  }

  ngOnDestroy(): void {
    if (this.countdownInterval) {
      clearInterval(this.countdownInterval);
    }
  }

  logoutDesdeHeader(): void {
    this.adminAuthService.logout();
  }

  private updateCountdown(): void {
    const now = new Date();
    const millisecondsLeft = this.weddingDate.getTime() - now.getTime();
    const totalSeconds = Math.max(0, Math.floor(millisecondsLeft / 1000));

    const days = Math.floor(totalSeconds / (24 * 60 * 60));
    const hours = Math.floor((totalSeconds % (24 * 60 * 60)) / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const seconds = totalSeconds % 60;

    this.countdownDays = days.toString();
    this.countdownHours = this.pad(hours);
    this.countdownMinutes = this.pad(minutes);
    this.countdownSeconds = this.pad(seconds);

    this.cdr.detectChanges();
  }

  private pad(value: number): string {
    return value.toString().padStart(2, '0');
  }
}
