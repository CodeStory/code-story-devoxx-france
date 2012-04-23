package net.codestory.cache;

import com.google.common.base.*;
import com.google.common.cache.*;
import com.google.common.util.concurrent.*;
import lombok.*;
import org.aopalliance.intercept.*;

import java.lang.reflect.*;
import java.util.concurrent.*;

public class CacheInterceptor implements MethodInterceptor {
	private final LoadingCache<ComparableMethodInvocation, Object> cache;

	public CacheInterceptor() {
		cache = CacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.MINUTES) //
				.build(new CacheLoader<ComparableMethodInvocation, Object>() {
					@Override
					public Object load(ComparableMethodInvocation invocation) {
						return invocation.proceed();
					}

					@Override
					public ListenableFuture<Object> reload(final ComparableMethodInvocation invocation, Object oldValue) {
						return ListenableFutureTask.create(new Callable<Object>() {
							@Override
							public Object call() {
								return invocation.proceed();
							}
						});
					}
				});
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		return cache.getUnchecked(new ComparableMethodInvocation(invocation));
	}

	@EqualsAndHashCode
	static class ComparableMethodInvocation {
		Method method;
		Object[] arguments;
		transient MethodInvocation invocation;

		ComparableMethodInvocation(MethodInvocation invocation) {
			this.method = invocation.getMethod();
			this.arguments = invocation.getArguments();
			this.invocation = invocation;
		}

		Object proceed() {
			try {
				return invocation.proceed();
			} catch (Throwable e) {
				throw Throwables.propagate(e);
			}
		}
	}
}
