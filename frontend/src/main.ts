import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

console.log('Starting Angular application...');

bootstrapApplication(AppComponent, appConfig)
  .then(() => console.log('Application started successfully'))
  .catch((err) => {
    console.error('Error starting application:', err);
  });
