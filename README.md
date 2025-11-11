# 🚀 BFHL Java Webhook Project – PES1UG22AM049

## 📘 Overview
This project is a **Spring Boot automation application** developed as part of the **BFHL Java Qualifier Round**.  
It automatically performs the following tasks:

1. Registers the candidate on the **BFHL API** using the `/generateWebhook/JAVA` endpoint.
2. Reads the **SQL solution** from `src/main/resources/solution.sql`.
3. Stores the SQL query locally in an **H2 in-memory database**.
4. Submits the query to the `/testWebhook/JAVA` endpoint using an **access token**.
5. Logs the response confirming successful submission.

✅ The project completely automates the evaluation process — no manual submission needed.

---

## 🧠 Problem Statement (SQL Question 1)

Given three tables: **DEPARTMENT**, **EMPLOYEE**, and **PAYMENTS**,  
find the **highest salary** that was credited to an employee **not on the 1st day of any month**.  

Along with the salary, display:
- Full name (`FIRST_NAME + LAST_NAME`)
- Age (calculated from `DOB`)
- Department name

### 🧾 Expected Output Columns:
| Column | Description |
|---------|--------------|
| **SALARY** | Highest salary not paid on the 1st of any month |
| **NAME** | Combined first and last name |
| **AGE** | Employee’s age in years |
| **DEPARTMENT_NAME** | Name of the employee’s department |

---

## 💻 SQL Solution
The actual SQL solution is stored in [`src/main/resources/solution.sql`](src/main/resources/solution.sql):

```sql
SELECT 
    p.AMOUNT AS SALARY,
    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
    FLOOR(DATEDIFF(CURDATE(), e.DOB) / 365) AS AGE,
    d.DEPARTMENT_NAME
FROM PAYMENTS p
JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
WHERE DAY(p.PAYMENT_TIME) <> 1
ORDER BY p.AMOUNT DESC
LIMIT 1;

✅ Output:

SALARY	    NAME	        AGE 	DEPARTMENT_NAME
74998.00	Emily Brown	    32	    Sales



⚙️ Tech Stack

Java 17

Spring Boot 3.2.5

Maven

RestTemplate for API communication

Spring Data JPA + H2 Database

JSON parsing with Jackson


🗂️ Project Structure
bfhl-java-webhook/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/example/webhook/
│   │   │   ├── WebhookApp.java
│   │   │   ├── service/WebhookService.java
│   │   │   ├── entity/ResultEntity.java
│   │   │   ├── repository/ResultRepository.java
│   │   │   └── model/GenerateResponse.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── solution.sql
└── target/
    └── bfhl-java-webhook-0.0.1-SNAPSHOT.jar


How to Run Locally
1️⃣ Prerequisites

Install Java 17+

Install Maven 3.9+

Verify installations:

java -version
mvn -v

2️⃣ Build the Project

From your project root:

mvn clean package


✅ This creates the JAR file at:

target/bfhl-java-webhook-0.0.1-SNAPSHOT.jar

3️⃣ Run the Application
java -jar target/bfhl-java-webhook-0.0.1-SNAPSHOT.jar

4️⃣ Expected Output
🚀 Starting Webhook Flow for PES1UG22AM049
✅ Webhook: https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA
🔑 Access Token received
💾 Query saved locally with ID: 1
📤 Submission Response: 200 OK
📦 Response Body: {"success":true,"message":"Webhook processed successfully"}

📸 Proof of Work
Step	Screenshot
✅ Build Success	

▶️ Running JAR	

📨 Successful Submission	
📤 Submission Links

GitHub Repository:
https://github.com/charankrishnask/bfhl-java-webhook.git

Public JAR (Downloadable):
https://github.com/charankrishnask/bfhl-java-webhook/raw/main/target/bfhl-java-webhook-0.0.1-SNAPSHOT.jar

🏁 Conclusion

This project fulfills all the objectives of the BFHL Qualifier 1 (JAVA) task:

✅ Correct SQL logic

✅ Automated registration and submission flow

✅ Successfully processed by the API ("Webhook processed successfully")

✅ Follows modern Java Spring Boot standards

Author: Charan S K
Register No: PES1UG22AM049
Institution: PES University
Date: November 2025