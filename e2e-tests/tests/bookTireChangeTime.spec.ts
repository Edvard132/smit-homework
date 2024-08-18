import { test } from '@playwright/test';
import { DashboardPage } from '../src/pages/DashboardPage';

const customTest = test.extend({
  dashboardPage: async ({ page }, use) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();
    await use(dashboardPage);
  },
});

customTest.describe('Book tire change time', () => {
  customTest('FTC-2 | Book the last available time in Manchester', async ({ dashboardPage }) => {
    await dashboardPage.bookAvailableTime('Manchester');
    await dashboardPage.assertManchesterTimeBooked();
  });

  customTest('FTC-3 | Book the last available time in London', async ({ dashboardPage }) => {
    await dashboardPage.bookAvailableTime('London');
    await dashboardPage.assertLondonTimeBooked();
  });

  customTest('FTC-4 | Book Manchester without contact information', async ({ dashboardPage }) => {
    await dashboardPage.bookAvailableTimeWithoutContactInformation('Manchester');
    await dashboardPage.assertNotBooked();
  });

  customTest('FTC-5 | Book London without contact information', async ({ dashboardPage }) => {
    await dashboardPage.bookAvailableTimeWithoutContactInformation('London');
    await dashboardPage.assertNotBooked();
  });
});
