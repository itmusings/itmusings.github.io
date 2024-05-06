---
title: Bug Trends and some interesting SQL stuff
link: http://itmusings.com/management/process/bug-trends-and-self-joins
author: raja
description: exploring SQL taking bug trends as an example. BOM explosion
post_id: 174
created: 2009-04-06 11:23:05
comment_status: open
post_name: bug-trends-and-self-joins
status: publish
layout: post
home_page: false
category: code
image: /images/sql.png
tags:
  - code
  - sql
  - bugs
  - trends
---

I was recently doing some interesting bug trends for one of the projects that I am managing. The idea was to track the bug burn rate of my developers and testers. I use a primitive tracking system that is capable of orchestrating a work flow between three statuses viz active, resolved and closed. Some one opens a bug and makes it active. This is then resolved and finally closed after verification. A person has the opportunity to re-activate the bug if necessary. The bug trends analysis took me to some interesting by-roads of SQL and prompted me to pen this blog. If you want to know about cross joins, left outer joins, self joins with preceding rows, with statement, select case etc. read on - else consider yourself warned!  We start off with a table called "bugs" that contains a bug ID, description, status,release, open date, resolved date and closed date along with a ton of other information which is irrelevant to this article. We had a second table called "all_dates" that just contained one date field called asOfDate. This contains multiple rows one for each date that is of interest to us. Since I was doing the analysis for dates between Feb 2 2009 to May 1 2009 , I had every date in this range in the table "dates". So there is a row for Feb 2 2009, one for Feb 3 2009 etc. till May 1 2009. To sumarize, here is the DDL.
```sql    

    create table "bugs" (

    ID int ,  
    description varchar(255), 
    status char(20), -- valid values are 'Active', 'Closed', 'Resolved'
    release char(20) -- 4.2 is the current release.   
    open_date datetime, resolved_date datetime, closed_date datetime
    -- some more info which i am skipping.

    );

    create table "all_dates" (

    asOfDate datetime 

    );

    insert into all_dates values ('2009-02-02-00:00:00')  ;

    insert into all_dates values ('2009-02-03-00:00:00')  ;

    and so on till..(excluding holidays, saturdays and sundays)

    insert into all_dates values ('2009-05-01-00:00:00')  ;
```

I can extract all the bugs that are of interest to me by issuing a select with a where clause that filters out bugs for the particular release that my team is working on. So for instance if the current release is 4.2 then my select would be

```sql    
    select * from bugs where release = '4.2'
```
Any bug being actively worked by my team should appear for this query. Else it is considered to be "punted" to a future release. I did this analysis in Microsoft SQL Server but now with most of commercial databases supporting analytical functions and the "with" clause this SQL must be portable across at least SQL server, Oracle and DB2. Here is a step by step break up for the process involved:

1. We should make a table that lists out the bug counts for active bugs, resolved bugs and closed bugs for each day, All bugs that are active as of a certain date would participate in the active bug count, bugs that are resolved would participate in the resolved bug count etc.
2. We should be able to query the table to extract some interesting trends such as bug burn rate,# bugs punted etc.
The first part is achieved by writing a query that gives us the counts. This in itself is an interesting query.

```sql    
    select  d.asOfDate
    	, sum(case when b.status = 'active' then 1 else 0 end) as activeBugCount
    	, sum(case when b.status = 'resolved' then 1 else 0 end) as resolvedBugCount
    	, sum(case when b.status = 'closed' then 1 else 0 end) as closedBugCount
    from bugs b
          cross join 
            all_dates d
    	where
    		b.release = '4.2'
    	group by
    		d.asOfDate
```
This query uses a cross join which is useful for accomplishing a cartesian product between all_dates and the bugs tables. This artifact is especially useful in reporting applications. The "cross join" keyword itself is optional but makes the intention very clear that we DO want the cartesian product. The sum with the case statement is an ideal artifice to use to accomplish selective summing of rows based on the values in specific columns (in this case the status column). The second part needs to compute statistics based on this information. Among the stats that can be useful, one of them is the bug burn rate. A bug burn rate can be defined as the rate in which bugs are getting solved. Let us assume that on April 3 there were 4 active bugs, 3 resolved bugs and 2 closed bugs.   On April 4 let us assume that there are 5 active bugs, 4 resolved bugs and 4 closed bugs. This means that 2 bugs got closed during the day. This also means that the 3 resolved bugs must have got reduced to 1 resolved bug (since 2 resolved bugs were closed). However, the resolved bug count became 4. This means that 3 more bugs have got resolved on April 3. This means that there are 3 bugs which got "burnt" on that particular day. Hence: bug burn rate = (number of closed bugs on a day - number of closed bugs on the previous day) + (number of resolved bugs on a day - number of resolved bugs on the previous day) . The interesting thing here is that this requires knowledge about two rows viz the current row and the preceding row. Writing a SQL to solve this problem required knowledge of the newly introduced SQL analytical functions as well as the "with clause". The previous day is a somewhat risky thing to find since the previous day might be a holiday and hence may not be present in the all_dates table. So we need to work with the row numbers rather than the actual asOdDate.  Here is the final solution:

```sql    
    with bugcounts as (
    select

    		row_number() over(ORDER BY d.asOfDate ASC) AS ROWID,

    , d.asOfDate
    	, sum(case when b.status = 'active' then 1 else 0 end) as activeBugCount
    	, sum(case when b.status = 'resolved' then 1 else 0 end) as resolvedBugCount
    	, sum(case when b.status = 'closed' then 1 else 0 end) as closedBugCount
    from bugs b
          cross join 
    all_dates d
    	where
    		b.release = '4.2'
    	group by
    		d.asOfDate

    ) select
       newrow.asOfDate as BugDate
      ,newrow.activeBugCount
      ,newrow.resolvedBugCount
      ,newrow.closedBugCount
      ,newrow.activeBugCount - isNull(precedingrow.activeBugCount,0)  as incomingCount
      ,(newrow.closedBugCount - isNull(precedingrow.closedBugCount,0) +
          newrow.resolvedBugCount - isNull(precedingrow.resolvedBugCount,0)) as fixCount
      from bugcounts newrow left outer join  bugcounts precedingrow
      on
        precedingrow.rowid = newrow.rowid - 1
```
The first part enhances the old select with the row number by using the row_number() over() function so that it is possible to use the row number instead of the asOfDate for doing the joins. The second is to join one row with its preceding row to achieve the comparison. Note that it is a left outer self join. The left outer join is required since the first row will not have a preceding row. Also, the isNull() function (or its equivalent in other databases) must be used to make sure that all nulls are treated as zeroes.
