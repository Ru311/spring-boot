/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.condition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConditionalOnBooleanProperty @ConditionalOnBooleanProperty}.
 *
 * @author Phillip Webb
 */
class ConditionalOnBooleanPropertyTests {

	private ConfigurableApplicationContext context;

	private final ConfigurableEnvironment environment = new StandardEnvironment();

	@AfterEach
	void tearDown() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	void defaultsWhenTrue() {
		load(Defaults.class, "test=true");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	void defaultsWhenFalse() {
		load(Defaults.class, "test=false");
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Test
	void defaultsWhenMissing() {
		load(Defaults.class);
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Test
	void havingValueTrueMatchIfMissingFalseWhenTrue() {
		load(HavingValueTrueMatchIfMissingFalse.class, "test=true");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	void havingValueTrueMatchIfMissingFalseWhenFalse() {
		load(HavingValueTrueMatchIfMissingFalse.class, "test=false");
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Test
	void havingValueTrueMatchIfMissingFalseWhenMissing() {
		load(HavingValueTrueMatchIfMissingFalse.class);
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Test
	void havingValueTrueMatchIfMissingTrueWhenTrue() {
		load(HavingValueTrueMatchIfMissingTrue.class, "test=true");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	void havingValueTrueMatchIfMissingTrueWhenFalse() {
		load(HavingValueTrueMatchIfMissingTrue.class, "test=false");
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Test
	void havingValueTrueMatchIfMissingTrueWhenMissing() {
		load(HavingValueTrueMatchIfMissingTrue.class);
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	void havingValueFalseMatchIfMissingFalseWhenTrue() {
		load(HavingValueFalseMatchIfMissingFalse.class, "test=true");
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Test
	void havingValueFalseMatchIfMissingFalseWhenFalse() {
		load(HavingValueFalseMatchIfMissingFalse.class, "test=false");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	void havingValueFalseMatchIfMissingFalseWhenMissing() {
		load(HavingValueFalseMatchIfMissingFalse.class);
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Test
	void havingValueFalseMatchIfMissingTrueWhenTrue() {
		load(HavingValueFalseMatchIfMissingTrue.class, "test=true");
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Test
	void havingValueFalseMatchIfMissingTrueWhenFalse() {
		load(HavingValueFalseMatchIfMissingTrue.class, "test=false");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	void havingValueFalseMatchIfMissingTrueWhenMissing() {
		load(HavingValueFalseMatchIfMissingTrue.class);
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	void withPrefix() {
		load(HavingValueFalseMatchIfMissingTrue.class, "foo.test=true");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	private void load(Class<?> config, String... environment) {
		TestPropertyValues.of(environment).applyTo(this.environment);
		this.context = new SpringApplicationBuilder(config).environment(this.environment)
			.web(WebApplicationType.NONE)
			.run();
	}

	static abstract class BeanConfiguration {

		@Bean
		String foo() {
			return "foo";
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBooleanProperty("test")
	static class Defaults extends BeanConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBooleanProperty(name = "test", havingValue = true, matchIfMissing = false)
	static class HavingValueTrueMatchIfMissingFalse extends BeanConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBooleanProperty(name = "test", havingValue = true, matchIfMissing = true)
	static class HavingValueTrueMatchIfMissingTrue extends BeanConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBooleanProperty(name = "test", havingValue = false, matchIfMissing = false)
	static class HavingValueFalseMatchIfMissingFalse extends BeanConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBooleanProperty(name = "test", havingValue = false, matchIfMissing = true)
	static class HavingValueFalseMatchIfMissingTrue extends BeanConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBooleanProperty(prefix = "foo", name = "test")
	static class WithPrefix extends BeanConfiguration {

	}

}
