package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Client started!");
        String address = "127.0.0.1";
        int port = 23456;
        try (
                Socket socket = new Socket(InetAddress.getByName(address), port);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {


            System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): > ");
            String action = scanner.nextLine();

            switch (action) {
                case "1":
                    System.out.print("Enter filename: > ");
                    String filename = scanner.nextLine();
                    output.writeUTF("get " + filename);
                    System.out.println("The request was sent.");
                    if (input.readInt() == 200) {
                        System.out.println("The content of the file is: " + input.readUTF());
                    } else {
                        System.out.println("The response says that the file was not found!");
                    }
                    break;

                case "2":
                    System.out.print("Enter filename: > ");
                    filename = scanner.nextLine();
                    output.writeUTF("add " + filename);
                    if (input.readInt() == 403) {
                        System.out.println("The response says that creating the file was forbidden!");
                    } else {
                        System.out.println("Enter file content:");
                        String content = scanner.nextLine();
                        output.writeUTF(content);
                        System.out.println("The request was sent.");
                        System.out.println("The response says that file was created!");
                    }
                    break;

                case "3":
                    System.out.print("Enter filename: > ");
                    filename = scanner.nextLine();
                    output.writeUTF("delete " + filename);
                    System.out.println("The request was sent.");
                    if (input.readInt() == 200) {
                        System.out.println("The response says that the file was successfully deleted!");
                    } else {
                        System.out.println("The response says that the file was not found!");
                    }
                    break;

                case "exit":
                    output.writeUTF("exit program");
                    System.out.println("The request was sent.");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}