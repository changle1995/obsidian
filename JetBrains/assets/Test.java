import javassist.*;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException {
        // 插件包环境地址
        String pluginJarPath = "C:\\Users\\admin\\AppData\\Roaming\\JetBrains\\IntelliJIdea2023.2\\plugins\\Restful Fast Request - API Buddy\\lib\\instrumented-restful-fast-request-pro-2023.2.2.4.jar";
        // 插件包中对应需要替换的class文件路径（不需要.class结尾）
        String licenseClassPath = "io.github.kings1990.plugin.fastrequest.cofig.llll1IIIll1II11I";
        // license判断逻辑方法的名称
        String licenseMethodName = "IIl1lIl1111IlIlI1";
        hookMethod(pluginJarPath, licenseClassPath, licenseMethodName);
    }

    public static void hookMethod(String pluginJarPath, String licenseClassPath, String licenseMethodName) throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = ClassPool.getDefault();
        // 设置需要替换的插件包环境
        pool.insertClassPath(pluginJarPath);
        // 设置插件包中对应需要替换的class文件路径（不需要.class结尾）
        CtClass driverclass = pool.get(licenseClassPath);
        // 找到对应的license判断逻辑方法
        CtMethod[] declaredmethods = driverclass.getDeclaredMethods();
        CtMethod hookmethod = null;
        for (CtMethod declaredmethod : declaredmethods) {
            if (declaredmethod.getName().equals(licenseMethodName)) {
                hookmethod = declaredmethod;
                break;
            }
        }
        // 修改方法体，一般直接返回false或者true即可
        if (hookmethod != null) {
            System.out.println(hookmethod.getDeclaringClass());
            hookmethod.setBody("return false;");
        }
        // 将修改后的class文件输出到对应的目录文件
        driverclass.writeFile("writeFile");
    }
}
