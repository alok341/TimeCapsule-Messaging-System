# ⏳ Time Capsule Messaging System

A secure **Java-based desktop application** that allows users to write encrypted messages that unlock at a future time — just like a digital time capsule. Users must register, log in, and can send messages to themselves or others, which will remain unreadable until the specified unlock date/time.

---

## 🚀 Features

- 👤 User **Registration** and **Login**
- 🔐 **Password Hashing (MD5)**
- ✉️ **Send encrypted messages** (AES) to other users
- 📅 **Unlock messages only after future time** (time capsule logic)
- 🔓 Message **decryption only after unlock time**
- 🗄️ MySQL Database + JDBC integration
- 🖥️ GUI built using **Java Swing**
- ⚙️ LocalDateTime support for scheduled message unlocking
- 🧪 Input validation and error handling

---

## 🧰 Tech Stack

| Component       | Technology              |
|-----------------|--------------------------|
| Language        | Java (JDK 8–17)          |
| GUI             | Swing                    |
| Database        | MySQL                    |
| Security        | AES (encryption), MD5 (hashing) |
| JDBC Driver     | mysql-connector-java     |
| Date/Time       | `java.time.LocalDateTime` |

---

## 🧪 Database Setup

Create your MySQL schema and the following tables:

```sql
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) UNIQUE,
  password_hash VARCHAR(100)
);

CREATE TABLE messages (
  id INT AUTO_INCREMENT PRIMARY KEY,
  sender VARCHAR(100),
  recipient VARCHAR(100),
  encrypted_message TEXT,
  unlock_time DATETIME,
  is_read BOOLEAN DEFAULT FALSE
);
⚙️ Running the App
Clone the repo

bash
Copy
Edit
git clone https://github.com/your-username/timecapsule-messaging.git
Import into Eclipse / IntelliJ

Update DB Credentials
Edit DBConnection.java:

java
Copy
Edit
String url = "jdbc:mysql://localhost:3306/timecapsule_db";
String user = "root";
String pass = "your_password";
Add MySQL Connector JAR to classpath

Download from: https://dev.mysql.com/downloads/connector/j/

Run TimeCapsuleUI.java

🔐 Security Details
🔒 Password Hashing
Uses MD5 to hash passwords before storing in DB

Stored passwords are never saved in plain text

🔏 Message Encryption
Uses AES (Advanced Encryption Standard)

Messages are encrypted before inserting into DB

Decryption only occurs after the unlock time

📝 Input Format for Unlock Time
makefile
Copy
Edit
YYYY-MM-DDTHH:MM
Example:

makefile
Copy
Edit
2025-06-16T14:30
This is parsed with LocalDateTime.parse().

✅ License
This project is licensed under the MIT License. See LICENSE for details.

🙋‍♂️ Author
Alok – @alok341

🌟 Star This Repo
If you like this project, don’t forget to ⭐ star it and share with others!

yaml
Copy
Edit

---

Let me know if you want:

- This `README.md` saved as a downloadable file (`.md`)
- A version with Hindi or Hinglish instructions
- A pre-filled GitHub repo structure (as a `.zip`)

Ready to help!
