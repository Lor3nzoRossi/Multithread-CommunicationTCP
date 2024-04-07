
import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Studenti
 */
public class Client {
    private String nome;
    private String colore;
    Socket socket;
    //per scrivere
    BufferedWriter bw;
    //per leggere
    BufferedReader br;
    

    public Client(String nomeDefault, String coloreDefault) {
        this.nome = nomeDefault;
        this.colore = coloreDefault;
    }
    //metodo per connettersi ad un server
    public void connetti(String nomeServer, int portaServer) {
        try {
            this.socket = new Socket(nomeServer, portaServer);
            System.out.println(this.colore+"Il client si è connesso al server " + nomeServer + " sulla porta " + portaServer);
            //definisco i due stream per scrivere e leggere dal server
            this.bw = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            //avvio i thread per la lettura e la scrittura
            leggi();
            scrivi();
        } catch (IOException e) {
            System.err.println("Errore durante la connessione al server " + nomeServer + " sulla porta " + portaServer + ": " + e.getMessage());
        } 
    }
    //metodo per scrivere
    public void scrivi(){
        Thread scrittura = new Thread(() -> {
            try {
                Scanner scanner = new Scanner(System.in);
                String input;
                do {
                    input = scanner.nextLine();
                    this.bw.write(this.colore + this.nome + ") " + input);
                    this.bw.newLine();
                    this.bw.flush();
                    switch(input){
                        case "1":
                            Server.operazioneMatematica();
                            break;
                        case "2":
                            Server.raccontaBarzelletta();
                            break;
                        case "3":
                            Server.stampaCrediti();
                            break;
                    }
                    System.out.println("Scegliere un'altra azione da compiere"
                                        + "o premere 'exit' per uscire");
                    
                } while (!input.equalsIgnoreCase("exit"));
            } catch (IOException e) {
                System.err.println("Client: errore nella scrittura" + e);
            }
        });
        scrittura.start();
    }
    //metodo per leggere
    public void leggi(){
        Thread lettura = new Thread(() -> {
            try {
                while(!this.socket.isClosed()){
                    String msg = this.br.readLine();
                    if(msg!=null){
                        System.out.println(msg + this.colore);
                    }
                }
            } catch (IOException e) {
                System.err.println("Client: errore nella lettura " + e);
            }
        });
        lettura.start();
    }
    //metodo per chiudere la connessione
    public void chiudi(){
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
