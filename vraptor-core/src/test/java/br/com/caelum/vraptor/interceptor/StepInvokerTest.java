/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package br.com.caelum.vraptor.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.vidageek.mirror.list.dsl.MirrorList;

import org.junit.Test;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.BeforeCall;
import br.com.caelum.vraptor.interceptor.example.ExampleOfSimpleStackInterceptor;
import br.com.caelum.vraptor.interceptor.example.InterceptorWithInheritance;
import br.com.caelum.vraptor.interceptor.example.WeldProxy$$$StyleInterceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.spy;

public class StepInvokerTest {

	private StepInvoker stepInvoker = new StepInvoker();
	
	@Test
	public void shouldNotReadInheritedMethods() throws Exception {
		Class<?> interceptorClass = InterceptorWithInheritance.class;
		Method method = findMethod(interceptorClass, BeforeCall.class);
		assertEquals(method, interceptorClass.getDeclaredMethod("begin"));
	}

	@Test(expected=IllegalStateException.class)
	public void shouldThrowsExceptionWhenInterceptorHasMoreThanOneAnnotatedMethod() {
		Class<?> interceptorClass = InterceptorWithMoreThanOneBeforeCallMethod.class;
		findMethod(interceptorClass, BeforeCall.class);
	}

	@Test
	public void shouldFindFirstMethodAnnotatedWithInterceptorStep(){
		ExampleOfSimpleStackInterceptor proxy = spy(new ExampleOfSimpleStackInterceptor());
		findMethod(proxy.getClass(), BeforeCall.class);
	}

	@Test
	public void shouldFindMethodFromWeldStyleInterceptor() throws SecurityException, NoSuchMethodException{
		Class<?> interceptorClass = WeldProxy$$$StyleInterceptor.class;
		assertNotNull(findMethod(interceptorClass, AroundCall.class));
	}

	private Method findMethod(Class<?> interceptorClass, Class<? extends Annotation> step) {
		MirrorList<Method> methods = stepInvoker.findAllMethods(interceptorClass);
		Method method = stepInvoker.findMethod(methods, step, interceptorClass);
		return method;
	}
}