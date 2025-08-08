package com.codigopiojoso.screenmatch.principal;
import com.codigopiojoso.screenmatch.model.*;
import com.codigopiojoso.screenmatch.repository.SerieRepository;
import com.codigopiojoso.screenmatch.service.ConsumoAPI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            System.out.println("Bienvenido a Screenmatch");
            System.out.println("1. Buscar una serie");
            System.out.println("2. Buscar episodios");
            System.out.println("3. Buscar una película");
            System.out.println("4. Mostrar series buscadas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            try {
                var opcion = consola.nextInt();
                consola.nextLine();
                switch (opcion) {
                    case 1 -> buscarSerie();
                    case 2 -> buscarEpisodios();
                    case 3 -> buscarPelicula();
                    case 4 -> mostrarSeriesBuscadas();
                    case 5 -> {
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
        mostrarSeriesBuscadas();
        System.out.print("Seleccione el número de la serie para buscar episodios:");
        var nombreSerie = consola.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
        if(serie.isPresent()){
            var serieEncontrada = serie.get();

            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoAPI.obtenerDatos(API_URL + serieEncontrada.getTitulo().replace(" ", "+") + "&Season=" + i + API_KEY);
                DatosTemporadas datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporadas);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d->d.episodios().stream()
                            .map(e-> new Episodio(d.numEpisodio(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            serieRepository.save(serieEncontrada);
        }else{
            System.out.println("Serie no encontrada.");
        }
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

    /*
    private void buscarSerie() {
        ConsumoAPI consumoAPI = new ConsumoAPI();
        // Solicita el nombre de la serie al usuario
        System.out.print("Ingrese el nombre de la serie: ");
        String nombreSerie = consola.nextLine();
        // Aquí se llamaría al servicio para buscar la serie
        var json = consumoAPI.obtenerDatos( API_URL + nombreSerie.replace(" ", "+") + API_KEY);

        ConvertirDatos conversor = new ConvertirDatos();
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);
        //Busca los datos de todas las temporadas de la serie
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalTemporadas(); i++) {
            json = consumoAPI.obtenerDatos(API_URL + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporadas);
        }
        //temporadas.forEach(System.out::println);
        //temporadas.forEach(t->t.episodios().forEach(e-> System.out.println(e.titulo())));

        // Muestra el número de temporada y los títulos de los episodios
        temporadas.forEach(t -> {
            System.out.println("Temporada " + t.numeroTemporada() + ":");
            t.episodios().forEach(e -> System.out.println("  - " + e.titulo()));
        });

        //Convertir toda la informacion en una lista del tipo DatoEpisodio
        List<DatosEpisodio> datosEpisodios = temporadas.stream().
                flatMap(t -> t.episodios().stream()).collect(Collectors.toList());

        //TOP Episodios
        System.out.println("TOP 5 EPISODIOS: ");
        datosEpisodios.stream()
                .filter(e -> !e.clasificacion().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DatosEpisodio::clasificacion).reversed())
                .limit(5)
                .forEach(System.out::println);

        //Convirtiendo los datos a una lista de tipo episodio
        List<Episodio> episodios = temporadas.stream().
                flatMap(t->t.episodios().stream()
                        .map(d->new Episodio(t.numeroTemporada(),d)))
                .collect(Collectors.toList());
        //episodios.forEach(System.out::println);

        //Busqueda de episodios por año
        System.out.println("Ingrese el año de lanzamiento de los episodios: ");
        var fecha = consola.nextInt();
        consola.nextLine();

        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                        ", Episodio: " + e.getNumeroEpisodio() +
                        ", Título: " + e.getTitulo() +
                        ", Fecha de lanzamiento: " + e.getFechaDeLanzamiento().format(dtf)
                ));

        //Busca episodios por titulo

        System.out.print("Ingrese el título del episodio a buscar: ");
        String tituloEpisodio = consola.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(tituloEpisodio.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()) {
            System.out.println("Episodio encontrado: \n" + episodioBuscado.get());
        }else{
            System.out.println("Episodio no encontrado...");
        }

        Map<Integer, Double> evaluacionPorTemporada = episodios.stream()
                .filter(e->e.getClasificacion() > 0.0 || e.getClasificacion() != null)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getClasificacion)));
        for (Map.Entry<Integer, Double> entry : evaluacionPorTemporada.entrySet()) {
            System.out.println("Temporada " + entry.getKey() + ": " + String.format("%.1f", entry.getValue()));
        }

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e->e.getClasificacion() > 0.0 || e.getClasificacion() != null)
                .collect(Collectors.summarizingDouble(Episodio::getClasificacion));
        System.out.println("Estadísticas de clasificación: " +
                "Máxima: " + String.format("%.1f", est.getMax()) +
                ", Mínima: " + String.format("%.1f", est.getMin()) +
                ", Promedio: " + String.format("%.1f", est.getAverage()));
    }
     */

}
