# Spring源码深入分析

## 主要内容介绍
* [Spring介绍](#spring)
* [IOC容器的实现](#spring-ioc)
* [AOP的实现](#spring-aop)
* [Spring-JDBC](#spring-jdbc)
* [Spring事务处理](#spring-transManager)
* [Spring事务处理案例介绍](#spring-transManager-sample)

## Spring介绍(https://spring.io/)
> Spring为开发者提供了一站式的轻量级应用开发平台,面向接口开发,为企业级应用开发奠定了基础
Spring基本组成模块中包含Spring IOC,AOP,MVC,JDBC等核心模块

## IOC容器的实现
IOC(Inversion Of Control)控制反转,它的思想是将需要引用和调用其他组件的服务交给IOC容器来进行管理.

依赖注入有三种方式:
- setter注入
- 构造函数注入
- 接口注入

IOC容器的设计与实现:
- BeanFactory:提供了最基本的IOC容器的功能,是由org.springframework.beans.factory.xml.XmlBeanFactory来实现的
```shell script
public XmlBeanFactory(Resource resource, BeanFactory parentBeanFactory) throws BeansException {
		super(parentBeanFactory);
        //加载BeanDefinition,通过XmlBeanDefinitionReader来读取
		this.reader.loadBeanDefinitions(resource);
	}
```
真正读取xml配置的bean是通过org.springframework.beans.factory.xml.XmlBeanDefinitionReader来实现的

- ApplicationContext:由多个实现类比如FileSystemXmlApplicationContext,ClassPathXmlApplicationContext,AnnotationConfigApplicationContext
下面我们以ClassPathXmlApplicationContext为例子来分析源码的实现:
```shell script
public ClassPathXmlApplicationContext(
			String[] configLocations, boolean refresh, @Nullable ApplicationContext parent)
			throws BeansException {

		super(parent);
		setConfigLocations(configLocations);
		if (refresh) {
		//进入refresh会涉及IOC启动的复杂的流程 这也是IOC的入口
                refresh();
		}
	}
```
下面我们来分析一下IOC的过程,分为三步:Resource定位,BeanDefinition加载,BeanDefinition注册
### 1.Resource定位
定位是BeanDefinition的资源定位,由ResourceLoader通过统一的Resource接口来完成,定位的过程类似于寻找数据的过程

### 2.BeanDefinition加载
把用户定义好的Bean表示成IOC容器内部的数据结构,BeanDefinition实际上是POJO在IOC容器中的抽象数据

### 3.注册BeanDefinition
通过BeanDefinitionRegistry来完成注册，是IOC容器注册BeanDefinition的过程,这个过程实际上是把加载过程中解析得到的BeanDefinition向IOC容器中进行注册
通过源码分析我们知道IOC容器是通过HashMap来持有BeanDefinition数据的

**值得注意的是Bean定义的载入和依赖注入是两个独立的过程**

依赖注入一般发生在应用第一次通过getBean想从容器中获取Bean的时候

讲到这里我们接下来写一个简单的例子来分析ioc的具体源码实现:
在分析源码之前我们来写一个简单的例子

1.定义一个接口
```shell script
public interface HelloService {

    String sayHello(String message);
}
```

2.实现这个接口
```shell script
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String message) {
        return "nihao " + message;
    }
}
```

3.在appcontext-service.xml中配置bean
```shell script
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                        http://www.springframework.org/schema/beans/spring-beans-4.2.xsd
                         http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
                         http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">

    <context:component-scan base-package="com.java.tech"/>

    <bean id="helloService" class="com.java.tech.impl.HelloServiceImpl"/>

</beans>
```

4.测试代码
```shell script
public class HelloServiceTest {

    @Test
    public void testHelloService() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:config/appcontext-service.xml");
        HelloService helloService = (HelloService) applicationContext.getBean("helloService");
        String message = helloService.sayHello("Jason");
        System.out.println(message);
    }
}
```

**接下来我们来分析源码的实现**
整个IOC容器启动的入口
```shell script
public ClassPathXmlApplicationContext(
			String[] configLocations, boolean refresh, @Nullable ApplicationContext parent)
			throws BeansException {

		super(parent);
		setConfigLocations(configLocations);
		if (refresh) {
			refresh();
		}
	}
```
进入到refresh
```shell script
@Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
			prepareRefresh();

			// 获得BeanFactory
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// Prepare the bean factory for use in this context.
			prepareBeanFactory(beanFactory);
  }
```
org.springframework.context.support.AbstractApplicationContext
```shell script
protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
		//刷新BeanFactory
        refreshBeanFactory();
		return getBeanFactory();
	}
```

这里使用来模版方法模式,由子类org.springframework.context.support.AbstractRefreshableApplicationContext来实现
```shell script
@Override
	protected final void refreshBeanFactory() throws BeansException {
		if (hasBeanFactory()) {
			destroyBeans();
			closeBeanFactory();
		}
		try {
			DefaultListableBeanFactory beanFactory = createBeanFactory();
			beanFactory.setSerializationId(getId());
			customizeBeanFactory(beanFactory);
            //开始进入Resource定位
			loadBeanDefinitions(beanFactory);
			synchronized (this.beanFactoryMonitor) {
				this.beanFactory = beanFactory;
			}
		}
		catch (IOException ex) {
			throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
		}
	}
```

org.springframework.context.support.AbstractXmlApplicationContext
```shell script
@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
		//由XmlBeanDefinitionReader来读取Xml中定义的Bean 
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

		// Configure the bean definition reader with this context's
		// resource loading environment.
		beanDefinitionReader.setEnvironment(this.getEnvironment());
		beanDefinitionReader.setResourceLoader(this);
		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

		// Allow a subclass to provide custom initialization of the reader,
		// then proceed with actually loading the bean definitions.
		initBeanDefinitionReader(beanDefinitionReader);
		loadBeanDefinitions(beanDefinitionReader);
	}
```

org.springframework.beans.factory.xml.XmlBeanDefinitionReader
```shell script
public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
		Assert.notNull(encodedResource, "EncodedResource must not be null");
		if (logger.isTraceEnabled()) {
			logger.trace("Loading XML bean definitions from " + encodedResource);
		}

		Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();
		if (currentResources == null) {
			currentResources = new HashSet<>(4);
			this.resourcesCurrentlyBeingLoaded.set(currentResources);
		}
		if (!currentResources.add(encodedResource)) {
			throw new BeanDefinitionStoreException(
					"Detected cyclic loading of " + encodedResource + " - check your import definitions!");
		}
		try {
			InputStream inputStream = encodedResource.getResource().getInputStream();
			try {
				InputSource inputSource = new InputSource(inputStream);
				if (encodedResource.getEncoding() != null) {
					inputSource.setEncoding(encodedResource.getEncoding());
				}
                //真正干活的 加载BeanDefinition的入口 IOC容器的第二步
				return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
			}
			finally {
				inputStream.close();
			}
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException(
					"IOException parsing XML document from " + encodedResource.getResource(), ex);
		}
		finally {
			currentResources.remove(encodedResource);
			if (currentResources.isEmpty()) {
				this.resourcesCurrentlyBeingLoaded.remove();
			}
		}
	}
```

```shell script
protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
			throws BeanDefinitionStoreException {

		try {
		    //真正解析Document 这个时候代表IOC加载流程结束
			Document doc = doLoadDocument(inputSource, resource);
			}
            //要开始进入BeanDefinition的注册流程
			int count = registerBeanDefinitions(doc, resource);
			if (logger.isDebugEnabled()) {
				logger.debug("Loaded " + count + " bean definitions from " + resource);
			}
			return count;
		}
}
```

org.springframework.beans.factory.xml.DefaultDocumentLoader
```shell script
@Override
	public Document loadDocument(InputSource inputSource, EntityResolver entityResolver,
			ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {

		DocumentBuilderFactory factory = createDocumentBuilderFactory(validationMode, namespaceAware);
		if (logger.isTraceEnabled()) {
			logger.trace("Using JAXP provider [" + factory.getClass().getName() + "]");
		}
		DocumentBuilder builder = createDocumentBuilder(factory, entityResolver, errorHandler);
		return builder.parse(inputSource);
	}
}
```
XmlBeanDefinitionReader:
```shell script
public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
		BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
		int countBefore = getRegistry().getBeanDefinitionCount();
		
        //将加载解析得到的BeanDefinition注册到HashMap中
        documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
		return getRegistry().getBeanDefinitionCount() - countBefore;
	}
```

注册的核心逻辑,org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader
```shell script
protected void doRegisterBeanDefinitions(Element root) {
		//使用到了委派模式
        BeanDefinitionParserDelegate parent = this.delegate;
		this.delegate = createDelegate(getReaderContext(), root, parent);

		if (this.delegate.isDefaultNamespace(root)) {
			String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
			if (StringUtils.hasText(profileSpec)) {
				String[] specifiedProfiles = StringUtils.tokenizeToStringArray(
						profileSpec, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
				// We cannot use Profiles.of(...) since profile expressions are not supported
				// in XML config. See SPR-12458 for details.
				if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Skipped XML bean definition file due to specified profiles [" + profileSpec +
								"] not matching: " + getReaderContext().getResource());
					}
					return;
				}
			}
		}
        //真正开始解析Bean
        //预解析
		preProcessXml(root);

        //解析BeanDefinition
		parseBeanDefinitions(root, this.delegate);
		postProcessXml(root);

		this.delegate = parent;
	}
```
org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader
```shell script
private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
		if (delegate.nodeNameEquals(ele, IMPORT_ELEMENT)) {
			importBeanDefinitionResource(ele);
		}
		else if (delegate.nodeNameEquals(ele, ALIAS_ELEMENT)) {
			processAliasRegistration(ele);
		}
        //解析xml中配置的bean
		else if (delegate.nodeNameEquals(ele, BEAN_ELEMENT)) {
			processBeanDefinition(ele, delegate);
		}
		else if (delegate.nodeNameEquals(ele, NESTED_BEANS_ELEMENT)) {
			// recurse
			doRegisterBeanDefinitions(ele);
		}
	}
```

DefaultBeanDefinitionDocumentReader
```shell script
protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
		//解析xml中id 各种标签属性
        BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
		if (bdHolder != null) {
			bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
			try {
				// Register the final decorated instance.
                // BeanDefinition注册入口
				BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
			}
			catch (BeanDefinitionStoreException ex) {
				getReaderContext().error("Failed to register bean definition with name '" +
						bdHolder.getBeanName() + "'", ele, ex);
			}
			// Send registration event.
			getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
		}
	}
```

BeanDefinitionReaderUtils
```shell script
public static void registerBeanDefinition(
			BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
			throws BeanDefinitionStoreException {

		// Register bean definition under primary name.
		String beanName = definitionHolder.getBeanName();
 
        //BeanDefinitionRegistry来完成BeanDefinition的注册流程
		registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());

		// Register aliases for bean name, if any.
		String[] aliases = definitionHolder.getAliases();
		if (aliases != null) {
			for (String alias : aliases) {
				registry.registerAlias(beanName, alias);
			}
		}
	}
```
最后通过HashMap来完成注册的流程
```shell script
@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException {

		Assert.hasText(beanName, "Bean name must not be empty");
		Assert.notNull(beanDefinition, "BeanDefinition must not be null");

		if (beanDefinition instanceof AbstractBeanDefinition) {
			try {
				((AbstractBeanDefinition) beanDefinition).validate();
			}
			catch (BeanDefinitionValidationException ex) {
				throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName,
						"Validation of bean definition failed", ex);
			}
		}
  
        //首先去缓存查询
		BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);
		if (existingDefinition != null) {
			if (!isAllowBeanDefinitionOverriding()) {
				throw new BeanDefinitionOverrideException(beanName, beanDefinition, existingDefinition);
			}
			else if (existingDefinition.getRole() < beanDefinition.getRole()) {
				// e.g. was ROLE_APPLICATION, now overriding with ROLE_SUPPORT or ROLE_INFRASTRUCTURE
				if (logger.isInfoEnabled()) {
					logger.info("Overriding user-defined bean definition for bean '" + beanName +
							"' with a framework-generated bean definition: replacing [" +
							existingDefinition + "] with [" + beanDefinition + "]");
				}
			}
			else if (!beanDefinition.equals(existingDefinition)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Overriding bean definition for bean '" + beanName +
							"' with a different definition: replacing [" + existingDefinition +
							"] with [" + beanDefinition + "]");
				}
			}
			else {
				if (logger.isTraceEnabled()) {
					logger.trace("Overriding bean definition for bean '" + beanName +
							"' with an equivalent definition: replacing [" + existingDefinition +
							"] with [" + beanDefinition + "]");
				}
			}
			this.beanDefinitionMap.put(beanName, beanDefinition);
		}
		else {
			if (hasBeanCreationStarted()) {
				// Cannot modify startup-time collection elements anymore (for stable iteration)
				synchronized (this.beanDefinitionMap) {
					this.beanDefinitionMap.put(beanName, beanDefinition);
					List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
					updatedDefinitions.addAll(this.beanDefinitionNames);
					updatedDefinitions.add(beanName);
					this.beanDefinitionNames = updatedDefinitions;
					removeManualSingletonName(beanName);
				}
			}
			else {
				// 如果没有则添加到Map中
				this.beanDefinitionMap.put(beanName, beanDefinition);
				this.beanDefinitionNames.add(beanName);
				removeManualSingletonName(beanName);
			}
			this.frozenBeanDefinitionNames = null;
		}

		if (existingDefinition != null || containsSingleton(beanName)) {
			resetBeanDefinition(beanName);
		}
	}
```

## IOC容器的依赖注入
依赖注入的触发是通过getBean,入口代码
org.springframework.beans.factory.support.AbstractBeanFactory
这里是实际取得Bean的地方,也是触发依赖注入发生的地方
```shell script
protected <T> T doGetBean(final String name, @Nullable final Class<T> requiredType,
			@Nullable final Object[] args, boolean typeCheckOnly) throws BeansException {

		final String beanName = transformedBeanName(name);
		Object bean;

		//从缓存中取得bean
        Object sharedInstance = getSingleton(beanName);
		if (sharedInstance != null && args == null) {
			if (logger.isTraceEnabled()) {
				if (isSingletonCurrentlyInCreation(beanName)) {
					logger.trace("Returning eagerly cached instance of singleton bean '" + beanName +
							"' that is not fully initialized yet - a consequence of a circular reference");
				}
				else {
					logger.trace("Returning cached instance of singleton bean '" + beanName + "'");
				}
			}
            //完成的是FactoryBean的相关处理
			bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
		}

		else {
			if (isPrototypeCurrentlyInCreation(beanName)) {
				throw new BeanCurrentlyInCreationException(beanName);
			}

			BeanFactory parentBeanFactory = getParentBeanFactory();
			if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
				// Not found -> check parent.
				String nameToLookup = originalBeanName(name);
				if (parentBeanFactory instanceof AbstractBeanFactory) {
					return ((AbstractBeanFactory) parentBeanFactory).doGetBean(
							nameToLookup, requiredType, args, typeCheckOnly);
				}
				else if (args != null) {
					return (T) parentBeanFactory.getBean(nameToLookup, args);
				}
				else if (requiredType != null) {
					return parentBeanFactory.getBean(nameToLookup, requiredType);
				}
				else {
					return (T) parentBeanFactory.getBean(nameToLookup);
				}
			}

			if (!typeCheckOnly) {
				markBeanAsCreated(beanName);
			}

			try {
				final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
				checkMergedBeanDefinition(mbd, beanName, args);

				String[] dependsOn = mbd.getDependsOn();
				if (dependsOn != null) {
					for (String dep : dependsOn) {
						if (isDependent(beanName, dep)) {
							throw new BeanCreationException(mbd.getResourceDescription(), beanName,
									"Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
						}
						registerDependentBean(dep, beanName);
						try {
							getBean(dep);
						}
						catch (NoSuchBeanDefinitionException ex) {
							throw new BeanCreationException(mbd.getResourceDescription(), beanName,
									"'" + beanName + "' depends on missing bean '" + dep + "'", ex);
						}
					}
				}

				// 通过createBean方法创建singleton bean的实例
				if (mbd.isSingleton()) {
					sharedInstance = getSingleton(beanName, () -> {
						try {
						     //开始进入createBean流程
}							return createBean(beanName, mbd, args);
						}
						catch (BeansException ex) {
							// Explicitly remove instance from singleton cache: It might have been put there
							// eagerly by the creation process, to allow for circular reference resolution.
							// Also remove any beans that received a temporary reference to the bean.
							destroySingleton(beanName);
							throw ex;
						}
					});
					bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
				}
                //这里是创建Prototype的地方
				else if (mbd.isPrototype()) {
					Object prototypeInstance = null;
					try {
						beforePrototypeCreation(beanName);
						prototypeInstance = createBean(beanName, mbd, args);
					}
					finally {
						afterPrototypeCreation(beanName);
					}
					bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
				}
			}
			catch (BeansException ex) {
				cleanupAfterBeanCreationFailure(beanName);
				throw ex;
			}
		}
		return (T) bean;
	}
```
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory
```shell script
@Override
	protected Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
			throws BeanCreationException {

		if (logger.isTraceEnabled()) {
			logger.trace("Creating instance of bean '" + beanName + "'");
		}
		RootBeanDefinition mbdToUse = mbd;
		//判断bean是否可以实例化
		Class<?> resolvedClass = resolveBeanClass(mbd, beanName);
		if (resolvedClass != null && !mbd.hasBeanClass() && mbd.getBeanClassName() != null) {
			mbdToUse = new RootBeanDefinition(mbd);
			mbdToUse.setBeanClass(resolvedClass);
		}

		try {
			mbdToUse.prepareMethodOverrides();
		}
		catch (BeanDefinitionValidationException ex) {
			throw new BeanDefinitionStoreException(mbdToUse.getResourceDescription(),
					beanName, "Validation of method overrides failed", ex);
		}

		try {
		    //开始进入到createBean 这里是创建Bean的调用
}			Object beanInstance = doCreateBean(beanName, mbdToUse, args);
			if (logger.isTraceEnabled()) {
				logger.trace("Finished creating instance of bean '" + beanName + "'");
			}
			return beanInstance;
		}
		catch (BeanCreationException | ImplicitlyAppearedSingletonException ex) {
			throw ex;
		}
		catch (Throwable ex) {
			throw new BeanCreationException(
					mbdToUse.getResourceDescription(), beanName, "Unexpected exception during bean creation", ex);
		}
	}
```
接下来看下doCreateBean中Bean是怎样生成的 
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory
```shell script
protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final @Nullable Object[] args)
			throws BeanCreationException {

		// 是用来持有创建出来的bean对象的
		BeanWrapper instanceWrapper = null;
		if (mbd.isSingleton()) {
			instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
		}
		if (instanceWrapper == null) {
            //这里是创建Bean的地方,由createBeanInstance来创建
}			instanceWrapper = createBeanInstance(beanName, mbd, args);
		}
		final Object bean = instanceWrapper.getWrappedInstance();
		Class<?> beanType = instanceWrapper.getWrappedClass();
		if (beanType != NullBean.class) {
			mbd.resolvedTargetType = beanType;
		}
        try {
            //这里是Bean的初始化,依赖注入都是发生在这里的
}}        	populateBean(beanName, mbd, instanceWrapper);
        	exposedObject = initializeBean(beanName, exposedObject, mbd);
        }
        catch (Throwable ex) {
        	if (ex instanceof BeanCreationException && beanName.equals(((BeanCreationException) ex).getBeanName())) {
        				throw (BeanCreationException) ex;
          }
         else {
        		throw new BeanCreationException(
        			  mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);
        	}
        }
}
```
接下来再看下createBeanInstance是如何创建Bean的实例
```shell script
protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {
		// 判断需要创建的Bean是否可以实例化
		Class<?> beanClass = resolveBeanClass(mbd, beanName);

		if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {
			throw new BeanCreationException(mbd.getResourceDescription(), beanName,
					"Bean class isn't public, and non-public access not allowed: " + beanClass.getName());
		}

		Supplier<?> instanceSupplier = mbd.getInstanceSupplier();
		if (instanceSupplier != null) {
			return obtainFromSupplier(instanceSupplier, beanName);
		}
        //使用工厂方法对Bean进行实例化
		if (mbd.getFactoryMethodName() != null) {
			return instantiateUsingFactoryMethod(beanName, mbd, args);
		}

		boolean resolved = false;
		boolean autowireNecessary = false;
		if (args == null) {
			synchronized (mbd.constructorArgumentLock) {
				if (mbd.resolvedConstructorOrFactoryMethod != null) {
					resolved = true;
					autowireNecessary = mbd.constructorArgumentsResolved;
				}
			}
		}
		if (resolved) {
			if (autowireNecessary) {
				return autowireConstructor(beanName, mbd, null, null);
			}
			else {
				return instantiateBean(beanName, mbd);
			}
		}

		//使用构造函数进行实例化
		Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
		if (ctors != null || mbd.getResolvedAutowireMode() == AUTOWIRE_CONSTRUCTOR ||
				mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args)) {
			return autowireConstructor(beanName, mbd, ctors, args);
		}

		ctors = mbd.getPreferredConstructors();
		if (ctors != null) {
			return autowireConstructor(beanName, mbd, ctors, null);
		}
        //使用默认的构造函数来实例化bean
		return instantiateBean(beanName, mbd);
	}
```

实例化过程:
```shell script
protected BeanWrapper instantiateBean(final String beanName, final RootBeanDefinition mbd) {
		try {
			Object beanInstance;
			final BeanFactory parent = this;
			if (System.getSecurityManager() != null) {
				beanInstance = AccessController.doPrivileged((PrivilegedAction<Object>) () ->
						getInstantiationStrategy().instantiate(mbd, beanName, parent),
						getAccessControlContext());
			}
			else {
				beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
			}
			BeanWrapper bw = new BeanWrapperImpl(beanInstance);
			initBeanWrapper(bw);
			return bw;
		}
		catch (Throwable ex) {
			throw new BeanCreationException(
					mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
		}
	}
```

下面我们来结合几个简单的例子(Map,List,Set,Properties)来演示依赖注入的使用

### List注入
1.先定义一个类
```shell script
@Data
public class ShoppingCart {

    private List<String> fruits;
}
```

2.在appcontext.service.xml中配置
```shell script
 <!--注入list-->
    <bean id="shoppingCart" class="com.java.tech.model.ShoppingCart">
        <property name="fruits">
            <list>
                <value>apple</value>
                <value>pear</value>
                <value>orange</value>
            </list>
        </property>
    </bean>
```
配置map
```shell script
        <!--注入map-->
        <property name="basicInfo">
            <map>
                <entry key="member" value-ref="member"></entry>
                <entry key="vip" value-ref="vip"></entry>
                <entry key="superVIP" value-ref="superVIP"></entry>
            </map>
        </property>

         <bean id="member" class="com.java.tech.model.Customer">
               <property name="name" value="member"/>
               <property name="address" value="shanghai"/>
               <property name="mobile" value="18701705689"/>
           </bean>
       
           <bean id="vip" class="com.java.tech.model.Customer">
               <property name="name" value="vip"/>
               <property name="address" value="nanjing"/>
               <property name="mobile" value="18701705688"/>
           </bean>
       
           <bean id="superVIP" class="com.java.tech.model.Customer">
               <property name="name" value="superVIP"/>
               <property name="address" value="shanghai"/>
               <property name="mobile" value="18701705655"/>
           </bean>
```

3.测试代码
```shell script
public class ShoppingCartTest {

    @Test
    public void test(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:config/appcontext-service.xml");
        ShoppingCart shoppingCart  = (ShoppingCart) applicationContext.getBean("shoppingCart");
        List<String> fruits = shoppingCart.getFruits();
        System.out.println(fruits);
    }
}
```

## ApplicationContextAware,DisposableBean和InitializingBean的用法
InitializingBean:表示在初始化bean之前执行的方法,用于设置bean的属性配置,类似BeanFactoryAware,BeanNameAware和ApplicationContextAware
```shell script
public interface InitializingBean {
	void afterPropertiesSet() throws Exception;
}
```

DisposableBean:就是在bean被销毁的时候,spring容器会帮你自动执行该方法,对于使用完以后需要释放资源的bean都会实现这个接口
```shell script
public interface DisposableBean {

	/**
	 * Invoked by the containing {@code BeanFactory} on destruction of a bean.
	 * @throws Exception in case of shutdown errors. Exceptions will get logged
	 * but not rethrown to allow other beans to release their resources as well.
	 */
	void destroy() throws Exception;
}
```

ApplicationContextAware:在容器初始化的过程中将bean属性注入,与InitializingBean的区别是ApplicationContextAware当容器初始化的时候
会自动将ApplicationContextAware注入进来,我们可以通过注入进来的applicationContext来获取bean对象,Spring Aware的目的是让Bean获得容器的服务。

**spring源码中接口以Aware结尾的接口表示如果在某个类里面想要使用Spring就可以实现该接口告诉spring**
- BeanNameAware:在Bean中得到IOC容器中的Bean的实例名字
- BeanFactoryAware:在Bean中获得所在的IOC容器
- ApplicationContextAware:在Bean中得到Bean所在的应用上下文
- MessageSourceAware:在Bean中得到消息源
- ResourceLoaderAware:在Bean中得到ResourceLoader

```shell script
public interface ApplicationContextAware extends Aware {

	void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
`````
如何应用,下面来举一个例子
ApplicationContextAware在实际中作为一个组件,一个类如果实现该接口表示你可以从spring容器获取bean
```shell script
public class SpringContainer implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContainer.applicationContext == null) {
            SpringContainer.applicationContext = applicationContext;
        }
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }
}
```

然后编写测试类
```shell script
public class SpringContainerTest extends AbstractSpringContextTest {

    @Test
    public void testGetBeanName() {
        AccountService accountService = (AccountService) SpringContainer.getBean("accountService");
        accountService.create(buildAccountParam());
    }

    @Test
    public void testGetBeanByClassName(){
        EchoService echoService = SpringContainer.getBean("echoService", EchoService.class);
        String result = echoService.echo("Peter");
        System.out.println(result);
    }

    @Test
    public void testGetBean(){
        HelloService helloService = SpringContainer.getBean(HelloServiceImpl.class);
        String message =  helloService.sayHello("lucy");
        System.out.println(message);
    }

    private AccountRequest buildAccountParam() {
        return AccountRequest.builder().account(Account.builder().sender("lucy").receiver("lili").amount(2000.00).build()).build();
    }
}
```

## AOP的实现
#### 什么是AOP
>AOP(Aspect Oriented Programming)面向切面的编程,在程序开发中主要用来解决一些系统层面上的问题,比如日志,事务和权限
在不改变原有的逻辑的基础上,增加一些额外的功能

AOP有几个非常重要的概念:
- JoinPoint:连接点程序执行过程中的一个点,一般是方法的调用
- Advice通知:定义连接点做什么,为切面增强提供入口,分为前置通知,后置通知,异常通知,最终通知和环绕通知(切面要完成的功能)
- PointCut切入点:决定Advice通知应该作用于哪个连接点,是连接点匹配的表达式,用于确定是否需要执行通知
- Advisor通知器:对Advice与PointCut进行结合
- Aspect切面:表示PointCut(切入点)和Advice(通知)的结合
- AOP代理(AOP Proxy):AOP框架创建的对象,代理就是目标对象的加强

## AspectJ支持
@AspectJ是一种使用Java注解来实现AOP的编码风格

### 使用Java Configuration来支持@AspectJ
```shell script
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
}
```

### 使用XML方式
```shell script
<aop:aspectj-autoproxy/>
```

### AOP使用场景
- 权限
- 缓存
- 日志
- 错误处理
- 事务

#### AOP的设计与实现原理
AOP的设计与实现是基于JDK动态代理的技术

建立AopProxy代理对象是通过ProxyFactoryBean来生成AopProxy代理对象
下面我们来分析一下源码实现
ProxyFactoryBean中的getObject

```shell script
@Override
	@Nullable
	public Object getObject() throws BeansException {
	    //初始化通知器链
		initializeAdvisorChain();
        //这里对singleton和protype的类型进行区分生成对应的proxy
		if (isSingleton())
		} {
			return getSingletonInstance();
		}
		else {
			if (this.targetName == null) {
				logger.info("Using non-singleton proxies with singleton targets is often undesirable. " +
						"Enable prototype proxies by setting the 'targetName' property.");
			}
			return newPrototypeInstance();
		}
	}
```

生成单件代理对象
```shell script
private synchronized Object getSingletonInstance() {
		if (this.singletonInstance == null) {
			this.targetSource = freshTargetSource();
			if (this.autodetectInterfaces && getProxiedInterfaces().length == 0 && !isProxyTargetClass()) {
				//根据AOP框架来判断需要代理的接口
                Class<?> targetClass = getTargetClass();
				if (targetClass == null) {
					throw new FactoryBeanNotInitializedException("Cannot determine target class for proxy");
				}
				setInterfaces(ClassUtils.getAllInterfacesForClass(targetClass, this.proxyClassLoader));
			}
			super.setFrozen(this.freezeProxy);
			this.singletonInstance = getProxy(createAopProxy());
		}
		return this.singletonInstance;
	}
```

AopProxyFactory来创建AopProxy
```shell script
public interface AopProxyFactory {
	AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException;
}
```
默认实现类是DefaultAopProxyFactory
```shell script
public class DefaultAopProxyFactory implements AopProxyFactory, Serializable {

	@Override
	public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
		if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
			Class<?> targetClass = config.getTargetClass();
			if (targetClass == null) {
				throw new AopConfigException("TargetSource cannot determine target class: " +
						"Either an interface or a target is required for proxy creation.");
			}
			if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
                //使用的是JDK动态代理技术
}				return new JdkDynamicAopProxy(config);
			}
			return new ObjenesisCglibAopProxy(config);
		}
		else {
			return new JdkDynamicAopProxy(config);
		}
	}
}
```
## 下面我们还是来一个简单的使用例子
1.引入相关maven依赖
```shell script
       <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
        </dependency>
```
2.在appcontext-aop.xml文件中配置
```shell script
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                        http://www.springframework.org/schema/beans/spring-beans-4.2.xsd
                         http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
                         http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
                         http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.3.xsd">
    <context:component-scan base-package="com.java.tech"/>

    <!--开启aop注解-->
    <aop:aspectj-autoproxy proxy-target-class="true"/>
</beans>
```

3.定义一个注解
```shell script
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyLog {
    String value() default "";
}
```

4.在实现类加注解
```shell script
@Service
public class EchoServiceImpl implements EchoService {
    @MyLog(value = "info")
    @Override
    public String echo(String name) {
        return "Hello " + name;
    }
}
```

不加注解的实现类
```shell script
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String message) {
        return "nihao " + message;
    }
}
```

5.定义一个切面
```shell script
@Aspect
@Component
public class LogAspect {

    @Pointcut("@annotation(com.java.tech.aop.MyLog)")
    public void annotationPointCut() {
        System.out.println("annotationPointCut");
    }

    @After("annotationPointCut()")
    public void after(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method =  methodSignature.getMethod();
        MyLog myLog = method.getAnnotation(MyLog.class);
        System.out.println("注解日志等级:" + myLog.value());
    }

    @Before("execution(* com.java.tech.impl..*.*(..))")
    public void before(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        System.out.println("方法名称:"+ method.getName());
    }
}
```
6.编写测试类
```shell script
public class AopTest extends AbstractSpringContextTest {

    @Autowired
    private HelloService helloService;

    @Autowired
    private EchoService echoService;

    @Test
    public void testAop() {
//        String message = helloService.sayHello("Jason");
//        System.out.println(message);
          String result = echoService.echo("Peter");
          System.out.println(result);
    }
}
```

AOP入口代码AopNamespaceHandler来进行解析
```shell script
public class AopNamespaceHandler extends NamespaceHandlerSupport {
	@Override
	public void init() {
		registerBeanDefinitionParser("config", new ConfigBeanDefinitionParser());
        //在appcontext-aop.xml中配置的
		registerBeanDefinitionParser("aspectj-autoproxy", new AspectJAutoProxyBeanDefinitionParser());
		registerBeanDefinitionDecorator("scoped-proxy", new ScopedProxyBeanDefinitionDecorator());
		registerBeanDefinitionParser("spring-configured", new SpringConfiguredBeanDefinitionParser());
	}

}
```
接下来看一下解析的代码
```shell script
class AspectJAutoProxyBeanDefinitionParser implements BeanDefinitionParser {

	@Override
	@Nullable
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		AopNamespaceUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(parserContext, element);
		extendBeanDefinition(element, parserContext);
		return null;
	}
}
```
# AOP使用场景
## 需求2:方法调用日志
假设我们有这样一个需求:
- 记录某个方法调用需要有日志,记录调用的参数和结果
- 当方法调用抛出异常时,有特殊处理,比如打印异常日志和报警

代码部分参考项目spring/aop文件夹

## 需求3:服务监控
- 为服务中的每个方法调用进行调用耗时记录.
- 将方法调用的时间戳, 方法名, 调用耗时上报到监控平台

代码部分参考项目spring/aop文件夹

## AOP表达的使用
1.execution用于匹配方法执行的连接点,使用例子如下:
```shell script
   @Before("execution(* com.java.tech.impl..*.*(..))")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        System.out.println("方法名称:" + method.getName());
    }
```
下面详细解释一下参数:
execution(<修饰符模式>?<返回类型模式><方法名模式>(<参数模式>)<异常模式>?)
- execution(),表达式的主体
- 第一个* 表示返回值类型任意
- com.java.tech.impl, aop所切的服务的包名,就是我们的业务部分
- 包名后面的..,表示当前包及子包
- 第二个*,表示类名,*即所有类
- .*(..),表示任何方法名,括号表示参数，两个点表示任何参数类型


2.用于匹配指定类型的方法执行,within的使用
匹配指定包中所有方法,但不包括子包
```shell script
within(com.java.tech.service.*)
```
匹配指定包中所有方法,但包括子包
```shell script
within(com.java.tech.service..*)
```
匹配当前包中的指定类中的方法
```shell script
within(logService)
```

## Spring-JDBC
在JDBC中, JdbcTemplate是一个主要的模版类,从继承关系可以看出JdbcTemplate继承了基类JdbcAccessor和
接口类JdbcOperation

- 在基类JdbcAccessor的设计中,对DataSource数据源进行管理和配置
- 在JdbcOperations接口中定义了通过JDBC操作数据库的基本操作方法,比如execute,query,update

```java
public class JdbcTemplate extends JdbcAccessor implements JdbcOperations {
    
}
```

下面我们来一个例子介绍一下JdbcTemplate如何使用
1.引入相关jar
```shell script
  <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
    </dependencies>
```
2.在appcontext-dal.xml中配置jdbcTemplate
```shell script
    <!--配置数据源-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="${jdbc.driverName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>
```
3.定义一个接口
```shell script
public interface UserService {

    User queryUser(String sql);
}
```

4.定义实现类
```shell script
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate userTemplate;

    @Override
    public User queryUser(String sql) {
        return userTemplate.queryForObject(sql,new UserRowMapper());
    }
}
```

5.编写测试类
```shell script
public class UserServiceTest extends AbstractSpringContextTest {

    @Autowired
    private UserService userService;

    @Test
    public void testQueryUser() {
        String sql = "select * from cu_user where id=1";
        User user = userService.queryUser(sql);
        System.out.println(user);
    }
}
```
更多的详细代码请参考项目spring/jdbc文件夹

## Spring事务处理
> Spring可以支持编程式事务和声明式事务
- 编程式事务:通过编写代码实现事务管理,包括定义事务的开始,程序正常执行后的事务提交,异常时进行的事务回滚,缺点是对业务代码有入侵
- 基于AOP技术实现的声明式事务管理 @Transactional, 缺点是只能作用到方法级别,无法做到控制代码块级别

## 编程式事务管理
编程式事务管理接口PlatformTransactionManager实现如下:
```shell script
public interface PlatformTransactionManager extends TransactionManager {

	TransactionStatus getTransaction(@Nullable TransactionDefinition definition)
			throws TransactionException;
	void commit(TransactionStatus status) throws TransactionException;
	void rollback(TransactionStatus status) throws TransactionException;
}
```
它的实现类是DataSourceTransactionManager

如何配置:
```shell script
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
```

## 声明式事务管理
> 在编程的事务管理中,需要手动的去修改业务层代码,对代码的侵入性比较高,声明事务管理是基于AOP技术实现,主要思想是将事务管理作为切面代码
单独编写,只关心核心业务逻辑代码

```shell script
 <!--声明事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--配置事务模版 transactionTemplate-->
    <bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
        <property name="transactionManager" ref="transactionManager"/>
    </bean>

    <!--开启注解驱动的事务管理-->
    <tx:annotation-driven transaction-manager="transactionManager"/>
```
哪个类需要使用事务管理,就在那个类中加@Transactional注解即可

## 什么是事务
> 事务就是把一系列的动作当成一个独立的工作单元,这些动作要么全部完成,要么全部不起作用,就是把一系列的操作当成原子性去执行

## 事务的四大特性ACID:
- A(原子性)
- C(一致性)
- I(隔离性)
- D(持久性)

## 事务的传播行为和隔离级别
### 事务的传播行为:
- REQUIRED:如果有事务在运行,当前的方法就在这个事务内运行,否则就开启一个新的事务,并在自己的事务内运行,是默认的传播行为
- REQUIRED_NEW:当前方法必须启动新事务,并在自己的事务内运行,如果有事务正在运行,则将它挂起
- SUPPORTS:如果有事务在运行,当前的方法就在这个事务内运行
- NOT_SUPPORTS:表示该方法不应该运行在事务中,如果存在当前事务,在该方法运行期间,当前事务被挂起
- MANDATORY:当前的方法必须运行在事务内部,如果没有正在运行的事务,就会抛出异常
- NEVER:当前方法不应该运行在事务中,如果有运行的事务就抛出异常
- NESTED:如果有事务在运行,当前的方法就应该在这个事务的嵌套事务内运行

### 事务的隔离级别:
- READ_UNCOMMITED:允许事务读取未被其他事务提交的更改,脏读 不可重复读 幻读都可能出现
- READ_COMMITED:只允许事务读取已经被其他事务提交的更改,可以避免脏读,但不可重复读和幻读问题仍然出现
- REPEATABLE_READ:可重复读
- SERIALIZABLE:序列化 是事务隔离级别最高,但是性能比较低


spring是通过TransactionTemplate事务模版来实现事务

**说明由于代码比较多 部分代码省略更多详细请参考项目**
1.配置transactionTemplate
```shell script
<!--声明事务管理-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--配置transactionTemplate-->
    <bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
        <property name="transactionManager" ref="transactionManager"/>
    </bean>
```

2.定义接口
```shell script
public interface UserService {

    User queryUser(String sql);

    List<User> queryList(String param);
}
```

3.实现接口
```shell script
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate userTemplate;

    @Override
    public User queryUser(String sql) {
        return userTemplate.queryForObject(sql, new UserRowMapper());
    }

    @Override
    public List<User> queryList(String param) {
        return userTemplate.query(param, new UserRowMapper());
    }
}
```

4.编写测试代码
```shell script
public class TransactionTemplateServiceTest extends AbstractSpringContextTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void testTransaction() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                String sql = "select * from cu_user where id=1";
                userService.queryUser(sql);
            }
        });

        transactionTemplate.execute(new TransactionCallback<List<User>>() {

            @Override
            public List<User> doInTransaction(TransactionStatus status) {
                String sql = "select * from cu_user";
                return userService.queryList(sql);
            }
        });
    }
}
```

## Spring事务处理案例介绍
> 背景介绍:我们主要是做一个简单的转账业务

**特别说明一下 这块代码考虑到实际项目当中的设计 部分代码就没有贴了 详细代码可去项目当中查看**

1.先设计一个简单的转账模型类
```shell script
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseModel {

    private String sender;
    private String receiver;
    private Double amount;
}
```

2.定义一个接口类
```shell script
public interface AccountService {

    AccountResponse transfer(AccountRequest request);
}
```

3.定义一个实现类
```shell script
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public AccountResponse transfer(AccountRequest account) {
        return ServiceTemplate.execute(account, new ServiceCallBack<AccountRequest, AccountResponse>() {
            @Override
            public void checkParameter(AccountRequest request) {
                System.out.println("开始进入账户校验");
            }

            @Override
            public AccountResponse process(AccountRequest request) {
                System.out.println("开始进入转账流程");

                //转出
                accountDao.cashOut(account.getAccount().getSender(), account.getAccount().getAmount());
                
                //故意制造异常,会导致金额已经转出 但是没有转入到收款方
                int i = 1/0;
               
                //转入
                accountDao.cashIn(account.getAccount().getReceiver(), account.getAccount().getAmount());

                //如果返回true代表成功
                return AccountResponse.builder().success(true).build();
            }

            @Override
            public AccountResponse fillFailedResult(AccountRequest request, String message) {
                //转账失败的业务处理 回滚操作
                System.out.println("转账失败 进入回滚,异常信息为:" + message);
                return null;
            }

            @Override
            public void afterProcess() {

            }
        });

    }
}
```

4.编写测试类
```shell script
public class AccountServiceTest extends AbstractSpringContextTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testTransfer(){
        //转出方 张三转账500给李四
        Account transfer = Account.builder().sender("张三").receiver("李四").amount(500.00).build();
        //构建转账请求
        AccountRequest transferRequest = AccountRequest.builder().account(transfer).build();

        AccountResponse accountResponse = accountService.transfer(transferRequest);
        System.out.println(accountResponse);
    }
}
```
**当执行完成以后会发现表中的数据张三少了500块钱,但是李四并没有增加500元,这里是因为没有引入事务管理机制**

## 1.引入编程式事务管理
1.在appcontext-dal.xml中配置
```shell script
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                        http://www.springframework.org/schema/beans/spring-beans-4.2.xsd
                         http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
                         http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
                         http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.3.xsd">

    <context:property-placeholder location="classpath*:db.properties"/>

    <!--配置数据源-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="${jdbc.driverName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--配置事务模版 transactionTemplate-->
    <bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
        <property name="transactionManager" ref="transactionManager"/>
    </bean>

    <!--开启注解驱动的事务管理-->
    <tx:annotation-driven transaction-manager="transactionManager"/>
</beans>
```

2.需要修改业务代码,需要使用transactionTemplate
```shell script
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public AccountResponse transfer(AccountRequest account) {
        return ServiceTemplate.execute(account, new ServiceCallBack<AccountRequest, AccountResponse>() {
            @Override
            public void checkParameter(AccountRequest request) {
                System.out.println("开始进入账户校验");
            }

            @Override
            public AccountResponse process(AccountRequest request) {
                System.out.println("开始进入转账流程");

                //将转账操作放入到transactionTemplate中执行
                transactionTemplate.execute(new TransactionCallback<AccountResponse>() {
                    @Override
                    public AccountResponse doInTransaction(TransactionStatus status) {
                        //转出
                        accountDao.cashOut(account.getAccount().getSender(), account.getAccount().getAmount());

                        //故意制造异常,转出成功 转入失败 就会回滚 转出金额不变
                        int i = 1 / 0;
                        //转入
                        accountDao.cashIn(account.getAccount().getReceiver(), account.getAccount().getAmount());
                        return AccountResponse.builder().success(true).build();
                    }
                });
                return null;
              
            }

            @Override
            public AccountResponse fillFailedResult(AccountRequest request, String message) {
                //转账失败的业务处理 回滚操作
                System.out.println("转账失败 进入回滚,异常信息为:" + message);
                return null;
            }

            @Override
            public void afterProcess() {

            }
        });

    }
}
```
3.再次进行单元测试,发现当发生异常的时候,发生了回滚,转出账户和转入账户数据不变


## 2.声明式事务管理
编程式事务管理有一个缺点就是需要修改业务的代码,对业务入侵比较严重,使用非常简单只需要在对应的方法加上@Transactional

