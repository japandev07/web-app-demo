package com.webfirmframework.web.launcher;

import com.webfirmframework.wffwebconfig.server.constants.ServerConstants;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.scan.Constants;
import org.apache.tomcat.util.scan.StandardJarScanFilter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

/**
 * Use this main if you want to configure embedded tomcat or some other embedded server.
 * For UI development write code inside wffwebui module.
 */
public class Main {

    private static File getRootFolder() {

        try {
            File root;
            String runningJarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll("\\\\", "/");
            int lastIndexOf = runningJarPath.lastIndexOf("/target/");
            if (lastIndexOf < 0) {
                root = new File("");
            } else {
                root = new File(runningJarPath.substring(0, lastIndexOf));
            }
            //System.out.println("application resolved root folder: " + root.getAbsolutePath());
            return root;
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) throws LifecycleException, IOException {

        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = ServerConstants.LOCAL_MACHINE_PORT;

        int port = Integer.parseInt(webPort);
        final Properties systemProperties = System.getProperties();

        for (Map.Entry<Object, Object> entry : systemProperties.entrySet()) {
            System.out.println("property: " + entry.getKey() + ", value: " + entry.getValue());
        }

        File root = getRootFolder();
        System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");

        Tomcat tomcat = new Tomcat();
        tomcat.getServer().setPort(-1);

        Connector connector = tomcat.getConnector();
        connector.setProperty("protocol", "org.apache.coyote.http11.Http11Nio2Protocol");
        connector.setProperty("useAsyncIO", "true");
        connector.setProperty("maxThreads", "8");
        connector.setPort(port);

        Path tempPath = Files.createTempDirectory("tomcat-base-dir");
        tomcat.setBaseDir(tempPath.toString());

        tomcat.setPort(port);
        //NB Trigger the creation of the default connector
        //In the latest version of tomcat 9 it doesn't automatically create it
        //this configuration should NOT be just before tomcat.start


        File webContentFolder = new File(root.getAbsolutePath(), "src/main/webapp/");
        if (!webContentFolder.exists()) {
            webContentFolder = Files.createTempDirectory("default-doc-base").toFile();
        }
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", webContentFolder.getAbsolutePath());
        //Set execution independent of current thread context classloader (compatibility with exec:java mojo)
        ctx.setParentClassLoader(Main.class.getClassLoader());

        File configFile = new File(root.getAbsolutePath(), "src/main/webapp/META-INF/context.xml");
        ctx.setConfigFile(configFile.toURI().toURL());

        //Disable TLD scanning by default
        if (System.getProperty(Constants.SKIP_JARS_PROPERTY) == null) {
            System.out.println("disabling TLD scanning");
            StandardJarScanFilter jarScanFilter = (StandardJarScanFilter) ctx.getJarScanner().getJarScanFilter();
            jarScanFilter.setTldSkip("*");
        }

        System.out.println("configuring app with basedir: " + webContentFolder.getAbsolutePath());

        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        File additionWebInfClassesFolder = new File(root.getAbsolutePath(), "target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);

        WebResourceSet resourceSet;
        if (additionWebInfClassesFolder.exists()) {
            resourceSet = new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClassesFolder.getAbsolutePath(), "/");
            System.out.println("loading WEB-INF resources from as '" + additionWebInfClassesFolder.getAbsolutePath() + "'");
        } else {
            resourceSet = new EmptyResourceSet(resources);
        }
        resources.addPreResources(resourceSet);
        ctx.setResources(resources);

        ctx.setDisplayName("wffweb demo");
        ctx.addWelcomeFile("ui");
        if (!ServerConstants.MULTI_NODE_MODE) {
            ctx.setSessionTimeout(ServerConstants.SESSION_TIMEOUT);
        }

        tomcat.start();

        tomcat.getServer().await();
    }
}
