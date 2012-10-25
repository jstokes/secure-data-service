/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.slc.sli.search.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slc.sli.search.process.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RemoteCommandService implements ApplicationContextAware, Runnable {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final int DEFAULT_PORT = 10024;
    private int port = DEFAULT_PORT;

    private boolean stopRemoteCommandService;

    // server socket
    private ServerSocket serverSocket;

    // because we just want to shut down application
    private ClassPathXmlApplicationContext context;

    // Extractor object to run as batch program
    private Admin admin;

    // thread executor
    private static final ScheduledExecutorService scheduledService = Executors.newSingleThreadScheduledExecutor();

    /**
     * initial method run by Spring init-method
     * 
     * @throws IOException
     */
    public void init() throws IOException {
        this.serverSocket = new ServerSocket(port);
        // this.serverSocket.setSoTimeout(socketTimeout);

        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void destroy() throws Exception {
        scheduledService.shutdownNow();
        closeSocket();
    }

    private void closeSocket() throws Exception {
        if (this.serverSocket != null) {
            this.serverSocket.close();
            this.serverSocket = null;
        }
    }

    // Thread run
    public void run() {
        // make loop
        while (this.stopRemoteCommandService == false) {
            try {
                listen();
            } catch (SocketException e) {
                // Most likely Socket is closed because of "stop" command
            } catch (Throwable t) {
                logger.error("Error detected in Remote Command Service...", t);
            }
        }

        scheduledService.shutdownNow();
        if (this.stopRemoteCommandService) {
            if (this.context != null) {
                this.context.close();
            }
        }

    }

    // Main function, Listen server socket
    private void listen() throws Throwable {
        Socket socket = null;
        try {
            // Wait for client to connect
            socket = this.serverSocket.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            RemoteCommand command = null;
            // read Input String
            String inputCommand = in.readLine();
            if (inputCommand != null) {
                String[] commandLine = inputCommand.split("\\s+");

                if (commandLine == null || commandLine.length == 0) {
                    command = new RemoteCommand(Commands.HELP);
                } else {
                    for (int index = 0; index < commandLine.length; index++) {
                        // this is the first read token.
                        // it means command
                        if (index == 0) {
                            if (commandLine[0].toLowerCase().equals("extract")) {
                                command = new RemoteCommand(Commands.EXTRACT);
                            } else if (commandLine[0].toLowerCase().equals("stop")) {
                                command = new RemoteCommand(Commands.STOP);
                            } else if (commandLine[0].toLowerCase().equals("reconcile")) {
                                command = new RemoteCommand(Commands.RECONCILE);                   
                            } else if (commandLine[0].toLowerCase().equals("reload")) {
                                command = new RemoteCommand(Commands.RELOAD);                   
                            } else {
                                command = new RemoteCommand(Commands.HELP);
                            }
                        } else {
                            // token is an option for a command
                            command.setOption(commandLine[index]);
                        }
                    }
                }
            }

            // if command is null, it is invalid socket request. just ignore it
            if (command != null) {
                String option = null;
                // execute command
                switch (command.getCommands()) {
                    case RELOAD:
                    case EXTRACT:
                        logger.info("Remote Service received Extract command");
                        option = command.getOption();
                        if ("sync".equals(option)) {
                            this.admin.reloadAll();
                        } else if (option != null) {
                            this.admin.reconcile(option);
                        } else {
                            final Admin admin = this.admin;
                            scheduledService.schedule(new Runnable() {
                                public void run() {
                                    admin.reloadAll();
                                }
                            }, 0, TimeUnit.SECONDS);
                        }
                        command.setReply("sent extract command");
                        break;
                    case RECONCILE:
                        logger.info("Remote Service received Extract command");
                        option = command.getOption();
                        if ("sync".equals(option)) {
                            this.admin.reconcileAll();
                        } else if (option != null) {
                            this.admin.reconcile(option);
                        } else {
                            final Admin admin = this.admin;
                            scheduledService.schedule(new Runnable() {
                                public void run() {
                                    admin.reconcileAll();
                                }
                            }, 0, TimeUnit.SECONDS);
                        }
                        command.setReply("sent extract command");
                        break;
                    case STOP:

                        int delay = 5;
                        try {
                            option = command.getOption();
                            if (option != null && !option.isEmpty()) {
                                delay = Integer.parseInt(option);
                            }
                        } finally {
                            logger.info("Remote Service received Stop command, shutting down in " + delay
                                    + " second(s)");
                            scheduledService.schedule(new Runnable() {
                                public void run() {
                                    commandShutdown();
                                }
                            }, delay, TimeUnit.SECONDS);
                        }
                        command.setReply("Shutting down in " + delay + " second(s)\n");
                        break;
                    default:
                        command.setReply("Available Commands:\n" + "extract (start Extractor job)\n"
                                + "extract sync(start Extract job with synchronization)\n"
                                + "stop (stop search-indexer with 5 seconds delay)\n"
                                + "stop # (stop search-indexer with # second(s) delay\n");
                        break;
                }

                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println(command.getReply());
                out.close();
            }
        } catch (Throwable t) {
            // SocketException is thrown by calling close while socket is blocked by accept.
            // this is expected exception because search-indexer is about shutting down.
            throw t;
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private void commandShutdown() {
        try {
            logger.info("Shutting down search-indexer");
            this.stopRemoteCommandService = true;
            closeSocket();
        } catch (Exception e) {
            // something went wrong.
            logger.error("Exception while shutting down socket", e);
        }
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = (ClassPathXmlApplicationContext) context;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    private class RemoteCommand {
        private final Commands command;
        private String reply;
        private String option;

        public RemoteCommand(Commands command) {
            this.command = command;
        }

        public String getReply() {
            return this.reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public String getOption() {
            return this.option;
        }

        public Commands getCommands() {
            return this.command;
        }
    }

    private enum Commands {
        RELOAD, EXTRACT, RECONCILE, STOP, HELP, HEALTH;
    }

}
