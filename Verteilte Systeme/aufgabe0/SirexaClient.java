import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SirexaClient {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java SirexaClient <hostname> <port>");
            return;
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(hostname, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            // Anmeldung
            String username = "User123"; // Frei wählbar
            String password = readPasswordFromFile("credentials.txt");
            
            out.writeUTF(username);
            out.writeUTF(password);
            
            System.out.println("Server: " + in.readUTF());
            
            // Chat-Funktionalität
            while (true) {
                System.out.print("Client: ");
                String message = scanner.nextLine();
                
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                
                out.writeUTF(message);
                System.out.println("Server: " + in.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readPasswordFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return reader.readLine();
        }
    }
}
