# Microfinance Application - Project Structure & Routes Guide

## Project Overview

A Spring Boot microfinance management application for managing groups, members, loans, and weekly collections.

---

## Database Configuration

- **Database**: PostgreSQL
- **JPA**: Hibernate with auto-update schema
- **Port**: 8080

### Database Tables

- `groups` - Group information
- `members` - Member information
- `loan` - Loan records
- `weekly_collection` - Weekly collection records

---

## API Routes & Navigation Map

### **1. Dashboard (Home Page)**

**Route**: `GET /`  
**Controller**: `DashboardController`  
**Template**: `dashboard.html`  
**Navigation**: Landing page with quick links to all major features

---

### **2. Groups Management**

#### View All Groups

- **Route**: `GET /groups`
- **Controller**: `GroupController.groups()`
- **Template**: `groups.html`
- **Features**: List all groups, Delete button for each group

#### Create New Group

- **Route**: `GET /groups/new`
- **Controller**: `GroupController.newGroup()`
- **Template**: `add-group.html`
- **Form Fields**: Group Name, Start Date

#### Save Group

- **Route**: `POST /groups` (or `/groups/save`)
- **Controller**: `GroupController.saveGroup()`
- **Redirect**: Back to `/groups`

#### Delete Group

- **Route**: `GET /groups/delete/{id}`
- **Controller**: `GroupController.deleteGroup()`
- **Redirect**: Back to `/groups`

---

### **3. Members Management**

#### View All Members (Active Only)

- **Route**: `GET /members`
- **Controller**: `MemberController.list()`
- **Template**: `members.html`
- **Filters**: Shows only ACTIVE members (soft delete)

#### Create New Member

- **Route**: `GET /members/new`
- **Controller**: `MemberController.add()`
- **Template**: `add-member.html`
- **Form Fields**: Name, Aadhaar, Address, Landmark, Group (select)

#### Save Member

- **Route**: `POST /members`
- **Controller**: `MemberController.save()`
- **Redirect**: Back to `/members`

#### Delete Member (Soft Delete)

- **Route**: `POST /members/delete/{id}`
- **Controller**: `MemberController.delete()`
- **Action**: Sets status to INACTIVE instead of deleting
- **Redirect**: Back to `/members`

---

### **4. Loans Management**

#### View All Loans

- **Route**: `GET /loans`
- **Controller**: `LoanController.loans()`
- **Template**: `loans.html`
- **Features**: List all loans with details (Principal, Interest, Weekly Installment, Balances, Status)

#### Create New Loan

- **Route**: `GET /loans/new`
- **Controller**: `LoanController.newLoan()`
- **Template**: `add-loan.html`
- **Form Fields**: Member (select), Loan Amount, Interest Amount, Total Weeks
- **Note**: Requires members to be loaded from database

#### Save Loan

- **Route**: `POST /loans/save`
- **Controller**: `LoanController.saveLoan()`
- **Redirect**: Back to dashboard `/`

#### Delete Loan

- **Route**: `GET /loans/delete/{id}`
- **Controller**: `LoanController.deleteLoan()`
- **Redirect**: Back to dashboard `/`

#### Export Loans to Excel

- **Route**: `GET /loans/export`
- **Controller**: `LoanExportController.exportLoans()`
- **Format**: XLSX (Excel file)
- **Columns**: Member Name, Loan Amount, Interest, Weeks

---

### **5. Weekly Collections**

#### View Collection Page

- **Route**: `GET /collections`
- **Controller**: `WeeklyCollectionController.showCollectionPage()`
- **Template**: `weekly-collection.html`
- **Features**: Group selection, Date picker for Sunday collections

#### Search Loans for Collection

- **Route**: `POST /collections/search`
- **Controller**: `WeeklyCollectionController.searchLoans()`
- **Template**: `weekly-collection.html`
- **Parameters**: Group ID, Collection Date
- **Returns**: Filtered loans for the selected group on that date

#### Process Payment

- **Route**: `POST /collections/pay`
- **Controller**: `WeeklyCollectionController.pay()`
- **Parameters**: Loan ID, Amount, Date, Group ID
- **Actions**:
  - Updates loan balances (Principal & Interest)
  - Automatically marks loan as CLOSED when balances reach 0
  - Saves payment record in database

---

### **6. Reports**

#### Weekly Report

- **Route**: `GET /reports/weekly`
- **Template**: `weekly-report.html`
- **POST Route**: `/reports/weekly`
- **Features**: View total deposits and group-wise totals for a specific date

#### Yearly Report

- **Route**: `GET /reports/yearly?year={year}`
- **Template**: `yearly-report.html`
- **Features**: View yearly Sunday collection plan

---

### **7. Home (Alternative)**

- **Route**: `GET /home`
- **Controller**: `HomeController.home()`
- **Template**: `home.html`

---

## Entity Relationships

```
Group (1) -----> (Many) Member
         └---> (Many) Loan

Member (1) -----> (Many) Loan
         └---> (Many) WeeklyCollection

Loan (1) -----> (Many) WeeklyCollection
```

---

## Key Features Implemented

### ✅ Groups

- Create, view, and delete groups
- Track group start date
- Soft/Hard delete support

### ✅ Members

- Add members to groups
- Aadhaar and address tracking
- Soft delete (status-based)
- View only active members

### ✅ Loans

- Create loans linked to members
- Track principal and interest amounts
- Calculate weekly installments
- Track principal and interest balances
- Loan status (ACTIVE, CLOSED)
- Export to Excel

### ✅ Weekly Collections

- Search loans by group and date
- Record weekly payments
- Automatic balance calculation
- Auto-close loans when paid off
- Group-wise collection tracking

### ✅ Reports

- Weekly deposit summaries
- Group-wise collection reports
- Yearly Sunday plan

---

## Common Whitelabel Error Fixes Applied

### Issue 1: Missing Template Routes

**Problem**: Clicking buttons redirected to non-existent routes
**Solutions Applied**:

- ✅ Added `/loans` endpoint to LoanController
- ✅ Added `/groups/new` endpoint to GroupController
- ✅ Updated dashboard with Loans link
- ✅ Fixed template name from `loan-form` to `add-loan`

### Issue 2: Property Name Mismatches

**Problem**: Templates referenced wrong entity properties
**Solutions Applied**:

- ✅ Fixed groups.html to use `groupName` instead of `name`
- ✅ Updated form inputs to match entity field names

### Issue 3: Package/Import Errors

**Problem**: Entities in wrong package locations
**Solutions Applied**:

- ✅ Fixed Loan.java package declaration
- ✅ Updated all imports to use correct entity package
- ✅ Fixed LoanExportController import

### Issue 4: Missing Endpoints

**Problem**: Form submissions had no corresponding POST endpoints
**Solutions Applied**:

- ✅ Added POST endpoint for groups at `/groups`
- ✅ Maintained legacy `/groups/save` endpoint for backward compatibility

---

## Testing the Application

### Flow 1: Create & Manage Groups

1. Go to Dashboard → Click "Groups"
2. Click "Add Group" → Fill form → Save
3. View group list → Delete if needed

### Flow 2: Create & Manage Members

1. Go to Dashboard → Click "Members"
2. Click "Add Member" → Select group → Fill form → Save
3. View member list → Soft delete if needed

### Flow 3: Create & Manage Loans

1. Go to Dashboard → Click "Create Loan"
2. Select member → Enter amounts → Fill weeks → Save
3. Go back to Dashboard → Click "Loans" to view all
4. Click "Download Loans Excel" to export

### Flow 4: Weekly Collections

1. Go to Dashboard → Click "Collections"
2. Select group → Pick Sunday date → "Load Loans"
3. Enter payment amount for each loan → Click "Pay"
4. View updated balances and auto-closed loans

### Flow 5: Reports

1. Go to Reports → Weekly/Yearly
2. Select date/year → View summaries

---

## Database Query Examples

```sql
-- Find active members in a group
SELECT * FROM members WHERE status = 'ACTIVE' AND group_id = ?

-- Find active loans for a specific date range
SELECT * FROM loan
WHERE status = 'ACTIVE'
AND start_date <= ?
AND end_date >= ?

-- Group-wise collection totals for a date
SELECT group_id, SUM(principal_paid + interest_paid)
FROM weekly_collection
WHERE collection_date = ?
GROUP BY group_id
```

---

## Performance Notes

- Lazy loading enabled for relationships
- Queries optimized for group-wise filtering
- Excel export streams data efficiently
- Weekly collection queries use date-based indexing

---

## Deployment Checklist

- ✅ Database connected and configured
- ✅ All routes properly mapped
- ✅ Templates correctly referenced
- ✅ Entity packages consistent
- ✅ Form submission endpoints exist
- ✅ No compilation errors
- ✅ Soft delete logic implemented
- ✅ Excel export working

**Status**: Ready for deployment ✅
