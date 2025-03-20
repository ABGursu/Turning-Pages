# TurningPages BookStore Management System

![TurningPages Logo](resources/logo.png) <!-- You can add a logo image to your resources folder -->

## Overview

TurningPages is a comprehensive bookstore management system designed to streamline inventory management, sales tracking, and supplier relationships for bookstore owners and staff. This Java-based desktop application provides an intuitive graphical user interface with robust database functionality to manage all aspects of a bookstore business.

## Features

### User Authentication and Management
- **Multi-level Access Control**: Supports different user roles (Admin and Regular User)
- **Secure Login**: Password-protected access to the system
- **User Creation**: Admin ability to create new user accounts
- **Password Management**: Users can update their passwords
- **User Privileges**: Admin can manage user access rights
- **Read-Only Mode**: Restricted access for certain user accounts

### Book Management
- **Add New Books**: Enter complete book details including title, author, ISBN, price, etc.
- **Update Books**: Modify existing book information
- **Delete Books**: Remove books from inventory
- **Search Functionality**: Find books by various criteria
- **Validation**: Input validation to ensure data integrity

### Inventory Management
- **Stock Tracking**: Monitor current inventory levels
- **Add Stock**: Record new stock arrivals
- **Delete Stock**: Remove stock entries
- **Low Stock Alerts**: Visual indicators for items with low inventory

### Supplier Management
- **Add Suppliers**: Record supplier information
- **Remove Suppliers**: Delete supplier entries
- **Supplier Association**: Link suppliers to specific book inventory

### Sales Management
- **Record Sales**: Track book sales transactions
- **Delete Sales**: Remove incorrect sales entries
- **Sales History**: View historical sales data

### Data Import/Export
- **Export to CSV**: Export data from any table to CSV format
- **Import Data**: Import external data into the system
- **Bulk Operations**: Clear tables for data reset

### User Interface
- **Dark/Light Mode**: Toggle between dark and light themes for comfortable viewing
- **Intuitive Layout**: User-friendly interface with logical grouping of functions
- **Responsive Design**: Properly sized elements for optimal usability
- **Visual Feedback**: Clear indicators for actions and status

## Getting Started

### System Requirements
- Java Runtime Environment (JRE) 8 or higher
- Minimum 2GB RAM
- 100MB free disk space
- Windows, macOS, or Linux operating system

### Installation
1. Download the latest release from the [releases page](https://github.com/ABGursu/TurningPages/releases)
2. Extract the ZIP file to your preferred location
3. Run the `TurningPages.exe` file by double-clicking or using the command:

### First-time Setup
1. On first launch, the system will create a new SQLite database file (`bookstore.db`)
2. The default admin credentials are:
   - Username: `Admin`
   - Password: `123.321.`
3. **Important**: Change the default admin password immediately after first login

## Usage Guide

### Login
1. Launch the application
2. Enter your username and password
3. Click "Login" or press Enter

### Admin Functions
1. **User Management**:
   - Create new users
   - Modify user privileges
   - Delete users

2. **Password Management**:
   - Update your own password
   - Reset user passwords

3. **System Administration**:
   - Enter the main GUI
   - Delete all users (use with caution)

### Main Interface
1. **Book Management**:
   - Fill in book details in the form fields
   - Click "New Book" to add a book
   - Select a book and click "Update Book" to modify
   - Select a book and click "Delete Book" to remove

2. **Supplier Management**:
   - Add supplier information
   - Remove suppliers as needed

3. **Stock Management**:
   - Record new stock arrivals
   - Delete stock entries

4. **Sales Management**:
   - Record new sales
   - Delete incorrect sales entries

5. **Data Operations**:
   - Export data to CSV files
   - Import external data
   - Clear tables (use with caution)

6. **Interface Customization**:
   - Toggle dark/light mode using the dedicated button

### Searching
1. Enter search criteria in the search field
2. Click the search button or press Enter
3. View filtered results in the display area

### Exporting Data
1. Click the "Export" button
2. Select the table to export or choose "All Tables"
3. Choose the destination folder
4. Confirm the export operation

## Database Structure

The application uses an SQLite database with the following main tables:
- BOOKS: Stores book information
- SUPPLIERS: Contains supplier details
- STOCK: Tracks inventory levels
- SALES: Records sales transactions
- USERS: Stores user accounts and privileges

## Troubleshooting

### Common Issues
1. **Database Connection Error**:
   - Ensure the database file is not corrupted
   - Check file permissions

2. **Login Issues**:
   - Verify username and password
   - Contact admin if password reset is needed

3. **Export/Import Problems**:
   - Ensure you have write permissions to the destination folder
   - Check that CSV files are properly formatted for import

### Data Recovery
The system automatically creates database backups during critical operations. Backup files are stored in the `backups` folder.

## Security Considerations

- User passwords are stored securely
- Regular users have limited access based on their privileges
- The application validates all inputs to prevent SQL injection

## Support and Feedback

For support, bug reports, or feature requests, please contact:
- Email: support@turningpages.com
- Issue Tracker: [GitHub Issues](https://github.com/ABGursu/TurningPages/issues)

## License

This software is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgments

- SQLite for the embedded database
- Java Swing for the GUI components
- All contributors to the TurningPages project 