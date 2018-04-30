package com.stdakov.tools;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.session.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SshService {

    private static final Logger logger = LoggerFactory.getLogger(SshService.class);

    private SshClient client;
    private ClientSession session;
    private String username;
    private String password;
    private String hostname;
    private int port;

    public SshService(String hostname, String username, String password) {
        this(hostname, username, password, 22);
    }

    public SshService(String hostname, String username, String password, int port) {
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public void open() throws IOException {
        this.client = SshClient.setUpDefaultClient();
        this.client.start();

        ConnectFuture connectFuture = this.client.connect(this.username, this.hostname, this.port);

        boolean isConnected = connectFuture.await();
        if (!isConnected) {
            throw new IllegalStateException("InterruptedException for connect await");
        }

        this.session = connectFuture.getSession();

        this.session.addPasswordIdentity(this.password);
        this.session.auth().verify();
    }


    public String exec(String cmd) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ClientChannel channel = null;
        try {
            channel = this.session.createChannel(ClientChannel.CHANNEL_EXEC, cmd);
            channel.setIn(new ByteArrayInputStream(baos.toByteArray()));
            channel.setOut(out);
            channel.setErr(err);
            channel.open();
            List<ClientChannelEvent> clientChannelEvents = new ArrayList<>();
            //clientChannelEvents.add(ClientChannelEvent.CLOSED);
            clientChannelEvents.add(ClientChannelEvent.EOF);
            channel.waitFor(clientChannelEvents, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = out.toString().trim();

        if (channel != null) {
            channel.close(false);
        }

        return result;
    }

    public String shell(List<String> commands) {

        PipedOutputStream pipedIn = new PipedOutputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        ClientChannel channel = null;
        try {
            channel = this.session.createChannel(ClientChannel.CHANNEL_SHELL);
            channel.setIn(new PipedInputStream(pipedIn));
            channel.setOut(out);
            channel.setErr(err);
            channel.open();
            List<ClientChannelEvent> clientChannelEvents = new ArrayList<>();
            clientChannelEvents.add(ClientChannelEvent.TIMEOUT);
            channel.waitFor(clientChannelEvents, 2L * 5000L);
        } catch (IOException e) {
            e.printStackTrace();
        }

        commands.forEach(command -> {
            command += "\r\n";
            try {
                pipedIn.write(command.getBytes());
                pipedIn.flush();
                Thread.sleep(500);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });


        String result = out.toString().trim();

        if (channel != null) {
            channel.close(false);
        }

        return result;
    }

    public void close() {
        if (this.session != null) {
            session.close(false);
        }

        if (this.client != null) {
            client.stop();
        }
    }
}
