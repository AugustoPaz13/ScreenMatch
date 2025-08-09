package com.codigopiojoso.screenmatch.principal;
import com.codigopiojoso.screenmatch.model.*;
import com.codigopiojoso.screenmatch.repository.SerieRepository;
import com.codigopiojoso.screenmatch.service.ConsumoAPI;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    Scanner consola = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvertirDatos conversor = new ConvertirDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private final String API_URL = "https://www.omdbapi.com/?t=";
    private final String API_KEY = System.getenv("API_KEY");
    private SerieRepository serieRepository;
    private List<Serie> series;
    public Principal(SerieRepository repository) {
        this.serieRepository = repository;
    }

    public void mostrarMenu() {

        while (true) {
            System.out.println("------------------------------------");
            System.out.println("Bienvenido a Screenmatch");
            System.out.println("1. Buscar una serie");
            System.out.println("2. Buscar episodios");
            System.out.println("3. Buscar serie por título");
            System.out.println("4. Buscar serie por categoría");
            System.out.println("5. Mostrar series buscadas");
            System.out.println("6. TOP 3 MEJORES SERIES");
            System.out.println("7. Buscar una película");
            System.out.println("8. Salir");
            System.out.println("------------------------------------");
            System.out.print("Seleccione una opción: ");
            try {
                var opcion = consola.nextInt();
                consola.nextLine();
                switch (opcion) {
                    case 1 -> buscarSerie();
                    case 2 -> buscarEpisodios();
                    case 3 -> buscarSeriePorTitulo();
                    case 4 -> buscarSeriePorCategoria();
                    case 5 -> mostrarSeriesBuscadas();
                    case 6 -> topSeries();
                    case 7 -> buscarPelicula();
                    case 8 -> {
                        System.out.println("Saliendo de la aplicación...");
                        return;
                    }
                    default -> System.out.println("Opción no válida, por favor intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Debe ingresar un número válido.");
                consola.nextLine(); // Limpiar el buffer
            }
        }
    }

    private DatosSerie getDatosSerie() {
        System.out.print("Escribe el nombre de la serie: ");
        var nombreSerie = consola.nextLine();
        var json = consumoAPI.obtenerDatos(API_URL + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private void buscarEpisodios() {
        listarSeriesBuscadas();
        System.out.print("Escriba el nombre de la serie que desea ver: ");
        var nombreSerie = consola.nextLine();
        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
        if (serie.isPresent()){
            var serieBuscada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieBuscada.getTotalTemporadas(); i++) {
                var json = consumoAPI.obtenerDatos(API_URL + serieBuscada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d-> d.episodios().stream()
                            .map(e -> new Episodio(d.numeroTemporada(), e)))
                    .collect(Collectors.toList());

            serieBuscada.setEpisodios(episodios);
            serieRepository.save(serieBuscada);
        }
    }

    private void listarSeriesBuscadas() {
        series = serieRepository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }

    private void buscarSerie(){
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        serieRepository.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);
    }

    private void buscarPelicula() {
        ConsumoAPI consumoAPI = new ConsumoAPI();

        System.out.print("Ingrese el nombre de la película: ");
        String nombrePelicula = consola.nextLine();

        // Aquí se llamaría al servicio para buscar la película
        var json = consumoAPI.obtenerDatos(API_URL + nombrePelicula.replace(" ", "+") + API_KEY);
        ConvertirDatos conversor = new ConvertirDatos();
        var datos = conversor.obtenerDatos(json, DatosPelicula.class);
        // Muestra los detalles de la película
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        series = serieRepository.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                //MOSTRAR SOLAMENTE LOS TIUTLOS:
                .map(Serie::getTitulo)
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.print("Ingrese el título de la serie: ");
        String nombreSerie = consola.nextLine();
        Optional<Serie> serieBuscada = serieRepository.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Serie encontrada: \n" + serieBuscada.get());
        } else {
            System.out.println("No se encontró ninguna serie con el título: " + nombreSerie);
        }
    }

    private void topSeries() {
        List<Serie> topSeries = serieRepository.findTop3ByOrderByClasificacionDesc();
        topSeries.forEach(s-> System.out.println("Título: " + s.getTitulo() + " / Clasificación: " + s.getClasificacion()));
    }

    private void buscarSeriePorCategoria() {
        System.out.print("Ingrese la categoría que sea buscar: ");
        String genero = consola.nextLine();

        var categoria = Categoria.fromEspanol(genero);

        List<Serie> seriesPorCategoria = serieRepository.findByGenero(categoria);

        System.out.println("Series en la categoría " + categoria + ":");
        seriesPorCategoria.forEach(s -> System.out.println("Título: " + s.getTitulo() + " / Clasificación: " + s.getClasificacion()));

    }
}
