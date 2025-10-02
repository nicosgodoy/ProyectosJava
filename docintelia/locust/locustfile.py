from locust import HttpUser, task, between
import random

palabras_clave = [
    "java", "evolución", "algoritmo", "documento", "texto", 
    "índice", "variable", "relevancia", "programación", "genético"
]

class SimuladorBusqueda(HttpUser):
    wait_time = between(1, 2)

    def on_start(self):
        self.token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU5JU1RSQURPUiIsInN1YiI6ImFkbWluQGFkbWluLmNvbSIsImlhdCI6MTc1MzgyMTg1NCwiZXhwIjoxNzUzOTA4MjU0fQ.KfOtzVTvT9bNqLmc5rbTyj8KfDVkl7SM6D9DSV4nLqU"  # Reemplazá esto
        self.headers = {
            "Authorization": self.token,
            "Content-Type": "application/json"
        }

    @task
    def realizar_busqueda(self):
        termino = random.choice(palabras_clave)
        with self.client.get(
            f"/api/documento/buscar?q={termino}",
            headers=self.headers,
            name="/api/documento/buscar",
            catch_response=True
        ) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Fallo con estado {response.status_code}")
