package com.stdakov.tools;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SshClientTest {
    @Test
    public void testSsh() {
        SshClientOld sshClient = new SshClientOld();
        sshClient.setDebug(true);
        sshClient.setHostname("127.0.0.1");
        sshClient.setConnectionTimeout(2000);
        sshClient.setPort(1973);
        sshClient.setUsername("admin");
        sshClient.setPassword("password");
        sshClient.setEndCommand("end-command");
        sshClient.setCommandDelay(2000);

        List<String> commands = new ArrayList<>();
        commands.add("ls -l");
        commands.add("end-command");

        try {
            sshClient.execute(commands);
            System.out.println(sshClient.readOutput());
        } catch (IllegalStateException | InterruptedException e) {
            e.printStackTrace();
        }

        sshClient.close();
    }
}
