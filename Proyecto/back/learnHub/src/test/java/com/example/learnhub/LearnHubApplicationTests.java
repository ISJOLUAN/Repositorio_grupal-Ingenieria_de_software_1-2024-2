package com.example.learnhub;

import com.example.learnhub.entities.Canal;
import com.example.learnhub.repository.CanalRepository;
import com.example.learnhub.services.CanalService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class LearnHubApplicationTests {
	@Mock
	private CanalRepository canalRepository; // Mockeamos el repositorio
	@InjectMocks
	private LearnHubApplicationTests testInstance; // Clase bajo prueba
	/*@Test
	public void testBuscarPorArea() throws ExecutionException, InterruptedException {

		// Datos de prueba
		Canal canal1 = new Canal("1", "Minería de Datos", "Tecnología", 100, 50, "Seminario sobre minería de datos");
		Canal canal2 = new Canal("2", "Biología Marina", "Ciencia", 80, 40, "Investigación de la vida marina");
		Canal canal3 = new Canal("3", "Criptografía", "Tecnología", 150, 75, "Todo sobre criptografía");

		List<Canal> canales = Arrays.asList(canal1, canal2, canal3);
		String area = "Tecnología";

		// Simular el comportamiento del repositorio
		when(canalRepository.buscarPorArea(area)).thenReturn(
				canales.stream().filter(c -> c.getArea().equalsIgnoreCase(area)).toList()
		);

		List<Canal> resultado = canalRepository.buscarPorArea(area);

		// Imprimir resultados
		System.out.println("Cantidad de canales en " + area + ": " + resultado.size());
		System.out.println("Nombres de los canales: " + resultado.stream().map(Canal::getNombre).toList());

		// Assert
		assertEquals(2, resultado.size(), "Deberían devolverse 2 canales de Tecnología");
	}*/
}
