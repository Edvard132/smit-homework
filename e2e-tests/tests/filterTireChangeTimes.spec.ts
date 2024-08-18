import { expect, test } from '@playwright/test';
import { DashboardPage } from '../src/pages/DashboardPage';

const customTest = test.extend({
  dashboardPage: async ({ page }, use) => {
    const dashboardPage = new DashboardPage(page, '/book');
    await dashboardPage.gotoDashboard();
    await use(dashboardPage);
  }
})

customTest('FTC-1 | View all times', async ({ dashboardPage }) => {
  const eventsCount = await dashboardPage.countTireChangeTimes('All');
  if (eventsCount === 0) {
    await dashboardPage.gotoNextWeek();
    await dashboardPage.assertCountNonZero('All');
  } else {
    expect(eventsCount).toBeGreaterThan(0);
  }
});

customTest('FTC-6 | View only Manchester times', async ({ dashboardPage }) => {
  const eventsCount = await dashboardPage.countTireChangeTimes('All');
  if (eventsCount === 0) {
    await dashboardPage.gotoNextWeek();
  }
  await dashboardPage.clickFilter('London');
  await dashboardPage.assertCountZero('London');
});

customTest('FTC-7 | View only London times', async ({ dashboardPage }) => {
  const eventsCount = await dashboardPage.countTireChangeTimes('All');
  if (eventsCount === 0) {
    await dashboardPage.gotoNextWeek();
  }
  await dashboardPage.clickFilter('Manchester');
  await dashboardPage.assertCountZero('Manchester');
});

customTest('FTC-8 | View only Car times', async ({ dashboardPage }) => {
  const eventsCount = await dashboardPage.countTireChangeTimes('All');
  if (eventsCount === 0) {
    await dashboardPage.gotoNextWeek();
  }
  await dashboardPage.clickFilter('Truck');
  await dashboardPage.assertCountNonZero('Manchester');
  await dashboardPage.assertCountNonZero('London');
});

customTest('FTC-9 | View only Truck times', async ({ dashboardPage }) => {
  const eventsCount = await dashboardPage.countTireChangeTimes('All');
  if (eventsCount === 0) {
    await dashboardPage.gotoNextWeek();
  }
  await dashboardPage.clickFilter('Car');
  await dashboardPage.assertCountZero('London');
})

customTest('FTC-10 | No workshop selected', async ({ page, dashboardPage }) => {
  const eventsCount = await dashboardPage.countTireChangeTimes('All');
  if (eventsCount === 0) {
    await dashboardPage.gotoNextWeek();
  }
  await dashboardPage.clickFilter('London');
  await page.waitForTimeout(100);
  await dashboardPage.clickFilter('Manchester');

  await dashboardPage.assertErrorMsg('Select at least one workshop');
});

customTest('FTC-11 | No vehicle type selected', async ({ page, dashboardPage }) => {
  const eventsCount = await dashboardPage.countTireChangeTimes('All');
  if (eventsCount === 0) {
    await dashboardPage.gotoNextWeek();
  }
  await dashboardPage.clickFilter('Car');
  await page.waitForTimeout(100);
  await dashboardPage.clickFilter('Truck');

  await dashboardPage.assertErrorMsg('Select at least one vehicle type');
});

customTest('FTC-12 | "From" date after "Until"', async ({ page, dashboardPage }) => {
  await page.getByLabel('Until').fill('2024-08-13');
  await dashboardPage.assertErrorMsg('"Until" date cannot be before or equal to "From" date');
});

customTest('FTC-13 | Filter 28 August events', async ({ dashboardPage }) => {
  await dashboardPage.gotoNextWeek();
  await dashboardPage.selectNextWeekDays();

  await dashboardPage.assertEventsDay(/.*28, August.*/);
});
