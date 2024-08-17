import { test } from '@playwright/test';
import { DashboardPage } from '../src/pages/DashboardPage';

test.describe('Book tire change time', () => {
  test('Book the last available time in Manchester', async ({ page }) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();

    await dashboardPage.bookAvailableTime('Manchester');
    await dashboardPage.assertManchesterTimeBooked();
  });

  test('Book the last available time in London', async ({ page }) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();

    await dashboardPage.bookAvailableTime('London');
    await dashboardPage.assertLondonTimeBooked();
  });

  test('Book Manchester without contact information', async ({ page }) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();

    await dashboardPage.bookAvailableTimeWithoutContactInformation('Manchester');
    await dashboardPage.assertNotBooked();
  });

  test('Book London without contact information', async ({ page }) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();

    await dashboardPage.bookAvailableTimeWithoutContactInformation('London');
    await dashboardPage.assertNotBooked();
  });
});
