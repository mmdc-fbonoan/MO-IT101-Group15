# 📦 MO-IT101-Group15
MMDC Milestone 2

---
## 🚀 Tech Stack
* Java 25
* Gradle
* JUnit 5
* Spotless (Palantir formatter)

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
|   |          ├── store/
|   |          ├── ui/
|   |          ├── utils/
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
### Windows
```bash
gradlew.bat build
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