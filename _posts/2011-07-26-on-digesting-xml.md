---
title: On Digesting XML
link: http://itmusings.com/java/on-digesting-xml
author: raja
description: Use of the digester commons library for parsing XML using a SAX model
post_id: 714
created: 2011-07-26 02:37:13
post_name: on-digesting-xml
status: publish
layout: post
image: /images/2011/07/stomach.jpg
home_page: false
category: code
---

One of the earliest uses of XML was for the purpose of storing configuration. It was soon realized that XML constructs are more amenable for specifying nested configurations rather than properties or INI files that were hitherto used for the same purpose. Since I am a confessed frame-workaholic (a term I just coined to denote a person who has a penchant for writing frameworks at the drop of a hat) , I have always been using XML forever to specify configuration for my frameworks.

But how do I go thru this morass of XML configuration and make sense of it? We need a parser that is flexible and lends itself to this kind of thing. That is when I discovered the commons digester.

[Apache Commons digester](http://commons.apache.org/digester/) is an offshoot of the earliest Struts project. It was chiefly used to parse the struts.xml file at that time but was found to be more generic and hence a new project was created within the apache realm to publish the framework in its own right.

The first thing that strikes us as we navigate the framework is the fact that it uses a stack. Coming to think of it, an xml itself is a perfect example of a LIFO construction - the last tag inserted is the first one that is closed followed by the tag that precedes it and so on. Hence it is most amenable to be stored as a stack structure. This small but brilliant observation motivated the creation of a framework that replicates the stack to store various configuration elements. I typically use a value object which I call a descriptor to store various configurations from the XML. Let us illustrate this with an example:

Consider the following XML from the [state transition machine (STM)](/stm) :
```xml    
    
    <?xml version="1.0" encoding="UTF-8"?>
    <flows>
    <flow id='orderApproval' defaultFlow='true'>
    
    <state id='pendingApproval'>
       <on eventId='approved' newStateId='fulfilled'/>
       <on eventId='rejected'newStateId='discarded'/>
    </state>
    </flow>
    </flows>
```
Without going too much into the detail of the framework itself, the objective is to parse this xml and  keep information about each flow. We can quickly invent a "FlowDescriptor", "StateDescriptor" and TransitionDescriptor  value objects  that contains information from various levels of granularity. Here are the quick Java definitions.
```java   
    
    /**
    Stores information from the "on" tag
    */
    public class TransitionDescriptor {  
    private String eventId;  
    private String newStateId;
    // getters and setters
    }
    /**
    Stores information about states
    */
    public class StateDescriptor{
       private List<TransitionDescriptor> transitions = new ArrayList<TransitionDescriptor>();
       public void addTransition(TransitionDescriptor td) {
           transitions.add(td);
    }}
    /*
     Stores information about one flow.
    */
    public class FlowDescriptor {
     private String id;
     private boolean defaultFlow = false;
     // getters and setters.
     private List<StateDescriptor> states = new ArrayList<StateDescriptor>();
     public void addState(StateDescriptor sd){
       states.add(sd);
    } 
    }
    /**
    Acts as a container for flow objects
    */
    public class FlowsContainer {
    private List<FlowDescriptor> flows = new ArrayList<FlowDescriptor>();
    public void addFlow(FlowDescriptor fd) {
    flows.add(fd);
    }}
```
# Parsing the XML

The XML above can be parsed through the digester by passing a stream that contains this XML through the digester. But before that we need to ensure that the digester is set up correctly.

The whole idea is to set up the digester stack and add elements to it as we navigate thru the XML so that the correct value objects are created and added. Let us talk about the plan in detail.

The plan is to first add a FlowsContainer object to the top of the stack before we even begin to parse the XML. Then as we encounter individual flow tags we should create new FlowDescriptor objects and add them to the FlowContainer object. Then we need to do the same for the StateDescriptor and TransitionDescriptor objects. Here is a snippet of code that accomplishes this:
```java   
    
    /*
    Sets up the digester. Make sure that you import Commons digester.
    */
    Digester digester = new Digester();
    // push the flows container in the stack.
    FlowsContainer flowsContainer = new FlowsContainer();
    digester.push(flowsContainer);
    // add rule to create a flow descriptor as and when a flow element is encountered in the XML.
    digester.addObjectCreate("flows/flow", FlowDescriptor.class);
    //the above snippet of code uses the "built in" object creation rule to create a FlowDescriptor object
    // as and when a flow tag is encountered. The newly created flow descriptor instance is inserted also
    // by this rule into the stack. The stack contains FlowContainer->FlowDescriptor (for the current flow tag)
    // at this point in time.
    // Now we need to populate the defaultFlow and id attributes of the FlowDescriptor object
    digester.addSetProperties("flows/flow");
    // the Set Properties rule populates the properties of the last object in the stack (the flow descriptor object
    // in this case) with the attributes of the flow tag. Type conversions are made from string to boolean in case
    // of the defaultFlow attribute.
    // proceed along the same lines for the state descriptor object and the transition descriptor objects
    digester.addObjectCreate("flows/flow/state", StateDescriptor.class);
    digester.addSetProperties("flows/flow/state");
    digester.addObjectCreate("flows/flow/state/on", TransitionDescriptor.class);
    digester.addSetProperties("flows/flow/state/on");
    // As we approach the on tag the stack contains objects as follows: (from bottom to top)
    // FlowsContainer->FlowDescriptor->StateDescriptor->TransitionDescriptor
    // all the properties of the descriptors would have been populated from the corresponding attributes in the
    // corresponding XML tags
    // Now we need to unwind the stack as the XML unwinds and put the objects back into the object
    digester.addSetNext("flows/flow/state/on","addTransition");
    // the SetNext rule takes the first element of the stack and puts it into the second element.
    // In this case it takes the TransitionDescriptor object and calls the addTransition method of the
    // second element (in this case the StateDescriptor)
    digester.addSetNext("flows/flow/state","addState");
    digester.addSetNext("flows/flow","addFlow");
    // similar stuff above.
```
As we see, the built in rules of the digester make XML parsing trivial. The stack based parsing and processing makes the entire process trivial and keeps it encapsulated well.

# Digester Extensions

It is possible to add our own rules to the digester to achieve very specific behavior. We just need to extend the Rule base class.
