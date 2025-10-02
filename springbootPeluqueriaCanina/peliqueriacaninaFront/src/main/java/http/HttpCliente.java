
package http;

import MascotaDto.MascotaDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpCliente {

    private static final String BASE_URL = "http://localhost:8080/peluqueriacanina";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String crearMascota(MascotaDTO mascotaDTO) {
        try {
            String json = mapper.writeValueAsString(mascotaDTO);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL+ "/mascotas"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al crear mascota: " + e.getMessage();
        }
    }

    public static MascotaDTO obtenerMascota(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/" + id))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), MascotaDTO.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

   
    public static List<MascotaDTO> listarMascotas() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/mascotas"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), new TypeReference<List<MascotaDTO>>() {});

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
   
    public static String modificarMascota(int id, MascotaDTO mascotaDTO) {
        try {
            String json = mapper.writeValueAsString(mascotaDTO);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al modificar mascota: " + e.getMessage();
        }
    }

 
    public static String eliminarMascota(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/" + id))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar mascota: " + e.getMessage();
        }
    }
}
