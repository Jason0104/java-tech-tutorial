# Spring源码深入分析

## 主要内容介绍
* [Spring介绍](#spring)
* [IOC容器的实现](#spring-ioc)
* [AOP的实现](#spring-aop)
* [Spring-JDBC](#spring-jdbc)
* [Spring事务处理](#spring-transManager)
* [Spring远端调用的实现](#spring-remote)

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
真正读取在xml配置的bean是通过org.springframework.beans.factory.xml.XmlBeanDefinitionReader来实现的

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

### AOP使用场景
- 权限
- 缓存
- 日志
- 错误处理
- 事务

#### AOP的设计与实现
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

AOP入口代码AopNamespaceHandler
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

## Spring-JDBC


## Spring事务处理


## Spring远程调用的实现



