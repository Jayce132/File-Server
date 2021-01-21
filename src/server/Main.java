package server;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Server started!");
        String address = "127.0.0.1";
        int port = 23456;
        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
        Socket socket = server.accept();
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        System.out.println("Received: " + input.readUTF());
        output.writeUTF("All files were sent!");
        System.out.println("Sent: All files were sent!");

        Scanner scanner = new Scanner(System.in);
        ArrayList<String> files = new ArrayList<>();
        while (true) {
            String[] commandAndName = scanner
                    .nextLine()
                    .split(" ");
            String command = commandAndName[0];
            String name = "name";
            if (commandAndName.length == 2) {
                name = commandAndName[1];
            }
            switch (command) {
                case "add":
                    if (files.size() <= 10
                            && name.matches("file10|file[1-9]")
                            && !files.contains(name)) {
                        files.add(name);
                        System.out.println("The file " + name + " added successfully");
                    } else {
                        System.out.println("Cannot add the file " + name);
                    }
                    break;
                case "get":
                    if (files.contains(name)) {
                        System.out.println("The file " + name + " was sent");
                    } else {
                        System.out.println("The file " + name + " not found");
                    }
                    break;
                case "delete":
                    if (files.remove(name)) {
                        System.out.println("The file " + name + " was deleted");
                    } else {
                        System.out.println("The file " + name + " not found");
                    }
                    break;
                case "exit":
                    return;
                default:
                    break;
            }
        }
    }
}