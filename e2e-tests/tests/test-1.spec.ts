import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('http://localhost:3000/book');
  await page.getByLabel('From').fill('2024-08-29');
});
