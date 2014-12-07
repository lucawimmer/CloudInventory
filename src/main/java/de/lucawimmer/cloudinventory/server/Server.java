package de.lucawimmer.cloudinventory.server;

import de.lucawimmer.cloudinventory.CloudInventory;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    static Socket server = null;
    static ServerSocket ssocket = null;
    InetAddress ip;
    Integer port;

    public Server(InetAddress IP, Integer PORT) {
        this.ip = IP;
        this.port = PORT;
    }

    public void start() {
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

        Runnable serverTask = new Runnable() {

            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    serverSocket.setReuseAddress(true);
                    ssocket = serverSocket;
                    CloudInventory.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[CloudInventory] Server socket has been started!");
                    CloudInventory.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[CloudInventory] Waiting for players to connect...");
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        server = clientSocket;
                        clientProcessingPool.submit(new ClientProcessing(clientSocket));
                    }
                } catch (IOException e) {
                    if (server.isConnected()) {
                        CloudInventory.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[CloudInventory] Unable to process client request");
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
    }

    public void stop() throws IOException {
        ssocket.close();
    }

}
