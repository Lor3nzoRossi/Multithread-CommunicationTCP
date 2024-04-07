/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Studenti
 */
public class MainClient {
    public static void main(String[] args) {
        Client client = new Client("user", "\033[34m");
        client.connetti("localhost", 4200);
    }
}
