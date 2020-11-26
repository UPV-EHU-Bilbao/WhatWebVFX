package ehu.isad.utils;

import ehu.isad.model.MongoErabiltzailea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Bilaketa {


    public List<String> urlIrakurri(String url) {
        List<String> processes = new LinkedList<String>();
        try {
            String line;
            Process p=null;

            String komandoa="";


            //MongoDB erabiltzen du?
            if (!MongoErabiltzailea.getInstance().getCollection().equals("")){
                komandoa="whatweb --color=never --log-mongo-host localhost --log-mongo-database "+Utils.lortuEzarpenak().getProperty("dbMongo")+" --log-mongo-collection "+MongoErabiltzailea.getInstance().getCollection()+" "+url;
            }
            else {
                komandoa = "whatweb --log-sql="+ Utils.lortuEzarpenak().getProperty("pathToInserts")+"insertak.sql " + url+" --color=never";
            }

            //sistema eragilea
            if(System.getProperty("os.name").toLowerCase().contains("win")) {
                komandoa = "wsl " +Utils.lortuEzarpenak().getProperty("pathToExekutagarria")+ komandoa+" -p +"+Utils.lortuEzarpenak().getProperty("pathToExekutagarria")+"plugins-disabled/charset.rb";
            }

            p = Runtime.getRuntime().exec(komandoa);
            p.waitFor();

            System.out.println(komandoa);

            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                processes.add(line);
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }

        return processes;
    }

}
