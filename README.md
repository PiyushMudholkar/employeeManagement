# employeeManagement
Employee Management application
# Application details 

This is employee management application supplorts for add,delete,update,get employee .
it also supports addition of employees via csv.
find employees by any string
API details are mentioned below.


if Junits are filing for file please add below in the build path 

Add the dir /employeeManagement/src/test/resources in build path

===================================================================================================================

# Testing the application

Once you download the application you can run it in your respective IDE.
to call the API's you can use postman . below are the API's mentioned.

# API's

# Get employees by passing variour parameters

GET http://localhost:8080/users?sortBy=name&order=dec&minSalary=10&maxSalary=10000&offset=2&limit=5&filter=2001

This will return you the employees form the DB whose salay is between 0 to 4000 also please note default limit is set to 0 , you need to mention the limit (i.e) the number records to be fetched.

Request Parameters :
sortBy=id,name,login,salary  => default value is id
order=asc,dec => default value is asc 
minSalary=0  => default value is 0
maxSalary=40000 => default value is 4000
offset=2 => default value is 0
limit=5 => provide the value to get the n number of elmenets , default value is 0
filter=2001 => this is wildcard filtering , used for filtering in whole data , so any of id,login,name,salary,startDate matches it will return the values. 


===================================================================================================================

# Get employee by id

GET http://localhost:8080/users/e0001

This is to get the employee by id 

Responses
--If employee id  present in db employee record is returned HTTP status 200
{
    "id": "e0001",
    "login": "piyush",
    "name": "Mudholkar",
    "salary": 1000.0,
    "startDate": "2010-05-22"
}

--If employee id not present in DB : 400 Employee not present

===================================================================================================================
# Create employee

POST http://localhost:8080/users

Sample request body
{
"id":"e0001",
"login":"piyush",
"name":"Mudholkar",
"salary":"1000",
"startDate":"2010-05-22"
}

Responses

Record inserted in DB : 201 Successfully created 
--if id already exists in DB : 400  Employee ID already exists
--if login already exists in DB : 400  Employee login not unique
--if salary not >= 0 : 400  Invalid salary
--if startDate not in yyyy-MM-dd or dd-MMM-yy format: 400 Invalid date
===================================================================================================================

# Delete employee
DELETE http://localhost:8080/users/e0002

This API is to delete the employee form the DB .

Responses

--if employee is deleted form DB then HTTP status 200 and below is response
{
    "message": "Successfully deleted"
}

--If employee id is not present in DB  : 400 No such employee
===================================================================================================================

# Update employee
PUT /PATCH http://localhost:8080/users
This API is to update the employee in the DB .

Request
{
"id":"e0002",
"login":"piyush4",
"name":"mudholkar4",
"salary":1234.5,
"startDate":"2022-10-12"
}

Responses

--if employee is updated in DB then HTTP status 200 and below is response
{
    "message": "Successfully updated"
}

--If employee id is not present in DB  : 400 No such employee
===================================================================================================================

# CSV file upload
POST http://localhost:8080/users/upload

This API is to upload the employees via csv file 
csv must have all 5 header columns 
each row must have 5 records
salary must be >=0 
id must be unique in the file 
startDate format supported are yyyy-MM-dd and dd-MMM-yy

If there are validation failure then  no data will be inserted or updated and HTTP 400 response will be sent 

Responses
-- If the records are inserted or update HTTP status code 201 and response 
{
    "message": "Data created or uploaded "
}

--If the file data is valid and no records are updated HTTP 200 and below response
{
    "message": "Success but no data updated"
} 

--If the file data is having duplicate employee id then HTTP 400 and below response
{
    "message": "Duplicate id in the file e0001"
}

--If the file data is having invalid date format then HTTP 400 and below response
{
    "message": "Invalid date format for 15-12-2001 , supportd formats are yyyy-MM-dd and dd-MMM-yy "
}

--If the file data is having invalid header then HTTP 400 and below response
{
    "message": "Please upload csv file with all 5 headers in given sequence id,login,name,salary,startDate"
}

--If the file data is having invalid records (here blank record) then HTTP 400 and below response
{
    "message": "row must contain all 5 non empty records e0002,rwesley,,1934.5,2001-11-16"
}
--If the file data is having invalid records (here only 4 record) then HTTP 400 and below response
{
    "message": "row must contain all 5 records e0002,rwesley,1934.5,2001-11-16"
}
===================================================================================================================
