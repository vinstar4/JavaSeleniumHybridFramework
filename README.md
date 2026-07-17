# JavaSeleniumHybridFramework

A **data-driven Selenium test automation framework** for web UI testing, built with **Java + TestNG + Maven**, using the **Page Object Model (POM)** design pattern.

Currently automates one flow: **user login**, tested against `https://freelance-learn-automation.vercel.app` (a public demo app).

Called a "hybrid" framework because it combines several patterns together: Page Object Model + TestNG DataProviders + Excel-driven test data + custom reporting/listeners.

---

## 1. Tech Stack

| Tool | Purpose |
|---|---|
| Java 8 | Language |
| Maven | Build tool & dependency management |
| TestNG | Test execution engine (`@Test`, `@DataProvider`, listeners, `testng.xml` suite config) |
| Selenium WebDriver | Browser automation |
| Apache POI (`poi`, `poi-ooxml`) | Reads test data from Excel (`.xlsx`) files |
| ChainTest (`chaintest-testng`) | Test report generation (HTML dashboard + email-style report) |

---

## 2. Prerequisites

1. **Java 8+** installed
2. **Maven 3.9+** installed
3. **Git** installed
4. **Google Chrome** installed (default browser used by the framework)

---

## 3. Project Structure

```
JavaSeleneiumHybridFramework/
├── config/
│   └── config.properties          # Environment URLs, browser, timeouts, screenshot flags
│
├── testdata/
│   └── testdata.xlsx              # Test input data (sheet: "login", plus unused "registration"/"coursedetails" sheets)
│
├── testng.xml                      # TestNG suite — which test classes run, listeners registered
│
├── src/
│   ├── main/java/
│   │   ├── base/
│   │   │   ├── BaseClass.java         # @BeforeClass/@AfterClass — starts/quits the browser per test class
│   │   │   └── BasePage.java          # Reusable wait/type/click/getText methods, used by all Page Objects
│   │   ├── factory/
│   │   │   └── BrowserFactory.java    # Creates & configures the WebDriver (Chrome/Firefox/Edge)
│   │   ├── helper/
│   │   │   ├── ConfigReader.java      # Reads config.properties
│   │   │   ├── ExcelReader.java       # Reads rows/columns from testdata.xlsx using Apache POI
│   │   │   ├── DataProviders.java     # Exposes Excel data to tests via @DataProvider
│   │   │   └── Utility.java           # Screenshot capture helper (saves to /screenshots)
│   │   ├── listeners/
│   │   │   └── ReportListener.java    # TestNG listener — logs results + embeds screenshots into ChainTest report
│   │   └── pages/
│   │       ├── LoginPage.java         # Locators + actions for the login page
│   │       ├── DashboardPage.java     # Locators + actions for the post-login dashboard
│   │       └── RegistrationPage.java  # (empty stub) not currently used
│   │
│   └── test/java/
│       └── testcases/
│           └── LoginTest.java          # The actual test case — extends BaseClass
│
├── src/test/resources/
│   └── chaintest.properties        # ChainTest report configuration
│
├── reports/chaintest/              # Generated report output (Index.html, Email.html)
├── screenshots/                    # Screenshots captured during test runs
│
└── pom.xml                         # Maven dependencies
```

---

## 4. How It All Fits Together (Read This to Understand the Flow)

Think of it in layers, from "what runs" down to "how it's proven":

1. **`testng.xml`** — the entry point. Defines the suite: which test class(es) to run (`testcases.LoginTest`), how many threads to use, and which listeners to attach (`ChainTestListener` for reporting, `ReportListener` for custom logging/screenshots).

2. **`LoginTest.java`** — the actual test method. It extends `BaseClass` (so browser setup/teardown is inherited automatically), and its `@Test` is wired to a `@DataProvider` — meaning it runs once for every row of data supplied.

3. **`DataProviders.java` + `ExcelReader.java`** — supply the test's input data. `ExcelReader` opens `testdata/testdata.xlsx` (using Apache POI) and converts the `"login"` sheet into a 2D array; `DataProviders` exposes that array to TestNG as `"logindetails"`. This is what makes the framework **data-driven** — add more rows to the spreadsheet, and the same test runs against each one, no code changes needed.

4. **`LoginPage.java` / `DashboardPage.java`** (Page Objects) — hold the actual element locators (`By.xpath(...)`) and page-specific actions. `LoginPage.loginToApplication()` fills the form and returns a `DashboardPage` object — this "returns the next page" style lets tests read like a natural sequence of steps. Test methods never touch locators directly.

5. **`BasePage.java`** — the shared toolkit every Page Object inherits: `type()`, `click()`, `getText()`, plus explicit waits (`waitForClickable`, `waitForVisible`) so tests don't fail on timing issues.

6. **`BrowserFactory.java` + `BaseClass.java`** — the plumbing that runs before/after every test class:
   - `BaseClass`'s `@BeforeClass` starts a browser via `BrowserFactory`, using the browser name from `config.properties`.
   - `BrowserFactory` applies settings (headless mode, timeouts) and navigates to the login page.
   - `BaseClass`'s `@AfterClass` quits the browser.

7. **`ReportListener.java`** — hooks into TestNG's pass/fail/skip events for every test. Depending on flags in `config.properties` (`screenshot_on_success`, `screenshot_on_failure`, `screenshot_on_skip`), it captures a screenshot (via `Utility.screenshot()`) and embeds it directly into the ChainTest report, along with a log line and (on failure) the exception message.

**In short:** `testng.xml → LoginTest → DataProviders/ExcelReader (data) → LoginPage/DashboardPage (actions) → BrowserFactory (browser)`, with `ReportListener` watching every result in the background to build the report.

---

## 5. Configuration

All environment settings live in `config/config.properties` — change these instead of hardcoding values in Java:

```properties
qaenv=https://freelance-learn-automation.vercel.app
stagenv=https://stag.freelance-learn-automation.vercel.app
browser=chrome
headless=false
pageloadtime=60
implicitwait=10
retry=2
screenshot_on_failure=true
screenshot_on_success=false
screenshot_on_skip=false
incognito=false
lambdahub=https://hub.lambdatest.com
lt_username=sample@gmail.com
lt_api_key=1115456145
```

`ConfigReader.java` reads this file at runtime, so switching browsers, environments, or reporting behavior only requires editing this file — no Java changes needed.

> **Note:** `lambdahub`, `lt_username`, and `lt_api_key` are present for future LambdaTest cloud-grid integration, but `BrowserFactory.java` doesn't currently use them — tests only run locally today. Similarly, `stagenv` is defined but not referenced anywhere; `BrowserFactory` always uses `qaenv`.

---

## 6. Test Data

Test input values live in `testdata/testdata.xlsx`, not hardcoded in Java. Currently:

| Sheet | Used? | Content |
|---|---|---|
| `login` | ✅ Yes | One row: `admin@email.com`, `admin@123` |
| `registration` | ❌ No | Empty — placeholder for a future signup test |
| `coursedetails` | ❌ No | Empty — placeholder, unused |

To add another login test case, just add another row to the `login` sheet — `LoginTest` will automatically run once per row via the `@DataProvider`.

---

## 7. Running the Tests

```bash
git clone <repo-url>
cd JavaSeleneiumHybridFramework
mvn clean test
```

This uses `testng.xml` (referenced by Maven Surefire by default when present) to determine which tests run.

---

## 8. Reports & Screenshots

| Output | Location | What it is |
|---|---|---|
| ChainTest dashboard | `reports/chaintest/Index.html` | Main report — dark-themed, detailed, includes embedded screenshots and log lines |
| ChainTest email report | `reports/chaintest/Email.html` | Same data, formatted for pasting into an email |
| Screenshots | `screenshots/` | PNGs captured per test result, based on the `screenshot_on_*` flags in `config.properties` |

👉 **After running tests, open `reports/chaintest/Index.html` — that's the main report to check.**

Report behavior (enabled/disabled generators, output paths, styling) is configured in `src/test/resources/chaintest.properties`.

---

## 9. Known Limitations / Notes

- **`RegistrationPage.java`** is an empty, unused stub — no signup flow is automated yet (matches the empty `registration` sheet in the test data).
- **LambdaTest / cloud grid config exists but isn't wired up** — `BrowserFactory.java` only ever launches a local browser (Chrome/Firefox/Edge); it never creates a `RemoteWebDriver` using the `lambdahub`/`lt_username`/`lt_api_key` values.
- **`stagenv` is unused** — there's no environment-switching logic; tests always run against `qaenv`.
- **`incognito` and `retry` properties are defined but not read anywhere in the code** — no retry-on-failure logic or incognito-mode logic exists yet.
- Only `Chrome`, `Firefox`, and `Edge` are supported browsers.

---

## 10. Extending This Framework

To add a new test:
1. Add test data as a new sheet/rows in `testdata/testdata.xlsx` (if data-driven).
2. Add a `@DataProvider` method in `DataProviders.java` if a new data source is needed.
3. Add/extend a Page Object under `src/main/java/pages/` for any new page involved (extend `BasePage` to reuse wait/type/click helpers).
4. Write the test method under `src/test/java/testcases/`, extending `BaseClass`.
5. Register the new test class inside `testng.xml` so it actually gets run.
