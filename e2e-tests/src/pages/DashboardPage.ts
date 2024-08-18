import { expect, Page } from '@playwright/test';

export class DashboardPage {
  constructor(private page: Page, private url: string) {}

  public async gotoDashboard(): Promise<void> {
    await this.page.goto(this.url);
  }

  cityRegex = {
    London: /.*Car$/,
    Manchester: /.*Car\/Truck$/,
    All: /.*Car(\/Truck)?/,
  };

  public async bookAvailableTime(city: string): Promise<void> {
    await this.page.getByRole('button', { name: 'Next week' }).click();
    await this.page.locator('a').filter({ hasText: this.cityRegex[city] }).last().click({ force: true });
    const field = this.page.getByPlaceholder('Name, phone');
    await field.fill('Roger, 76782321');
    await this.page.getByRole('button', { name: 'Confirm service' }).click();
  }

  public async assertManchesterTimeBooked(): Promise<void> {
    await expect(this.page.getByText('You have successfully booked')).toBeVisible();
    await expect(this.page.getByText('Address: 14 Bury New Rd, Manchester')).toBeVisible();
    await expect(this.page.getByText('Vehicle Type: Car/Truck')).toBeVisible();
  }

  public async assertLondonTimeBooked(): Promise<void> {
    await expect(this.page.getByText('You have successfully booked')).toBeVisible();
    await expect(this.page.getByText('Address: 1A Gunton Rd, London')).toBeVisible();
    await expect(this.page.getByText('Vehicle Type: Car')).toBeVisible();
  }

  public async bookAvailableTimeWithoutContactInformation(city: string): Promise<void> {
    await this.page.getByRole('button', { name: 'Next week' }).click();
    await this.page.locator('a').filter({ hasText: this.cityRegex[city] }).last().click({ force: true });
    await this.page.getByPlaceholder('Name, phone').click();
    await this.page.getByRole('button', { name: 'Confirm service' }).click();
  }

  public async assertNotBooked(): Promise<void> {
    await expect(this.page.getByText('Please enter your contact')).toBeVisible();
  }

  public async clickFilter(filter: string): Promise<void> {
    await this.page.getByLabel(filter).click();
  }

  public async countTireChangeTimes(city: string): Promise<number> {
    const count = await this.page.locator('a').filter({ hasText: this.cityRegex[city] }).count();
    return count;
  }

  public async assertCountZero(city: string): Promise<void> {
    await expect(this.page.locator('a').filter({ hasText: this.cityRegex[city] })).toHaveCount(0);
  }

  public async assertCountNonZero(city: string): Promise<void> {
    await expect(this.page.locator('a').filter({ hasText: this.cityRegex[city] })).not.toHaveCount(0);
  }

  public async gotoNextWeek(): Promise<void> {
    await this.page.getByRole('button', { name: 'Next week' }).click();
  }

  public async assertErrorMsg(msg: string): Promise<void> {
    await expect(this.page.getByText(msg)).toBeVisible();
  }

  public async selectNextWeekDays(): Promise<void> {
    await this.page.getByLabel('From').fill('2024-08-28');
    await this.page.waitForTimeout(100);

    await this.page.getByLabel('Until').fill('2024-08-28');
    await this.page.waitForTimeout(300);
  }

  public async assertEventsDay(regex: RegExp): Promise<void> {
    await this.page
      .locator('a')
      .filter({ hasText: /.*Car(\/Truck)?/ })
      .last()
      .click({ force: true });
    const last = await this.page.locator('p').nth(1).textContent();
    expect.soft(last).toMatch(regex);
    await this.page.waitForTimeout(1000);
    await this.page.getByRole('button', { name: 'Close' }).click();
    await this.page.waitForTimeout(1000);
    await this.page
      .locator('a')
      .filter({ hasText: /.*Car(\/Truck)?/ })
      .first()
      .click({ force: true });
    const first = await this.page.locator('p').nth(1).textContent();
    expect.soft(first).toMatch(regex);
  }
}
