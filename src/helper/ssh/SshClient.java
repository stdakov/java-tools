package helper.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;

public class SshClient {

    protected Session session;
    protected ChannelShell channel;
    protected String username;
    protected String password;
    protected String hostname;
    protected Integer port;
    protected PrintStream printStream;
    protected boolean debug = false;
    protected String endCommand = "end-command";
    protected int connectionTimeout = 30000;

    public SshClient() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getEndCommand() {
        return endCommand;
    }

    public void setEndCommand(String endCommand) {
        this.endCommand = endCommand;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    protected Session getSession() {
        if (this.session == null || !this.session.isConnected()) {
            this.session = this.openSession();
        }
        return session;
    }

    protected Session openSession() {

        if (this.debug) {
            System.out.println("Opening a session");
        }

        if (this.hostname == null) {
            throw new IllegalArgumentException("Missing hostname!");
        }

        if (this.username == null) {
            throw new IllegalArgumentException("Missing username!");
        }

        if (this.password == null) {
            throw new IllegalArgumentException("Missing password!");
        }


        try {
            Session session;

            JSch jSch = new JSch();

            if (this.port == null) {
                session = jSch.getSession(this.username, this.hostname);
            } else {
                session = jSch.getSession(this.username, this.hostname, this.port);
            }

            session.setPassword(this.password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            if (this.debug) {
                System.out.println("Connecting to " + this.hostname + " - Please wait for few seconds... ");
            }
            session.connect(this.connectionTimeout);
            if (this.debug) {
                System.out.println("Connected!");
            }

            return session;

        } catch (Exception e) {
            throw new IllegalStateException("An error occurred while connecting to " + this.hostname + ": " + e);
        }
    }

    protected Channel getChannel() {

        try {
            if (this.channel == null || !this.channel.isConnected() || this.channel.isClosed()) {
                Session session = this.getSession();
                if (session != null) {

                    if (this.debug) {
                        System.out.println("Open channel");
                    }

                    this.channel = (ChannelShell) session.openChannel("shell");
                    this.channel.connect();
                }
            }

            return this.channel;

        } catch (Exception e) {
            throw new IllegalStateException("Error while opening channel: " + e);
        }
    }


    public String execute(List<String> commands) {
        String output = null;

        try {
            Channel channel = this.getChannel();
            this.sendCommands(channel, commands);
            output = this.readChannelOutput(channel);

        } catch (Exception e) {
            System.out.println("An error ocurred during executeCommands: " + e);
            e.printStackTrace();
        }

        return output;
    }

    protected void sendCommands(Channel channel, List<String> commands) {

        if (this.debug) {
            System.out.println("Sending commands...");
        }

        try {
            if (this.printStream == null) {
                this.printStream = new PrintStream(channel.getOutputStream());
            }
            for (String command : commands) {
                if (this.debug) {
                    System.out.println("Sending command: " + command);
                }
                this.printStream.println(command);
                Thread.sleep(500);
            }
            this.printStream.flush();
        } catch (Exception e) {
            throw new IllegalStateException("Error while sending commands: " + e);
        }

        if (this.debug) {
            System.out.println("Finished sending commands!");
        }
    }

    protected String readChannelOutput(Channel channel) throws InterruptedException {

        if (this.debug) {
            System.out.println("Start reading output channel!");
        }

        if (this.endCommand == null) {
            throw new IllegalArgumentException("Missing ending command");
        }

        byte[] buffer = new byte[1024];
        StringBuilder strBuilder = new StringBuilder();

        try {
            InputStream in = channel.getInputStream();
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(buffer, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    strBuilder.append(new String(buffer, 0, i));
                }

                if (strBuilder.toString().contains(this.endCommand)) {
                    break;
                }

                if (channel.isClosed()) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error while reading channel output: " + e);
        }

        return strBuilder.toString();
    }

    public void close() {
        if (this.printStream != null) {
            this.printStream.close();
            this.printStream = null;
        }

        if (this.channel != null) {
            this.channel.disconnect();
        }

        if (this.session != null) {
            this.session.disconnect();
        }

        if (this.debug) {
            System.out.println("Disconnected channel and session");
        }
    }

}
