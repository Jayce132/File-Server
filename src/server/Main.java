package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        System.out.println("Server started!");
        String address = "127.0.0.1";
        int port = 23456;
        Path dataLocation = Path.of(
                System.getProperty("user.dir"),
                "src", "server", "data"
        );


        boolean isRun = true;
        while (isRun) {
            try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
                 Socket socket = server.accept();
                 DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                String[] commandAndName = input
                        .readUTF()
                        .split(" ");

                String command = commandAndName[0];
                String name = "name";

                if (commandAndName.length == 2) {
                    name = commandAndName[1];
                }

                switch (command) {
                    case "add":
                        File file = new File(dataLocation + File.separator + name);
                        if (file.exists()) {
                            output.writeInt(403);
                        } else {
                            output.writeInt(200);
                            try (FileWriter fileWriter = new FileWriter(file);
                                 BufferedWriter writer = new BufferedWriter(fileWriter)) {
                                String content = input.readUTF();
                                writer.write(content);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                    case "get":
                        file = new File(dataLocation + File.separator + name);

                        if (file.exists()) {
                            try (FileReader fileReader = new FileReader(file);
                                 BufferedReader reader = new BufferedReader(fileReader)) {
                                output.writeInt(200);
                                output.writeUTF(reader.readLine());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            output.writeInt(404);
                        }
                        break;

                    case "delete":
                        file = new File(dataLocation + File.separator + name);

                        if (file.delete()) {
                            output.writeInt(200);
                        } else {
                            output.writeInt(404);
                        }
                        break;

                    case "exit":
                        isRun = false;
                        return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}