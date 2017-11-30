/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.sos.messaging.orders.integration;

import example.sos.messaging.orders.ProductInfo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.web.JsonProjectingMethodInterceptorFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

/**
 * @author Oliver Gierke
 */
@Configuration
class MessagingConfiguration {

	@Bean
	SpelAwareProxyProjectionFactory projectionFactory(ObjectMapper mapper) {

		JacksonMappingProvider provider = new JacksonMappingProvider(mapper);
		JsonProjectingMethodInterceptorFactory interceptorFactory = new JsonProjectingMethodInterceptorFactory(provider);

		SpelAwareProxyProjectionFactory factory = new SpelAwareProxyProjectionFactory();
		factory.registerMethodInvokerFactory(interceptorFactory);

		return factory;
	}

	@Bean
	SimpleModule ordersModule() {

		SimpleModule simpleModule = new SimpleModule();
		simpleModule.setMixInAnnotation(ProductInfo.ProductNumber.class, ProductNumberMixin.class);

		return simpleModule;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	interface ProductNumberMixin {}
}
