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
    private Optional<Serie> serieBuscada;
    public Principal(SerieRepository repository) {
        this.serieRepository = repository;
    }

    public void mostrarMenu() {

        while (true) {
            System.out.println("------------------------------------");
            System.out.println("Bienvenido a Screenmatch");
            System.out.println("1. Buscar una serie");
            System.out.println("2. Buscar episodios");
            System.out.println("3. Buscar episodios por título");
            System.out.println("4. Buscar serie por título");
            System.out.println("5. Buscar serie por categoría");
            System.out.println("6. Mostrar series buscadas");
            System.out.println("7. TOP 3 MEJORES SERIES");
            System.out.println("8. TOP 3 MEJORES EPISODIOS");
            System.out.println("9. Filtrar series");
            System.out.println("10. Buscar una película");
            System.out.println("11. Salir");
            System.out.println("------------------------------------");
            System.out.print("Seleccione una opción: ");
            try {
                var opcion = consola.nextInt();
                consola.nextLine();
                switch (opcion) {
                    case 1 -> buscarSerie();
                    case 2 -> buscarEpisodios();
                    case 3 -> buscarEpisodiosPorTitulo();
                    case 4 -> buscarSeriePorTitulo();
                    case 5 -> buscarSeriePorCategoria();
                    case 6 -> mostrarSeriesBuscadas();
                    case 7 -> topSeries();
                    case 8 -> topEpisodios();
                    case 9 -> filtrarSeriesPorTemporadaYClasificacion();
                    case 10 -> buscarPelicula();
                    case 11 -> {
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
        serieBuscada = serieRepository.findByTituloContainsIgnoreCase(nombreSerie);

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

    public void filtrarSeriesPorTemporadaYClasificacion(){
        System.out.println("¿Filtrar séries con cuántas temporadas? ");
        System.out.print("Temporadas: ");
        var totalTemporadas = consola.nextInt();
        consola.nextLine();
        System.out.println("¿Con clasificación apartir de cuánto? ");
        System.out.print("Clasificación: ");
        var clasificacion = consola.nextDouble();
        consola.nextLine();
        List<Serie> filtroSeries = serieRepository.seriesPorTemporadaYClasificacion(totalTemporadas,clasificacion);
        System.out.println("""
                ----------------
                SERIES FILTRADAS
                ----------------
                """);
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - clasificación: " + s.getClasificacion()));
    }

    public void buscarEpisodiosPorTitulo() {
        System.out.print("Ingrese el título del episodio: ");
        String nombreEpisodio = consola.nextLine();
        List<Episodio> episodiosEncontrados = serieRepository.episodiosPorNombre(nombreEpisodio);

        if (episodiosEncontrados.isEmpty()) {
            System.out.println("No se encontraron episodios con el título: " + nombreEpisodio);
        } else {
            System.out.println("Episodios encontrados:");
            episodiosEncontrados.forEach(e -> System.out.printf("""
                    -------------------------------
                    Episodio: %s
                    Temporada: %d
                    Número de episodio: %d
                    Fecha de lanzamiento: %s
                    Clasificación: %.1f
                    """, e.getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getFechaDeLanzamiento(), e.getClasificacion()));
        }
    }

    public void topEpisodios() {
        buscarSeriePorTitulo();
        if(serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = serieRepository.top3Episodios(serie);
            if (topEpisodios.isEmpty()) {
                System.out.println("No se encontraron episodios para la serie: " + serie.getTitulo());
            } else {
                System.out.println("Top 3 episodios de la serie " + serie.getTitulo() + ":");
                topEpisodios.forEach(e -> System.out.printf("""
                        -------------------------------
                        Episodio: %s
                        Temporada: %d
                        Número de episodio: %d
                        Fecha de lanzamiento: %s
                        Clasificación: %.1f
                        """, e.getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getFechaDeLanzamiento(), e.getClasificacion()));
            }
        }
    }
}
