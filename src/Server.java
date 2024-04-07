
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Studenti
 */
public class Server {
    ServerSocket serverSocket;
    Socket dataSocket;
    int porta;
    public static String colore;
    public static String colorEND = "\033[0m";
    //per output
    BufferedWriter bw;
    //per input
    BufferedReader br;
    
    public Server(int porta, String colore){
        this.porta = porta;
        this.colore = colore;
    }
    public void attendi(){
        try {
            this.serverSocket = new ServerSocket(this.porta);
            System.out.println(this.colore+"Il server e' in ascolto...");
            this.dataSocket = this.serverSocket.accept(); //ottengo il socket ritornato da serverSocket.accept() 
            //thread a cui affidare
            new Thread(() -> {
                try {
                    System.out.println(this.colore+"Richiesta del client accettata con successo, connessione avvenuta.");
                    //definisco i due stream per comunicare con il Client
                    this.bw = new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream()));
                    this.br = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                    //visualizzare per la prima volta il menu
                    visualizzaMenu();
                    //per scrivere al client
                    scrivi();
                    //per ascoltare nuove richieste:
                    leggi();
                } catch (IOException e) {
                    System.out.println("Server: errore di I/O nel metodo 'attendi()'");
                }
            }).start();
        }catch(BindException e){
            System.err.println("Porta occupata.");
        }catch (IOException e) {
            System.err.println("Server: errore durante l'ascolto " + e);
        }
    }
    public void visualizzaMenu(){
        try {
            String menu = this.colore + "Server) Digitare il tasto associato all'azione che si vuole compiere\n" +
                        this.colore + "||MENU||\n" +
                        this.colore + "1) risoluzione di un'operazione matematica\n" +
                        this.colore + "2) barzelletta\n" +
                        this.colore + "3) crediti\n" +
                        this.colore + "Per terminare digitare 'exit'";
            this.bw.write(menu);
            this.bw.newLine();
            this.bw.flush();
        } catch (IOException e) {
            System.err.println("Server: errore nella visualizzazione del menu " + e);
        }
    }
    private void scrivi(){
        Thread scrittura = new Thread(() -> {
            try {
                while (!this.serverSocket.isClosed() && !this.dataSocket.isClosed()) {                
                    try {
                        Scanner scanner = new Scanner(System.in);
                        String msg = scanner.nextLine();
                        this.bw.write(this.colore + "server) " + msg);
                        this.bw.newLine();
                        this.bw.flush();
                    } catch (IOException e) {
                        e.getMessage();
                    }
                }
            } catch (Exception e) {
                System.err.println("Server: errore nella scrittura " + e);
            }
        });
        scrittura.start();
    }
    public void leggi(){
        Thread lettura = new Thread(() -> {
            try {
                while (!this.serverSocket.isClosed() && !this.dataSocket.isClosed()) {
                    String msg = br.readLine();
                    if(msg!=null){
                        System.out.println(msg + this.colore);
                    }else{
                        System.err.println("La connessione è stata interrotta");
                        chiudi();
                    }
                }
            } catch (IOException e) {
                System.err.println("Server: errore nella lettura: " + e);
            }
        });
        lettura.start();
    }
    //operazione matematica
    public static void operazioneMatematica(){
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> nums = new ArrayList<>();
        
        System.out.println(Server.colore + "Che operazione vuoi eseguire? somma/sottrazione/moltiplicazione/divisione");
        String operazione = scanner.nextLine();  
        //input membri operazioni
        String risposta;
        System.out.println("Inserisci un numero: ");
        nums.add(scanner.nextInt());
        scanner.nextLine();
        do{
            System.out.println("Inserisci un numero: ");
            nums.add(scanner.nextInt());
            scanner.nextLine();
            System.out.println("Vuoi inserire un altro numero? ");
            risposta = scanner.nextLine();
        }while(risposta.equalsIgnoreCase("si"));
        
        //calcolo
        int risultato = 0;
        switch(operazione){
            case "somma":
                for(int i=0;i<nums.size();i++){
                    risultato+=nums.get(i);
                }
                break;
            case "sottrazione":
                risultato = nums.get(0);
                for(int i=1;i<nums.size();i++){
                    risultato -= nums.get(i);
                }
                break;
            case "moltiplicazione":
                risultato = 1;
                for(int i=0;i<nums.size();i++){
                    risultato*=nums.get(i);
                }
                break;
            case "divisione":
                risultato = nums.get(0);
                for(int i=1;i<nums.size();i++){
                    risultato/=nums.get(i);
                }
                break;
        }
        nums.clear();
        System.out.println("risultato: " + risultato);
    }
    //barzelletta
    public static void raccontaBarzelletta(){
        System.out.println("Questa funzione deve essere ancora implementata.");
    }
    //visualizzazione crediti
    public static void stampaCrediti(){
        System.out.println("Questa funzione deve essere ancora implementata.");
    }
    //chiude la connessione con il client
    private void chiudi(){ 
        if(!dataSocket.isClosed()){
            try {
                dataSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        termina();
    }
    //termina completamente l'attività del server
    public void termina(){
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            System.err.println("Server: errore nella chiusura della connessione." + e);
        }
    }
}
