---
title: SaaS and Dynamic Database Columns
link: http://itmusings.com/design/saas-and-dynamic-database-columns
author: raja
description: How to handle dynamic columns in a traditional RDBMS? Some of these arguments become obsolete in NoSQL DBs but the pattern is still interesting
post_id: 249
date: 2009-07-26 19:20:42
comment_status: open
post_name: saas-and-dynamic-database-columns
layout: post
home_page: false
category: code
image: /images/saas_graphic.png
home_page: false
tags:
  - saas
  - sql
  - code
---


It has become anathema to write Data Manipulation Language (DML) statements in application programs. For instance, it is frowned upon if we write programs that alter a table to add a new column or create an index dynamically. The only exceptions to this dictum would be programs that actually are supposed to create or alter a schema dynamically - Ex: Code Generation programs and the like. In application code, DML is frowned upon.  There is a good reason for this prejudice.  A database schema often mirrors the class diagrams which in turn form part of the model for the program. Dynamically altering the model is tantamount to drastically changing the business logic on the fly. We can't have a class that changes itself. Code brittleness and all kinds of bad stuff would ensue from this transgression. So a common problem surfaces in a SaaS (Software as a Service) architecture. One running deployment of a SaaS program is supposed to execute across multiple customers, companies, organizations etc. To each one of them it has to give a custom fit experience. To design a SaaS program is besought with UI difficulties which would not be discussed here. But one that occurs repeatedly is the problem of dynamic database columns. Let us say we have a SaaS program that manages student profiles for a school. It probably has a table to store the attributes of each student. Just to make up something quick here is a quick schema of the student table. 
```sql   
    
    create table student (
    
    id varchar(20),
    
    school_id varchar(20),
    
    name varchar(100),
    
    age integer
    
    -- other fields here which i am not going to elaborate on. 
    
    );
```   

The problem is that an individual school may want some custom column to be stored for its students which is unique to the school. School A may want to store the student's hobby -a big text field. School B may intend to store the student's favorite color. Quite clearly, these two would require two columns which are not part of the "student" table. Typically, there are two solutions out of this quandary. One is to allow for it by using two or three columns extra in the student table. These additional columns acquire special meanings depending on the actual school. The new schema for student would look like this: 
```sql   
    
    create table student (
    
    id varchar(20),
    
    school_id varchar(20),
    
    name varchar(100),
    
    age integer,
    
    column1 varchar(100),
    
    column2 varchar(100),
    
    column3 varchar(100)
    
    -- other fields here which i am not going to elaborate on. 
    
    );
    
```
Now column1 would become a hobby field for school A and a color field for school B. column2 and column3 would not be used since both these schools needed just one additional column. This is a quick solution but would limit the number of flexible columns to just 3. This may or may not pose a problem and is an easy solution to implement.  The second solution is a more elaborate one. It requires a new table - let us call it student_ext (extended student table). The table may have a schema such as the following: 
```sql   
    
    create table student_ext (
    
    id varchar(20),
    
    school_id varchar(20),
    
    column_name integer,
    
    column_value varchar(100)
    
    );
    
```
Here columns can be mapped to some integer values. For instance "hobbies" column can be mapped to 100 and "fav color" column can be mapped to 105. Some typical records in the student_ext table would be : 
```   
    
    Id = 1, school_id = 'School A', column_name = 100, column_value = "playing, eating"
    
    Id = 6, school_id = 'School B', column_name = 105, column_value = "green"
```    

There are two students with IDs 1 and 6  in school A and school B respectively. Student with ID 1 has hobbies that are specified whereas student with ID 6 has green as his favorite color. However, there may be reporting requirements in School A that would require hobbies to become a column of the student table. This can be done using a view as follows: 
```sql    
    
    select student.*,
    
    max(case when student_ext.column_name = 100 then student_ext.column_value else '' end) as hobbies 
    
    from student LEFT OUTER JOIN student_ext
    
    on student.id = student_ext.id 
    group by student.id
```    

If this select statement can be made into a view then all the data from the student's table along with the relevant columns in student_ext (which stores the columns as rows) become available. This is a common trick used to de-normalize a table which stores data in extremely normalized forms.

## Comments

**[Gaurav Malhotra](#1610 "2009-08-01 15:35:30"):** Hi Raja, This is interesting article and in the recent past for my project I have done an intensive research on it. We done exactly on the lines you mentioned xxxTable & xxxTable_Dyn (which contain dynamic part). So in domain model we have now xxxEntity and xxxEntityDyn.. another problem?? Because UI/processing unit etc only want now about xxxEntity, in other words want to access the dynamic part of the entity as if its a part of the actual entity. Note: - 1) xxxEntity = jpa entity. 2) In processing unit = claim processing. We are developing claim processing generically so that it can be deployed in any contry. There are multiple solutions to solve above problem 1) Create proxy around xxxEntity and proxy handles the missing attributes, by calling the actual entity. 2) 2) Using dynamic language like Scala, groovy. We used java + groovy in our project and exploited groovy's MOP (Meta Object Protocol). Further we made all our JPA (persistent) entities as groovy aware to centrally control the dynamism in the entity plus. It will be worth to mention that we extended the search framework /persistence framework also + annotation based validation framework (business validations) on lines of JSR-303 specifications to hide the complexity totally from the end user. Note: - Since all the jpa entities are groovy aware one can write validation in groovy or java class or valang

**[Gaurav Malhotra](#1611 "2009-08-01 15:53:23"):** I would further like to mention that adding new columns to the table (entity) we called it free fields. But sometime there are requirement to create dynamic table (entity). Example in insurance domain each country has different medical procedures .One way is to create Procedure table for each country. We opted to design as new sub-system called Flex System, to address this. This sub-system also provides ability to link to existing table (entity) as free fields. I need to find time to blog my research.

**[Gaurav Malhotra](#1612 "2009-08-01 16:06:27"):** Its such an interesting topics. I forgot to mention validation on the free fields (dynamic column) & flex system (dynamic tables) After making free fields/ flex system, now its also common practice to specify the validations on it. For example if one add emailId to student table, the validation is "values to the column emailId should conform to xxx@yyyy.kkk So UI/Processing unit should fire these types of validation when storing (or importing values)

**[raja shankar kolluru](#1613 "2009-08-01 18:38:10"):** Great points Gaurav. Yes one of the biggest problem is to see how you can process the extended table. Dynamic typed languages like groovy definitely help out in this matter. Or you can use static typed languages such as Java with an inheritance to implement the dynamic part. So you would have different classes to take care of different schools with a base class that handles only the static classes. The Router pattern is useful there since the router can then use the actual school (in my original example) to determine which sub class to use. There are tons of other things actually which I did not add in this article in the interest of un-complicating it. In one of my projects I had developed a hashtable that can hold multiple types of values depending on some metadata. That takes care of your validation requirements. I had a hard coded set of types (such as int, double, email, string, date etc) that I could add. This hashtable dumped its contents into a table that also supported multiple database types. It is all insanely interesting.

