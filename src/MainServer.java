/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Studenti
 */
public class MainServer {
    public static void main(String[] args) {
        Server server = new Server(4200, "\033[35m");
        server.attendi(); //metti il server in ascolto
    }
}
