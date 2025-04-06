# ClinicSync

**ClinicSync** is a Java-based clinic management application designed to simplify the daily operations of medical clinics. With a user-friendly Java Swing interface, it allows administrators and receptionists to efficiently manage clinic tasks, including scheduling patient appointments, tracking doctor availability, managing medical services, and generating reports.

## Features

### 🔹 Login & User Management
- **Secure login** system for administrators and receptionists.
- **Password encryption** using BCrypt to securely store user credentials.
- Admins can **register and manage receptionist accounts**, controlling access levels.

### 🔹 Admin Dashboard
- **Manage Doctors**: Add, edit, or delete doctor profiles. Track doctors' specializations, work schedules, and contact information.
- **Manage Medical Services**: Set up different medical services offered by the clinic (e.g., consultation, lab tests). Specify name, price, duration, and any other relevant details.
- **Generate and Export Reports/Statistics**: Export clinic reports in CSV or XML formats for analysis. Admins can generate patient visit statistics, revenue reports, and more.

### 🔹 Receptionist Dashboard
- **Schedule Appointments**: Receptionists can schedule, update, or cancel patient appointments. 
- **Check Doctor Availability**: Receptionists can view the available time slots for each doctor to schedule patient visits.
- **Manage Appointment Statuses**: Easily update the status of each appointment (e.g., New, In Progress, Completed) to keep track of clinic operations.

## Technologies Used

- **Java (JDK 17+)**: The core programming language for developing the application.
- **Java Swing**: Used for building the graphical user interface (GUI) to ensure a smooth user experience.
- **MySQL**: Relational database management system for storing clinic data (appointments, doctors, patients, services).
- **BCrypt**: A secure hashing algorithm used for password encryption to ensure the safety of sensitive user data.
- **JDBC**: Java Database Connectivity for interacting with the MySQL database.
