# 📦 MO-IT101-Group15
Team Details
- Project setup: John Kenneth Racelis
- CSV Reader: John Kenneth Racelis
- Hours Worked Calculation: Rodelconrad Aday
- Gross and Net Pay: Rodelconrad Aday
- Show Employee Information: John Kenneth Racelis

Program Details
- Console-based MotorPH payroll system with two roles: employee and payroll_staff.
- Reads employee and attendance data from CSV files.
- The program starts with a login prompt and supports two user roles: `employee` and `payroll_staff` with the password `12345`
- employee can view basic employee info using employee number.
- payroll_staff can process payroll for one employee or all employees.
- Payroll runs for June-December with two cutoffs per month (1-15, 16-31).
- Work hours count only between 8:00 AM and 5:00 PM, minus 1-hour lunch break.
- Computes gross pay, then deductions (SSS, PhilHealth, Pag-IBIG, tax) to get net pay.

Project Plan Link
- [spreadsheets_link](https://docs.google.com/spreadsheets/d/1oGS8OfyJHqV2tVsEiWUQSEDbwsIJC_nYP4yy37y5gCI/edit?usp=sharing)

---
## 🚀 Tech Stack
* Java 25
* Gradle
* JUnit 5
* Spotless (formatter)

---
## 📁 Project Structure
```
MO-IT101-Group15/
├── .github/workflows/java-ci.yml
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── src/
│   ├── main/java/
|   |          ├── Main.java
│   └── test/java/
```

---
## 🛠 Installation
### Ubuntu
Install JDK 25:
```bash
sudo apt install openjdk-25-jdk
```

---
## 🔧 Build
### Linux / macOS
```bash
./gradlew build
```
Run the generated JAR:
```bash
java -jar build/libs/MO-IT101-Group15-0.0.0.jar
```
---
## ▶ Run Application

```bash
./gradlew run
```

## 🐳 running in docker (Optional)
 - Project folder is bind-mounted (shared with container).
```bash
# Build
docker compose build
# Run (interactive shell):
docker compose run --rm -it java
# Run the app
./gradlew run
```

---
## 🧪 Run Tests
```bash
./gradlew test
```
---
## 🎨 Code Formatting
Format code:
```bash
./gradlew spotlessApply
```
Check formatting:
```bash
./gradlew spotlessCheck
```
Formatting is enforced during `build`.