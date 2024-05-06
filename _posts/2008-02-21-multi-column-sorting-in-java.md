---
title: Multi Column Sorting in Java
link: http://itmusings.com/java/multi-column-sorting-in-java
author: raja
description: how do you trivially accomplish sorting by multiple columns in a search result in java
post_id: 23
date: 2008-02-21 19:53:56
post_name: multi-column-sorting-in-java
status: publish
layout: post
category: code
image: /images/sorting.png
home_page: true
tags:
  - code
  - sorting
---

[Link to code.](/code/sorting) Java provides a reasonable API for sorting. The Collections.sort() static method which has been modelled from C++ STL caters to a decent cross-section of sorting requirements. This accepts a list of objects and sorts them. To summarize what is provided by the sorting API in java, there are two variants of the sort static method of the Collections class. 

## Collections.sort(List list);

This will mutate the list and make it sorted. The items in the list are assumed to implement the java.lang.Comparable interface. Hence the items would have the ability to specify the order for the sort. For example let us take the following class 
```java    
    
    public class Account implements Comparable{
      private String accountNumber;
      private double balance;
      // Getters and setters.
      public int compareTo(Object other) {
        // return 0  or +ve number or negative number
        // depending on if the current object
       // is equal, greater or lesser than the other object
      // Any property can be used to make
      // that comparison. Let us assume that this implementation
      //  uses the accountNumber property. Then the sort
      // would happen on the basis of the account numbers
    
      }
```

The limitation of the above usage is that only one kind of sort (on the basis of accountNumber) is possible.  To address the need for sorting by other or multiple sort criteria, the API adds another static method to the Collections class. The second variant is 

## Collections.sort(List list,Comparator comparator);

This variant utilizes the comparator to determine the ordering of the items participating in the sort. Hence the ordering of the elements in this approach need not be the "natural" sorting order. Hence a list of Account objects can now be sorted by the account number or balance or any other property that is available in the Account class. These two variants while being powerful are not sufficient to cater to sorting requirements in real applications. Hence the need arises to build on the basic blocks provided by the Collections.sort() method and accomplish complex sorts. This article would discuss these strategies. Some typical requirements for sorting are enumerated below: 

  * Applications (whether they are web based or otherwise) need the ability to sort by multiple criteria. These criteria would have to be passed during run time.
  * Part of a clean sorting design should also involve the design of domain objects (or DTOs) that would specify these criteria.
  * The sorting algorithm should be extended to cater to sorting by multiple criteria. Ideally, it should be able to work on any domain object without the object implementing particular interfaces such as Comparable.
  * Sorting design should also be well integrated with "pagination" design as these two concerns go hand in hand.
Here is a simple class that would specify the SortCriterion.It defines if it is an ascending or descending sort and the criterion that should be used to do the sorting. The criterion can be a column name or a column index. 
```java   
    
       /**
         * Name of the criterion. Typically the column name to sort by.
         */
        private String name;
        // index of the criterion. The order of the thing.
        private int index;
    
        /**
         * Is this an ascending sort ??
         */
        private boolean ascendingOrder = true;
```

Since we do want the ability to sort by multiple sort criteria there should be another class that contains the different sort criteria. Here is a SortCriteria class. It contains a list of SortCriterion objects. We don't want to write a sorting algorithm (Collections.sort() performs well enough). But we do want to incorporate our SortCriteria class and accomplish the sorting. We don't want to modify the model object that is stored in the list. We cannot create a new compareTo() implementation for it based on the passed sort criteria(hey this is not ruby where we can have method over-rides at the instance level) Hence we cannot use the Collections.sort(list) signature but would have to use the second form of the sort method namely Collections.sort(List,Comparator). We will have to write a Comparator that is able to handle the sortCriteria passed to determine the order of the sort itself. Further we also would like the model object to have a say (if it desires) on the sorting. To accomplish these apparently conflicting objectives we write first an interface Sortable and a GenericComparator. The Sortable interface just enhances the compareTo() method to accept a sort criterion which is just a string that can be used to determine the order of sorting. Here it is 
    
```java   
      public interface Sortable {
        /**
         * @param sortCriterion -
         *            a field name to sort by
         * @param other -
         *            The other object to compare against.
         * @return -1 , 0 or +1 (same as java.util.Comparable)
         */
        int compareTo(String sortCriterion, Object other);
    }
```

This does not have to handle ascending and descending sorts or a list of sort criteria. The onus is on the GenericComparator class. Please look at it [here](/code/sorting/GenericComparator.java). This class looks at each SortCriterion passed and tries to compare the passed argument with the current one. It uses the column name that was passed as a field within the domain object to do the compareTo(). If the object implements Sortable then the method is invoked. Lower levels of sorts would only be needed if two objects are equal for a higher level sort. (ex:if the sort has to be by account number and balance then the balance compare is only required if the account numbers are the same) Finally, we are ready to write our SortCommand. Here is a snippet of code. 
```java    
    
    public void execute(List list, SortCriteria sortCriteria){
    
       GenericComparator gc = new GenericComparator(sortCriteria);
       Collections.sort(list,gc);
    
    }
```
As simple as that! Now we can sort any kind of list based on complex sort criteria. 
    
    
    [Link to code.](/code/sorting)

## Comments

**[Randy](#1604 "2009-05-11 19:39:56"):** Multi-Column Sorting in Java I read your column it kinda made sense. I really need to see the entire source to understand. Is it possible to email me the source. I would appreciate it. Thanx...

**[raja shankar kolluru](#1605 "2009-05-12 06:04:00"):** Randy, The source for generic comparator can be found at [this place](/code/sorting/GenericComparator.java). The other stuff such as SortCriterion and SortCriteria are simple data containers which can be found inline in the post. Please let me know if you need anything else. raja

