

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;

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
