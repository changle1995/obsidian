# 确认是否为付费插件
如 Restful Fast Request 插件这种
![[Pasted image 20231206094434.png]]
![[Pasted image 20231206094517.png]]
# 原理介绍
JetBrains Idea 插件需要满足以下规则：
1. 在 JetBrains Idea 开发插件需要使用到 product-descriptor 这个属性，才能在注册页面看到对应的插件激活选项
2. 插件代码中一般会需要调用 JetBrains 的上下文环境获取 license 信息判断是否已激活，之后走 if 条件判断是弹窗提示激活还是正常执行逻辑
所以基于以上基础，破坏这两个条件即可破解 JetBrains Idea 的插件。
# 操作步骤
1. 在插件市场或者离线方式安装对应的插件：![[Pasted image 20231206101351.png]]
2. 安装后找到对应的插件的执行 jar 包，如：`C:\Users\admin\AppData\Roaming\JetBrains\IntelliJIdea2023.2\plugins\Restful Fast Request - API Buddy\lib\instrumented-restful-fast-request-pro-2023.2.2.4.jar`
3. 将 jar 包找个临时目录先复制过去并切换到对应目录，如复制到：`C:\Users\admin\Downloads`
4. 替换执行 jar 包中的 `META-INF\plugin.xml` 文件（product-descriptor 属性就是在这个文件中配置，所以这里是打破原理中的第 1 点）
	1. 将 `META-INF\plugin.xml` 文件提取出来，执行代码：`jar -xvf instrumented-restful-fast-request-pro-2023.2.2.4.jar META-INF\plugin.xml`
	2. 试用编辑器打开提取出来的 `META-INF\plugin.xml` 文件，搜索 `product-descriptor`，将对应行直接删除 ![[Pasted image 20231206094540.png]]
	3. 用修改后的 `META-INF\plugin.xml` 文件替换原 jar 包中的 `META-INF\plugin.xml` 文件，执行代码：`jar -uvf instrumented-restful-fast-request-pro-2023.2.2.4.jar META-INF\plugin.xml`
5. 修改执行 jar 包中的对应的 license 判断代码，这里比较麻烦，需要找到对应的 class 文件，各个插件文件不同，但是大致原理相同，详细看如下具体步骤（这里是打破原理中的第 2 点）
	1. 创建存放反编译文件的目录，不创建目录执行下面的代码会报错，如：instrumented-restful-fast-request-pro
	2. 使用 `java-decompiler.jar` 对 `instrumented-restful-fast-request-pro-2023.2.2.4.jar` 进行反编译便于搜索代码，执行代码：`java -jar java-decompiler.jar -log=warn instrumented-restful-fast-request-pro-2023.2.2.4.jar instrumented-restful-fast-request-pro`
	3. 打开反编译代码目录，看到反编译之后的 jar 包，这个时候的 jar 包中的代码已经是 java 类型文件了，直接使用解压软件解压这个 jar 包，复制代码文件目录到 IDE
	4. 搜索关键字 `LicensingFacade.getInstance()` 找到对应的获取license权限代码，记住对应的类文件和方法名，大致看一下代码逻辑和调用关系判断返回 true 或 false 哪个表示已激活，比如这里返回 false 表示已激活![[Pasted image 20231206094735.png]]![[Pasted image 20231206094844.png]]
	6. 使用 javassist 操作字节码替换原 class 文件中对应方法的判断逻辑
		1. 新建一个maven项目
		2. 引入 javassist 依赖
		3. 新建一个 Test 类，将以下代码复制进去，根据自己的情况适当修改后执行：![[JetBrains/assets/Test.java]]
		4. 执行后找到对应生成的 class 文件：![[Pasted image 20231206100239.png]]
		5. 将原 class 文件提取出来，执行代码：`jar -xvf instrumented-restful-fast-request-pro-2023.2.2.4.jar io\github\kings1990\plugin\fastrequest\cofig\llll1IIIll1II11I`![[Pasted image 20231206101000.png]]
		6. 用生成的文件替换提取出来的 class 文件
		7. 将替换后的 class 文件打包到 jar 包中替换原 class 文件，执行代码：`jar -uvf instrumented-restful-fast-request-pro-2023.2.2.4.jar io\github\kings1990\plugin\fastrequest\cofig\llll1IIIll1II11I`
	7. 将原 jar 包改名备份，并用修改后的 jar 包替换原 jar 包：![[Pasted image 20231206101234.png]]
	8. 重启 Idea 可以看到对应的插件已经解锁并可正常使用了：![[Pasted image 20231206101531.png]]