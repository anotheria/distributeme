package org.distributeme.tools.agents;

import org.distributeme.agents.AgencyImpl;
import org.distributeme.core.Location;
import org.distributeme.core.RegistryUtil;
import org.distributeme.core.ServiceDescriptor;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendAgent {
    public static void main(String[] args) throws RemoteException, Exception {
        System.out.println("Hello, SendAgent!");
        String DEFAULT_HOST = "10.156.0.11";
        int DEFAULT_PORT = 9229;
        final String host;
        final int port;
        if (args.length != 2){
            System.out.println("Use java ... "+SendAgent.class+" host port");
            System.out.println("will assume defaults, for tests sake "+DEFAULT_HOST+":"+DEFAULT_PORT);
            host = DEFAULT_HOST;
            port = DEFAULT_PORT;
        }else {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }
        System.out.println("Listing of services  @ \"+host+\":\"+port");
        Location registryLocation = new Location() {
            @Override
            public String getHost() {
                return host;
            }

            @Override
            public int getPort() {
                return port;
            }

            @Override
            public String getProtocol() {
                return "http";
            }

            @Override
            public String getContext() {
                return "distributeme";
            }
        };

        List<ServiceDescriptor> services = RegistryUtil.getServicesRegisteredInRegistry(registryLocation);
        System.out.println("Read services: "+ services);
        for (ServiceDescriptor sd : services){
            System.out.println("Sending agent to "+sd);
            AgencyImpl.INSTANCE.sendAgent(new TestAgent(), sd);
        }


    }

}
