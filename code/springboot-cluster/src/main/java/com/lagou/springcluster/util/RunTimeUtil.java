package com.lagou.springcluster.util;


import javax.management.*;
import java.util.ArrayList;
import java.util.Set;

public class RunTimeUtil {

    //获取 tomcat 运行端口
    public static int getTomcatPort(){

        int port = 0 ;

        MBeanServer mBeanServer = null;
        ArrayList<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer(null);
        if (mBeanServers.size() > 0) {

            mBeanServer = mBeanServers.get(0);

        }

        if (mBeanServer == null) {
            throw new IllegalStateException("没有发现JVM中关联的MBeanServer.");
        }

        Set<ObjectName> objectNames = null;
        try {
            objectNames = mBeanServer.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
        }
        catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (objectNames == null || objectNames.size() <= 0) {
            throw new IllegalStateException("没有发现JVM中关联的MBeanServer : "+ mBeanServer.getDefaultDomain() + " 中的对象名称.");
        }

        try {
            for (ObjectName objectName : objectNames) {
                String protocol = (String) mBeanServer.getAttribute(objectName, "protocol");
                if (protocol.equals("HTTP/1.1")) {
                    port = (Integer) mBeanServer.getAttribute(objectName, "port");
                }
            }
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (MBeanException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        }

        return port;

    }


}