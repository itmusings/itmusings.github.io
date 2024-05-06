---
title: Asynchronous Execution Wrapper
link: http://itmusings.com/java/asynchronous-execution-wrapper
author: raja
description: Making a Java class asynchronous by proxying it with a dynamic wrapper
post_id: 21
created: 2008-02-10 20:18:50
comment_status: open
post_name: asynchronous-execution-wrapper
status: publish
layout: post
category: code
home_page: true
image: /images/fnf.png
tags:
  - code
  - asynchronous
  - java
  - design
---


With the advent of Java 5 , asynchronous execution has become an easy thing to code. One has to be just grab hold of an [ExecutorService](http://java.sun.com/j2se/1.5.0/docs/api/java/util/concurrent/ExecutorService.html) from the java.util.concurrent library and start passing it a [runnable](http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Runnable.html). The only problem with this is that our class has to implement the Runnable interface which is not often desirable.

My intention was to open the doors of this to every interface - not just Runnable.  I wanted to create a class that is capable of implementing any interface. So let us say I have a _Foo_ interface. There are a few criteria around Foo.

  * A method of Foo either returns a void.

OR

  * returns a meaningful value but in the process does something to expedite future calls (such as caching the result of the current call)

 

Let us say I have a FooImpl which implements Foo but does so synchronously.

I want to automatically make FooImpl asynchronous. I want to write a class that wraps FooImpl. This class should implement Foo during runtime. This way, the consumer of Foo does not know that it is dealing with any asynchronous stuff. It merely calls Foo methods and voila! they get executed asynchronously and none the wiser for it. I wanted to use JDK proxies to create a runtime implementation of Foo. So combining these two thoughts, I created a simple class [AsyncExecutor](/code/async/AsyncExecutor.java).

```java
/**
 * This class allows the user the ability to asynchronously execute the methods of an impl of any interface.
 * <p>It has a static {@link #getInstance(Class, Object, ExecutorService)} method that needs to be passed 
 * a <code>JDK 1.5 style executor service</code> along with the interface class and the implementation of the interface.
 * The returned object can be cast to the interface and be used as the proxy for the underlying impl. The underlying
 * implementation is executed asynchronously in a  separate thread as dictated by the executor service. 
 * @author raja
 *
 */
public class AsyncExecutor implements InvocationHandler{
  
  /**
   * 
   * @param interfaceToProxy - the interface class to proxy.
   * @param objectToWrap - the object that needs to be wrapped by the proxy.
   * @param executorService - the executor service that governs the asynchronous execution of the implementation class.
   * @return - the actual proxy that can be used in lieu of the objectToWrap. The proxy implements the passed interface
   * and hence can be cast to it.
   */
  @SuppressWarnings("unchecked")
  public static<T> T getInstance(Class<T> interfaceToProxy, T objectToWrap,
      ExecutorService executorService){
    return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class[] {interfaceToProxy},
        new AsyncExecutor(objectToWrap,executorService));
  }

  private Object objectToWrap;
  private ExecutorService executorService;
  
  private AsyncExecutor(Object objectToWrap,ExecutorService executorService){
    this.objectToWrap = objectToWrap;
    this.executorService = executorService;
  }

  public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
    executorService.submit(new Runnable(){
      public void run() {
        try {
          method.invoke(objectToWrap, args);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    return null;
  }
}
```

This class implements the [InvocationHandler](http://java.sun.com/j2se/1.5.0/docs/api/java/lang/reflect/InvocationHandler.html) to take advantage of the java reflection interface proxy. Separate asynchronous proxies are created for every tuple of Interface, Implementation to wrap and executor service. Notice how the static method's signature uses java generics to convey its meaning precisely.

This class can be very useful for imbibing asynchronous behavior to synchronous classes.

## Comments

**[Stefan](#2124 "2012-09-17 14:04:43"):** Hi, can you give an example of how to call the AsyncExecutor class , assuming you have a Foo interface and FooImpl class ? I must be doing something wrong but can't seem to get it. Thx.

**[raja shankar kolluru](#2125 "2012-09-17 19:05:51"):** Hello Stefan My bad. I must have been clearer. You would do this assuming you have a Foo interface and a FooImpl class. Let us say you have: 
```java  
    
    Foo foo = new FooImpl();
    // obtain an executor service from the java concurrent API.
    // I wanted to give control to the caller to create this one so you can reuse thread pools.
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    Foo asynchFoo = AsynchExecutor.getInstance(Foo.class, foo, executorService);
    // Now invoke asynchFoo as if it is FooImpl.
    asynchFoo.doSomething(); // where doSomething() exists in the Foo interface.
    // this method should be void return type. Else we would return null.
    // This is because the call would return immediately and the actual doSomething() task
    // is queued up to be executed offline depending on the executorService passed to the 
    //getInstance() method above.
```
**[Stefan](#2126 "2012-09-18 08:53:21"):** Hi Raja, thanks for the explanation. It work very well now. Great piece of code ! The only thing we have changed is that we have put in the AsyncExecutor class following: private static ExecutorService executorService = Executors.newCachedThreadPool(); So we can proxy multiples classes without the need to create multiple newFixedThreadPool. So we have 1 Pool in the AsyncExecutor class that will handle all executions. This simplifies the use of hte AsyncExecutor class in the calling classes. Thx a lot again for sharing this... Regards, Stefan

**[raja shankar kolluru](#2127 "2012-09-19 18:35:34"):** Thanks Stefan. I understand the value of putting the executor service inside AsynchExecutor. The only reason I left it outside of it is so that you can re-use the thread pool whether you use the AsynchExecutor or not. i.e you can submit jobs to it ourside of the AsynchExecutor and do not have to deal with multiple executor services in your code - one created by the AsynchExecutor and the other outside.: I typically handle the construction of the ExecutorService and also the construction of the Asynch Executor in a framework like Spring so that you would never see the code I wrote above. Instead you would build the whole lot using static constructor invocations in Spring.

