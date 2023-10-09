package com.github.kangmoo.utils.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * @author kangmoo Heo
 */
public class SSHExecutor {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final int DEFAULT_PORT = 22;
    private static final Consumer<String> DEFAULT_CALLBACK = o -> {};

    public Future<Void> executeCommand(String user, String host, String password, String command) {
        return executeCommand(user, host, DEFAULT_PORT, password, command, DEFAULT_CALLBACK);
    }

    public Future<Void> executeCommand(String user, String host, int port, String password, String command) {
        return executeCommand(user, host, port, password, command, DEFAULT_CALLBACK);
    }

    public Future<Void> executeCommand(String user, String host, String password, String command, Consumer<String> outputCallBack) {
        return executeCommand(user, host, DEFAULT_PORT, password, command, outputCallBack);
    }

    public Future<Void> executeCommand(String user, String host, int port, String password, String command, Consumer<String> outputCallBack) {
        return executorService.submit(() -> {
            Session session = null;
            ChannelExec channel = null;

            try {
                JSch jsch = new JSch();
                session = jsch.getSession(user, host, port);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(command);

                InputStream in = channel.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                channel.connect();

                String line;
                while ((line = reader.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                    outputCallBack.accept(line);
                }
            } catch (Exception e) {
                throw new RuntimeException("SSH command execution failed", e);
            } finally {
                if (channel != null) {
                    channel.disconnect();
                }
                if (session != null) {
                    session.disconnect();
                }
            }
            return null;
        });
    }

    public void shutdown() {
        executorService.shutdownNow();
    }
}
