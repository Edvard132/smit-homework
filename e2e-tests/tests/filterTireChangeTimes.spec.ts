import { test, expect } from '@playwright/test';
import { DashboardPage } from '../src/pages/DashboardPage';

test.describe('Filter tire change times', () => {
  test('View only Manchester times', async ({ page }) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();
    const eventsCount = await dashboardPage.countTireChangeTimes('All');
    if (eventsCount === 0) {
      await dashboardPage.gotoNextWeek();
    }
    await dashboardPage.clickFilter('London');
    await dashboardPage.assertCountZero('London');
  });

  test('View only London times', async ({ page }) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();
    const eventsCount = await dashboardPage.countTireChangeTimes('All');
    if (eventsCount === 0) {
      await dashboardPage.gotoNextWeek();
    }
    await dashboardPage.clickFilter('Manchester');
    await dashboardPage.assertCountZero('Manchester');
  });

  test('View only Truck times', async ({ page }) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();
    const eventsCount = await dashboardPage.countTireChangeTimes('All');
    if (eventsCount === 0) {
      await dashboardPage.gotoNextWeek();
    }
    await dashboardPage.clickFilter('Car');
    await dashboardPage.assertCountZero('London');
  });

  test('View only Car times', async ({ page }) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();
    const eventsCount = await dashboardPage.countTireChangeTimes('All');
    if (eventsCount === 0) {
      await dashboardPage.gotoNextWeek();
    }
    await dashboardPage.clickFilter('Truck');
    await dashboardPage.assertCountZero('Manchester');
  });

  test('Filter 22 August events', async ({ page }) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();
    await dashboardPage.gotoNextWeek();
    await dashboardPage.selectNextWeekDays();

    await dashboardPage.assertEventsDay(/.*22, August.*/);
  });
});

test.describe('Invalid filters combination', () => {
  test('No workshop selected', async ({ page }) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();
    const eventsCount = await dashboardPage.countTireChangeTimes('All');
    if (eventsCount === 0) {
      await dashboardPage.gotoNextWeek();
    }
    await dashboardPage.clickFilter('London');
    await page.waitForTimeout(100);
    await dashboardPage.clickFilter('Manchester');

    await dashboardPage.assertErrorMsg('Select at least one workshop');
  });

  test('No vehicle type selected', async ({ page }) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();
    const eventsCount = await dashboardPage.countTireChangeTimes('All');
    if (eventsCount === 0) {
      await dashboardPage.gotoNextWeek();
    }
    await dashboardPage.clickFilter('Car');
    await page.waitForTimeout(100);
    await dashboardPage.clickFilter('Truck');

    await dashboardPage.assertErrorMsg('Select at least one vehicle type');
  });

  test('"From" date after "Until"', async ({ page }) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();

    await page.getByLabel('Until').fill('2024-08-13');

    await dashboardPage.assertErrorMsg('"From" date cannot be after "until" date');
  });
});
