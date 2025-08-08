package com.codigopiojoso.screenmatch.principal;

import com.codigopiojoso.screenmatch.modelos.Titulo;
import com.codigopiojoso.screenmatch.modelos.TituloOmdb;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalBusqueda {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner consola = new Scanner(System.in);
        List<Titulo> listaDeContenido = new ArrayList<>();

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        while (true) {
            System.out.print("Escriba el nombre de la película que desea buscar: ");
            var pelicula = consola.nextLine();

            if(pelicula.equalsIgnoreCase("Salir")){
                break;
            }

            String url = "https://www.omdbapi.com/?t=" + pelicula.replace(" ", "%20") + "&apikey=f6524010";

            try{
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .build();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                String json = response.body();
                System.out.println(json);

                TituloOmdb miTituloOmbd = gson.fromJson(json, TituloOmdb.class);

                Titulo miTitulo = new Titulo(miTituloOmbd);
                System.out.println(miTituloOmbd);

                listaDeContenido.add(miTitulo);
            }catch (NumberFormatException e){
                System.out.println("Ocurrió un error al procesar la película: " + e.getMessage());
            }catch (IllegalArgumentException e){
                System.out.println("Ocurrió un error al procesar la url: " + e.getMessage());
            }catch (Exception e){
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            }
        }
        System.out.println("Lista de peliculas: " + listaDeContenido);

        FileWriter transcribir = new FileWriter("Peliculas.json");
        transcribir.write(gson.toJson(listaDeContenido));
        transcribir.close();
    }
}
