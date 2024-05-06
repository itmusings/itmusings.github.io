---
title: Calculating percentiles in MYSQL
link: http://itmusings.com/code-snippets/calculating-percentiles-in-mysql
author: raja
description: a SQL to compute the 90th percentile of values associated with an ID in MYSQL
date: 2010-10-10 03:53:31
comment_status: open
post_name: calculating-percentiles-in-mysql
status: publish
layout: post
category: code
image: /images/90thpercentile.png
home_page: false
tags:
  - sql
  - code
  - performance
---


I was doing some interesting analysis on percentiles. In the process, I had to put some results in MYSQL and use it for calculating them. I am somewhat fanatical about the usage of select statements without recourse to stored procedures for doing anything in the database. So I was hunting for a "pure select" way of accomplishing this. It turns out that MYSQL does support stored procedure variables without mandating the use of the "create procedure" directive. This, to me, represented the best of both worlds. I wanted to avoid the admittedly dubious pain (that springs out of my personal prejudices about writing an SP) of writing a stored procedure and instead accomplish the objective with a plain vanilla select. So off I went in that direction and this blog post is the result.

Let us say we have a table called rank1 that stores two columns - an Id and a value. The ID is non-unique in that an ID can have multiple values. I want to compute the average and the 90th percentile of the values for each ID. Example: Consider the following data:

|ID| Value|
|--|------|
|1|1|
|1|2|
|1|3|
|1|4|
|1|5|
|1|6|
|1|7|
|1|8|
|1|9|
|1|10|
|2|5|
|2|10|
|2|15|
|2|20|
|2|25|
|2|30|
|2|35|
|2|40|
|2|45|
|2|50|

What I want is the following:

|ID|90th percentile|Average|Count|
|1|9|5.5|10|
|2|45|27.5|10|

Here is a sql that accomplishes this:
 ```sql   
    
    select
    id,
    value as percentile,
    count_col,
    average
      from (
            SELECT @row := if(@prev_id = rank2.id, @row + 1, 1) as row,
                rank2.ID as id ,rank2.VALUE as value ,
                COUNT_TABLE.COUNT_COL as count_col,
                count_table.percentile_rownum as percentile_rownum, 
                count_table.average as average, 
                @prev_id := rank2.id as dummy 
            FROM RANK1 AS RANK2,
            (SELECT 
                COUNT(*) as COUNT_COL,
                rank1.id, 
                round(0.90*count(*)) as percentile_rownum, 
                avg(rank1.value) as average
                FROM RANK1 group by RANK1.ID) AS COUNT_TABLE
                WHERE
                count_table.id = rank2.id
                ORDER BY rank2.id, VALUE
            ) as allrows 
      where
        row = percentile_rownum
```
The gist is as follows. We first compute the average, count and the number at which the percentile figure occurs in the inner most select.  
We use this information to join back all the Ids and values along with the grouped information in the intermediary select. This intermediate SQL also computes the rownumber IDs and groups them.  
The if clause in the @row computation works to group the row numbers by ID. The @id is required to achieve this since it is a variable that monitors the change in ID from 1 to 2.

The percentile is computed by finding the row number that corresponds to the number that was computed in the inner most select.  Shoot me a question if you are having trouble following the logic.

## Comments

**[Elio Carreras](#2117 "2012-07-13 07:30:57"):** Dear Raja, Thank you very much for this. You're just a genious :) I took me half a day to understand it due to some inconsistencies in the notation (sometimes you use AS to name a column and sometimes you just omit it) and the use of comma instead of left join that i wasn't aware of. I also changed the WHERE clause by an ON clause (i'm using MySQL). It would be useful to remark that the in table (the one called "rank1") should be order by id and value, in order to select the proper row containing the percentile. Thanks a lot again. This is going to be very useful to me. Best regards, Elio

**[raja shankar kolluru](#2119 "2012-07-14 06:48:06"):** Thanks Elio. I am glad you found it useful. I know I must correct the conventions and make the SQL a tad more understandable. Will do so. Raja

