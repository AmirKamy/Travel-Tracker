# Travel Tracker

An Android app for tracking trips and visualizing user movement on a map using **OSMDroid**, built with **Clean Architecture + MVVM** in a **multi-module** structure.

---

## Features
- **Multi-module architecture**:
  - `app`: MainActivity, Navigation, app entry point.
  - `core:model`: Domain models and mappers.
  - `core:domain`: Repository interfaces, UseCases (Login, Register, Tracking).
  - `core:data`: Repository implementations, DataSources, password hashing.
  - `core:database`: Room (DAO, Entities, AppDb).
  - `feature:login`: Login screen + ViewModel.
  - `feature:register`: Register screen + ViewModel (with full input validation).
  - `feature:home`: Map screen, `HomeViewModel`, route tracking (OSMDroid).

---

## Database
All data is stored in a **Room Database**. Trips are **persisted** (not limited to the last trip).

### Tables
- **users**
  - `id`: PK
  - `username`: unique
  - `firstName`, `lastName`, `age`, `birthDate`
  - `passwordHash`: bcrypt hashed password

- **tracks**
  - `id`: PK
  - `startedAt`: start timestamp
  - `endedAt`: end timestamp (nullable)

- **track_points**
  - `id`: PK
  - `trackId`: FK → `tracks`
  - `lat`, `lon`
  - `timestamp`: point time

---

## App Flow
- On first launch, the user registers (with validation). Passwords are **bcrypt hashed** before saving.
- After login, user lands on the **map screen**.
- **Start**:
  - Opens a new trip.
  - Immediately saves the current location.
  - Following location updates (if user moves) are stored + drawn on the map.
- **Stop**: Closes the trip.
- **Export**: Outputs all points of the trip as a CSV (with readable date-time).
- **FAB** (bottom-right): Centers map on current location.
- Location **permissions** are requested at runtime. If GPS is off, Google’s location settings dialog is shown.
- **Debug-only QA button**: Uses Android’s **Mock Location** to simulate fake GPS routes for testing. Not visible in Release.

---

## Security
- Passwords are stored hashed with **bcrypt**.
- User session is stored simply for the app’s lifecycle (not persistent beyond logout).
- No plain text password is ever stored.

---

## Testing
- Unit tests for `HomeViewModel` using **JUnit + MockK + kotlinx-coroutines-test**.
- ~80% coverage:
  - Starting & ending trips
  - Adding the first point
  - Adding points on movement
  - No-op when not tracking
  - Exporting CSV with formatted dates

---

## Tech Stack
- Kotlin, Coroutines, Flow
- Jetpack Compose + Material3
- Hilt (DI)
- Room
- OSMDroid
- Google Play Services Location
- MockK, JUnit, Robolectric (tests)

---

# رهگیر سفر (Travel Tracker)

یک اپلیکیشن اندرویدی برای **ردیابی سفرها** و نمایش مسیر کاربر روی نقشه با **OSMDroid**، طراحی‌شده با معماری **Clean Architecture + MVVM** و ساختار **مولتی‌ماژول**.

---

## ویژگی‌ها
- **ماژول‌ها**:
  - `app`: مین‌اکتیویتی و نویگیشن.
  - `core:model`: مدل‌های دامنه و مپرها.
  - `core:domain`: اینترفیس‌های ریپازیتوری، یوزکیس‌ها (لاگین، رجیستر، ترکینگ).
  - `core:data`: پیاده‌سازی ریپازیتوری‌ها، دیتا سورس‌ها، هش رمز عبور.
  - `core:database`: Room (DAO، Entity، AppDb).
  - `feature:login`: صفحه لاگین.
  - `feature:register`: صفحه ثبت‌نام (با ولیدیشن کامل ورودی‌ها).
  - `feature:home`: صفحه نقشه و ViewModel اصلی برای ردیابی مسیر.

---

## دیتابیس
داده‌ها در **Room Database** ذخیره می‌شوند. تمام سفرها ثبت می‌شوند و فقط محدود به آخرین سفر نیستند.

### جدول‌ها
- **users**: اطلاعات کاربر + پسورد هش‌شده با bcrypt  
- **tracks**: سفرها (شروع و پایان)  
- **track_points**: نقاط هر سفر (lat/lon/timestamp)

---

## جریان اپ
- ثبت‌نام اولیه (با ولیدیشن کامل) → ذخیره پسورد به صورت **هش**.  
- بعد از ورود، کاربر وارد **صفحه نقشه** می‌شود.  
- **شروع سفر**: ایجاد رکورد جدید + ثبت اولین موقعیت. سپس تغییر موقعیت‌ها ذخیره و روی نقشه رسم می‌شوند.  
- **پایان سفر**: بستن رکورد سفر.  
- **دانلود خروجی**: CSV با زمان فرمت‌شده و قابل‌خواندن.  
- **دکمه FAB**: رفتن به موقعیت فعلی کاربر.  
- **پرمیشن‌های لوکیشن** در زمان اجرا گرفته می‌شوند. اگر GPS خاموش باشد، دیالوگ Google برای روشن‌کردن باز می‌شود.  
- **دکمه تست QA (فقط Debug)**: استفاده از **Mock Location** اندروید برای ساخت مسیرهای فیک جهت تست. در ریلیز غیرفعال است.  

---

## امنیت
- ذخیره رمزها به صورت **bcrypt hash**  
- نگه‌داری سشن کاربر در طول حیات اپ به شکل ساده  
- عدم ذخیره رمز خام  

---

## تست
- تست واحد برای `HomeViewModel` با **JUnit + MockK + coroutines-test**  
- کاوریج حدود **۸۰٪**: شروع/پایان سفر، اضافه‌کردن نقاط، بی‌اثر بودن در حالت خاموش، خروجی CSV  

---

## تکنولوژی‌ها
- Kotlin, Coroutines, Flow  
- Jetpack Compose + Material3  
- Hilt  
- Room  
- OSMDroid  
- Google Play Services Location  
- MockK, JUnit, Robolectric  

---
