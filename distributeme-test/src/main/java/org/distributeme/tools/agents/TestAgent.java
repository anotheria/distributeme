package org.distributeme.tools.agents;

import org.distributeme.agents.Agent;

public class TestAgent implements Agent {
    @Override
    public void prepareForTransport() {
        System.out.println("TestAgent.+"+toString()+" prepareForTransport");
    }

    @Override
    public void awake() {
        System.out.println("TestAgent.+"+toString()+" hello");
    }
}
